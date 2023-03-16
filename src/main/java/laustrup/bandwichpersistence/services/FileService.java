package laustrup.bandwichpersistence.services;

import laustrup.bandwichpersistence.utilities.console.Printer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * A Service that includes functions of actions with files.
 */
public class FileService {

    /**
     * Singleton instance of the Service.
     */
    private static FileService _instance = null;
    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static FileService get_instance() {
        if (_instance == null) _instance = new FileService();
        return _instance;
    }

    private FileService() {}

    /**
     * Will gather the content of a file without excluding any characters.
     * @param path The path including directories and file with / or \ as separators and filetype at the end.
     * @return The gathered content of a file.
     */
    public String getContent(String path) {
        return getContent(path, new String[][]{});
    }

    /**
     * Will gather the content of a file without excluding any characters.
     * @param directory The directory with its path where the file is located.
     *                  Can end with or without /.
     * @param file The file title with its type.
     *                  Can start with or without a /.
     * @return The gathered content of a file.
     */
    public String getContent(String directory, String file) {
        return getContent(directory, file, new String[][]{});
    }

    /**
     * Will gather the content of a file with excluding specific characters.
     * @param directory The directory with its path where the file is located.
     *                  Can end with or without /.
     * @param file The file title with its type.
     *                  Can start with or without a /.
     * @param replacings The different Strings that will be replaced,
     *                 first index is the value that will be replaced with the other index.
     * @return The gathered content of a file.
     */
    public String getContent(String directory, String file, String[][] replacings) {
        return getContent(configurePath(directory, file), replacings);
    }

    /**
     * Will gather the content of a file with excluding specific characters.
     * @param path The path including directories and file with / or \ as separators and filetype at the end.
     * @param replacings The different Strings that will be replaced,
     *                 first index is the value that will be replaced with the other index.
     * @return The gathered content of a file.
     */
    public String getContent(String path, String[][] replacings) {
        String content = null;

        try {
            content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);

            for (String[] replacing : replacings)
                content = content.replace(replacing[0],replacing[1]);
        } catch (IOException e) {
            Printer.get_instance().print("Couldn't read string of \n\n\t" + path + "\n\nwith excludes: " + Arrays.toString(replacings), e);
        } catch (Exception e) {
            Printer.get_instance().print("Couldn't get content from \n\n\t" + path + "\n\nwith excludes: " + Arrays.toString(replacings), e);
        }

        return content;
    }

    /**
     * Will write content into a given path location.
     * If the file already exists, it will add the content, otherwise create a new file.
     * @param directory The directory with its path where the file is located.
     *                  Can end with or without /.
     * @param file The file title with its type.
     *                  Can start with or without a /.
     * @param content The content that will be written into the file.
     * @return The Path of the file.
     */
    public Path write(String directory, String file, String content) {
        return write(configurePath(directory, file),content);
    }

    /**
     * Will write content into a given path location.
     * If the file already exists, it will add the content, otherwise create a new file.
     * @param path The path including directories and file with / or \ as separators and filetype at the end.
     * @param content The content that will be written into the file.
     * @return The Path of the file.
     */
    public Path write(String path, String content) {
        try {
            return Files.write(Paths.get(path), content.getBytes());
        } catch (IOException e) {
            Printer.get_instance().print("Couldn't write content to " + path, e);
        }
        return null;
    }

    /**
     * Will delete a file.
     * @param directory The directory with its path where the file is located.
     *                  Can end with or without /.
     * @param file The file title with its type.
     *                  Can start with or without a /.
     * @return True if the file existed and is deleted.
     */
    public boolean delete(String directory, String file) {
        return delete(configurePath(directory, file));
    }

    /**
     * Will delete a file.
     * @param path The path including directories and file with / or \ as separators and filetype at the end.
     * @return True if the file existed and is deleted.
     */
    public boolean delete(String path) {
        File file = new File(path);

        if (!file.exists())
            Printer.get_instance().print("File of " + path + " Doesnt exists and can't be deleted...", new Exception());

        return file.delete();
    }

    /**
     * Will generate the path.
     * @param directory The directory with its path where the file is located.
     *                  Can end with or without /.
     * @param file The file title with its type.
     *                  Can start with or without a /.
     * @return The generated path.
     */
    private String configurePath(String directory, String file) {
        return directory.charAt(directory.length()-1) == '/' || file.charAt(0) == '/' ? directory + file : directory + "/" + file;
    }
}
