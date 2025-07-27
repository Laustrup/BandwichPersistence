package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.models.ConjunctionTable;
import laustrup.bandwichpersistence.core.persistence.models.DatabaseTable;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Clause.Clausement;
import laustrup.bandwichpersistence.core.services.TableAnnotationService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.join;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.EQUALS;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.IS_NULL;
import static laustrup.bandwichpersistence.core.services.TableAnnotationService.get_tableTitle;
import static laustrup.bandwichpersistence.core.services.TableAnnotationService.toAlias;

public class SelectService {

    public static Selecting selecting(String table) {
        return new Selecting(new Selecting.Properties(table));
    }

    public static Selecting selecting(Selecting.Properties properties) {
        return new Selecting(properties);
    }

    public static class Selecting {

        private interface ISubSelecting {
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
                    "select%s%sfrom %s",
                    _properties.is_distinct() ? " distinct " : " ",
                    _properties.get_selections().stream()
                            .map(Field::get_content)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("* "),
                    _properties.get_table()
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
            String
                    joins = _joins.stream()
                            .map(Join::apply)
                            .reduce((a, b) -> a + "\n" + b)
                            .orElse(""),
                    where = _properties.get_that()
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

            private final Set<Field> _selections;

            private final Optional<Clausement> _that;

            private final String _table;

            public Properties(String table) {
                this(table, false, new Seszt<>());
            }

            public Properties(String table, boolean distinct, Set<Field> selections) {
                this(table, distinct, selections, Optional.empty());
            }

            public Properties(String table, Clausement where) {
                this(table, false, new Seszt<>(), Optional.of(where));
            }

            public Properties(String table, Set<Field> selections) {
                this(table, false, selections, Optional.empty());
            }

            public Properties(String table, boolean distinct, Optional<Clausement> that) {
                this(table, distinct, new Seszt<>(), that);
            }

            public Properties(String table, boolean distinct, Set<Field> selections, Optional<Clausement> that) {
                if (table == null)
                    throw new NullPointerException("table can't be null for selecting properties!");

                _table = table;
                _distinct = distinct;
                _selections = selections;
                _that = that;
            }

            public String getSelects() {
                return _selections == null || _selections.isEmpty() ? "*" : _selections.stream()
                        .map(Field::get_content)
                        .reduce((a,b) -> a + b)
                        .orElse("*");
            }
        }

        public record Table(String title) {

            public Table(Class<?> clazz) {
                this(get_tableTitle(clazz));
            }

            public String alias() {
                return toAlias(title);
            }
        }

        @Getter
        public static class Join implements ISubSelecting {

            private final Area _area;

            private final String _table;

            private final String _alias;

            private final Seszt<Product> _products;

            public Join(Area area, Condition condition) {
                this(area, condition.get_this().alias(), Seszt.of(Product.of(condition)));
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
                _alias = TableAnnotationService.toAlias(table);
                _products = products;
            }

            public static Join left(ConjunctionTable table, Condition... conditions) {
                return left(table, conditions);
            }

            public static Join left(DatabaseTable table, Condition... conditions) {
                return left(table.get_title(), conditions);
            }

            public static Join left(String table, Condition... condition) {
                return new Join(Area.LEFT, table, new Seszt<>(Product.of(condition)));
            }

            public static Join left(String table, Product... products) {
                return new Join(Area.LEFT, table, new Seszt<>(products));
            }

            public static Join left(Field internal, Field external) {
                return new Join(Area.LEFT, Condition.equals(internal, external));
            }

            public static Join inner(Field internal, Field external) {
                return new Join(Area.INNER, Condition.equals(internal, external));
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

            public record Product(Seszt<Condition> _conditions) {

                public static Product of(Condition... conditions) {
                    return new Product(new Seszt<>(conditions));
                }

                public String apply() {
                    String statement = _conditions.stream()
                            .map(Condition::apply)
                            .reduce((a, b) -> join(" || ", a, b))
                            .orElse("");

                    return statement.isEmpty() ? "" : (
                            statement.length() > 0
                            ? String.format("(%s)", statement)
                            : statement
                    );
                }
            }
        }

        @Getter
        public static class Where implements ISubSelecting {

            private static Clause _clause;

            public static Clause complying() {
                _clause = new Clause();
                return _clause;
            }

            @Override
            public String apply() {
                return _clause.apply();
            }

            public static class Clause implements ISubSelecting {

                private static Clausement _clausement;

                public Clausement which(Condition condition) {
                    _clausement = new Clausement(condition.apply());
                    return _clausement;
                }

                @Override
                public String apply() {
                    return _clausement.apply();
                }

                public static class Clausement implements ISubSelecting {

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
            public static class Condition implements ISubSelecting {

                private final Field _this;

                private final Field _that;

                private final Object _thing;

                private final Selection _selection;

                private final Equation _equation;

                public Condition(Field thiz, Equation equation, Field that) {
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

                public Condition(Field thiz, Equation equation, Object thing) {
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

                public Condition(Field thiz, Equation equation) {
                    if (thiz == null)
                        throw new NullPointerException("this can't be null for selecting properties!");
                    validateEquation(equation, false, false);

                    _this = thiz;
                    _that = null;
                    _equation = equation;
                    _selection = null;
                    _thing = null;
                }

                public Condition(Field thiz, Equation equation, Selection selection) {
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

                public static Condition equals(Field thiz, String thing) {
                    return of(thiz, EQUALS, thing);
                }

                public static Condition equals(Field thiz, Field that) {
                    return of(thiz, EQUALS, that);
                }

                public static Condition of(Field thiz, Equation equation, Field that) {
                    return new Condition(thiz, equation, that);
                }

                public static Condition of(Field thiz, Equation equation) {
                    return new Condition(thiz, equation);
                }

                public static Condition of(Field thiz, Equation equation, String thing) {
                    return new Condition(thiz, equation, thing);
                }

                public static Condition of(Field thiz, Equation equation, Selection selection) {
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
