package com.minalien.mffs.client

import com.minalien.mffs.core.CommonProxy
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.item.Item

/**
 * Created by Katrina on 22/12/2014.
 */
class ClientProxy extends CommonProxy{
  override def registerInventoryBlock(block: Block,name: String): Unit =
  {
    Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(Item.getItemFromBlock(block),0,new ModelResourceLocation("modularforcefieldsystem"+":"+name,"inventory"))
  }

  override def registerItem(item: Item, name: String): Unit =
  {
    Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(item,0,new ModelResourceLocation("modularforcefieldsystem"+":"+name,"inventory"))
  }
}
