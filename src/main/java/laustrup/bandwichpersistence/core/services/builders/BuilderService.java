package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition;
import laustrup.bandwichpersistence.core.repositories.bandwich.BandwichEntityDataCollection;
import laustrup.bandwichpersistence.core.services.ModelService;
import laustrup.bandwichpersistence.core.services.TypeService;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.lang.reflect.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.services.ClassFieldService.*;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

abstract class BuilderService<MODEL> {

    private final Logger _logger;

    private static DatabaseDefinition.Entity _entity;

    protected Map<? extends Member, AtomicReference<?>> _fields;

    protected BuilderService() {
        _logger = Logger.getLogger(new TypeService<MODEL>().getClassOfType().getName());
        _entity = get_entityData();
        _fields = _entity.get_columns().keySet().stream()
                .map(field ->
                        new AbstractMap.SimpleImmutableEntry<>(field, new AtomicReference<>())
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    protected static String classToTableName(Class<?>... classes) {
        return classToTableName(Arrays.stream(classes).map(Class::getSimpleName).toArray(String[]::new));
    }

    protected static String classToTableName(String... titles) {
        return String.join("", titles);
    }

    void printError(Member id, Exception exception) {
        _logger.warning(String.format(
                "Could not build object with id %s:\n%s",
                id.getDeclaringClass().getSimpleName(),
                exception.getMessage()
        ));
        throw new RuntimeException(exception);
    }

    static <MODEL> DatabaseDefinition.Entity get_entityData() {
        return (DatabaseDefinition.Entity) BandwichEntityDataCollection.get_instance()
                .get(new TypeService<MODEL>().getClassOfType());
    }

    public MODEL build(ResultSet resultSet) {
        interaction(
                resultSet,
                () -> _entity.get_columns().entrySet().forEach(column -> {
                    if (memberIsCollection(column.getKey()))
                        combine(
                                get_field(column.getKey().getName()),
                                get_BuilderService(column.getKey().getDeclaringClass()).build(resultSet)
                        );
                    else if (memberIsPartOfEntity(column.getKey()))
                        get_BuilderService(column.getKey().getDeclaringClass()).complete(
                                get_field(column.getKey().getName()),
                                resultSet
                        );
                    else
                        set(_fields, column);
                }),
                _entity.get_primaries().get_data()
        );

        return construct();
    }

    private BuilderService<?> get_BuilderService(Class<?> clazz) {
        return BandwichBuilderServiceCollection.getInstance()
                .get_builderService(clazz);
    }

    public void complete(AtomicReference<MODEL> reference, ResultSet resultSet) {
        this.complete(reference, this.build(resultSet));
    }

    public void complete(AtomicReference<MODEL> reference, MODEL model) {
        if (reference.get() == null) {
            reference.set(model);
            return ;
        }

        completion(reference.get(), model);
    }

    protected abstract void completion(MODEL reference, MODEL object);

    protected abstract MODEL construct();

    protected <COMBINED> void combine(Seszt<COMBINED> collection, Seszt<COMBINED> entities) {
        entities.forEach(entity -> combine(collection, entity));
    }

    public <COMBINED> Seszt<COMBINED> combine(Seszt<COMBINED> collection, COMBINED entity) {
        AtomicBoolean isIdentical = new AtomicBoolean(true);
        AtomicInteger counter = new AtomicInteger(0);

        collection.forEach(m -> {
            if (ModelService.equals(m, entity)) {
                collection.get_data()[counter.get()] = entity;
                return ;
            }
            counter.incrementAndGet();
        });

        if (!isIdentical.get())
            collection.add(entity);

        return collection;
    }

    protected void interaction(ResultSet resultSet, Runnable action, Member... primaries) {
        try {
            JDBCService.build(resultSet, action, primaries);
        } catch (SQLException exception) {
            printError(primaries[0], exception);
        }
    }

    public void interaction(ResultSet resultSet, Runnable action, Function<Member, Boolean> breaker, Member... ids) {
        try {
            JDBCService.build(resultSet, action, breaker, ids);
        } catch (SQLException exception) {
            for (Member id : ids)
                printError(id, exception);
        }
    }

    public void interaction(ResultSet resultSet, Runnable action) {
        JDBCService.build(resultSet, () -> {
            action.run();
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    protected <FIELD> FIELD get_field(String name) {
        return (FIELD) _fields.get(getDeclared(new TypeService<MODEL>().getClassOfType(), name));
    }
}
