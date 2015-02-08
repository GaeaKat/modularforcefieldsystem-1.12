package com.minalien.mffs.api

import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.world.World

/**
 * Created by Katrina on 13/01/2015.
 */
trait IPowerLinkItem {
  def getPercentageCapacity(itemStack: ItemStack, pos: BlockPos, world: World): Int

  def getAvailablePower(itemStack: ItemStack, pos: BlockPos, world: World): Int

  def getMaximumPower(itemStack: ItemStack, pos: BlockPos, world: World): Int

  def consumePower(itemStack: ItemStack, powerAmount: Int, simulation: Boolean, pos: BlockPos, world: World): Boolean

  def insertPower(itemStack: ItemStack, powerAmount: Int, simulation: Boolean, pos: BlockPos, world: World): Boolean

  def getPowersourceID(itemStack: ItemStack, pos: BlockPos, world: World): Int

  def getfreeStorageAmount(itemStack: ItemStack,pos: BlockPos, world: World): Int

  def isPowersourceItem: Boolean

}
