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
import java.nio.file.attribute.FileTime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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

                        ScriptorianRepository.putScriptory(generateParameters(currentFile, errorMessage, now));


                        if (errorMessage != null)
                            break;

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
            if (!fileNamedAccepted(script.getName())) {
                filesRenamed.append("\n").append(script.getName());;
                try {
                    if (!validateName(script))
                        rename(script, readAttributes(script.toPath(), BasicFileAttributes.class), now);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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

    private static boolean fileNamedAccepted(String fileName) {
        char[] chars = fileName.toCharArray();

        return chars.length > 20 && chars[0] == 'V' && chars[20] == _splitter;
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
                    BasicFileAttributes
                            previousAttributes,
                            currentAttributes;

                    try {
                        previousAttributes = readAttributes(previous.toPath(), BasicFileAttributes.class);
                        currentAttributes = readAttributes(current.toPath(), BasicFileAttributes.class);
                        int comparison = define(previous).get_versionstamp().compareTo(define(current).get_versionstamp());

                        return comparison == 0
                                ? previousAttributes.creationTime().compareTo(currentAttributes.creationTime())
                                : comparison;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

    private static Scriptorian.Scriptory define(File file) throws IllegalArgumentException {
        String[] split = file.getName().split(String.valueOf(_splitter));
        String[] attributes = Arrays.stream(split[0].split("[VC]"))
                .filter(attribute -> !attribute.isEmpty())
                .toArray(String[]::new);
        String[] versionstampAttributes = attributes[0].split("[-T]");

        if (!(
                attributes.length == 2 &&
                split.length <= 2 &&
                versionstampAttributes.length == 6
        ))
            throw new IllegalArgumentException();

        return new Scriptorian.Scriptory(
                split[1],
                file.getName(),
                null,
                FileService.getContent(file),
                LocalDateTime.of(
                        parseInt(versionstampAttributes[0]),
                        parseInt(versionstampAttributes[1]),
                        parseInt(versionstampAttributes[2]),
                        parseInt(versionstampAttributes[3]),
                        parseInt(versionstampAttributes[4]),
                        parseInt(versionstampAttributes[5])
                ).toInstant(ZoneOffset.of("Z")),
                null,
                null
        );
    }

    private static Seszt<DatabaseParameter> generateParameters(File file, String errorMessage, Instant versionstamp) {
        return file == null ? null : new Seszt<>(
                new DatabaseParameter(Parameter.TITLE.get_key(), file.getName().split(String.valueOf(_splitter))[1]),
                new DatabaseParameter(Parameter.FILE_NAME.get_key(), file.getName()),
                new DatabaseParameter(Parameter.ERROR_MESSAGE.get_key(), errorMessage),
                new DatabaseParameter(Parameter.CONTENT.get_key(), FileService.getContent(file)),
                new DatabaseParameter(Parameter.VERSIONSTAMP.get_key(), versionstamp),
                new DatabaseParameter(Parameter.SUCCESSSTAMP.get_key(), errorMessage == null ? Instant.now() : null)
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
                                resultSet.getString(Scriptorian.Scriptory.DatabaseColumns.title.name()),
                                resultSet.getString(Scriptorian.Scriptory.DatabaseColumns.file_name.name()),
                                resultSet.getString(Scriptorian.Scriptory.DatabaseColumns.error_message.name()),
                                resultSet.getString(Scriptorian.Scriptory.DatabaseColumns.content.name()),
                                get(
                                        resultSet.getTimestamp(Scriptorian.Scriptory.DatabaseColumns.versionstamp.name()),
                                        Timestamp::toInstant
                                ),
                                get(
                                        resultSet.getTimestamp(Scriptorian.Scriptory.DatabaseColumns.successstamp.name()),
                                        Timestamp::toInstant
                                ),
                                get(
                                        resultSet.getTimestamp(Scriptorian.Scriptory.DatabaseColumns.timestamp.name()),
                                        Timestamp::toInstant
                                )
                        );
                    } catch (SQLException exception) {
                        throw new RuntimeException(exception);
                    }
                }
        ));
    }
}
