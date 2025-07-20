package laustrup.bandwichpersistence.core.models.identification;

import lombok.Getter;

@Getter
public abstract class CommonIdentity<SIGNATURE extends Signature<?>> implements Identity<SIGNATURE> {

    protected final SIGNATURE _identifier;

    public CommonIdentity(SIGNATURE identifier) {
        _identifier = identifier;
    }

    @Override
    public Identifier<SIGNATURE> getIdentifier() {
        return Identifier.of(_identifier);
    }

    @Override
    public String toString() {
        return _identifier.toString();
    }
}
