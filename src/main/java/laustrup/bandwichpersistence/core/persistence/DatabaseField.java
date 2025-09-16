package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.services.DatabaseDefinitionService.*;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;

public record DatabaseField(Table table, Column column) {

    private interface Unit {

        boolean contains(String value);
        String getKey();
    }

    private static final String[] _idIndicators = new String[]{"id", "_id"};

    public static Map<DatabaseField, String> toSelections(Map<? extends Member, DatabaseField> data) {
        return data.keySet().stream()
                .map(DatabaseField::of)
                .collect(Collectors.toMap(Function.identity(), DatabaseField::get_tableColumn));
    }

    public static DatabaseField of(Member member) {
        return new DatabaseField(new Table(member.getDeclaringClass()), new Column(member));
    }

    public static DatabaseField idOf(Class<?> clazz) {
        return new DatabaseField(new Table(clazz), new Column("id"));
    }

    public static DatabaseField of(Class<?> entity, DatabaseEntity.Column columnEntity) {
        return new DatabaseField(new Table(entity), Column.of(columnEntity));
    }

    public String get_tableColumn() {
        return handleSelection(".");
    }

    public String get_columnAlias() {
        return handleSelection("_");
    }

    private String handleSelection(String delimiter) {
        return String.format("%s%s%s", table.getKey(), delimiter, column.title());
    }

    public boolean is_key() {
        return Arrays.stream(_idIndicators).anyMatch(column::contains);
    }

    public record Column(String title, String alias) implements Unit {

        public Column(Member member) {
            this(get_columnTitle(member));
        }

        public Column(DatabaseEntity.Column column) {
            this(column.title());
        }

        private Column(String title) {
            this(title, toAlias(title));
        }

        public static Column of(Member member, DatabaseEntity.Column columnEntity) {
            return new Column(ifNotEmpty(columnEntity.title())
                    .otherwise(get_columnTitle(member))
            );
        }

        public static Column of(DatabaseEntity.Column columnEntity) {
            return new Column(columnEntity.title());
        }

        @Override
        public boolean contains(String value) {
            return title.contains(value) || alias.contains(value);
        }

        @Override
        public String getKey() {
            return ifNotNull(alias).otherwise(title);
        }
    }

    public record Table(String title, String alias) implements Unit {

        public Table(String title) {
            this(title, toAlias(title));
        }
        
        public Table(DatabaseEntity entity) {
            this(entity.title(), toAlias(entity.title()));
        }

        public Table(Class<?> clazz) {
            this(
                    get_tableTitle(clazz),
                    toAlias(get_tableTitle(clazz))
            );
        }

        @Override
        public boolean contains(String value) {
            return title.contains(value) || alias.contains(value);
        }

        @Override
        public String getKey() {
            return ifNotNull(alias).otherwise(title);
        }
    }
}