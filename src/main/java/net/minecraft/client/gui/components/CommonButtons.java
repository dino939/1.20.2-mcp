package net.minecraft.client.gui.components;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CommonButtons {
   public static SpriteIconButton language(int p_299277_, Button.OnPress p_299778_, boolean p_301098_) {
      return SpriteIconButton.builder(Component.translatable("options.language"), p_299778_, p_301098_).width(p_299277_).sprite(new ResourceLocation("icon/language"), 15, 15).build();
   }

   public static SpriteIconButton accessibility(int p_300710_, Button.OnPress p_298571_, boolean p_299983_) {
      return SpriteIconButton.builder(Component.translatable("options.accessibility"), p_298571_, p_299983_).width(p_300710_).sprite(new ResourceLocation("icon/accessibility"), 15, 15).build();
   }
}