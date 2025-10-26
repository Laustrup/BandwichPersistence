package laustrup.bandwichpersistence.core.persistence.worm.models;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.models.EntityDataCollection;
import laustrup.bandwichpersistence.core.persistence.models.members.SimpleField;
import laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Junction;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.lang.reflect.Member;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static laustrup.bandwichpersistence.core.persistence.models.EntityDataCollection.Key.conjunctionKey;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.getIdColumnOf;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.*;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getDeclared;
import static laustrup.bandwichpersistence.core.services.collections.MapService.collectMap;

public interface DatabaseDefinition {

    String get_title();

    Class<?> get_class();

    Map<? extends Member, DatabaseField> get_columns();

    Seszt<Member> get_primaries();

    Where.Condition joinOf(DatabaseDefinition external);

    Map.Entry<EntityDataCollection.Key, DatabaseDefinition> toEntry();

    default DatabaseField get_databaseField(Member member) {
        return get_columns().get(member);
    }

    String get_idReference();

    default DatabaseField get_databaseFieldWithReference(Member member) {
        String idReference = DatabaseDefinitionService.getIdReference(member.getDeclaringClass());
        DatabaseField databaseField = get_databaseField(member);

        return new DatabaseField(databaseField.table(), new DatabaseField.Column(idReference, toAlias(idReference)));
    }

    @Getter
    class Entity implements DatabaseDefinition {

        private final Class<?> _class;
        private final String _title;
        private final String _idReference;
        private final Map<? extends Member, DatabaseField> _columns;

        private Entity(
                Class<?> clazz,
                String title,
                String idReference,
                Map<? extends Member, DatabaseField> columns
        ) {
            if (clazz == null)
                throw new IllegalArgumentException(String.format("clazz cannot be null in %s!", getClass().getName()));

            _class = clazz;
            _title = title;
            _idReference = idReference;
            _columns = columns;
        }

        public Entity(Class<?> clazz) {
            this(
                    clazz,
                    getTableTitle(clazz),
                    DatabaseDefinitionService.getIdReference(clazz),
                    DatabaseColumnService.get_columns(clazz)
            );
        }

        public Seszt<Member> get_primaries() {
            return new Seszt<>(_columns.keySet().stream()
                    .filter(member -> Optional.ofNullable(getTableColumn(member))
                            .map(Table.Column::isPrimary)
                            .orElse(false)
                    )
            );
        }

        public static DatabaseDefinition.Conjunction of(DatabaseDefinition.Entity target, DatabaseDefinition.Entity common) {
            return new DatabaseDefinition.Conjunction(target, common);
        }

        @Override
        public Where.Condition joinOf(DatabaseDefinition external) {
            return Where.Condition.equals(
                    get_databaseFieldWithReference(getDeclared(get_class(), getIdColumnOf(get_class()))),
                    external.get_databaseFieldWithReference(getDeclared(external.get_class(), getIdColumnOf(external.get_class())))
            );
        }

        @Override
        public Map.Entry<EntityDataCollection.Key, DatabaseDefinition> toEntry() {
            return new AbstractMap.SimpleImmutableEntry<>(new EntityDataCollection.Key(get_class()), this);
        }
    }

    record Conjunction(Entity target, Entity common) implements DatabaseDefinition {

        public Conjunction(DatabaseDefinition target, DatabaseDefinition common) {
            this((Entity) target, (Entity) common);
        }

        public static Conjunction of(Class<?> target, Class<?> common) {
            if (target == null || common == null)
                throw new IllegalArgumentException("Neither target nor common class is allowed to be null!");

            return new Conjunction(new Entity(target), new Entity(common));
        }

        private Junction get_metaData() {
            return get_junction(common.get_class());
        }

        private Table.Column[] get_metaDataColumns() {
            Junction metaData = get_metaData();
            return Liszt.of(metaData.entityColumns())
                    .Add(metaData.additionalColumns())
                    .toArray(Table.Column[]::new);
        }

        @Override
        public String get_title() {
            return conjunctionKey(target.get_class(), common.get_class());
        }

        @Override
        public Class<?> get_class() {
            return target.get_class();
        }

        @Override
        public Map<? extends Member, DatabaseField> get_columns() {
            return collectMap(Arrays.stream(get_metaDataColumns())
                    .map(column -> new DatabaseField(
                            new DatabaseField.Table(get_title(), toAlias(get_title())),
                            new DatabaseField.Column(TableColumnData.of(get_class(), column).member())
                    )).map(field -> new AbstractMap.SimpleImmutableEntry<>(new SimpleField(get_class(), field), field))
            );
        }

        @Override
        public Seszt<Member> get_primaries() {
            Table.Column[] columns = get_metaDataColumns();

            return new Seszt<>(Arrays.stream(columns)
                    .filter(Table.Column::isPrimary)
                    .map(column -> new SimpleField(get_class(), column))
            );
        }

        @Override
        public Where.Condition joinOf(DatabaseDefinition external) {
            return Where.Condition.equals(
                    get_databaseFieldWithReference(getDeclared(get_class(), Model.ModelDTO.Fields.id)),
                    external.get_databaseFieldWithReference(getDeclared(external.get_class(), Model.ModelDTO.Fields.id))
            );
        }

        @Override
        public Map.Entry<EntityDataCollection.Key, DatabaseDefinition> toEntry() {
            return new AbstractMap.SimpleImmutableEntry<>(get_collectionKey(), this);
        }

        @Override
        public String get_idReference() {
            return String.join(get_title(), "id");
        }

        public EntityDataCollection.Key get_collectionKey() {
            return new EntityDataCollection.Key(target.get_class(), common.get_class());
        }
    }
}