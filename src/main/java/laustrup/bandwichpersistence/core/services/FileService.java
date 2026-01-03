package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
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

  public static Seszt<Class<?>> getClasses(String packagePath) throws ClassNotFoundException {
    InputStream stream = ClassLoader.getSystemClassLoader()
        .getResourceAsStream(packagePath);

    if (stream == null)
      throw new ClassNotFoundException(String.format("Couldn't find class of package %s", packagePath));

    Function<String, Class<?>> getClass = className -> {
      try {
        return Class.forName(String.format("%s.%s",
            packagePath.replace("/", "."),
            className.substring(0, className.lastIndexOf('.'))
        ));
      } catch (ClassNotFoundException e) {
        log.warn("Couldn't find class {} of {}", className, packagePath);
      }

      return null;
    };

    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    return new Seszt<>(reader.lines()
        .filter(line -> line.endsWith(".class"))
        .map(getClass)
        .filter(Objects::nonNull)
    );
  }
}
