package net.minecraft.client.gui;

import java.util.Set;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiSpriteManager extends TextureAtlasHolder {
   private static final Set<MetadataSectionSerializer<?>> METADATA_SECTIONS = Set.of(AnimationMetadataSection.SERIALIZER, GuiMetadataSection.TYPE);

   public GuiSpriteManager(TextureManager p_299556_) {
      super(p_299556_, new ResourceLocation("textures/atlas/gui.png"), new ResourceLocation("gui"), METADATA_SECTIONS);
   }

   public TextureAtlasSprite getSprite(ResourceLocation p_298308_) {
      return super.getSprite(p_298308_);
   }

   public GuiSpriteScaling getSpriteScaling(TextureAtlasSprite p_298924_) {
      return this.getMetadata(p_298924_).scaling();
   }

   private GuiMetadataSection getMetadata(TextureAtlasSprite p_299594_) {
      return p_299594_.contents().metadata().getSection(GuiMetadataSection.TYPE).orElse(GuiMetadataSection.DEFAULT);
   }
}