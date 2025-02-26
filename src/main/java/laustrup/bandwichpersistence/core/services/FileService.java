package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Stream;

public class FileService {

    public static Stream<File> getFiles(String path) throws FileNotFoundException {
        File[] files = new File(path).listFiles();

        if (files == null)
            throw new FileNotFoundException(String.format("""
                    Couldn't find files in path: "%s"
                    """,
                    path
            ));

        return Stream.of(files).filter(file -> !file.isDirectory());
    }

    public static String getContent(File file) {
        StringBuilder content = new StringBuilder();

        try {
            FileReader reader = new FileReader(file);
            for (int i = reader.read(); i != -1; i = reader.read())
                content.append((char) i);
            reader.close();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return content.toString();
    }
}
