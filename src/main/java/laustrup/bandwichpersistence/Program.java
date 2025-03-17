package laustrup.bandwichpersistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;

public class Program {

    public static void start(Class<?> applicationClass, String defaultSchema, String... args) {
        start(applicationClass, defaultSchema, null, args);
    }

    public static void start(
            Class<?> applicationClass,
            String defaultSchema,
            ILogo logo,
            String... args
    ) {
        if (logo != null)
            logo.print();

        if (ProgramInitializer.startup(args, defaultSchema))
            SpringApplication.run(applicationClass, args);
        else
            System.exit(-1);
    }

    @Getter @AllArgsConstructor
    public enum CommandOption {
        SKIP_CONFIRMATION("skipConfirmation", true);

        private final String _title;

        private final boolean _flag;
    }
}
