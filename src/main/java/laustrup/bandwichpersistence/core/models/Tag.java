package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Tag extends Model<Tag.Id, Signature.UUID> {

  private final User<User.Id> _tagger;

  private final User<User.Id> _target;

  private final Model<Identity<?>, ?> _model;

  @Table.Constructor
  public Tag(
      Id id,
      User<User.Id> tagger,
      User<User.Id> target,
      Model<Identity<?>, ?> model,
      Instant timestamp
  ) {
    super(id, timestamp);
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
