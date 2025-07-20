package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Address;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Phone;
import laustrup.bandwichpersistence.core.services.TableAnnotationService;
import lombok.Getter;

import java.lang.reflect.Field;

import static java.util.Optional.ofNullable;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getDeclared;
import static laustrup.bandwichpersistence.core.services.TableAnnotationService.toAlias;

@Getter
public enum DatabaseTable {
    CONTACT_INFO(ContactInfo.class),
    PHONE(Phone.class),
    ADDRESS(Address.class),
    COUNTRY(ContactInfo.Country.class),
    ARTIST(Artist.class),
    BAND_MEMBERSHIP(Band.Membership.class),
    BAND(Band.class),
    ORGANISATION_EMPLOYEE(Employee.class),
    ORGANISATION_EMPLOYMENT(getDeclared(Employee.class, Employee.Fields._roles)),
    AUTHORITIES(User.Authority.class),
    SUBSCRIPTION(Subscription.class),
    CHAT_ROOM(ChatRoom.class),
    MESSAGE(Message.class);

    private final Class<?> _clazz;

    private final Field _field;

    DatabaseTable(Class<?> clazz) {
        _clazz = clazz;
        _field = null;
    }

    DatabaseTable(Field field) {
        _field = field;
        _clazz = null;
    }

    public String get_title() {
        return ofNullable(_clazz)
                .map(TableAnnotationService::get_tableTitle)
                .orElse(ofNullable(_field)
                        .map(TableAnnotationService::get_tableTitle)
                        .orElse(null)
                );
    }

    public String get_alias() {
        return toAlias(get_title());
    }
}
