package laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.repositories.sub_repositories.BandRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.EntityService;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains logic for CRUD of Bands.
 */
public class BandPersistenceService extends EntityService<Band> {

    /**
     * Singleton instance of the Service.
     */
    private static BandPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static BandPersistenceService get_instance() {
        if (_instance == null) _instance = new BandPersistenceService();
        return _instance;
    }

    private BandPersistenceService() {}

    /**
     * Will create a Band by using BandRepository.
     * Only does this, if id doesn't already exist.
     * Will also include the generated key.
     * Uses Assembly to get the values from the database,
     * to insure it exists and also to close connections.
     * @param band The Band that will be created.
     * @param password The password for the Band.
     * @return If success, the created Band with its generated key, otherwise null.
     */
    public Band create(Band band, String password) {
        if (band.get_primaryId() == 0) {
            ResultSet set = BandRepository.get_instance().create(band, password);
            Subscription subscription = band.get_subscription();
            ContactInfo contactInfo = band.get_contactInfo();

            try {
                if (set.isBeforeFirst())
                    set.next();
                band = (Band) Assembly.get_instance().getUserUnassembled(set.getLong("users.id"));
            } catch (SQLException e) {
                Printer.get_instance().print("ResultSet error in Band create service...", e);
                return null;
            }

            //Puts in subscription and contactInfo
            band = new Band(band.get_primaryId(), band.get_username(), band.get_description(),contactInfo,
                    band.get_albums(), band.get_ratings(), band.get_events(),band.get_gigs(),band.get_chatRooms(),
                    subscription,band.get_bulletins(),band.get_members(),
                    band.get_runner(), band.get_fans(),band.get_idols(), band.get_timestamp()
            );

            if (upsert(band))
                return band;
        }
        return null;
    }
}
