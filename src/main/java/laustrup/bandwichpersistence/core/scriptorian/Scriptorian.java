package laustrup.bandwichpersistence.core.scriptorian;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
public class Scriptorian {

    private List<Scriptory> _scriptories;

    @Getter @AllArgsConstructor @FieldNameConstants
    public static class Scriptory {

        private String _title;

        private String _fileName;

        private String _errorMessage;

        private String _content;

        /**
         * When the file had version control activated.
         */
        private Instant _versionstamp;

        /**
         * When the file was executed successfully.
         */
        private Instant _successstamp;

        /**
         * The creation of the file.
         */
        private Instant _createdstamp;

        /**
         * When the file first ran.
         */
        private Instant _timestamp;

        public boolean is_success() {
            return _successstamp != null;
        }
    }
}
