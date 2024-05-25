package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class DistanceTrigger extends SimpleCriterionTrigger<DistanceTrigger.TriggerInstance> {
   public DistanceTrigger.TriggerInstance createInstance(JsonObject p_286885_, Optional<ContextAwarePredicate> p_297431_, DeserializationContext p_286678_) {
      Optional<LocationPredicate> optional = LocationPredicate.fromJson(p_286885_.get("start_position"));
      Optional<DistancePredicate> optional1 = DistancePredicate.fromJson(p_286885_.get("distance"));
      return new DistanceTrigger.TriggerInstance(p_297431_, optional, optional1);
   }

   public void trigger(ServerPlayer p_186166_, Vec3 p_186167_) {
      Vec3 vec3 = p_186166_.position();
      this.trigger(p_186166_, (p_284572_) -> {
         return p_284572_.matches(p_186166_.serverLevel(), p_186167_, vec3);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<LocationPredicate> startPosition;
      private final Optional<DistancePredicate> distance;

      public TriggerInstance(Optional<ContextAwarePredicate> p_299242_, Optional<LocationPredicate> p_300746_, Optional<DistancePredicate> p_300813_) {
         super(p_299242_);
         this.startPosition = p_300746_;
         this.distance = p_300813_;
      }

      public static Criterion<DistanceTrigger.TriggerInstance> fallFromHeight(EntityPredicate.Builder p_186198_, DistancePredicate p_186199_, LocationPredicate.Builder p_300400_) {
         return CriteriaTriggers.FALL_FROM_HEIGHT.createCriterion(new DistanceTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(p_186198_)), Optional.of(p_300400_.build()), Optional.of(p_186199_)));
      }

      public static Criterion<DistanceTrigger.TriggerInstance> rideEntityInLava(EntityPredicate.Builder p_186195_, DistancePredicate p_186196_) {
         return CriteriaTriggers.RIDE_ENTITY_IN_LAVA_TRIGGER.createCriterion(new DistanceTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(p_186195_)), Optional.empty(), Optional.of(p_186196_)));
      }

      public static Criterion<DistanceTrigger.TriggerInstance> travelledThroughNether(DistancePredicate p_186193_) {
         return CriteriaTriggers.NETHER_TRAVEL.createCriterion(new DistanceTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(p_186193_)));
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.startPosition.ifPresent((p_300367_) -> {
            jsonobject.add("start_position", p_300367_.serializeToJson());
         });
         this.distance.ifPresent((p_298273_) -> {
            jsonobject.add("distance", p_298273_.serializeToJson());
         });
         return jsonobject;
      }

      public boolean matches(ServerLevel p_186189_, Vec3 p_186190_, Vec3 p_186191_) {
         if (this.startPosition.isPresent() && !this.startPosition.get().matches(p_186189_, p_186190_.x, p_186190_.y, p_186190_.z)) {
            return false;
         } else {
            return !this.distance.isPresent() || this.distance.get().matches(p_186190_.x, p_186190_.y, p_186190_.z, p_186191_.x, p_186191_.y, p_186191_.z);
         }
      }
   }
}