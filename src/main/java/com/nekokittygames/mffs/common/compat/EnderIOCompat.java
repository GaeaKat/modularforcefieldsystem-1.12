package com.nekokittygames.mffs.common.compat;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import crazypants.enderio.EnderIO;
import crazypants.enderio.capacitor.CapacitorHelper;
import crazypants.enderio.capacitor.DefaultCapacitorData;
import crazypants.enderio.conduit.item.FunctionUpgrade;
import crazypants.enderio.conduit.item.ItemConduit;
import crazypants.enderio.conduit.power.PowerConduit;
import crazypants.enderio.item.skull.BlockEndermanSkull;
import crazypants.enderio.machine.capbank.CapBankType;
import crazypants.enderio.material.Alloy;
import crazypants.enderio.material.FrankenSkull;
import crazypants.enderio.material.ItemCapacitor;
import crazypants.enderio.material.MachinePart;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by katsw on 20/10/2016.
 */
public class EnderIOCompat {

    public static Object[] getEnderIORecipes(String[] recipeSplit)
    {
        return new Object[]{
                recipeSplit[0],
                recipeSplit[1],
                recipeSplit[2],
                'a',
                Items.ENDER_PEARL,
                'b',
                Items.IRON_PICKAXE,
                'c',
                Items.BUCKET,
                'd',
                Items.LAVA_BUCKET,
                'e',
                Items.WATER_BUCKET,
                'f',
                Items.BONE, // Vanilla Stuff a++
                'g',
                Items.BLAZE_ROD,
                'h',
                Items.ROTTEN_FLESH,
                'i',
                Items.DIAMOND,
                'j',
                Items.SPIDER_EYE,
                'k',
                Blocks.OBSIDIAN,
                'l',
                Blocks.GLASS,
                'm',
                Items.REDSTONE,
                'n',
                Blocks.LEVER,
                'o',
                Items.PAPER,

                'u',
                ModularForceFieldSystem.MFFSitemForcicium,
                'v',
                ModularForceFieldSystem.MFFSitemFocusmatix,
                'w',
                ModularForceFieldSystem.MFFSProjectorTypCube,
                'x',
                new ItemStack(
                        ModularForceFieldSystem.MFFSitemForcePowerCrystal,
                        1, -1), // MFFs Stuff z--
                'y',
                ModularForceFieldSystem.MFFSitemFocusmatix,
                'z', ModularForceFieldSystem.MFFSItemIDCard,

                'A', Alloy.ELECTRICAL_STEEL.getStackIngot(),
                'B', EnderIO.itemExtractSpeedUpgrade,
                'C', new ItemStack(EnderIO.itemBasicCapacitor,1,DefaultCapacitorData.BASIC_CAPACITOR.ordinal()),
                'D', new ItemStack(EnderIO.itemBasicCapacitor,1,DefaultCapacitorData.ACTIVATED_CAPACITOR.ordinal()),
                'E', Alloy.ENERGETIC_ALLOY.getStackIngot(),
                'F', new ItemStack(EnderIO.itemMachinePart,1,MachinePart.MACHINE_CHASSI.ordinal()),
                'G', EnderIO.blockCrusher,
                'H', new ItemStack(EnderIO.itemPowerConduit, 1, 0),
                'I', new ItemStack(EnderIO.itemPowerConduit, 1, 1),
                'J', new ItemStack(EnderIO.itemFrankenSkull,1, FrankenSkull.ENDER_RESONATOR.ordinal()),
                'K', Alloy.DARK_STEEL.getStackIngot(),
                'M', new ItemStack(EnderIO.itemPowerConduit, 1, 2),
                'N', new ItemStack(EnderIO.blockCapBank,1, CapBankType.SIMPLE.ordinal()),
                'O', new ItemStack(EnderIO.blockCapBank,1, CapBankType.ACTIVATED.ordinal()),
                'P', new ItemStack(EnderIO.blockCapBank,1, CapBankType.VIBRANT.ordinal()),
                'Q', new ItemStack(EnderIO.blockEndermanSkull,1, BlockEndermanSkull.SkullType.TORMENTED.ordinal()),
                'S', EnderIO.itemYetaWench};
    }
}
