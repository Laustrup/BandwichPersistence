package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.utilities.Coollection;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

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

    public <CANDIDATE> Binder<CANDIDATE> then(CANDIDATE candidate) {
      return new Binder<>(new Operator.Property<>(candidate, _success));
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

    public Binder(Liszt<Operator.Property<ELEMENT>> properties) {
      super(properties);
    }

    public Binder<ELEMENT> or(Property<ELEMENT> property) {
      return new Binder<>(_properties.Add(property));
    }
  }

  @FieldNameConstants
  @Getter
  public static class Operator<ELEMENT> {

    protected Liszt<Property<ELEMENT>> _properties;

    @SafeVarargs
    public Operator(Property<ELEMENT>... properties) {
      this(Liszt.of(properties));
    }

    public Operator(Liszt<Property<ELEMENT>> properties) {
      _properties = properties;
    }

    public ELEMENT orElse(Supplier<ELEMENT> action) {
      return orElse(action.get());
    }

    public ELEMENT orElseThrow(Exception exception) throws Exception {
      Optional<ELEMENT> item = findSuccessfulProperty()
          .orElse(Optional.empty());

      return exception != null
          ? item.orElseThrow(() -> exception)
          : item.orElseThrow();
    }

    public ELEMENT orElseThrow() throws Exception {
      return orElseThrow(null);
    }

    public ELEMENT orElse(ELEMENT alternative) {
      return findSuccessfulProperty()
          .orElse(Optional.ofNullable(alternative))
          .orElse(null);
    }

    private Optional<Optional<ELEMENT>> findSuccessfulProperty() {
      return _properties.stream()
          .filter(Property::is_success)
          .map(Property::get_option)
          .findFirst();
    }

    public ELEMENT orElseNull() {
      return orElse(() -> null);
    }

    @FieldNameConstants
    public static class Property<ELEMENT> {

      private final Supplier<ELEMENT> _option;
      private final Predicate<ELEMENT> _success;

      public Property(Supplier<ELEMENT> option, Predicate<ELEMENT> success) {
        _option = option;
        _success = success;
      }

      Property(ELEMENT option, boolean success) {
        this(() -> option, ignored -> success);
      }

      public boolean is_success() {
        return _success.test(_option.get());
      }

      public static <STATIC_ELEMENT> Property<STATIC_ELEMENT> of(
          STATIC_ELEMENT option,
          boolean condition
      ) {
        return new Property<>(() -> option, ignored -> condition);
      }

      public static <STATIC_ELEMENT> Property<STATIC_ELEMENT> of(
          STATIC_ELEMENT option,
          Predicate<STATIC_ELEMENT> condition
      ) {
        return new Property<>(() -> option, condition);
      }

      public Optional<ELEMENT> get_option() {
        return Optional.ofNullable(_option.get());
      }
    }
  }
}
