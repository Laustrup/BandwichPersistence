package laustrup.bandwichpersistence.core.libraries;

import laustrup.bandwichpersistence.core.services.PathService;
import lombok.Getter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PathLibrary {

    @Getter
    private static final String _rootPath = findSpringBootDirectory();

    @Getter
    private static final String _migrationDirectoryPath = "/src/main/resources/database/scriptorian/migrations/";

    @Getter
    private static final String _migrationDirectoryFullPath = get_rootPath() + get_migrationDirectoryPath();

    @Getter
    private static final String _injectionDirectoryPath = "/src/main/resources/database/scriptorian/injections/";

    @Getter
    private static final String _injectionDirectoryFullPath = get_rootPath() + get_injectionDirectoryPath();

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
