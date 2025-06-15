package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Whereing.Thating;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

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

        private interface ISubSelecting {
            String apply();
        }

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
            return _statement + _properties.get_that()
                    .map(Thating::apply)
                    .orElse("");
        }

        @Getter
        public static class Properties {

            private final boolean _distinct;

            private final Set<Field> _selections;

            private final Optional<Thating> _that;

            private final String _table;

            public Properties(String table) {
                this(table, false, new Seszt<>());
            }

            public Properties(String table, boolean distinct, Set<Field> selections) {
                this(table, distinct, selections, Optional.empty());
            }

            public Properties(String table, Thating where) {
                this(table, false, new Seszt<>(), Optional.of(where));
            }

            public Properties(String table, Set<Field> selections) {
                this(table, false, selections, Optional.empty());
            }

            public Properties(String table, boolean distinct, Set<Field> selections, Optional<Thating> that) {
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

        @Getter
        public static class Join implements ISubSelecting {

            private final Area _area;

            private final String _table;

            private final String _alias;

            private final Field _internal;

            private final Field _external;

            public Join(Area area, String table, Field internal, Field external) {
                if (area == null)
                    throw  new NullPointerException("area can't be null for selecting properties!");
                if (table == null)
                    throw new NullPointerException("table can't be null for selecting properties!");
                if (internal == null)
                    throw new NullPointerException("internal can't be null for selecting properties!");
                if (internal.table() == null)
                    throw new NullPointerException("alias can't be null for selecting properties!");
                if (external == null)
                    throw new NullPointerException("external can't be null for selecting properties!");
                _area = area;
                _table = table;
                _alias = internal.table();
                _internal = internal;
                _external = external;
            }

            public Join(Area area, Field internal, Field external) {
                if (area == null)
                    throw  new NullPointerException("area can't be null for selecting properties!");
                if (internal == null)
                    throw new NullPointerException("internal can't be null for selecting properties!");
                if (internal.table() == null)
                    throw new NullPointerException("table can't be null for selecting properties!");
                if (external == null)
                    throw new NullPointerException("external can't be null for selecting properties!");
                _area = area;
                _table = internal.table();
                _alias = null;
                _internal = internal;
                _external = external;
            }

            public static Join of(Area area, String alias, Field internal, Field external) {
                return new Join(area, alias, internal, external);
            }

            public static Join of(Area area, Field internal, Field external) {
                return new Join(area, internal, external);
            }

            @Override
            public String apply() {
                return format(
                        /*language=MySQL*/ " %s join %s%s on %s = %s",
                        _area.get_statement(),
                        _table,
                        _alias == null ? "" : " " + _alias,
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
        public static class Where implements ISubSelecting {

            private static Whereing _whereing;

            public static Whereing complying() {
                _whereing = new Whereing();
                return _whereing;
            }

            @Override
            public String apply() {
                return _whereing.apply();
            }

            public static class Whereing implements ISubSelecting {

                private static Thating _thating;

                public Thating that(Condition condition) {
                    _thating = new Thating(condition.apply());
                    return _thating;
                }

                @Override
                public String apply() {
                    return _thating.apply();
                }

                public static class Thating implements ISubSelecting {

                    private String _statement;

                    public Thating(String statement) {
                        _statement = statement;
                    }

                    public Thating and(Condition condition) {
                        _statement += Gate.AND.get_statement() + condition.apply();

                        return this;
                    }

                    public Thating or(Condition condition) {
                        _statement += Gate.OR.get_statement() + condition.apply();

                        return this;
                    }

                    @Override
                    public String apply() {
                        return _statement.isEmpty() ? "" : format(
                                /*language=MySQL*/ " where %s",
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

                private final String _thing;

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

                public Condition(Field thiz, Equation equation, String thing) {
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

                private String prepareThing(String thing) {
                    if (_thing == null)
                        thing = "null";
                    else {
                        if (thing.charAt(0) != '\'')
                            thing = "'" + thing;
                        if (thing.charAt(thing.length() - 1) != '\'')
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
                    return format(
                            /*language=MySQL*/ "%s = %s",
                            _this.get_content(),
                            _that.get_content()
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
