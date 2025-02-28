package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.users.Band;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

public class Relation {

    @Getter @AllArgsConstructor
    public static class VenueOrganisations {

        private Venue venue;

        private Set<Organisation> organisations;
    }

    @Getter @AllArgsConstructor
    public static class OrganisationVenues {

        private Organisation _organisation;

        private Set<Venue> _venues;
    }

    @Getter @AllArgsConstructor
    public static class MemberBands {

        private Band.Membership member;

        private Set<Band> bands;
    }

    @Getter @AllArgsConstructor
    public static class BandMembers {

        private Band band;

        private Set<Band.Membership> members;
    }
}
