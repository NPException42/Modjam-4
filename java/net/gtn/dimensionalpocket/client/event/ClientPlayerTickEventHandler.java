package net.gtn.dimensionalpocket.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gtn.dimensionalpocket.client.renderer.tile.TileRendererPocket;
import net.gtn.dimensionalpocket.client.utils.version.VersionChecker;
import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;

@SideOnly(Side.CLIENT)
public class ClientPlayerTickEventHandler {
    
    private static boolean checkForVersion = Reference.DO_VERSION_CHECK;
    
    public static boolean hideStuffFromNEI = false;
    
    /**
     * Hides blocks and items from NEI that it should not show. The Dimensional Pocket Wall f.e.
     */
    private static void hideStuffFromNEI() {
        try {
            Class<?> neiApiClass = Class.forName("codechicken.nei.api.API");
            Method hideItemMethod = neiApiClass.getDeclaredMethod("hideItem", ItemStack.class);
            hideItemMethod.invoke(null, new ItemStack(ModBlocks.dimensionalPocketWall));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void checkPlayerForNetherCrystal(EntityPlayer player) {
        ItemStack equippedItem = player.getCurrentEquippedItem();
        TileRendererPocket.doIndicateSides = equippedItem != null && equippedItem.getItem() == ModItems.netherCrystal;
    }
    
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void event(PlayerTickEvent evt) {
        if (evt.player != Minecraft.getMinecraft().thePlayer)
            return;
        
        if (hideStuffFromNEI) {
            hideStuffFromNEI = false;
            hideStuffFromNEI();
        }
        
        if (checkForVersion) {
            checkForVersion = false;
            VersionChecker.checkUpToDate(evt.player);
        }
        
        // check for Nether Crystal in hand to trigger side color coding of pockets
        checkPlayerForNetherCrystal(evt.player);
    }
}
