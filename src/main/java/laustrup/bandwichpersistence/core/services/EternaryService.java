package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.utilities.Coollection;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EternaryService {

  public static <ITEM> Itemernary<ITEM> ifNotNull(ITEM item) {
    return stating(item != null, item);
  }

  public static <ITEM> Itemernary<ITEM> ifNull(ITEM item) {
    return stating(item == null, item);
  }

  public static Eternary ifEmpty(String string) {
    return stating(string == null || string.isEmpty());
  }

  public static Itemernary<String> ifNotEmpty(String string) {
    return stating(string != null && !string.isEmpty(), string);
  }

  public static <ITEM> Itemernary<ITEM> stating(boolean success, ITEM item) {
    return new Itemernary<>(success, item);
  }

  @SafeVarargs
  public static <CANDIDATE> Eternary isSame(CANDIDATE candidate, CANDIDATE other, CANDIDATE... candidates) {
    return stating(Stream.of(candidates)
        .allMatch(candidate::equals) && other.equals(candidate)
    );
  }

  public static Eternary stating(boolean success) {
    return new Eternary(success);
  }

  public static <ELEMENT> Binder<ELEMENT> stating(Coollection<Operator.Property<ELEMENT>> properties) {
    return new Binder<>(properties);
  }

  static <ELEMENT> boolean nextCondition(boolean isSuccess, Operator.Property<ELEMENT> properties) {
    return nextCondition(isSuccess, Liszt.of(properties));
  }

  static <ELEMENT> boolean nextCondition(boolean isSuccess, Coollection<Operator.Property<ELEMENT>> properties) {
    return isSuccess && properties.stream()
        .filter(Operator.Property::is_success)
        .toList()
        .size() <= 1;
  }

  @FieldNameConstants
  public static class Eternary {

    protected boolean _success;

    public Eternary(boolean success) {
      _success = success;
    }

    public <CANDIDATE> Binder<CANDIDATE> then(Supplier<CANDIDATE> candidate) {
      return new Binder<>(new Operator.Property<>(candidate, _success));
    }

    public <CANDIDATE> Binder<CANDIDATE> then(CANDIDATE candidate) {
      return then(() -> candidate);
    }

    public <CANDIDATE> CANDIDATE thenElseNull(CANDIDATE element) {
      return then(element)
          .orElseNull();
    }
  }

  public static class Itemernary<ITEM> extends Eternary {

    private final ITEM _item;

    public Itemernary(boolean success, ITEM item) {
      super(success);
      _item = item;
    }

    public ITEM otherwise(ITEM item) {
      return _success ? _item : item;
    }

    public <RETURN> Optional<RETURN> get(Function<ITEM, RETURN> action) {
      return _success
          ? Optional.ofNullable(thenElseNull(action.apply(_item)))
          : Optional.empty();
    }

    public ITEM elseNull() {
      return _success ? _item : null;
    }
  }

  @FieldNameConstants
  public static class Binder<ELEMENT> extends Operator<ELEMENT> {

    @SafeVarargs
    public Binder(Operator.Property<ELEMENT>... properties) {
      super(properties);
    }

    public Binder(Coollection<Operator.Property<ELEMENT>> properties) {
      super(properties);
    }

    public Binder<ELEMENT> or(Collection<Property<ELEMENT>> properties) {
      get_properties().addAll(properties);
      return this;
    }

    public Binder<ELEMENT> or(Property<ELEMENT> property) {
      _properties.add(property);
      return new Binder<>(_properties);
    }
  }

  @FieldNameConstants
  @Getter
  public static class Operator<ELEMENT> {

    protected Coollection<Property<ELEMENT>> _properties;

    @SafeVarargs
    public Operator(Property<ELEMENT>... properties) {
      this(Liszt.of(properties));
    }

    public Operator(Coollection<Property<ELEMENT>> properties) {
      _properties = properties;
    }

    public ELEMENT orElse(ELEMENT alternative) {
      return orElse(() ->  alternative);
    }

    public ELEMENT orElse(Supplier<ELEMENT> action) {
      return orEmpty()
          .orElseGet(action);
    }

    public Optional<ELEMENT> orEmpty() {
      return findSuccessfulPropertyOption()
          .map(Supplier::get);
    }

    public ELEMENT orElseThrow(Exception exception) throws Exception {
      Optional<ELEMENT> item = findSuccessfulPropertyOption()
          .map(Supplier::get);

      return exception != null
          ? item.orElseThrow(() -> exception)
          : item.orElseThrow();
    }

    public ELEMENT orElseThrow() throws Exception {
      return orElseThrow(null);
    }

    private Optional<Supplier<ELEMENT>> findSuccessfulPropertyOption() {
      return _properties.stream()
          .filter(Property::is_success)
          .map(Property::get_option)
          .findFirst();
    }

    public ELEMENT orElseNull() {
      return orElse(() -> null);
    }

    @FieldNameConstants
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Property<ELEMENT> {

      @Getter
      private final Supplier<ELEMENT> _option;
      private final Boolean _success;
      private final Predicate<ELEMENT> _condition;

      Property(Supplier<ELEMENT> option, Boolean success) {
        this(option, success, null);
      }

      Property(Supplier<ELEMENT> option, Predicate<ELEMENT> condition) {
        this(option, null, condition);
      }

      public boolean is_success() {
        return get_success().orElse(
            get_condition().map(predication -> predication.test(_option.get()))
                .orElse(false)
            );
      }

      private Optional<Boolean> get_success() {
        return Optional.ofNullable(_success);
      }

      private Optional<Predicate<ELEMENT>> get_condition() {
        return Optional.ofNullable(_condition);
      }

      public static Case inCase(boolean condition) {
        return inCase(() -> condition);
      }

      public static Case inCase(Supplier<Boolean> condition) {
        return new Case(condition);
      }

      public record Case(Supplier<Boolean> condition) {

        public Case(boolean condition) {
          this(() -> condition);
        }

        public <OPTION> Property<OPTION> then(OPTION option) {
          return then(() -> option);
        }

        public <OPTION> Property<OPTION> then(Supplier<OPTION> option) {
          return new Property<>(option, condition.get());
        }
      }
    }
  }
}
