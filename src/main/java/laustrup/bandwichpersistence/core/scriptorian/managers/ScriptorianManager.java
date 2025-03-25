package laustrup.bandwichpersistence.core.scriptorian.managers;

import laustrup.bandwichpersistence.core.libraries.PathLibrary;
import laustrup.bandwichpersistence.core.persistence.DatabaseParameter;
import laustrup.bandwichpersistence.core.scriptorian.Scriptorian;
import laustrup.bandwichpersistence.core.services.FileService;
import laustrup.bandwichpersistence.core.scriptorian.repositories.ScriptorianRepository;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import static java.nio.file.Files.readAttributes;
import static laustrup.bandwichpersistence.core.persistence.queries.ScriptorianQueries.Parameter;
import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class ScriptorianManager {

    private static final Logger _logger = Logger.getLogger(ScriptorianManager.class.getSimpleName());

    private static final char _splitter = ' ';

    private static final char _replacement = '_';

    private static final String _dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    private static final ZoneId _zoneId = ZoneId.of("GMT");

    public static void onStartup() {
        try {
            _logger.log(
                    Level.INFO,
                    "Scriptorian started"
            );

            Instant now = Instant.now();
            Seszt<File> scripts = prepareScripts(now);

            databaseInteraction(() -> {
                ScriptorianRepository.createDefaultSchemaIfNotExists();
                Seszt<Scriptorian.Scriptory> scriptoriesWithoutSuccess = scriptoriesWithoutSuccess();
                if (!scriptoriesWithoutSuccess.isEmpty())
                    throw new IllegalStateException(String.format("""
                            %nThere is a conflict with scriptories, please resolve it.
                            
                            conflict to resolve is:
                            %s
                            
                            It can either be done by deleting the row with successstamp of null, which will make it run again on next startup or by setting it to now(), which will ignore the script on startup.
                            """,
                            scriptoriesWithoutSuccess.getFirst().get_errorMessage()
                    ));

                String currentFileName = "";
                File currentFile;

                Seszt<File> scriptsNotRecorded = findScriptsNotRecorded(scripts, buildScriptories(ScriptorianRepository.findAllScriptories()));

                try {
                    for (File file : scriptsNotRecorded) {
                        currentFile = file;
                        currentFileName = file.getName();
                        String errorMessage = null;

                        try {
                            ScriptorianRepository.executeScript(FileService.getContent(file));
                        } catch (RuntimeException exception) {
                            String logMessage = String.format("Couldn't execute migration sql file \"%s\"", currentFileName);
                            _logger.log(
                                    Level.WARNING,
                                    logMessage,
                                    exception.getMessage()
                            );
                            errorMessage = logMessage + "\n" + exception.getMessage();
                        }

                        ScriptorianRepository.putScriptory(generateParameters(currentFile, errorMessage));

                        if (errorMessage != null) {
                            System.exit(6);
                        }

                        _logger.log(
                                Level.INFO,
                                String.format("Successfully migrated file \"%s\"!", currentFileName)
                        );
                    }
                } catch (Exception exception) {
                    _logger.log(
                            Level.WARNING,
                            String.format(
                                    "Conflict not relevant to sql of migration file \"%s\".\n\n%s",
                                    currentFileName,
                                    exception.getMessage()
                            )
                    );
                    System.exit(5);
                }
            });
        } catch (Exception exception) {
            _logger.warning("Exception occurred in Scriptorian.\n" + exception.getMessage() + "\n");
            exception.printStackTrace();
            System.exit(-2);
        }
    }

    private static Seszt<File> prepareScripts(Instant now) {
        StringBuilder filesRenamed = new StringBuilder();

        Seszt<File> scripts = getScripts();

        scripts.forEach(script -> {
            try {
                if (!validateName(script)) {
                    filesRenamed.append("\n").append(script.getName());
                    rename(script, readAttributes(script.toPath(), BasicFileAttributes.class), now);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        boolean filesHasBeenRenamed = !filesRenamed.isEmpty();

        if (filesHasBeenRenamed)
            _logger.log(Level.INFO, "Renamed " + filesRenamed);

        return getScripts();
    }

    private static boolean validateName(File file) {
        try {
            define(file);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return true;
    }

    private static Seszt<File> getScripts() {
        try {
            return new Seszt<>(FileService.getFiles(PathLibrary.get_migrationDirectoryFullPath()));
        } catch (FileNotFoundException e) {
            _logger.log(Level.SEVERE, "Scriptorian script not found", e);
            System.exit(3);
        }

        return null;
    }

    private static void rename(File file, BasicFileAttributes attributes, Instant now) {
        try {
            Files.move(
                    file.toPath(),
                    Paths.get(PathLibrary.get_migrationDirectoryFullPath() + String.format(
                            "V%sC%s%s%s",
                            fileDateTime(now),
                            fileDateTime(attributes.creationTime().toInstant()),
                            _splitter,
                            cleanFileName(file.getName())
                    )),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            _logger.log(
                    Level.SEVERE,
                    "Failed to rename file " + file.getName() + " within the Scriptorian process",
                    e
            );
            System.exit(4);
        }
    }

    private static String fileDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, _zoneId)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ofPattern(
                        _dateTimeFormat
                                .replace(":", "-")
                                .replace(" ", "'T'")
                ));
    }

    private static String cleanFileName(String fileName) {
        return fileName.replace(_splitter, _replacement);
    }

    private static Seszt<Scriptorian.Scriptory> scriptoriesWithoutSuccess() {
        return buildScriptories(ScriptorianRepository.findScriptoriesWithoutSuccess());
    }

    private static Seszt<File> findScriptsNotRecorded(Seszt<File> scripts, Seszt<Scriptorian.Scriptory> scriptories) {
        Seszt<String> fileNames = new Seszt<>(scriptories.stream()
                .map(Scriptorian.Scriptory::get_fileName));

        return new Seszt<>(scripts.stream()
                .filter(script -> fileNames.isEmpty() || !fileNames.contains(script.getName()))
                .sorted((previous, current) -> {
                    Scriptorian.Scriptory
                            previousScriptory = define(previous),
                            currentScriptory = define(current);

                    int comparison = previousScriptory.get_versionstamp().compareTo(currentScriptory.get_versionstamp());

                    return comparison == 0
                            ? previousScriptory.get_createdstamp().compareTo(currentScriptory.get_createdstamp())
                            : comparison;
                }));
    }

    private static Scriptorian.Scriptory define(File file) throws IllegalArgumentException {
        String[] split = file.getName().split(String.valueOf(_splitter)),
                attributes = Arrays.stream(split[0].split("[VC]"))
                        .filter(attribute -> !attribute.isEmpty())
                        .toArray(String[]::new),
                versionstampAttributes = attributes[0].split("[-T]"),
                createdstampAttributes = attributes[1].split("[-T]");

        if (!(
                attributes.length == 2 &&
                split.length <= 2 &&
                versionstampAttributes.length == 6
        ))
            throw new IllegalArgumentException();

        Function<String[], Instant> createStamp = timeAttributes -> LocalDateTime.of(
                parseInt(timeAttributes[0]),
                parseInt(timeAttributes[1]),
                parseInt(timeAttributes[2]),
                parseInt(timeAttributes[3]),
                parseInt(timeAttributes[4]),
                parseInt(timeAttributes[5])
        ).toInstant(ZoneOffset.of("Z"));

        return new Scriptorian.Scriptory(
                split[1],
                file.getName(),
                null,
                FileService.getContent(file),
                createStamp.apply(versionstampAttributes),
                null,
                createStamp.apply(createdstampAttributes),
                null
        );
    }

    private static Seszt<DatabaseParameter> generateParameters(File file, String errorMessage) {
        if (file == null)
            return null;

        Scriptorian.Scriptory scriptory = define(file);

        return new Seszt<>(
                new DatabaseParameter(Parameter.TITLE.get_key(), file.getName().split(String.valueOf(_splitter))[1]),
                new DatabaseParameter(Parameter.FILE_NAME.get_key(), file.getName()),
                new DatabaseParameter(Parameter.ERROR_MESSAGE.get_key(), errorMessage),
                new DatabaseParameter(Parameter.CONTENT.get_key(), FileService.getContent(file)),
                new DatabaseParameter(Parameter.VERSIONSTAMP.get_key(), scriptory.get_versionstamp()),
                new DatabaseParameter(Parameter.SUCCESSSTAMP.get_key(), errorMessage == null ? Instant.now() : null),
                new DatabaseParameter(Parameter.CREATEDSTAMP.get_key(), scriptory.get_createdstamp())
        );
    }

    private static Scriptorian.Scriptory buildScriptory(ResultSet resultSet) {
        return buildScriptories(resultSet).getFirst();
    }

    private static Seszt<Scriptorian.Scriptory> buildScriptories(ResultSet resultSet) {
        return new Seszt<>(build(
                resultSet,
                () -> {
                    try {
                        return new Scriptorian.Scriptory(
                                resultSet.getString(Scriptorian.Scriptory.Fields._title),
                                resultSet.getString(Scriptorian.Scriptory.Fields._fileName),
                                resultSet.getString(Scriptorian.Scriptory.Fields._errorMessage),
                                resultSet.getString(Scriptorian.Scriptory.Fields._content),
                                getInstant(Scriptorian.Scriptory.Fields._versionstamp),
                                getInstant(Scriptorian.Scriptory.Fields._successstamp),
                                getInstant(Scriptorian.Scriptory.Fields._createdstamp),
                                getInstant(Scriptorian.Scriptory.Fields._timestamp)
                        );
                    } catch (SQLException exception) {
                        throw new RuntimeException(exception);
                    }
                }
        ));
    }
}
