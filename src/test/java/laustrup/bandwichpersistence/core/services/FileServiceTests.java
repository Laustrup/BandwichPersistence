package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.services.resources.TestResource;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.services.FileService.*;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileServiceTests extends BandwichTester {

  private final String
      testResourcesPath = "src/test/java/laustrup/bandwichpersistence/core/services/resources/",
      testResourcesPackageName = "laustrup/bandwichpersistence/core/services/resources";

  @Test
  void canGetFiles() {
    test(() -> {
      Liszt<File> expectations = arrange(Liszt.of(new File(testResourcesPath).listFiles()));

      try {
        Liszt<File> actuals = new Liszt<>(act(getFiles(testResourcesPath)));

        asserting(expectations)
            .is(actuals);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Test
  void canNotGetFiles() {
    test(() -> {
      String fakePath = arrange(testResourcesPath + "fklgndfgl");

      assertThrows(FileNotFoundException.class, () -> act(getFiles(fakePath)));
    });
  }

  @Test
  void canGetContent() {
    test(() -> {
      String path = arrange(testResourcesPath + "file_service_test_ressource.txt");
      File file = new File(path);
      String expected = "This is a test";

      String actual = act(getContent(file));

      asserting(expected)
          .is(actual);
    });
  }

  @Test
  void canGetClasses() {
    test(() -> {
      Seszt<Class<?>> expectations = arrange(Seszt.of(TestResource.class));

      Seszt<Class<?>> actuals;
      try {
        actuals = act(getClasses("laustrup/bandwichpersistence/core/services/resources"));
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }

      // Asserter has issues comparing Class instead of String
      asserting(expectations.stream().map(Class::getSimpleName).collect(Collectors.joining()))
          .is(actuals.stream().map(Class::getSimpleName).collect(Collectors.joining()));
    });
  }
}