package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;

public class PhoneBuilder extends BuilderService<ContactInfo.Phone> {

    private static PhoneBuilder _instance;

    public static PhoneBuilder get_instance() {
        if (_instance == null)
            _instance = new PhoneBuilder();

        return _instance;
    }

    private PhoneBuilder() {

    }

    @Override
    protected void completion(ContactInfo.Phone reference, ContactInfo.Phone object) {

    }

    @Override
    protected ContactInfo.Phone construct() {
        return new ContactInfo.Phone(
                get_field(ContactInfo.Phone.Fields._countryDigits),
                get_field(ContactInfo.Phone.Fields._numbers),
                get_field(ContactInfo.Phone.Fields._mobile),
                get_field(ContactInfo.Phone.Fields._business)
        );
    }
}
