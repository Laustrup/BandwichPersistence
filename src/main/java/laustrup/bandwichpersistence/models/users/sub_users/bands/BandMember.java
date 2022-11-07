package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.instruments.Instrument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor @ToString
public class BandMember extends User {

    @Getter
    private List<Band> _bands;

    @Getter
    private List<Instrument> _instruments;

    // Band methods
    public List<Band> addBand(Band band) {
        _bands.add(band);
        return _bands;
    }
    public List<Band> addBands(List<Band> bands) {
        _bands.addAll(bands);
        return _bands;
    }

    public List<Band> removeBands(Band bands) {
        _bands.remove(bands);
        return _bands;
    }
    public List<Band> removeBands(List<Band> bands) {
        _bands.removeAll(bands);
        return _bands;
    }

    // Instruments methods
    public List<Instrument> addInstrument(Instrument instrument) {
        _instruments.add(instrument);
        return _instruments;
    }

    public List<Instrument> addInstruments(List<Instrument> instruments) {
        _instruments.addAll(instruments);
        return _instruments;
    }
    public List<Instrument> removeInstrument(Instrument instrument) {
        _instruments.remove(instrument);
        return _instruments;
    }

    public List<Instrument> removeInstruments(List<Instrument> instruments) {
        _instruments.removeAll(instruments);
        return _instruments;
    }
}
