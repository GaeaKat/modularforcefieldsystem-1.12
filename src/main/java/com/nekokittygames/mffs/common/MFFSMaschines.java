/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    
    Matchlighter
    Thunderdark 

 */

package com.nekokittygames.mffs.common;


import com.nekokittygames.mffs.common.block.BlockMFFSBase;
import com.nekokittygames.mffs.common.block.ModBlocks;
import ic2.api.tile.ExplosionWhitelist;
import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum MFFSMaschines {

	Projector(1, "MFFSProjector", "Projector", "TileEntityProjector",
			"GuiProjector", "ContainerProjector",
			ModBlocks.PROJECTOR, 0, "KyKyFyKJK", " y yFyaIa","KyKyFyKJK"),
	Extractor(2, "MFFSExtractor", "Extractor", "TileEntityExtractor",
			"GuiExtractor", "ContainerForceEnergyExtractor",
			ModBlocks.EXTRACTOR, 0, " C xFx G ", " a xFxEIE"," C xFx G "),
	Capacitor(3, "MFFSCapacitor", "Capacitor", "TileEntityCapacitor",
			"GuiCapacitor", "ContainerCapacitor",
			ModBlocks.CAPACITOR, 0, "xJxCFCxJx", " a xGxxIx","xJxCFCxJx"),
	DefenceStation(5, "MFFSDefenceStation", "Defence Station", "TileEntityAreaDefenseStation",
			"GuiAreaDefenseStation", "ContainerAreaDefenseStation",
			ModBlocks.AREA_DEFENSE, 0, " J aFa E ", " a EFE I "," J aFa E "),
	SecurityStation(6, "MFFSSecurtyStation", "Security Station", "TileEntityAdvSecurityStation",
			"GuiAdvSecurityStation", "ContainerAdvSecurityStation",
			ModBlocks.ADV_SECURITY, 0, "KCKCFCKJK", " E EFEIaI","KCKCFCKJK"),
	SecurityStorage(7, "MFFSSecurtyStorage", "Security Storage", "TileEntitySecStorage",
			"GuiSecStorage", "ContainerSecStorage",
			ModBlocks.SECURITY_STORAGE, 0, "AAAACAAAA", "AAAAEAAAA","AAAACAAAA"),
	ControlSystem(8, "MFFSControlSystem", "Control System","TileEntityControlSystem",
			"GuiControlSystem", "ContainerControlSystem",
			ModBlocks.CONTROL_SYSTEM, 0, "aCaAFAACA", " C aFaAIA","aCaAFAACA");

	public int index;
	public String inCodeName;
	public String displayName;
	public Class<? extends TileEntity> clazz;
	public String Gui;
	public Class<? extends Container> Container;
	public Block block;
	public String recipeic;
	public String recipete;
	public String recipeei;
	public int baseTex;

	private MFFSMaschines(int index, String nm, String dispNm, String cls,
			String gui, String container, Block block, int baseTex,
			String recipeic, String recipete, String recipeei) {

		this.index = index;
		this.inCodeName = nm;
		this.displayName = dispNm;
		this.Gui = gui;
		try {
			this.clazz = (Class<? extends TileEntity>) Class
					.forName("com.nekokittygames.mffs.common.tileentity." + cls);
		} catch (ClassNotFoundException ex) {
			this.clazz = null;
		}
		try {
			this.Container = (Class<? extends Container>) Class
					.forName("com.nekokittygames.mffs.common.container." + container);
		} catch (ClassNotFoundException ex) {
			this.Container = null;
		}
		this.recipeic = recipeic;
		this.recipete = recipete;
		this.recipeei=recipeei;
		this.block = block;
		this.baseTex = baseTex;
	}

	public static MFFSMaschines fromdisplayName(String displayName) {
		for (MFFSMaschines mach : MFFSMaschines.values()) {
			if (mach.displayName.equals(displayName)) {
				return mach;
			}
		}
		return null;
	}

	public static MFFSMaschines fromTE(TileEntity tem) {
		for (MFFSMaschines mach : MFFSMaschines.values()) {
			if (mach.clazz.isInstance(tem)) {
				return mach;
			}
		}
		return null;
	}
	public static void preInit()
	{
		for (MFFSMaschines mach : MFFSMaschines.values()) {

			//GameRegistry.register(mach.block);
			//GameRegistry.register(new ItemBlock(mach.block),mach.block.getRegistryName());
			//ModularForceFieldSystem.proxy.setupClientMachine((BlockMFFSBase) mach.block,mach.block.getRegistryName().getResourcePath());


		}
	}
	public static void initialize() {

		for (MFFSMaschines mach : MFFSMaschines.values()) {

			if(ModularForceFieldSystem.ic2Found) {
				RecipesFactory.addRecipe(mach.recipeic, 1, 1, mach.block, null);
				AddWhitelist(mach.block);
			}

			if(ModularForceFieldSystem.enderIoFound)
				RecipesFactory.addRecipe(mach.recipeei, 1, 3, mach.block, null);

		}
	}

	@Optional.Method(modid = "IC2")
	private static void AddWhitelist(Block block) {
		ExplosionWhitelist.addWhitelistedBlock(block);
	}

}