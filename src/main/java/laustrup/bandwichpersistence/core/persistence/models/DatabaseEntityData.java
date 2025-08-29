package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.core.services.DatabaseEntityDataService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.getColumns;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineIdReference;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineTitle;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityDataService.get_tableTitle;

@Getter
public class DatabaseEntityData {

    private final String _title;
    private final String _idReference;
    private final Map<Member, String> _columns;

    private DatabaseEntityData(String title, String idReference, Map<Member, String> columns) {
        _title = title;
        _idReference = idReference;
        _columns = columns;
    }

    public DatabaseEntityData(Class<?> clazz) {
        this(
                get_tableTitle(clazz),
                DatabaseEntityDataService.get_idReference(clazz),
                getColumns(clazz)
        );
    }

    private DatabaseEntityData(DTOClass dto) {
        this(
                dto.get_title(),
                dto.get_idReference(),
                dto.get_members().stream()
                        .collect(Collectors.toMap(Function.identity(), Member::getName))
        );
    }

    public static DatabaseEntityData of(DatabaseEntityData target, DatabaseEntityData common, Member... members) {
        String title = defineTitle(target.get_title(), common.get_title());

        return new DatabaseEntityData(new DTOClass(title, defineIdReference(title), new Seszt<>(members)));
    }

    public static DatabaseEntityData of(String title, String... columns) {
        return new DatabaseEntityData(new DTOClass(title, new Seszt<>(Arrays.stream(columns)
                .map(SimpleField::new)
        )));
    }

    @Getter
    private static class DTOClass {

        private final String _title;
        private final String _idReference;
        private final Seszt<Member> _members;

        public DTOClass(String title, Seszt<Member> members) {
            this(title, defineIdReference(title), members);
        }

        public DTOClass(String title, String idReference, Seszt<Member> members) {
            _title = title;
            _idReference = idReference;
            _members = members;
        }
    }
}
