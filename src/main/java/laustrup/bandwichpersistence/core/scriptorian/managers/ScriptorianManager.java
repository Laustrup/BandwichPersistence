package laustrup.bandwichpersistence.core.scriptorian.managers;

import laustrup.bandwichpersistence.core.libraries.PathLibrary;
import laustrup.bandwichpersistence.core.persistence.DatabaseParameter;
import laustrup.bandwichpersistence.core.persistence.Scriptorian;
import laustrup.bandwichpersistence.core.services.FileService;
import laustrup.bandwichpersistence.core.scriptorian.repositories.ScriptorianRepository;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.persistence.queries.ScriptorianQueries.Parameter;
import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.build;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.get;

public class ScriptorianManager {

    private static final Logger _logger = Logger.getLogger(ScriptorianManager.class.getSimpleName());

    private static final char _splitter = '_';

    private static final String _dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    public static void onStartup() {
        _logger.log(
                Level.INFO,
                "Scriptorian started"
        );

        Instant now = Instant.now();
        Seszt<File> scripts = namingConvention(now);

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
            File currentFile = null;

            try {
                for (File file : findScriptsNotRecorded(scripts, buildScriptories(ScriptorianRepository.findAllScriptories()))) {
                    currentFile = file;
                    currentFileName = file.getName();

                    ScriptorianRepository.executeScript(FileService.getContent(file));
                    ScriptorianRepository.putScriptory(generateParameters(currentFile, null, now));

                    _logger.log(
                            Level.INFO,
                            String.format("Successfully migrated file \"%s\"!", currentFileName)
                    );
                }
            } catch (Exception exception) {
                ScriptorianRepository.putScriptory(generateParameters(currentFile, exception.getMessage(), now));

                _logger.log(
                        Level.WARNING,
                        String.format("Conflict with file \"%s\"", currentFileName),
                        exception.getMessage()
                );
                System.exit(5);
            }
        });
    }

    private static Seszt<File> namingConvention(Instant now) {
        StringBuilder filesRenamed = new StringBuilder();

        Seszt<File> scripts = getScripts();

        scripts.forEach(script -> {
            if (!fileNamedAccepted(script.getName())) {
                filesRenamed.append("\n").append(script.getName());
                rename(script, now);
            }
        });

        boolean filesHasBeenRenamed = !filesRenamed.isEmpty();

        if (filesHasBeenRenamed)
            _logger.log(Level.INFO, "Renamed " + filesRenamed);

        return filesHasBeenRenamed ? getScripts() : scripts;
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

    private static void rename(File file, Instant now) {
        try {
            Files.move(
                    file.toPath(),
                    Paths.get(PathLibrary.get_migrationDirectoryFullPath() + String.format(
                            "V%s%s%s",
                            LocalDateTime.ofInstant(now, ZoneId.of("GMT"))
                                    .truncatedTo(ChronoUnit.SECONDS)
                                    .format(DateTimeFormatter.ofPattern(_dateTimeFormat.replace(":", "-"))),
                            _splitter,
                            file.getName()
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

    private static Seszt<Scriptorian.Scriptory> scriptoriesWithoutSuccess() {
        return buildScriptories(ScriptorianRepository.findScriptoriesWithoutSuccess());
    }

    private static Seszt<File> findScriptsNotRecorded(Seszt<File> scripts,Seszt<Scriptorian.Scriptory> scriptories) {
        Seszt<String> fileNames = new Seszt<>(scriptories.stream()
                .map(Scriptorian.Scriptory::get_fileName));

        return new Seszt<>(scripts.stream()
                .filter(script -> !fileNames.contains(script.getName())));
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
