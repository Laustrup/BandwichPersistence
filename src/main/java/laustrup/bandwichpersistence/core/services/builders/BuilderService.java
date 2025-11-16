package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.models.EntityDataCollection;
import laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition;
import laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService;
import laustrup.bandwichpersistence.core.services.EternaryService.Operator;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.services.ClassFieldService.*;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

public abstract class BuilderService<MODEL> {

  private final Logger _logger;

  private static DatabaseDefinition.Entity _entity;

  protected Map<? extends Member, AtomicReference<?>> _fields;

  protected BuilderService(EntityDataCollection collection) {
    _logger = Logger.getLogger(getGeneric().getName());
    _entity = get_entityData(collection);
    _fields = _entity.get_columns().keySet().stream()
        .map(field ->
            new AbstractMap.SimpleImmutableEntry<>(field, new AtomicReference<>())
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @SuppressWarnings("unchecked")
  private Class<MODEL> getGeneric() {
    return (Class<MODEL>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  void printError(Member id, Exception exception) {
    _logger.warning(String.format(
        "Could not build object with id %s:\n%s",
        id.getDeclaringClass().getSimpleName(),
        exception.getMessage()
    ));
    throw new RuntimeException(exception);
  }

  private DatabaseDefinition.Entity get_entityData(EntityDataCollection collection) {
    return (DatabaseDefinition.Entity) collection.get(getGeneric());
  }

  public MODEL build(ResultSet resultSet) {
    interaction(resultSet, handleColumns(resultSet), _entity.get_primaries().get_data());
    return buildFromConstructor();
  }

  private MODEL buildFromConstructor() {
    Constructor<MODEL> constructor;
    try {
      constructor = DatabaseDefinitionService.get_tableConstructor(_entity.get_class());
    } catch (Exception exception) {
      throw new BuilderException(exception);
    }

    if (constructor == null)
      throw new BuilderException("No constructor found for class %s!", _entity.get_class());

    return stating(constructor.getParameterCount() == _fields.size())
        .then(construct(constructor))
        .orElseThrow(new BuilderException(String.format(
            "Parameter count for %s was %s, but builder found %s",
            _entity.get_class().getSimpleName(),
            constructor.getParameterCount(),
            _fields.size()
        )));
  }

  public Supplier<MODEL> construct(Constructor<MODEL> constructor) {
    Function<String, String> exceptionMessage = template ->
        String.format(template, _entity.get_class().getSimpleName());

    if (constructor.accessFlags().stream().noneMatch(flag -> flag == AccessFlag.PUBLIC))
      throw new BuilderException(exceptionMessage.apply("Constructor for %s is not public!"));

    return () -> {
      try {
        return constructor.newInstance(getConstructorParameters(constructor));
      } catch (InstantiationException exception) {
        throw new RuntimeException(exceptionMessage.apply("Couldn't initiate %s in builder!"), exception);
      } catch (IllegalAccessException exception) {
        throw new RuntimeException(exceptionMessage.apply("Couldn't access constructor for %s in builder!"), exception);
      } catch (InvocationTargetException exception) {
        throw new RuntimeException(exceptionMessage.apply("On builder for %s"), exception);
      }
    };
  }

  private Object[] getConstructorParameters(Constructor<MODEL> constructor) {
    return _fields.keySet().stream()
        .sorted(constructorParamComparator(constructor, member -> member.getName().substring(1)))
        .map(field -> _fields.get(field).get())
        .toArray();
  }

  private static <ELEMENT, CLASS> Comparator<ELEMENT> constructorParamComparator(
      Constructor<CLASS> constructor,
      Function<ELEMENT, String> parameterNameIdentifier
  ) {
    Liszt<String> parameterNames = Liszt.of(Arrays.stream(constructor.getParameters()).map(Parameter::getName));
    Function<ELEMENT, Integer> elementIndexOfParameter = (element) ->
        parameterNames.indexOf(parameterNameIdentifier.apply(element));
    BiFunction<ELEMENT, ELEMENT, Integer> comparation = (current, next) -> {
      int
          currentIndex = elementIndexOfParameter.apply(current),
          nextIndex = elementIndexOfParameter.apply(next);

      return stating(Liszt.of(
          Operator.Property.inCase(currentIndex > nextIndex)
              .then(1),
          Operator.Property.inCase(currentIndex < nextIndex)
              .then(-1)
      )).orElse(0);
    };

    return comparation::apply;
  }

  private Runnable handleColumns(ResultSet resultSet) {
    Consumer<Map.Entry<? extends Member, DatabaseField>> handling = column -> {
      if (memberIsCollection(column.getKey()))
        addToCollection(column, resultSet);
      else if (memberIsPartOfEntity(column.getKey()))
        buildEntityMember(column, resultSet);
      else
        set(_fields, column);
    };

    return () -> _entity.get_columns().entrySet()
        .forEach(handling);
  }

  private void addToCollection(Map.Entry<? extends Member, DatabaseField> column, ResultSet resultSet) {
    combine(
        get_field(column.getKey().getName()),
        get_builderService(column).build(resultSet)
    );
  }

  private void buildEntityMember(Map.Entry<? extends Member, DatabaseField> column, ResultSet resultSet) {
    get_builderService(column).complete(
        get_field(column.getKey().getName()),
        resultSet
    );
  }

  private BuilderService<?> get_builderService(Map.Entry<? extends Member, DatabaseField> column) {
    return get_BuilderService(column.getKey().getDeclaringClass());
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
      return;
    }

    completion(reference.get(), model);
  }

  protected abstract void completion(MODEL reference, MODEL object);

  protected <COMBINED> void combine(Seszt<COMBINED> collection, Seszt<COMBINED> entities) {
    entities.forEach(entity -> combine(collection, entity));
  }

  public <COMBINED> Seszt<COMBINED> combine(Seszt<COMBINED> collection, COMBINED entity) {
    AtomicBoolean isIdentical = new AtomicBoolean(true);
    AtomicInteger counter = new AtomicInteger(0);

    collection.forEach(collective -> {
      Predicate<COMBINED> modelEquals = model -> model.equals(entity);
      boolean toStringEquals = collective.toString().equals(entity.toString());

      if (toStringEquals || (collective instanceof Model<?, ?> && modelEquals.test(collective))) {
        collection.get_data()[counter.get()] = entity;
        return;
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
        }
    );
  }

  @SuppressWarnings("unchecked")
  private <FIELD> FIELD get_field(String name) {
    return (FIELD) _fields.get(getDeclared(getGeneric(), name));
  }

  private static class BuilderException extends RuntimeException {

    public BuilderException(String message) {
      super(message);
    }

    public BuilderException(Exception exception) {
      super(exception);
    }

    public BuilderException(String template, Class<?> entity) {
      super(String.format(template, _entity.get_class().getSimpleName()));
    }
  }
}
