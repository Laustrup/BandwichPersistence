package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Login;

public class LoginBuilder extends BandwichBuilderService<Login> {

  private static LoginBuilder _instance;

  public static LoginBuilder get_instance() {
    if (_instance == null)
      _instance = new LoginBuilder();

    return _instance;
  }

  private LoginBuilder() {

  }

  @Override
  protected void completion(Login reference, Login object) {

  }
}
