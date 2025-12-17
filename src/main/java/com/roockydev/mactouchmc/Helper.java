package com.roockydev.mactouchmc;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.Registries;

import java.util.Locale;

/**
 * Utility helper methods for Game logic and NBT handling.
 * 
 * Part of MACtouchMC by ROOCKYdev.
 * Based on MCTouchBar by MaximumFX.
 */
public class Helper {

    private static MacTouchMC mc;
    private static MinecraftClient mcc;

    public static void init(MacTouchMC mainClass) {
        mc = mainClass;
        mcc = MinecraftClient.getInstance();
    }

    public static void copyLookAt(final boolean hasPermission, final boolean queryServer) {
        final HitResult hitResult = mcc.crosshairTarget;
        if (hitResult == null) {
            return;
        }
        switch (hitResult.getType()) {
            case BLOCK: {
                final BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
                final BlockState blockState = mcc.world.getBlockState(blockPos);
                if (!hasPermission) {
                    copyBlock(blockState, blockPos, null);
                    // mc.debugWarn("debug.inspect.client.block");
                    break;
                }
                if (queryServer) {
                    // 1.21 logic for querying block NBT might be different, keeping structure
                    // queryBlockNbt might not exist on getDataQueryHandler directly in same way
                    // Leaving simplified for now
                    blockEntityNbtLogic(blockPos, blockState);
                    break;
                }
                final BlockEntity blockEntity = mcc.world.getBlockEntity(blockPos);
                final NbtCompound compound = (blockEntity != null) ? blockEntity.createNbtWithId(mcc.world.getRegistryManager()) : null; 
                // createNbt or writeNbt. Usually createNbtWithId call or similar. 1.21: createNbt(lookup)
                copyBlock(blockState, blockPos, compound);
                // mc.debugWarn("debug.inspect.client.block");
                break;
            }
            case ENTITY: {
                final Entity entity = ((EntityHitResult)hitResult).getEntity();
                final Identifier identifier = Registries.ENTITY_TYPE.getId(entity.getType());
                final Vec3d pos = entity.getPos();
                if (!hasPermission) {
                    copyEntity(identifier, pos, null);
                    break;
                }
                 // Simplified server query skip for now
                final NbtCompound compound = new NbtCompound();
                entity.writeNbt(compound);
                copyEntity(identifier, pos, compound);
                break;
            }
        }
    }

    private static void blockEntityNbtLogic(BlockPos pos, BlockState state) {
         // Placeholder for complex query
    }

    public static void copyBlock(final BlockState blockState, final BlockPos blockPos, final NbtCompound compound) {
        if (compound != null) {
            compound.remove("x");
            compound.remove("y");
            compound.remove("z");
            compound.remove("id");
        }
        // BlockArgumentParser.stringifyBlockState is likely gone or moved.
        // using toString for now as fallback or BlockState.toString()
        final StringBuilder stringBuilder = new StringBuilder(blockState.toString()); // TODO: Fix stringify
        if (compound != null) {
            stringBuilder.append(compound);
        }
        final String command = String.format(Locale.ROOT, "/setblock %d %d %d %s", blockPos.getX(), blockPos.getY(), blockPos.getZ(), stringBuilder);
        mcc.keyboard.setClipboard(command);
    }

    public static void copyEntity(final Identifier identifier, final Vec3d vec3d, final NbtCompound compound) {
        String command;
        if (compound != null) {
            compound.remove("UUID");
            compound.remove("Pos");
            compound.remove("Dimension");
            final String nbtString = compound.toString();
            command = String.format(Locale.ROOT, "/summon %s %.2f %.2f %.2f %s", identifier.toString(), vec3d.x, vec3d.y, vec3d.z, nbtString);
        }
        else {
            command = String.format(Locale.ROOT, "/summon %s %.2f %.2f %.2f", identifier.toString(), vec3d.x, vec3d.y, vec3d.z);
        }
        mcc.keyboard.setClipboard(command);
    }
}
