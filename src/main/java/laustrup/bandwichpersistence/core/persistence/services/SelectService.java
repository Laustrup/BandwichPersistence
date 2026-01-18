package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.libraries.PathLibrary;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Clause.Clausement;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition;
import laustrup.bandwichpersistence.core.services.FileService;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.String.join;
import static laustrup.bandwichpersistence.core.persistence.DatabaseField.toSelections;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.EQUALS;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.IS_NULL;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.get_databaseDefinitionTitle;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.toAlias;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public abstract class SelectService {

  public static Selecting selecting(String table) {
    return new Selecting(new Selecting.Properties(table));
  }

  public static Selecting selecting(Selecting.Properties properties) {
    return new Selecting(properties);
  }

  public static class SelectException extends RuntimeException {

    private SelectException(String message) {
      super(message);
    }

    public static SelectException noExternalDatabaseDefinition(DatabaseDefinition[] internals) {
      return new SelectException(String.format("There wasn't any external database definition with internals of %s",
          Arrays.stream(internals)
              .map(DatabaseDefinition::get_title)
              .collect(Collectors.joining())
      ));
    }

    public static SelectException noInternalDatabaseDefinition(DatabaseDefinition external) {
      return new SelectException(String.format("There wasn't any internal database definition of external %s",
          external.get_title()
      ));
    }

    public static SelectException noExternalOrInternalDatabaseDefinition() {
      return new SelectException("There could neither be found an internal nor external database definition!");
    }
  }

  public static class Selecting {

    private interface Selector {
      String apply();
    }

    private static final Seszt<Class<?>> _entityClasses;

    static {
      try {
        _entityClasses = FileService.getClasses(PathLibrary.get_entitiesDirectoryPath());
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Couldn't get the entity classes for Selecting class!", e);
      }
    }

    private final Properties _properties;

    private final String _statement;

    private final Seszt<Join> _joins;

    public Selecting(Properties properties) {
      if (properties == null)
        throw new NullPointerException("properties can't be null for selecting properties!");

      _properties = properties;
      _statement = defineSelectStatement();
      _joins = new Seszt<>();
    }

    private String defineSelectStatement() {
      return /*language=MySQL*/ format(
          "select%s%sfrom %s %s",
          _properties.is_distinct() ? " distinct" : "",
          _properties.get_selections()
              .apply(),
          _properties.get_table(),
          toAlias(_properties.get_table())
      );
    }

    public Selecting addJoins(Join... joins) {
      Arrays.stream(joins).forEach(this::addJoin);
      return this;
    }

    public Selecting addJoin(Join join) {
      _joins.add(join);
      return this;
    }

    public String select() {
      StringBuilder joins = new StringBuilder();

      for (Join join : _joins)
        joins.append(joins.isEmpty() ? "" : "\n").append(join.apply());

      String where = _properties.get_where()
          .map(Clausement::apply)
          .orElse("");
      boolean
          containsJoins = !_joins.isEmpty(),
          containsWhere = !where.isEmpty();

      return String.format("\n%s\n%s%s%s%s", _statement, joins, containsJoins ? "\n" : "", where, containsWhere ? "\n" : "");
    }

    @Getter
    public static class Properties {

      private final boolean _distinct;

      private final Selections _selections;

      private final Clausement _where;

      private final String _table;

      public Properties(Class<?> clazz) {
        this(Selections.of(clazz), clazz);
      }

      public Properties(String table) {
        this(Selections.asterisk(), table, false);
      }

      public Properties(Selections selections, String table, boolean distinct) {
        this(selections, table, null, distinct);
      }

      public Properties(Selections selections, Class<?> table) {
        this(selections, get_databaseDefinitionTitle(table), null, false);
      }

      public Properties(Class<?> table, Clausement where) {
        this(Selections.asterisk(), get_databaseDefinitionTitle(table), where, false);
      }

      public Properties(String table, boolean distinct, Clausement where) {
        this(Selections.asterisk(), table, where, distinct);
      }

      public Properties(Selections selections, String table, Clausement where, boolean distinct) {
        if (table == null)
          throw new NullPointerException("table can't be null for selecting properties!");

        _table = table;
        _distinct = distinct;
        _selections = selections;
        _where = where;
      }

      public Optional<Clausement> get_where() {
        return Optional.ofNullable(_where);
      }

      public static class Selections implements Selector {

        @SuppressWarnings("unchecked")
        private static final Seszt.Immutable<Class<?>> SELECTION_WHITELIST = Seszt.of(
            CommonIdentity.class,
            String.class,
            Integer.class,
            Boolean.class,
            UUID.class,
            Long.class,
            Float.class
        ).immutable();

        private final Seszt<DatabaseField> _groupings;

        private final Seszt<Class<?>> _classesInSelection;

        private String generateSelectionRow(DatabaseField databaseField) {
          return String.format("%s%s",
              databaseField.get_tableColumn(),
              stating(!databaseField.get_tableColumn().equals(databaseField.get_entityColumn()))
                  .then(" " + databaseField.get_entityColumn())
                  .orElse("")
          );
        }

        public Selections(Seszt<DatabaseField> groupings) {
          _groupings = groupings;
          _classesInSelection = new Seszt<>(_groupings.stream().map(DatabaseField::getEntity));
        }

        public static Selections asterisk() {
          return new Selections(new Seszt<>());
        }

        public static Selections of(Class<?>... classes) {
          return new Selections(new Seszt<>(Arrays.stream(classes)
              .flatMap(clazz ->
                  toSelections(new DatabaseDefinition.Entity(clazz).get_columns()).stream()
              ).distinct()
          ));
        }

        public static int orderGroupings(DatabaseField first, DatabaseField next) {
          if (first.getEntity() != next.getEntity())
            return 0;

          Predicate<Class<?>> declaresOfFieldNameConstants = declared ->
              declared.getSimpleName().equals("Fields") &&
              declared.isEnum();

          Function<DatabaseField, Integer> calculation = field ->
            Arrays.stream(field.getEntity().getDeclaredClasses())
                .filter(declaresOfFieldNameConstants)
                .flatMap(declared -> Arrays.stream(declared.getEnumConstants())
                    .map(constant -> (Enum<?>) constant)
                ).filter(constant -> constant.name().equals(field.column().getTitle()))
                .map(Enum::ordinal)
                .findFirst()
                .orElse(0);

          return Liszt.of(first, next).stream()
              .map(calculation)
              .reduce((a, b) -> a - b)
              .orElse(0);
        }

        //TODO Improve performance
        private Stream<DatabaseField> includeChildFields(DatabaseField databaseField) {
          Predicate<DatabaseField> filtering = field -> {
            Class<?> type = (field.getReflectedField().getType());

            return type.isPrimitive() || SELECTION_WHITELIST.stream()
                .anyMatch(clazz -> clazz.isAssignableFrom(type));
          };

          Seszt<DatabaseField> unfilteredFields = new Seszt<>(Seszt.of(databaseField)
              .Add(getChildFields(databaseField).toArray(DatabaseField[]::new)).stream()
              .filter(filtering)
          ).immutable();

          return unfilteredFields.stream();
        }

        //TODO Improve performance
        private Stream<DatabaseField> getChildFields(DatabaseField databaseField) {
          Map<String, Field> fields = new HashMap<>();

          Function<Field, String> generateFieldKey = field -> String.format("%s.%s",
              field.getDeclaringClass().getSimpleName(),
              field.getName()
          );

          Seszt<String> keysOfGroupings = new Seszt<>(_groupings.stream()
              .map(group -> generateFieldKey.apply(group.getReflectedField()))
          ).immutable();

          Function<Class<?>, Boolean> typeIsMissingOrAccepted = clazz ->
              !(_classesInSelection.contains(clazz) && _entityClasses.contains(clazz))
                  && !SELECTION_WHITELIST.contains(clazz);

          Consumer<Stream<DatabaseField>> putAll = databaseFields -> fields.putAll(databaseFields
              .flatMap(databaseFieldEntry -> {
                Field fieldOfDatabase = databaseFieldEntry.getReflectedField();

                return Map.of(
                    generateFieldKey.apply(fieldOfDatabase),
                    fieldOfDatabase
                ).entrySet().parallelStream()
                    .filter(entry -> !keysOfGroupings.contains(entry.getKey()));
              }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (first, second) -> first))
          );

          return Arrays.stream(databaseField.getEntity().getDeclaredFields()).flatMap(field -> {
            putAll.accept(Stream.of(DatabaseField.of(field)));

            Class<?> type = field.getType();

            if (typeIsMissingOrAccepted.apply(type)) {
              Liszt<Field> fieldsOfType = Liszt.of(Arrays.stream(type.getDeclaredFields()));
              _classesInSelection.add(type);
              putAll.accept(fieldsOfType.stream()
                  .map(DatabaseField::of)
              );
              Arrays.stream(type.getDeclaredFields())
                  .filter(typeField -> typeIsMissingOrAccepted.apply(typeField.getType()))
                  .map(DatabaseField::of)
                  .forEach(currentTypeField -> putAll.accept(getChildFields(currentTypeField)));
            }

            return fields.values().stream()
                .map(DatabaseField::of);
          });
        }

        @Override
        public String apply() {
          return stating(_groupings.isEmpty())
              .then(" * ")
              .orElse(() -> String.format("\n\t%s\n",
                  _groupings.stream()
                      .flatMap(this::includeChildFields)
                      .sorted(Properties.Selections::orderGroupings)
                      .map(this::generateSelectionRow)
                      .distinct()
                      .collect(Collectors.joining(",\n\t"))
              ));
        }
      }
    }

    @Getter
    public static class Join implements Selector {

      private final Area _area;

      private final String _table;

      private final String _alias;

      private final Seszt<Product> _products;

      public Join(Area area, String table, Condition... conditions) {
        this(area, table, Seszt.of(Product.of(conditions)));
      }

      public Join(Area area, String table, Product... products) {
        this(area, table, new Seszt<>(products));
      }

      public Join(Area area, String table, Seszt<Product> products) {
        if (area == null)
          throw new NullPointerException("area can't be null for selecting properties!");
        if (table == null)
          throw new NullPointerException("table can't be null for selecting properties!");
        if (products == null || products.isEmpty())
          throw new IllegalArgumentException("Join needs at least one product!");

        _area = area;
        _table = table;
        _alias = toAlias(table);
        _products = products;
      }

      public static Join left(String table, Condition... conditions) {
        return new Join(Area.LEFT, table, new Seszt<>(Product.of(conditions)));
      }

      public static Join left(String table, Stream<Condition> conditions) {
        return left(table, conditions.toArray(Condition[]::new));
      }

      public static Join left(
          DatabaseDefinition external,
          DatabaseDefinition internal,
          DatabaseDefinition... internals
      ) {
        if (external == null && internal == null)
          throw SelectException.noExternalOrInternalDatabaseDefinition();

        if (external == null)
          throw SelectException.noExternalDatabaseDefinition(stating(internals.length > 0)
              .then(internals)
              .orElse(() -> new DatabaseDefinition[]{internal})
          );

        Seszt<DatabaseDefinition> data = new Seszt<>(external, internal);
        data.addAll(Arrays.asList(internals));

        if (data.size() < 2 || data.stream().anyMatch(Objects::isNull))
          throw SelectException.noInternalDatabaseDefinition(external);

        return left(
            external.get_title(),
            data.stream()
                .map(datum -> datum.joinOf(external))
        );
      }

      public static Join left(String table, Product... products) {
        return new Join(Area.LEFT, table, new Seszt<>(products));
      }

      public static Join left(String table, DatabaseField internal, DatabaseField external) {
        return new Join(Area.LEFT, table, Condition.equals(internal, external));
      }

      public static Join inner(Class<?> table, DatabaseField internal, DatabaseField external) {
        return new Join(Area.INNER, get_databaseDefinitionTitle(table), Condition.equals(internal, external));
      }

      @Override
      public String apply() {
        return format(
            /*language=MySQL*/ "%s join %s%s on %s",
            _area.get_statement(),
            _table,
            _alias == null ? "" : " " + _alias,
            _products.stream()
                .map(Product::apply)
                .collect(Collectors.joining(" && "))
        );
      }

      @Getter
      public enum Area {
        INNER("inner"),
        OUTER("outer"),
        LEFT("left"),
        RIGHT("right");

        private final String _statement;

        Area(String statement) {
          _statement = statement;
        }
      }

      public record Product(Seszt<Condition> conditions) {

        public static Product of(Condition... conditions) {
          return new Product(new Seszt<>(conditions));
        }

        public String apply() {
          String statement = conditions.stream()
              .map(Condition::apply)
              .collect(Collectors.joining(" || "));

          return statement.isEmpty() ? "" : (conditions().size() > 1
              ? String.format("(%s)", statement)
              : statement
          );
        }
      }
    }

    @Getter
    public static class Where implements Selector {

      private static Clause _clause;

      public static Clause complying() {
        _clause = new Clause();
        return _clause;
      }

      @Override
      public String apply() {
        return _clause.apply();
      }

      public static class Clause implements Selector {

        private static Clausement _clausement;

        public Clausement which(Condition condition) {
          _clausement = new Clausement(condition.apply());
          return _clausement;
        }

        @Override
        public String apply() {
          return _clausement.apply();
        }

        public static class Clausement implements Selector {

          private String _statement;

          public Clausement(String statement) {
            _statement = statement;
          }

          public Clausement and(Condition condition) {
            _statement += Gate.AND.get_statement() + condition.apply();

            return this;
          }

          public Clausement or(Condition condition) {
            _statement += Gate.OR.get_statement() + condition.apply();

            return this;
          }

          @Override
          public String apply() {
            return _statement.isEmpty() ? "" : format(
                /*language=MySQL*/ "where %s",
                _statement
            );
          }
        }
      }

      @Getter
      public enum Gate {
        AND(" and "),
        OR(" or ");

        private final String _statement;

        Gate(String statement) {
          _statement = statement;
        }
      }

      @Getter
      public static class Condition implements Selector {

        private final DatabaseField _this;

        private final DatabaseField _that;

        private final Object _thing;

        private final Selection _selection;

        private final Equation _equation;

        public Condition(DatabaseField thiz, Equation equation, DatabaseField that) {
          if (thiz == null)
            throw new NullPointerException("This can't be null for selecting properties!");
          if (that == null)
            throw new NullPointerException("That can't be null for selecting properties!");
          validateEquation(equation, true, false);

          _this = thiz;
          _that = that;
          _equation = equation;
          _selection = null;
          _thing = null;
        }

        public Condition(DatabaseField thiz, Equation equation, Object thing) {
          if (thiz == null)
            throw new NullPointerException("This can't be null for selecting properties!");
          if (thing == null)
            throw new NullPointerException("Thing can't be null for selecting properties!");
          validateEquation(equation, true, false);

          _this = thiz;
          _thing = prepareThing(thing);
          _equation = equation;
          _selection = null;
          _that = null;
        }

        public Condition(DatabaseField thiz, Equation equation) {
          if (thiz == null)
            throw new NullPointerException("this can't be null for selecting properties!");
          validateEquation(equation, false, false);

          _this = thiz;
          _that = null;
          _equation = equation;
          _selection = null;
          _thing = null;
        }

        public Condition(DatabaseField thiz, Equation equation, Selection selection) {
          if (thiz == null)
            throw new NullPointerException("this can't be null for selecting properties!");
          if (selection == null)
            throw new NullPointerException("Selection can't be null for selecting properties!");
          validateEquation(equation, true, true);

          _this = thiz;
          _selection = selection;
          _equation = equation;
          _that = null;
          _thing = null;
        }

        public static Condition equals(DatabaseField thiz, String thing) {
          return of(thiz, EQUALS, thing);
        }

        public static Condition equals(DatabaseField thiz, DatabaseField that) {
          return of(thiz, EQUALS, that);
        }

        public static Condition of(DatabaseField thiz, Equation equation, DatabaseField that) {
          return new Condition(thiz, equation, that);
        }

        public static Condition of(DatabaseField thiz, Equation equation) {
          return new Condition(thiz, equation);
        }

        public static Condition of(DatabaseField thiz, Equation equation, String thing) {
          return new Condition(thiz, equation, thing);
        }

        public static Condition of(DatabaseField thiz, Equation equation, Selection selection) {
          return new Condition(thiz, equation, selection);
        }

        private Object prepareThing(Object thing) {
          if (thing instanceof String) {
            String varChar = String.valueOf(thing);

            if (varChar.charAt(0) != '\'')
              thing = "'" + thing;
            if (varChar.charAt(varChar.length() - 1) != '\'')
              thing = thing + "'";
          }

          return thing;
        }

        private void validateEquation(Equation equation, boolean isPlural, boolean isCollection) {
          if (equation == null)
            throw new NullPointerException("Equation must not be null for condition!");

          if (equation.is_plural() != isPlural || equation.is_collection() != isCollection)
            throw new IllegalStateException(format(
                "Equation %s is plural:%s and collection:%s, but should be plural:%s and collection:%s",
                equation.name(),
                equation.is_plural(),
                equation.is_collection(),
                isPlural,
                isCollection
            ));
        }

        @Override
        public String apply() {
          Optional<Object> product = get_product();
          Equation equation = product
              .map(ignored -> _equation)
              .orElse(IS_NULL);

          return format(
              "%s%s%s",
              _this.get_tableColumn(),
              equation.get_statement(),
              product.map(String::valueOf).orElse("")
          );
        }

        private Optional<Object> get_product() {
          return Optional.ofNullable(_that)
              .map(that -> (Object) that.get_tableColumn())
              .or(() -> Optional.ofNullable(_thing)
                  .or(() -> Optional.ofNullable(_selection)
                      .map(Selection::apply)
                  )
              );
        }

        @Getter
        public enum Equation {
          EQUALS(" = ", true, false),
          NOT_EQUALS(" != ", true, false),
          IN(" in ", true, true),
          NOT_IN(" not in ", true, true),
          IS_NULL(" is null ", false, false),
          IS_NOT_NULL(" is not null ", false, false);

          private final String _statement;

          private final boolean _plural;

          private final boolean _collection;

          Equation(String statement, boolean plural, boolean collection) {
            _statement = statement;
            _plural = plural;
            _collection = collection;
          }
        }

        public record Selection(Set<String> _items) implements Selector {

          @Override
          public String apply() {
            return format(
                "(%s)",
                join(", ", _items)
            );
          }
        }
      }
    }
  }
}
