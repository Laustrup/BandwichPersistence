package laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.repositories.sub_repositories.ParticipantRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.EntityService;
import laustrup.bandwichpersistence.utilities.printers.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains logic for CRUD of Participants.
 */
public class ParticipantPersistenceService extends EntityService<Participant> {

    /**
     * Singleton instance of the Service.
     */
    private static ParticipantPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ParticipantPersistenceService get_instance() {
        if (_instance == null) _instance = new ParticipantPersistenceService();
        return _instance;
    }

    private ParticipantPersistenceService() {}

    /**
     * Will create a Participant by using ParticipantRepository.
     * Only does this, if id doesn't already exist.
     * Will also include the generated key.
     * Uses Assembly to get the values from the database,
     * to insure it exists and also to close connections.
     * @param participant The Participant that will be created.
     * @param password The password for the Participant.
     * @return If success, the created Participant with its generated key, otherwise null.
     */
    public Participant create(Participant participant, String password) {
        if (participant.get_primaryId() == 0) {
            ResultSet set = ParticipantRepository.get_instance().create(participant, password);
            Subscription subscription = participant.get_subscription();
            ContactInfo contactInfo = participant.get_contactInfo();

            try {
                if (set.isBeforeFirst())
                    set.next();
                participant = (Participant) Assembly.get_instance().getUserUnassembled(set.getLong("users.id"));
            } catch (SQLException e) {
                Printer.get_instance().print("ResultSet error in Band create service...", e);
                return null;
            }

            //Puts in subscription and contactInfo
            participant = new Participant(participant.get_primaryId(), participant.get_username(),
                    participant.get_firstName(), participant.get_lastName(), participant.get_description(),contactInfo,
                    participant.get_albums(), participant.get_ratings(), participant.get_events(),participant.get_chatRooms(),
                    subscription,participant.get_bulletins(), participant.get_idols(),participant.get_timestamp()
            );

            if (upsert(participant))
                return participant;
        }
        return null;
    }
}
