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
import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum MFFSMaschines {

	Projector(1, "MFFSProjector", "Projector", "TileEntityProjector",
			"GuiProjector", "ContainerProjector",
			ModularForceFieldSystem.MFFSProjector, 0, "KyKyFyKJK", " y yFyaIa"),
	Extractor(2, "MFFSExtractor", "Extractor", "TileEntityExtractor",
			"GuiExtractor", "ContainerForceEnergyExtractor",
			ModularForceFieldSystem.MFFSExtractor, 0, " C xFx G ", " a xFxEIE"),
	Capacitor(3, "MFFSCapacitor", "Capacitor", "TileEntityCapacitor",
			"GuiCapacitor", "ContainerCapacitor",
			ModularForceFieldSystem.MFFSCapacitor, 0, "xJxCFCxJx", " a xGxxIx"),
	Converter(4, "MFFSForceEnergyConverter", "Converter", "TileEntityConverter",
			"GuiConverter", "ContainerConverter",
			ModularForceFieldSystem.MFFSForceEnergyConverter, 0, "ANAJOMAPA", "         "),
	DefenceStation(5, "MFFSDefenceStation", "Defence Station", "TileEntityAreaDefenseStation",
			"GuiAreaDefenseStation", "ContainerAreaDefenseStation",
			ModularForceFieldSystem.MFFSDefenceStation, 0, " J aFa E ", " a EFE I "),
	SecurityStation(6, "MFFSSecurtyStation", "Security Station", "TileEntityAdvSecurityStation",
			"GuiAdvSecurityStation", "ContainerAdvSecurityStation",
			ModularForceFieldSystem.MFFSSecurtyStation, 0, "KCKCFCKJK", " E EFEIaI"),
	SecurityStorage(7, "MFFSSecurtyStorage", "Security Storage", "TileEntitySecStorage",
			"GuiSecStorage", "ContainerSecStorage",
			ModularForceFieldSystem.MFFSSecurtyStorage, 0, "AAAACAAAA", "AAAAEAAAA"),
	ControlSystem(8, "MFFSControlSystem", "Control System","TileEntityControlSystem",
			"GuiControlSystem", "ContainerControlSystem",
			ModularForceFieldSystem.MFFSControlSystem, 0, "aCaAFAACA", " C aFaAIA");

	public int index;
	public String inCodeName;
	public String displayName;
	public Class<? extends TileEntity> clazz;
	public String Gui;
	public Class<? extends Container> Container;
	public Block block;
	public String recipeic;
	public String recipete;
	public int baseTex;

	private MFFSMaschines(int index, String nm, String dispNm, String cls,
			String gui, String container, Block block, int baseTex,
			String recipeic, String recipete) {

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

	public static void initialize() {

		for (MFFSMaschines mach : MFFSMaschines.values()) {

			GameRegistry.register(mach.block);
			GameRegistry.register(new ItemBlock(mach.block),mach.block.getRegistryName());
			GameRegistry.registerTileEntity(mach.clazz, mach.inCodeName);
			ModularForceFieldSystem.proxy.setupClientMachine((BlockMFFSBase) mach.block,mach.block.getRegistryName().getResourcePath());
			RecipesFactory.addRecipe(mach.recipete, 1, 1, mach.block, null);

		}
	}

}