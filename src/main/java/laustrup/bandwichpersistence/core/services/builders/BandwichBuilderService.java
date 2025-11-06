package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.repositories.bandwich.BandwichEntityDataCollection;

public abstract class BandwichBuilderService<BANDWICH_MODEL> extends BuilderService<BANDWICH_MODEL> {
  protected BandwichBuilderService() {
    super(BandwichEntityDataCollection.get_instance());
  }
}
