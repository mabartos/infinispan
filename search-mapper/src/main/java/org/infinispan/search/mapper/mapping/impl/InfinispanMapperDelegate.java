package org.infinispan.search.mapper.mapping.impl;

import org.hibernate.search.engine.search.loading.spi.EntityLoader;
import org.hibernate.search.mapper.pojo.mapping.building.spi.PojoContainedTypeExtendedMappingCollector;
import org.hibernate.search.mapper.pojo.mapping.building.spi.PojoIndexedTypeExtendedMappingCollector;
import org.hibernate.search.mapper.pojo.mapping.building.spi.PojoMapperDelegate;
import org.hibernate.search.mapper.pojo.mapping.spi.PojoMappingDelegate;
import org.hibernate.search.mapper.pojo.model.spi.PojoRawTypeModel;
import org.infinispan.search.mapper.common.EntityReference;
import org.infinispan.search.mapper.mapping.EntityConverter;

public final class InfinispanMapperDelegate implements PojoMapperDelegate<InfinispanMappingPartialBuildState> {

   private final InfinispanTypeContextContainer.Builder typeContextContainerBuilder =
         new InfinispanTypeContextContainer.Builder();
   private final EntityLoader<EntityReference, ?> entityLoader;
   private final EntityConverter entityConverter;

   public InfinispanMapperDelegate(EntityLoader<EntityReference, ?> entityLoader, EntityConverter entityConverter) {
      this.entityLoader = entityLoader;
      this.entityConverter = entityConverter;
   }

   @Override
   public void closeOnFailure() {
      // Nothing to do
   }

   @Override
   public <E> PojoIndexedTypeExtendedMappingCollector createIndexedTypeExtendedMappingCollector(
         PojoRawTypeModel<E> rawTypeModel, String entityName, String indexName) {
      return typeContextContainerBuilder.addIndexed(rawTypeModel, entityName);
   }

   @Override
   public <E> PojoContainedTypeExtendedMappingCollector createContainedTypeExtendedMappingCollector(
         PojoRawTypeModel<E> rawTypeModel, String entityName) {
      // This is a placeholder: we don't care about contained types at the moment.
      return new InfinispanContainedTypeContext.Builder();
   }

   @Override
   public InfinispanMappingPartialBuildState prepareBuild(PojoMappingDelegate mappingDelegate) {
      return new InfinispanMappingPartialBuildState(mappingDelegate, typeContextContainerBuilder.build(),
            entityLoader, entityConverter);
   }
}
