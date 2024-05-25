package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.minecraft.report.AbuseReport;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.minecraft.report.ReportedEntity;
import com.mojang.datafixers.util.Either;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.reporting.NameReportScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

@OnlyIn(Dist.CLIENT)
public class NameReport extends Report {
   private final String reportedName;

   NameReport(UUID p_300103_, Instant p_297358_, UUID p_301007_, String p_301332_) {
      super(p_300103_, p_297358_, p_301007_);
      this.reportedName = p_301332_;
   }

   public String getReportedName() {
      return this.reportedName;
   }

   public NameReport copy() {
      NameReport namereport = new NameReport(this.reportId, this.createdAt, this.reportedProfileId, this.reportedName);
      namereport.comments = this.comments;
      return namereport;
   }

   public Screen createScreen(Screen p_300004_, ReportingContext p_297616_) {
      return new NameReportScreen(p_300004_, p_297616_, this);
   }

   @OnlyIn(Dist.CLIENT)
   public static class Builder extends Report.Builder<NameReport> {
      public Builder(NameReport p_297219_, AbuseReportLimits p_298998_) {
         super(p_297219_, p_298998_);
      }

      public Builder(UUID p_298683_, String p_299992_, AbuseReportLimits p_299650_) {
         super(new NameReport(UUID.randomUUID(), Instant.now(), p_298683_, p_299992_), p_299650_);
      }

      public boolean hasContent() {
         return StringUtils.isNotEmpty(this.comments());
      }

      @Nullable
      public Report.CannotBuildReason checkBuildable() {
         return (this.report).comments.length() > this.limits.maxOpinionCommentsLength() ? Report.CannotBuildReason.COMMENT_TOO_LONG : null;
      }

      public Either<Report.Result, Report.CannotBuildReason> build(ReportingContext p_299061_) {
         Report.CannotBuildReason report$cannotbuildreason = this.checkBuildable();
         if (report$cannotbuildreason != null) {
            return Either.right(report$cannotbuildreason);
         } else {
            ReportedEntity reportedentity = new ReportedEntity((this.report).reportedProfileId);
            AbuseReport abusereport = AbuseReport.name((this.report).comments, reportedentity, (this.report).createdAt);
            return Either.left(new Report.Result((this.report).reportId, ReportType.USERNAME, abusereport));
         }
      }
   }
}