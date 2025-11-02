package laustrup.bandwichpersistence.core.services.builders;

public class HistoryBuilder {

  private static HistoryBuilder _instance;

  public static HistoryBuilder get_instance() {
    if (_instance == null)
      _instance = new HistoryBuilder();

    return _instance;
  }

  private HistoryBuilder() {
  }

  //TODO
}
