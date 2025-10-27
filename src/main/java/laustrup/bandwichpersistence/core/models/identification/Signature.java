package laustrup.bandwichpersistence.core.models.identification;

import lombok.extern.slf4j.Slf4j;

public interface Signature<VALUE> {

  VALUE get_value();

  default Class<? extends Signature<?>> getInstance(Option option) {
    return switch (option) {
      case UUID -> UUID.class;
    };
  }

  @SuppressWarnings("unchecked")
  default <SIGNATURE extends Signature<SIGNATURE_TYPE>, SIGNATURE_TYPE> SIGNATURE from(SIGNATURE_TYPE signatureType) {
    String signatureClassName = signatureType.getClass().getSimpleName();

    return (SIGNATURE) switch (signatureClassName) {
      case "UUID" -> new UUID((java.util.UUID) signatureType);
      default -> throw new IllegalStateException("Unexpected value of signature: " + signatureClassName);
    };
  }

  abstract class CommonSignature<VALUE> implements Signature<VALUE> {

    protected final VALUE _value;

    public CommonSignature(VALUE value) {
      _value = value;
    }

    @Override
    public VALUE get_value() {
      return _value;
    }

    @Override
    public String toString() {
      return _value.toString();
    }
  }

  @Slf4j
  class UUID extends CommonSignature<java.util.UUID> {

    public UUID(java.util.UUID uuid) {
      super(uuid);
    }

    public static Signature<java.util.UUID> fromString(String signature) {
      try {
        return new UUID(java.util.UUID.fromString(signature));
      } catch (IllegalArgumentException e) {
        log.warn("Invalid UUID: {}", signature);
        return null;
      }
    }
  }

  enum Option {
    UUID
  }
}
