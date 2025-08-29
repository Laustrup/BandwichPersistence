package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityData;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Clause.Clausement;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.String.join;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.EQUALS;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.IS_NULL;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityDataService.classFieldToDatabaseField;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityDataService.toAlias;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public abstract class SelectService {

    public static Selecting selecting(String table) {
        return new Selecting(new Selecting.Properties(table));
    }

    public static Selecting selecting(Selecting.Properties properties) {
        return new Selecting(properties);
    }

    public static class Selecting {

        private interface Selector {
            String apply();
        }

        private final Properties _properties;

        private String _statement = "";

        private Seszt<Join> _joins;

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
                    _properties.is_distinct() ? " distinct " : " ",
                    _properties.get_selections().apply(),
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

            public Properties(String table) {
                this(Selections.asterisk(), table, false);
            }

            public Properties(Selections selections, String table, boolean distinct) {
                this(selections, table, null, distinct);
            }

            public Properties(String table, Clausement where) {
                this(Selections.asterisk(), table, where, false);
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

                private final Map<DatabaseField, String> _groupings;

                private String generateRow(Map.Entry<DatabaseField, String> entry) {
                    return String.format("%s as %s", entry.getKey(), entry.getValue());
                }

                public Selections(Map<DatabaseField, String> groupings) {
                    _groupings = groupings;
                }

                public Selections(DatabaseEntityData configurationData) {
                    _groupings = new HashMap<>(classFieldToDatabaseField(configurationData.get_columns()));
                }

                public static Selections asterisk() {
                    return new Selections(new HashMap<>());
                }

                @SafeVarargs
                public static Selections of(Class<?>... classes) {
                    return new Selections(new HashMap<>(Arrays.stream(classes)
                            .flatMap(clazz ->
                                    classFieldToDatabaseField(new DatabaseEntityData(clazz).get_columns()).entrySet().stream()
                            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
                }

                @Override
                public String apply() {
                    String asterix = "*";

                    return stating(_groupings.isEmpty())
                            .then(asterix)
                            .orElse(() -> _groupings.entrySet().stream()
                                    .map(this::generateRow)
                                    .reduce((a, b) -> String.format("%s\n%s", a, b))
                                    .orElse(asterix)
                            );
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
                    throw  new NullPointerException("area can't be null for selecting properties!");
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

            public static Join left(String table, Product... products) {
                return new Join(Area.LEFT, table, new Seszt<>(products));
            }

            public static Join left(String table, DatabaseField internal, DatabaseField external) {
                return new Join(Area.LEFT, table, Condition.equals(internal, external));
            }

            public static Join inner(String table, DatabaseField internal, DatabaseField external) {
                return new Join(Area.INNER, table, Condition.equals(internal, external));
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
                                .reduce((a, b) -> String.join(" && ", a, b))
                                .orElseThrow(() -> new IllegalStateException("Join needs at least one product!"))
                );
            }

            @Getter
            public enum Area {
                INNER("inner"),
                OUTER("outer"),
                LEFT("left"),
                RIGHT("right");

               private String _statement;

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
                            .reduce((a, b) -> join(" || ", a, b))
                            .orElse("");

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
                            _this.get_content(),
                            equation.get_statement(),
                            product.map(String::valueOf).orElse("")
                    );
                }

                private Optional<Object> get_product() {
                    return Optional.ofNullable(_that)
                            .map(that -> (Object) that.get_content())
                            .or(() -> Optional.ofNullable(_thing)
                                    .or(() -> Optional.ofNullable(_selection)
                                            .map(Selection::apply)
                                    )
                            );
                }

                @Getter
                public enum Equation {
                    EQUALS(" = ", true, false),
                    NOT_EQUALS(" != ",  true, false),
                    IN(" in ", true, true),
                    NOT_IN(" not in ", true,  true),
                    IS_NULL(" is null ",  false, false),
                    IS_NOT_NULL(" is not null ",  false, false);

                    private final String _statement;

                    private boolean _plural;

                    private boolean _collection;

                    Equation(String statement, boolean plural, boolean collection) {
                        _statement = statement;
                        _plural = plural;
                        _collection = collection;
                    }
                }

                @Getter
                public static class Selection {

                    private final Set<String> _items;

                    public Selection(Set<String> items) {
                        _items = items;
                    }

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
