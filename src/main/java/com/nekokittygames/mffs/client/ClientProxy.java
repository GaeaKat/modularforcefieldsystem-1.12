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
    Thunderdark - initial implementation
 */

package com.nekokittygames.mffs.client;

import com.nekokittygames.mffs.client.renderer.CapacitorRenderer;
import com.nekokittygames.mffs.client.renderer.ExtractorRenderer;
import com.nekokittygames.mffs.client.renderer.MFFSBlockRenderer;
import com.nekokittygames.mffs.common.CommonProxy;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.block.BlockMFFSBase;
import com.nekokittygames.mffs.common.block.BlockProjector;
import com.nekokittygames.mffs.common.tileentity.TileEntityCapacitor;
import com.nekokittygames.mffs.common.tileentity.TileEntityExtractor;
import com.nekokittygames.mffs.common.tileentity.TileEntityForceField;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerTileEntitySpecialRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForceField.class,
				new MFFSBlockRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExtractor.class,new ExtractorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCapacitor.class,new CapacitorRenderer());

	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public boolean isClient() {
		return true;
	}

	@Override
	public  void setupClientBlock(Block block, String name)
	{

		Item itemBlockSimple = Item.getItemFromBlock(block);
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("modularforcefieldsystem:"+name, "inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(itemBlockSimple, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

	}

	@Override
	public void setupClientMachine(BlockMFFSBase block,String name)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),0,new ModelResourceLocation("modularforcefieldsystem:"+name, "active=false,facing=north"+((block instanceof BlockProjector)?",type=0":"")));
	}

	@Override
	public  void setupClientItem(Item item, String name)
	{
		final ModelResourceLocation loc=new ModelResourceLocation("modularforcefieldsystem:"+name,"inventory");
		ModelLoader.setCustomMeshDefinition(item,new MeshDefinition(loc));
	}
}