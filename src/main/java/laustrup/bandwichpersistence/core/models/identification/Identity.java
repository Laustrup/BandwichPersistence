package laustrup.bandwichpersistence.core.models.identification;

import lombok.Getter;

import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;
import static laustrup.bandwichpersistence.core.services.ObjectService.ifExists;

public interface Identity<SIGNATURE extends Signature<?>> {

    Identifier<SIGNATURE> getIdentifier();

    Class<?> getOwnerClassType();

    default SIGNATURE get_signature() {
        return ifExists(getIdentifier(), Identifier::get_signature);
    }

    default <SIGNATURE_VALUE> SIGNATURE_VALUE get_value() {
        return ifNotNull(getIdentifier()).get(Identifier::get_value);
    }

    default boolean classIsSameAs(Class<?> clazz) {
        return getOwnerClassType().equals(clazz);
    }

    @Getter
    class Identifier<SIGNATURE extends Signature<?>> {

        SIGNATURE _signature;

        public Identifier(SIGNATURE signature) {
            _signature = signature;
        }

        public static <STATIC_SIGNATURE extends Signature<?>> Identifier<STATIC_SIGNATURE> of(STATIC_SIGNATURE signature) {
            return new Identifier<>(signature);
        }

        @SuppressWarnings("unchecked")
        public <SIGNATURE_VALUE> SIGNATURE_VALUE get_value() {
            return (SIGNATURE_VALUE) ifNotNull(_signature).get(signature -> signature.get_value());
        }
    }
}