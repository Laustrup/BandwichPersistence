package laustrup.bandwichpersistence.core.libraries;

import laustrup.bandwichpersistence.core.services.PathService;
import lombok.Getter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PathLibrary {

    @Getter
    private static final String _rootPath = findSpringBootDirectory();

    @Getter
    private static final String _migrationDirectoryPath = "/src/main/resources/database/scriptorian/scripts/migrations/";

    @Getter
    private static final String _migrationDirectoryFullPath = get_rootPath() + get_migrationDirectoryPath();

    @Getter
    private static final String _populationsDirectoryPath = "/src/main/resources/database/scriptorian/scripts/populations/";

    @Getter
    private static final String _scriptsDirectoryPath = "/src/main/resources/database/scriptorian/scripts/";

    @Getter
    private static final String _injectionDirectoryFullPath = get_rootPath() + get_populationsDirectoryPath();

    private static String findSpringBootDirectory() {
        String rootPath = PathService.getRootPath();
        String directory = "/API";

        return rootPath + (
                Files.exists(Paths.get(rootPath + directory))
                        ? directory
                        : ""
        );
    }
}
