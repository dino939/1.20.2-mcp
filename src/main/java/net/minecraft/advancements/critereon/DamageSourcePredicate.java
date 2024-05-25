package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.phys.Vec3;

public record DamageSourcePredicate(List<TagPredicate<DamageType>> tags, Optional<EntityPredicate> directEntity, Optional<EntityPredicate> sourceEntity) {
   public static final Codec<DamageSourcePredicate> CODEC = RecordCodecBuilder.create((p_301331_) -> {
      return p_301331_.group(ExtraCodecs.strictOptionalField(TagPredicate.codec(Registries.DAMAGE_TYPE).listOf(), "tags", List.of()).forGetter(DamageSourcePredicate::tags), ExtraCodecs.strictOptionalField(EntityPredicate.CODEC, "direct_entity").forGetter(DamageSourcePredicate::directEntity), ExtraCodecs.strictOptionalField(EntityPredicate.CODEC, "source_entity").forGetter(DamageSourcePredicate::sourceEntity)).apply(p_301331_, DamageSourcePredicate::new);
   });

   public boolean matches(ServerPlayer p_25449_, DamageSource p_25450_) {
      return this.matches(p_25449_.serverLevel(), p_25449_.position(), p_25450_);
   }

   public boolean matches(ServerLevel p_25445_, Vec3 p_25446_, DamageSource p_25447_) {
      for(TagPredicate<DamageType> tagpredicate : this.tags) {
         if (!tagpredicate.matches(p_25447_.typeHolder())) {
            return false;
         }
      }

      if (this.directEntity.isPresent() && !this.directEntity.get().matches(p_25445_, p_25446_, p_25447_.getDirectEntity())) {
         return false;
      } else {
         return !this.sourceEntity.isPresent() || this.sourceEntity.get().matches(p_25445_, p_25446_, p_25447_.getEntity());
      }
   }

   public static Optional<DamageSourcePredicate> fromJson(@Nullable JsonElement p_25452_) {
      return p_25452_ != null && !p_25452_.isJsonNull() ? Optional.of(Util.getOrThrow(CODEC.parse(JsonOps.INSTANCE, p_25452_), JsonParseException::new)) : Optional.empty();
   }

   public JsonElement serializeToJson() {
      return Util.getOrThrow(CODEC.encodeStart(JsonOps.INSTANCE, this), IllegalStateException::new);
   }

   public static class Builder {
      private final ImmutableList.Builder<TagPredicate<DamageType>> tags = ImmutableList.builder();
      private Optional<EntityPredicate> directEntity = Optional.empty();
      private Optional<EntityPredicate> sourceEntity = Optional.empty();

      public static DamageSourcePredicate.Builder damageType() {
         return new DamageSourcePredicate.Builder();
      }

      public DamageSourcePredicate.Builder tag(TagPredicate<DamageType> p_270455_) {
         this.tags.add(p_270455_);
         return this;
      }

      public DamageSourcePredicate.Builder direct(EntityPredicate.Builder p_25473_) {
         this.directEntity = Optional.of(p_25473_.build());
         return this;
      }

      public DamageSourcePredicate.Builder source(EntityPredicate.Builder p_148232_) {
         this.sourceEntity = Optional.of(p_148232_.build());
         return this;
      }

      public DamageSourcePredicate build() {
         return new DamageSourcePredicate(this.tags.build(), this.directEntity, this.sourceEntity);
      }
   }
}