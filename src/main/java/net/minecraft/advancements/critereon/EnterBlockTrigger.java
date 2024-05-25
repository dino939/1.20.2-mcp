package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EnterBlockTrigger extends SimpleCriterionTrigger<EnterBlockTrigger.TriggerInstance> {
   public EnterBlockTrigger.TriggerInstance createInstance(JsonObject p_286490_, Optional<ContextAwarePredicate> p_297383_, DeserializationContext p_286764_) {
      Block block = deserializeBlock(p_286490_);
      Optional<StatePropertiesPredicate> optional = StatePropertiesPredicate.fromJson(p_286490_.get("state"));
      if (block != null) {
         optional.ifPresent((p_296118_) -> {
            p_296118_.checkState(block.getStateDefinition(), (p_31274_) -> {
               throw new JsonSyntaxException("Block " + block + " has no property " + p_31274_);
            });
         });
      }

      return new EnterBlockTrigger.TriggerInstance(p_297383_, block, optional);
   }

   @Nullable
   private static Block deserializeBlock(JsonObject p_31279_) {
      if (p_31279_.has("block")) {
         ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(p_31279_, "block"));
         return BuiltInRegistries.BLOCK.getOptional(resourcelocation).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
         });
      } else {
         return null;
      }
   }

   public void trigger(ServerPlayer p_31270_, BlockState p_31271_) {
      this.trigger(p_31270_, (p_31277_) -> {
         return p_31277_.matches(p_31271_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      @Nullable
      private final Block block;
      private final Optional<StatePropertiesPredicate> state;

      public TriggerInstance(Optional<ContextAwarePredicate> p_300383_, @Nullable Block p_286517_, Optional<StatePropertiesPredicate> p_299345_) {
         super(p_300383_);
         this.block = p_286517_;
         this.state = p_299345_;
      }

      public static Criterion<EnterBlockTrigger.TriggerInstance> entersBlock(Block p_31298_) {
         return CriteriaTriggers.ENTER_BLOCK.createCriterion(new EnterBlockTrigger.TriggerInstance(Optional.empty(), p_31298_, Optional.empty()));
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         if (this.block != null) {
            jsonobject.addProperty("block", BuiltInRegistries.BLOCK.getKey(this.block).toString());
         }

         this.state.ifPresent((p_298367_) -> {
            jsonobject.add("state", p_298367_.serializeToJson());
         });
         return jsonobject;
      }

      public boolean matches(BlockState p_31300_) {
         if (this.block != null && !p_31300_.is(this.block)) {
            return false;
         } else {
            return !this.state.isPresent() || this.state.get().matches(p_31300_);
         }
      }
   }
}