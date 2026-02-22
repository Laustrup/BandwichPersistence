package laustrup.bandwichpersistence.core.persistence.worm.services;

import laustrup.bandwichpersistence.core.persistence.models.members.IdReferenceMember;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Junction;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Supplier;

import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.get_databaseDefinition;
import static laustrup.bandwichpersistence.core.persistence.worm.services.NamingService.toColumnTitle;
import static laustrup.bandwichpersistence.core.persistence.worm.services.NamingService.toTableTitle;
import static laustrup.bandwichpersistence.core.services.EternaryService.Operator.Property.inCase;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public class IdReferenceService {

  public static Table.IdReference handleIdReference(Class<?> clazz) {
    Annotation annotation = get_databaseDefinition(clazz)
        .orElseThrow(() -> new IllegalStateException("Couldn't define database definition for " + clazz.getSimpleName()));

    return stating(Liszt.of(
        inCase(clazz.isAnnotationPresent(Table.class))
            .then(() -> ((Table) annotation).idReference()),
        inCase(clazz.isAnnotationPresent(Junction.class))
            .then(() -> ((Junction) annotation).idReference()),
        inCase(clazz.isAnnotationPresent(Table.Enum.class))
            .then(() -> ((Table.Enum) annotation).idReference())
    )).orElseNull();
  }

  public static Optional<String> getIdReferenceTitle(Class<?> entity) {
    if (entity == null)
      return Optional.empty();

    Table.IdReference idReference = handleIdReference(entity);

    if (idReference == null)
      return Optional.empty();

    return Optional.of(getIdReferenceTitle(entity, idReference));
  }

  public static String getIdReferenceTitle(Class<?> entity, Table.IdReference idReference) {
    return ifNotEmpty(idReference.value())
        .then(() -> toColumnTitle(new IdReferenceMember(entity, idReference.value())))
        .orElse(() -> defaultIdReference(entity).get());
  }

  public static Supplier<String> defaultIdReference(Class<?> clazz) {
    return () -> toTableTitle(clazz) + "_id";
  }
}
