package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.User;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Tag extends Model<Tag.Id, Signature.UUID> {

  private User<User.Id> _tagger;

  private User<User.Id> _target;

  private Model<Identity<?>, ?> _model;

  public Tag(
      Id id,
      User<User.Id> tagger,
      User<User.Id> target,
      Model<Identity<?>, ?> model,
      Instant timestamp
  ) {
    super(id, tagger.get_title() + "-" + target.get_title(), timestamp);
    _tagger = tagger;
    _target = target;
    _model = model;
  }

  public static class Id extends CommonIdentity<Signature.UUID> {

    public Id(Signature.UUID signature) {
      super(signature);
    }

    public Id(java.util.UUID signature) {
      super(new Signature.UUID(signature));
    }

    @Override
    public Class<?> getOwnerClassType() {
      return Tag.class;
    }
  }
}
