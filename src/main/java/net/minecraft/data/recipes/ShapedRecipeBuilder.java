package net.minecraft.data.recipes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class ShapedRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
   private final RecipeCategory category;
   private final Item result;
   private final int count;
   private final List<String> rows = Lists.newArrayList();
   private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
   private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
   @Nullable
   private String group;
   private boolean showNotification = true;

   public ShapedRecipeBuilder(RecipeCategory p_249996_, ItemLike p_251475_, int p_248948_) {
      this.category = p_249996_;
      this.result = p_251475_.asItem();
      this.count = p_248948_;
   }

   public static ShapedRecipeBuilder shaped(RecipeCategory p_250853_, ItemLike p_249747_) {
      return shaped(p_250853_, p_249747_, 1);
   }

   public static ShapedRecipeBuilder shaped(RecipeCategory p_251325_, ItemLike p_250636_, int p_249081_) {
      return new ShapedRecipeBuilder(p_251325_, p_250636_, p_249081_);
   }

   public ShapedRecipeBuilder define(Character p_206417_, TagKey<Item> p_206418_) {
      return this.define(p_206417_, Ingredient.of(p_206418_));
   }

   public ShapedRecipeBuilder define(Character p_126128_, ItemLike p_126129_) {
      return this.define(p_126128_, Ingredient.of(p_126129_));
   }

   public ShapedRecipeBuilder define(Character p_126125_, Ingredient p_126126_) {
      if (this.key.containsKey(p_126125_)) {
         throw new IllegalArgumentException("Symbol '" + p_126125_ + "' is already defined!");
      } else if (p_126125_ == ' ') {
         throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
      } else {
         this.key.put(p_126125_, p_126126_);
         return this;
      }
   }

   public ShapedRecipeBuilder pattern(String p_126131_) {
      if (!this.rows.isEmpty() && p_126131_.length() != this.rows.get(0).length()) {
         throw new IllegalArgumentException("Pattern must be the same width on every line!");
      } else {
         this.rows.add(p_126131_);
         return this;
      }
   }

   public ShapedRecipeBuilder unlockedBy(String p_176751_, Criterion<?> p_300780_) {
      this.criteria.put(p_176751_, p_300780_);
      return this;
   }

   public ShapedRecipeBuilder group(@Nullable String p_126146_) {
      this.group = p_126146_;
      return this;
   }

   public ShapedRecipeBuilder showNotification(boolean p_273326_) {
      this.showNotification = p_273326_;
      return this;
   }

   public Item getResult() {
      return this.result;
   }

   public void save(RecipeOutput p_298334_, ResourceLocation p_126142_) {
      this.ensureValid(p_126142_);
      Advancement.Builder advancement$builder = p_298334_.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_126142_)).rewards(AdvancementRewards.Builder.recipe(p_126142_)).requirements(AdvancementRequirements.Strategy.OR);
      this.criteria.forEach(advancement$builder::addCriterion);
      p_298334_.accept(new ShapedRecipeBuilder.Result(p_126142_, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.rows, this.key, advancement$builder.build(p_126142_.withPrefix("recipes/" + this.category.getFolderName() + "/")), this.showNotification));
   }

   private void ensureValid(ResourceLocation p_126144_) {
      if (this.rows.isEmpty()) {
         throw new IllegalStateException("No pattern is defined for shaped recipe " + p_126144_ + "!");
      } else {
         Set<Character> set = Sets.newHashSet(this.key.keySet());
         set.remove(' ');

         for(String s : this.rows) {
            for(int i = 0; i < s.length(); ++i) {
               char c0 = s.charAt(i);
               if (!this.key.containsKey(c0) && c0 != ' ') {
                  throw new IllegalStateException("Pattern in recipe " + p_126144_ + " uses undefined symbol '" + c0 + "'");
               }

               set.remove(c0);
            }
         }

         if (!set.isEmpty()) {
            throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + p_126144_);
         } else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
            throw new IllegalStateException("Shaped recipe " + p_126144_ + " only takes in a single item - should it be a shapeless recipe instead?");
         } else if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + p_126144_);
         }
      }
   }

   static class Result extends CraftingRecipeBuilder.CraftingResult {
      private final ResourceLocation id;
      private final Item result;
      private final int count;
      private final String group;
      private final List<String> pattern;
      private final Map<Character, Ingredient> key;
      private final AdvancementHolder advancement;
      private final boolean showNotification;

      public Result(ResourceLocation p_273548_, Item p_273530_, int p_272738_, String p_273549_, CraftingBookCategory p_273500_, List<String> p_273744_, Map<Character, Ingredient> p_272991_, AdvancementHolder p_297223_, boolean p_272862_) {
         super(p_273500_);
         this.id = p_273548_;
         this.result = p_273530_;
         this.count = p_272738_;
         this.group = p_273549_;
         this.pattern = p_273744_;
         this.key = p_272991_;
         this.advancement = p_297223_;
         this.showNotification = p_272862_;
      }

      public void serializeRecipeData(JsonObject p_126167_) {
         super.serializeRecipeData(p_126167_);
         if (!this.group.isEmpty()) {
            p_126167_.addProperty("group", this.group);
         }

         JsonArray jsonarray = new JsonArray();

         for(String s : this.pattern) {
            jsonarray.add(s);
         }

         p_126167_.add("pattern", jsonarray);
         JsonObject jsonobject = new JsonObject();

         for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
            jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson(false));
         }

         p_126167_.add("key", jsonobject);
         JsonObject jsonobject1 = new JsonObject();
         jsonobject1.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
         if (this.count > 1) {
            jsonobject1.addProperty("count", this.count);
         }

         p_126167_.add("result", jsonobject1);
         p_126167_.addProperty("show_notification", this.showNotification);
      }

      public RecipeSerializer<?> type() {
         return RecipeSerializer.SHAPED_RECIPE;
      }

      public ResourceLocation id() {
         return this.id;
      }

      public AdvancementHolder advancement() {
         return this.advancement;
      }
   }
}