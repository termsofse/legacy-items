package net.mrlucky.legacyitems;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public class DirectPlaceItem extends Item {
    private final Block block;
    private final BlockItemStateProperties pinned;

    public DirectPlaceItem(Block block, Map<String, String> pinned, Item.Properties properties) {
        super(properties);
        this.block = block;
        this.pinned = pinned.isEmpty() ? BlockItemStateProperties.EMPTY : new BlockItemStateProperties(pinned);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockPos pos = context.getClickedPos();
        if (!level.getBlockState(pos).canBeReplaced(placeContext)) {
            pos = pos.relative(context.getClickedFace());
        }
        if (!level.getBlockState(pos).canBeReplaced(placeContext)) {
            return InteractionResult.FAIL;
        }

        BlockState placed = this.orient(this.pinned.apply(this.block.defaultBlockState()), context);

        if (!level.isClientSide()) {
            level.setBlock(pos, placed, Block.UPDATE_CLIENTS);
            SoundType sound = placed.getSoundType();
            level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                    (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            Player player = context.getPlayer();
            if (player != null && !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }

    private BlockState orient(BlockState state, UseOnContext context) {
        StateDefinition<Block, BlockState> definition = state.getBlock().getStateDefinition();
        BlockState oriented = state;

        Property<?> facing = definition.getProperty("facing");
        if (facing != null && facing.getPossibleValues().contains(context.getHorizontalDirection())) {
            oriented = setProperty(oriented, facing, context.getHorizontalDirection());
        }

        Property<?> rotation = definition.getProperty("rotation");
        if (rotation != null) {
            Player player = context.getPlayer();
            float yaw = player == null ? 0.0F : player.getYRot();
            int value = Mth.floor(180.0F + yaw * 16.0F / 360.0F + 0.5D) & 15;
            if (rotation.getPossibleValues().contains(value)) {
                oriented = setProperty(oriented, rotation, value);
            }
        }
        return oriented;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static BlockState setProperty(BlockState state, Property property, Comparable value) {
        return state.setValue(property, value);
    }
}
