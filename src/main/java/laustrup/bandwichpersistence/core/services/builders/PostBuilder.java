package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostBuilder extends BandwichBuilderService<Post> {

  private static PostBuilder _instance;

  public static PostBuilder get_instance() {
    if (_instance == null)
      _instance = new PostBuilder();

    return _instance;
  }

  @Override
  protected void completion(Post reference, Post object) {

  }
}
