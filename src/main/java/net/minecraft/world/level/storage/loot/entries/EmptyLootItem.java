package net.minecraft.world.level.storage.loot.entries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EmptyLootItem extends LootPoolSingletonContainer {
   public static final Codec<EmptyLootItem> CODEC = RecordCodecBuilder.create((p_299697_) -> {
      return singletonFields(p_299697_).apply(p_299697_, EmptyLootItem::new);
   });

   private EmptyLootItem(int p_79519_, int p_79520_, List<LootItemCondition> p_300401_, List<LootItemFunction> p_300438_) {
      super(p_79519_, p_79520_, p_300401_, p_300438_);
   }

   public LootPoolEntryType getType() {
      return LootPoolEntries.EMPTY;
   }

   public void createItemStack(Consumer<ItemStack> p_79531_, LootContext p_79532_) {
   }

   public static LootPoolSingletonContainer.Builder<?> emptyItem() {
      return simpleBuilder(EmptyLootItem::new);
   }
}