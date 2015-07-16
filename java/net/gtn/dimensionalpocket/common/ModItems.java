package net.gtn.dimensionalpocket.common;

import net.gtn.dimensionalpocket.common.items.ItemBook;
import net.gtn.dimensionalpocket.common.items.ItemDP;
import net.gtn.dimensionalpocket.common.items.ItemEndCrystal;
import net.gtn.dimensionalpocket.common.items.ItemNetherCrystal;
import net.gtn.dimensionalpocket.common.lib.Strings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModItems {

    public static ItemDP book;
    public static ItemDP endCrystal;
    public static ItemDP netherCrystal;

    public static void init() {
        book = new ItemBook(Strings.INFO_BOOK);
        endCrystal = new ItemEndCrystal(Strings.END_CRYSTAL);
        netherCrystal = new ItemNetherCrystal(Strings.NETHER_CRYSTAL);
    }

    @SuppressWarnings("boxing")
    public static void initRecipes() {
        CraftingManager crafting = CraftingManager.getInstance();

        //@formatter:off
        crafting.addRecipe(new ItemStack(ModBlocks.dimensionalPocket, 4),
                "#N#",
                "IDI",
                "#E#",

                '#',
                new ItemStack(Blocks.stonebrick, 1, 0),

                'I',
                Blocks.iron_block,

                'D',
                Blocks.diamond_block,

                'N',
                netherCrystal,

                'E',
                endCrystal
                );

        crafting.addRecipe(new ItemStack(ModBlocks.dimensionalPocket, 4),
                "#E#",
                "IDI",
                "#N#",

                '#',
                new ItemStack(Blocks.stonebrick, 1, 0),

                'I',
                Blocks.iron_block,

                'D',
                Blocks.diamond_block,

                'N',
                netherCrystal,

                'E',
                endCrystal
                );

        crafting.addRecipe(new ItemStack(netherCrystal),
                "TTT",
                "TRT",
                "TTT",

                'T',
                Items.ghast_tear,

                'R',
                Blocks.redstone_block
                );

        crafting.addRecipe(new ItemStack(endCrystal),
                "EEE",
                "EGE",
                "EEE",

                'E',
                Items.ender_eye,

                'G',
                Blocks.glass);

        crafting.addShapelessRecipe(new ItemStack(book),Items.book, Items.leather);
        //@formatter:on
    }

}
