package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Login;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginBuilder extends BandwichBuilderService<Login> {

  private static LoginBuilder _instance;

  public static LoginBuilder get_instance() {
    if (_instance == null)
      _instance = new LoginBuilder();

    return _instance;
  }

  @Override
  protected void completion(Login reference, Login object) {

  }
}
