package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class UsedEnderEyeTrigger extends SimpleCriterionTrigger<UsedEnderEyeTrigger.TriggerInstance> {
   public UsedEnderEyeTrigger.TriggerInstance createInstance(JsonObject p_286861_, Optional<ContextAwarePredicate> p_300695_, DeserializationContext p_286916_) {
      MinMaxBounds.Doubles minmaxbounds$doubles = MinMaxBounds.Doubles.fromJson(p_286861_.get("distance"));
      return new UsedEnderEyeTrigger.TriggerInstance(p_300695_, minmaxbounds$doubles);
   }

   public void trigger(ServerPlayer p_73936_, BlockPos p_73937_) {
      double d0 = p_73936_.getX() - (double)p_73937_.getX();
      double d1 = p_73936_.getZ() - (double)p_73937_.getZ();
      double d2 = d0 * d0 + d1 * d1;
      this.trigger(p_73936_, (p_73934_) -> {
         return p_73934_.matches(d2);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final MinMaxBounds.Doubles level;

      public TriggerInstance(Optional<ContextAwarePredicate> p_298303_, MinMaxBounds.Doubles p_286810_) {
         super(p_298303_);
         this.level = p_286810_;
      }

      public boolean matches(double p_73952_) {
         return this.level.matchesSqr(p_73952_);
      }
   }
}