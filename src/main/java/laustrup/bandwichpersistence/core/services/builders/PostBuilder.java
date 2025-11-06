package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.chats.messages.Post;

public class PostBuilder extends BuilderService<Post> {

  private static PostBuilder _instance;

  public static PostBuilder get_instance() {
    if (_instance == null)
      _instance = new PostBuilder();

    return _instance;
  }

  private PostBuilder() {

  }

  @Override
  protected void completion(Post reference, Post object) {

  }
}
