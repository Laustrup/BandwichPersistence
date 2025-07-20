package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.users.Artist;

import java.util.function.BiFunction;

import static laustrup.bandwichpersistence.core.services.TableAnnotationService.get_tableTitle;

public enum ConjunctionTable {
    EMPLOYEE_AUTHORIZATIONS(Employee.class, User.Authority.class),
    ARTIST_AUTHORIZATIONS(Artist.class, User.Authority.class),
    ORGANISATION_EMPLOYEE_CHAT_ROOM(Employee.class, ChatRoom.class),
    ARTIST_CHAT_ROOMS(Artist.class, ChatRoom.class),;

    private final Class<?> _inner;
    private final Class<?> _outer;
    private final BiFunction<Class<?>, Class<?>, String> _title;

    ConjunctionTable(Class<?> inner, Class<?> outer, BiFunction<Class<?>, Class<?>, String> title) {
        _inner = inner;
        _outer = outer;
        _title = title;
    }

    ConjunctionTable(Class<?> inner, Class<?> outer) {
        _inner = inner;
        _outer = outer;
        _title = ConjunctionTable::constructTitle;
    }

    public String get_title() {
        return _title.apply(_inner, _outer);
    }

    private static String constructTitle(Class<?> inner, Class<?> outer) {
        String innerTitle = get_tableTitle(inner);

        return innerTitle.substring(0, innerTitle.length() - 1) + "_" + get_tableTitle(outer);
    }
}
