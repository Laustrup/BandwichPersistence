package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.utilities.Coollection;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class EternaryService {

    public static <ELEMENT> Itemernary<ELEMENT> ifNotNull(ELEMENT element) {
        return stating(element != null, element);
    }

    public static <ELEMENT> Itemernary<ELEMENT> ifNull(ELEMENT element) {
        return stating(element == null, element);
    }

    @SafeVarargs
    public static <ELEMENT> Eternary isSame(ELEMENT element, ELEMENT other, ELEMENT... elements) {
        return stating(Arrays.stream(elements)
                .allMatch(index -> index == element && index == other));
    }

    public static Eternary stating(boolean success) {
        return new Eternary(success);
    }

    public static <ITEM> Itemernary<ITEM> stating(boolean success, ITEM item) {
        return new Itemernary<>(success, item);
    }

    @SafeVarargs
    static <ELEMENT> boolean nextCondition(boolean current, Operator.Property<ELEMENT>... properties) {
        return Arrays.stream(properties).noneMatch(Operator.Property::is_success) && current;
    }

    @SuppressWarnings("unchecked")
    static <ELEMENT> boolean nextCondition(boolean current, Coollection<Operator.Property<ELEMENT>> properties) {
        return nextCondition(current, properties.get_data());
    }

    @FieldNameConstants
    public static class Eternary {

        protected boolean _success;

        public Eternary(boolean success) {
            _success = success;
        }

        public <ITEM> Binder<ITEM> then(ITEM element) {
            Operator.Property<ITEM> property = new Operator.Property<>(element, _success);
            return new Binder<>(new Operator.Property<>(element, nextCondition(_success, property)));
        }

        public <ITEM> ITEM thenElseNull(ITEM element) {
            return then(element).orElseNull();
        }
    }

    public static class Itemernary<ITEM> extends Eternary {

        private ITEM _item;

        public Itemernary(boolean success, ITEM item) {
            super(success);
            _item = item;
        }

        public <RETURN> RETURN otherwise(Function<ITEM, RETURN> action) {
            return !_success ? thenElseNull(action.apply(_item)) : null;
        }

        public <RETURN> RETURN get(Function<ITEM, RETURN> action) {
            return _success ? thenElseNull(action.apply(_item)) : null;
        }
    }

    @FieldNameConstants
    public static class Binder<ELEMENT> extends Operator<ELEMENT>{

        @SafeVarargs
        public Binder(Operator.Property<ELEMENT>... properties) {
            super(properties);
        }

        public Binder<ELEMENT> or(ELEMENT element, Predicate<ELEMENT> predicate) {
            _properties.add(new Property<>(element, nextCondition(predicate.test(element), _properties)));

            return new Binder<>(_properties.get_data());
        }
    }

    @FieldNameConstants
    public static class Operator<ITEM> {

        protected Liszt<Property<ITEM>> _properties;

        @SafeVarargs
        public Operator(Property<ITEM>... properties) {
            _properties = new Liszt<>(properties);
        }

        public ITEM orElse(ITEM alternative) {
            return _properties.stream()
                    .filter(Property::is_success)
                    .map(Property::get_option)
                    .findFirst()
                    .orElse(alternative);
        }

        public ITEM orElseNull() {
            return orElse(null);
        }

        @Getter
        @FieldNameConstants
        public static class Property<ELEMENT> {

            private ELEMENT _option;
            private boolean _success;

            public Property(ELEMENT option, boolean success) {
                _option = option;
                _success = success;
            }

            public static <M> Property<M> of(M option, boolean condition) {
                return new Property<>(option, condition);
            }
        }
    }
}
