package net.gtn.dimensionalpocket.common.block;

import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.common.tileentity.TileDimensionalPocket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDimensionalPocketFrame extends BlockDP {

    public BlockDimensionalPocketFrame(Material material, String name, float lightLevel) {
        super(material, name);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setLightLevel(lightLevel);
        setLightOpacity(15);
        disableStats();
        setCreativeTab(null);

        String sharedName = name.substring(0, name.lastIndexOf("_"));
        setBlockTextureName(sharedName);
        setBlockName(sharedName);
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());

        if (pocket == null)
            return 0;

        World srcWorld = MinecraftServer.getServer().worldServerForDimension(pocket.getBlockDim());
        CoordSet blockSet = pocket.getBlockCoords();

        Block block = world.getBlock(blockSet.getX(), blockSet.getY(), blockSet.getZ());

        if (block instanceof BlockDimensionalPocket){
            
            return ((BlockDimensionalPocket) block).getSurroundingPower(srcWorld, blockSet.getX(), blockSet.getY(), blockSet.getZ(), direction.ordinal());
        }
        DPLogger.info("HALP");
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        return isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return false;

        ItemStack itemStack = player.getCurrentEquippedItem();

        if (itemStack != null) {
            if (itemStack.getItem() == ModItems.craftingItems && (itemStack.getItemDamage() == 0 || itemStack.getItemDamage() == 1)) {
                if (player.dimension != Reference.DIMENSION_ID || world.isRemote)
                    return false;

                CoordSet coordSet = new CoordSet(x, y, z);

                Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());
                if (pocket == null)
                    return false;

                boolean setSpawn = pocket.setSpawnSet(coordSet.asSpawnPoint());

                if (setSpawn)
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                return true;
            }
            return false;
        }

        if (!player.isSneaking())
            return false;

        if (player.dimension == Reference.DIMENSION_ID) {
            if (world.isRemote)
                return true;

            Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
            if (pocket == null)
                return false;

            pocket.teleportFrom(player);
        }

        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return side != -1;
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }
}
