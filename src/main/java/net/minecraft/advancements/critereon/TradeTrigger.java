package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class TradeTrigger extends SimpleCriterionTrigger<TradeTrigger.TriggerInstance> {
   public TradeTrigger.TriggerInstance createInstance(JsonObject p_286654_, Optional<ContextAwarePredicate> p_298851_, DeserializationContext p_286772_) {
      Optional<ContextAwarePredicate> optional = EntityPredicate.fromJson(p_286654_, "villager", p_286772_);
      Optional<ItemPredicate> optional1 = ItemPredicate.fromJson(p_286654_.get("item"));
      return new TradeTrigger.TriggerInstance(p_298851_, optional, optional1);
   }

   public void trigger(ServerPlayer p_70960_, AbstractVillager p_70961_, ItemStack p_70962_) {
      LootContext lootcontext = EntityPredicate.createContext(p_70960_, p_70961_);
      this.trigger(p_70960_, (p_70970_) -> {
         return p_70970_.matches(lootcontext, p_70962_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ContextAwarePredicate> villager;
      private final Optional<ItemPredicate> item;

      public TriggerInstance(Optional<ContextAwarePredicate> p_300551_, Optional<ContextAwarePredicate> p_298281_, Optional<ItemPredicate> p_297475_) {
         super(p_300551_);
         this.villager = p_298281_;
         this.item = p_297475_;
      }

      public static Criterion<TradeTrigger.TriggerInstance> tradedWithVillager() {
         return CriteriaTriggers.TRADE.createCriterion(new TradeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
      }

      public static Criterion<TradeTrigger.TriggerInstance> tradedWithVillager(EntityPredicate.Builder p_191437_) {
         return CriteriaTriggers.TRADE.createCriterion(new TradeTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(p_191437_)), Optional.empty(), Optional.empty()));
      }

      public boolean matches(LootContext p_70985_, ItemStack p_70986_) {
         if (this.villager.isPresent() && !this.villager.get().matches(p_70985_)) {
            return false;
         } else {
            return !this.item.isPresent() || this.item.get().matches(p_70986_);
         }
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.item.ifPresent((p_301255_) -> {
            jsonobject.add("item", p_301255_.serializeToJson());
         });
         this.villager.ifPresent((p_299256_) -> {
            jsonobject.add("villager", p_299256_.toJson());
         });
         return jsonobject;
      }
   }
}