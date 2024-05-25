package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SpongeBlock extends Block {
   public static final int MAX_DEPTH = 6;
   public static final int MAX_COUNT = 64;
   private static final Direction[] ALL_DIRECTIONS = Direction.values();

   protected SpongeBlock(BlockBehaviour.Properties p_56796_) {
      super(p_56796_);
   }

   public void onPlace(BlockState p_56811_, Level p_56812_, BlockPos p_56813_, BlockState p_56814_, boolean p_56815_) {
      if (!p_56814_.is(p_56811_.getBlock())) {
         this.tryAbsorbWater(p_56812_, p_56813_);
      }
   }

   public void neighborChanged(BlockState p_56801_, Level p_56802_, BlockPos p_56803_, Block p_56804_, BlockPos p_56805_, boolean p_56806_) {
      this.tryAbsorbWater(p_56802_, p_56803_);
      super.neighborChanged(p_56801_, p_56802_, p_56803_, p_56804_, p_56805_, p_56806_);
   }

   protected void tryAbsorbWater(Level p_56798_, BlockPos p_56799_) {
      if (this.removeWaterBreadthFirstSearch(p_56798_, p_56799_)) {
         p_56798_.setBlock(p_56799_, Blocks.WET_SPONGE.defaultBlockState(), 2);
         p_56798_.playSound((Player)null, p_56799_, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

   }

   private boolean removeWaterBreadthFirstSearch(Level p_56808_, BlockPos p_56809_) {
      return BlockPos.breadthFirstTraversal(p_56809_, 6, 65, (p_277519_, p_277492_) -> {
         for(Direction direction : ALL_DIRECTIONS) {
            p_277492_.accept(p_277519_.relative(direction));
         }

      }, (p_296944_) -> {
         if (p_296944_.equals(p_56809_)) {
            return true;
         } else {
            BlockState blockstate = p_56808_.getBlockState(p_296944_);
            FluidState fluidstate = p_56808_.getFluidState(p_296944_);
            if (!fluidstate.is(FluidTags.WATER)) {
               return false;
            } else {
               Block block = blockstate.getBlock();
               if (block instanceof BucketPickup) {
                  BucketPickup bucketpickup = (BucketPickup)block;
                  if (!bucketpickup.pickupBlock((Player)null, p_56808_, p_296944_, blockstate).isEmpty()) {
                     return true;
                  }
               }

               if (blockstate.getBlock() instanceof LiquidBlock) {
                  p_56808_.setBlock(p_296944_, Blocks.AIR.defaultBlockState(), 3);
               } else {
                  if (!blockstate.is(Blocks.KELP) && !blockstate.is(Blocks.KELP_PLANT) && !blockstate.is(Blocks.SEAGRASS) && !blockstate.is(Blocks.TALL_SEAGRASS)) {
                     return false;
                  }

                  BlockEntity blockentity = blockstate.hasBlockEntity() ? p_56808_.getBlockEntity(p_296944_) : null;
                  dropResources(blockstate, p_56808_, p_296944_, blockentity);
                  p_56808_.setBlock(p_296944_, Blocks.AIR.defaultBlockState(), 3);
               }

               return true;
            }
         }
      }) > 1;
   }
}