package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseJunction;
import laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where;
import laustrup.bandwichpersistence.core.services.DatabaseDefinitionService;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.lang.reflect.Member;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

import static laustrup.bandwichpersistence.core.persistence.models.EntityDataCollection.Key.conjunctionKey;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineTitle;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getDeclared;
import static laustrup.bandwichpersistence.core.services.DatabaseDefinitionService.*;
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
        String idReference = DatabaseDefinitionService.get_idReference(member.getDeclaringClass());
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
            _class = clazz;
            _title = title;
            _idReference = idReference;
            _columns = columns;
        }

        public Entity(Class<?> clazz) {
            this(
                    clazz,
                    get_tableTitle(clazz),
                    DatabaseDefinitionService.get_idReference(clazz),
                    DatabaseColumnService.get_columns(clazz)
            );
        }

        public Seszt<Member> get_primaries() {
            return new Seszt<>(_columns.keySet().stream()
                    .filter(member -> get_databaseEntityColumn(member).isPrimary())
            );
        }

        public static DatabaseDefinition.Entity of(DatabaseDefinition.Entity target, DatabaseDefinition.Entity common, Member... members) {
            String title = defineTitle(target.get_title(), common.get_title());

            return new DatabaseDefinition.Entity(
                    null,
                    title,
                    title + "_id",
                    collectMap(Arrays.stream(members)
                            .map(member -> new AbstractMap.SimpleImmutableEntry<>(member, DatabaseField.of(member)))
                    )
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
            return new AbstractMap.SimpleImmutableEntry<>(new EntityDataCollection.Key(get_class()), this);
        }
    }

    record Conjunction(Entity target, Entity common) implements DatabaseDefinition {

        public Conjunction(DatabaseDefinition target, DatabaseDefinition common) {
            this((Entity) target, (Entity) common);
        }

        public static Conjunction of(Class<?> target, Class<?> common) {
            return new Conjunction(new Entity(target), new Entity(common));
        }

        private DatabaseJunction get_metaData() {
            return get_databaseJunction(common.get_class());
        }

        private DatabaseEntity.Column[] get_metaDataColumns() {
            DatabaseJunction metaData = get_metaData();
            return Liszt.of(metaData.entityColumns())
                    .Add(metaData.additionalColumns())
                    .toArray(DatabaseEntity.Column[]::new);
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
                            new DatabaseField.Column(column)
                    )).map(field -> new AbstractMap.SimpleImmutableEntry<>(new CommonMember(get_class(), field), field))
            );
        }

        @Override
        public Seszt<Member> get_primaries() {
            DatabaseEntity.Column[] columns = get_metaDataColumns();

            return new Seszt<>(Arrays.stream(columns)
                    .filter(DatabaseEntity.Column::isPrimary)
                    .map(column -> new CommonMember(get_class(), column))
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
            return String.join("_", get_title(), "id");
        }

        public EntityDataCollection.Key get_collectionKey() {
            return new EntityDataCollection.Key(target.get_class(), common.get_class());
        }
    }
}