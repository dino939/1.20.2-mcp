package net.minecraft.client.gui.components.debugchart;

import java.util.Locale;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.SampleLogger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PingDebugChart extends AbstractDebugChart {
   private static final int RED = -65536;
   private static final int YELLOW = -256;
   private static final int GREEN = -16711936;
   private static final int CHART_TOP_VALUE = 500;

   public PingDebugChart(Font p_299160_, SampleLogger p_300665_) {
      super(p_299160_, p_300665_);
   }

   protected void renderAdditionalLinesAndLabels(GuiGraphics p_298086_, int p_300322_, int p_299063_, int p_299018_) {
      this.drawStringWithShade(p_298086_, "500 ms", p_300322_ + 1, p_299018_ - 60 + 1);
   }

   protected String toDisplayString(double p_297770_) {
      return String.format(Locale.ROOT, "%d ms", (int)Math.round(p_297770_));
   }

   protected int getSampleHeight(double p_301040_) {
      return (int)Math.round(p_301040_ * 60.0D / 500.0D);
   }

   protected int getSampleColor(long p_299219_) {
      return this.getSampleColor((double)p_299219_, 0.0D, -16711936, 250.0D, -256, 500.0D, -65536);
   }
}