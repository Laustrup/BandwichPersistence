package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Whereing;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.String.join;

public class SelectService {

    public static Selecting selecting(String table) {
        return new Selecting(new Selecting.Properties(table));
    }

    public static Selecting selecting(Selecting.Properties properties) {
        return new Selecting(properties);
    }

    public static class Selecting {

        private final Properties _properties;

        private String _statement = "";

        public Selecting(Properties properties) {
            if (properties == null)
                throw new NullPointerException("properties can't be null for selecting properties!");

            _properties = properties;
            _statement = defineSelectStatement();
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

        public Selecting addJoin(Join join) {
            _statement += join.apply();
            return this;
        }

        public String select() {
            return _statement + _properties.get_where().map(Whereing::apply).orElse("");
        }

        @Getter
        public static class Properties {

            private final boolean _distinct;

            private final Set<Field> _selections;

            private final Optional<Whereing> _where;

            private final String _table;

            public Properties(String table) {
                this(table, false, new Seszt<>());
            }

            public Properties(String table, boolean distinct, Set<Field> selections) {
                this(table, distinct, selections, Optional.empty());
            }

            public Properties(String table, Whereing where) {
                this(table, false, new Seszt<>(), Optional.of(where));
            }

            public Properties(String table, Set<Field> selections) {
                this(table, false, selections, Optional.empty());
            }

            public Properties(String table, boolean distinct, Set<Field> selections, Optional<Whereing> where) {
                if (table == null)
                    throw new NullPointerException("table can't be null for selecting properties!");

                _table = table;
                _distinct = distinct;
                _selections = selections;
                _where = where;
            }

            public String getSelects() {
                return _selections == null || _selections.isEmpty() ? "*" : _selections.stream()
                        .map(Field::get_content)
                        .reduce((a,b) -> a + b)
                        .orElse("*");
            }
        }

        @Getter
        public static class Join {

            private final Area _area;

            private final String _table;

            private final String _alias;

            private final Field _internal;

            private final Field _external;

            public Join(Area area, String table, String alias, Field internal, Field external) {
                if (area == null)
                    throw  new NullPointerException("area can't be null for selecting properties!");
                if (table == null)
                    throw new NullPointerException("table can't be null for selecting properties!");
                if (alias == null)
                    throw new NullPointerException("alias can't be null for selecting properties!");
                if (internal == null)
                    throw new NullPointerException("internal can't be null for selecting properties!");
                if (external == null)
                    throw new NullPointerException("external can't be null for selecting properties!");
                _area = area;
                _table = table;
                _alias = alias;
                _internal = internal;
                _external = external;
            }

            public String apply() {
                return format(
                        /*language=MySQL*/ " %s join %s %s on %s = %s",
                        _area.get_statement(),
                        _table,
                        _alias,
                        _internal.get_content(),
                        _external.get_content()
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
        }

        @Getter
        public static class Where {

            public static Whereing complying() {
                return new Whereing();
            }

            public static class Whereing {

                private String _statement;

                public Whereing() {
                    _statement = "";
                }

                public Whereing that(Condition condition) {
                    if (!_statement.isEmpty())
                        throw new IllegalStateException("Multiple conditions in where must be acompanied with a gate statement!");

                    _statement += condition.apply();

                    return this;
                }

                public Whereing and(Condition condition) {
                    _statement += gate(() -> Gate.AND.get_statement() + condition.apply());

                    return this;
                }

                public Whereing or(Condition condition) {
                    _statement += gate(() -> Gate.OR.get_statement() + condition.apply());

                    return this;
                }

                private String gate(Supplier<String> action) {
                    if (_statement.isEmpty())
                        throw new IllegalStateException("Must have a statement in where in order to add gate of statements!");

                    return action.get();
                }

                public String apply() {
                    return _statement.isEmpty() ? "" : format(
                            /*language=MySQL*/ " where %s",
                            _statement
                    );
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
            public static class Condition {

                private final Field _this;

                private final Field _that;

                private final String _selection;

                private final Equation _equation;

                public Condition(Field thiz, Equation equation, Field that) {
                    if (thiz == null)
                        throw new NullPointerException("This can't be null for selecting properties!");
                    if (that == null)
                        throw new NullPointerException("That can't be null for selecting properties!");
                    switch (equation) {
                        case IN, NOT_IN, IS_NULL, IS_NOT_NULL -> throw new IllegalArgumentException(format(
                                "Equation %s not allow for where condition with this and that!",
                                equation
                        ));
                        case null ->  throw new NullPointerException("Equation is needed for where condition with this and that!");
                        default -> {}
                    }

                    _this = thiz;
                    _that = that;
                    _equation = equation;
                    _selection = null;
                }

                public Condition(Field thiz, Equation equation) {
                    if (thiz == null)
                        throw new NullPointerException("this can't be null for selecting properties!");
                    switch (equation) {
                        case EQUALS, NOT_EQUALS, IN, NOT_IN -> throw new IllegalArgumentException(format(
                                "Equation %s not allow for where condition with this!",
                                equation
                        ));
                        case null ->  throw new NullPointerException("Equation is needed for where condition with this!");
                        default -> {}
                    }

                    _this = thiz;
                    _that = null;
                    _equation = equation;
                    _selection = null;
                }

                public String apply() {
                    return format(
                            /*language=MySQL*/ "%s = %s",
                            _this.get_content(),
                            _that.get_content()
                    );
                }

                @Getter
                public enum Equation {
                    EQUALS(" = "),
                    NOT_EQUALS(" != "),
                    IN(" in "),
                    NOT_IN(" not in "),
                    IS_NULL(" is null "),
                    IS_NOT_NULL(" is not null ");

                    private final String _statement;

                    Equation(String statement) {
                        _statement = statement;
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
