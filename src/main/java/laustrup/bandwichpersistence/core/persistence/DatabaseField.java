package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.persistence.worm.annotations.DatabaseEntity;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.*;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;

public record DatabaseField(Table table, Column column) {

    private interface Unit {

        boolean contains(String value);
        String getKey();
    }

    private DatabaseField(Configuration configuration) {
        this(configuration.get_table(), configuration.get_column());
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

    public static DatabaseField of(Class<?> entity, DatabaseEntity.Column column) {
        return new DatabaseField(new Configuration(entity, column));
    }

    public static DatabaseField referenceOf(Class<?> entity, Class<?> target) {
        return referenceOf(get_databaseEntity(entity), get_databaseEntity(target));
    }

    public static DatabaseField referenceOf(DatabaseEntity entity, DatabaseEntity target) {
        return new DatabaseField(new Table(entity), new Column(get_idReference(target)));
    }

    public static DatabaseField of(Configuration configuration) {
        return new DatabaseField(configuration);
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

    public static class Configuration {

        private final Class<?> _declaringClass;
        private final DatabaseEntity _entity;
        private final DatabaseEntity.Column _column;

        private Configuration(Class<?> entity, DatabaseEntity.Column column) {
            if (entity == null)
                throw new IllegalArgumentException("Entity of database field configuration is null");
            if (column == null)
                throw new IllegalArgumentException("Column of database field configuration is null");

            _entity = get_databaseEntity(entity);

            if (_entity == null)
                throw new IllegalArgumentException(String.format(
                        "Could not define metadata from entity of %s in database field configuration!",
                        entity.getSimpleName()
                ));

            _declaringClass = entity;
            _column = column;
        }

        private Configuration(Class<?> entity, String columnName) {
            this(entity, get_entityColumn(entity, columnName));
        }

        public static Configuration databaseFieldConfiguration(Class<?> entity, DatabaseEntity.Column columnEntity) {
            return new Configuration(entity, columnEntity);
        }

        public static Configuration databaseFieldConfiguration(Class<?> entity, String columnName) {
            return new Configuration(entity, columnName);
        }

        public Table get_table() {
            return new Table(_declaringClass);
        }

        public Column get_column() {
            return Column.of(_column);
        }
    }

    public record Column(String title, String alias) implements Unit {

        public Column(Member member) {
            this(get_columnTitle(member));
        }

        public Column(DatabaseEntity.Column column) {
            this(column.value());
        }

        private Column(String title) {
            this(title, toAlias(title));
        }

        public static Column of(Member member, DatabaseEntity.Column columnEntity) {
            return new Column(ifNotEmpty(columnEntity.value())
                    .otherwise(get_columnTitle(member))
            );
        }

        public static Column of(DatabaseEntity.Column columnEntity) {
            return new Column(columnEntity.value());
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
            this(entity.value(), toAlias(entity.value()));
        }

        public Table(Class<?> clazz) {
            this(get_tableTitle(clazz), toAlias(get_tableTitle(clazz)));
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