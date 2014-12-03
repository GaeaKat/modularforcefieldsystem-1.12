package com.minalien.mffs.items

import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.items.cards.CardType
import com.minalien.mffs.items.cards.CardType.CardType
import com.minalien.mffs.machines.TileEntityExtractor
import com.mojang.authlib.GameProfile
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.{NBTTagCompound, NBTUtil}
import net.minecraft.util.IIcon
import net.minecraft.world.World

/**
 * Cards are used for various purposes in MFFS - primarily, linking machines.
 */
object ItemCard extends Item {
	setCreativeTab(MFFSCreativeTab)
	setUnlocalizedName("card")
	setMaxStackSize(16)
	setHasSubtypes(true)
	val NBT_LOCATIONDATA = "location"
	val NBT_PLAYERDATA = "player"
	/**
	 * List of icons for the various cards.
	 */
	private val icons = new Array[IIcon](CardType.maxId)

	/**
	 * Loads icons related to the item.
	 *
	 * @param iconRegister Icon Register used to load textures for stitching.
	 */
	override def registerIcons(iconRegister: IIconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:cards/blank")

		for(cardType <- CardType.values)
			icons(cardType.id) = iconRegister.registerIcon(s"mffs:cards/${cardType.toString.toLowerCase}")
	}

	/**
	 * Populates subItems with a list of cards that can be spawned through the Creative tab.
	 *
	 * @param item          Item being added.
	 * @param creativeTab   Creative tab being used.
	 * @param subItems      List to populate with sub-items.
	 */
	override def getSubItems(item: Item, creativeTab: CreativeTabs, subItems: java.util.List[_]) {
		val subItemList = subItems.asInstanceOf[java.util.List[ItemStack]]

		// Blank MFFS Card
		subItemList.add(new ItemStack(ItemCard, 1, CardType.Blank.id))
		// Power MFFS Card
		subItemList.add(new ItemStack(ItemCard, 1, CardType.Power.id))
	}

	/**
	 * @param damage Damage value of the item to retrieve the icon for.
	 *
	 * @return Icon in icons[] based on the damage value.
	 */
	override def getIconFromDamage(damage: Int): IIcon = {
		if(damage >= icons.length || damage < 0)
			itemIcon
		else
			icons(damage)
	}

	/**
	 * Adds card type-specific information to the item tooltip.
	 *
	 * @param itemStack             ItemStack representing the Multitool being used.
	 * @param player                Player holding the multitool.
	 * @param infoList              List of lines being added to the tooltip.
	 * @param showAdvancedTooltips  Whether or not advanced tooltips are enabled.
	 */
	override def addInformation(itemStack: ItemStack, player: EntityPlayer, infoList: java.util.List[_],
	                            showAdvancedTooltips: Boolean) {
		val infoListAsString = infoList.asInstanceOf[java.util.List[String]]
		val cardType = getCardType(itemStack)

		infoListAsString.add(s"Type: §2$cardType§r")
		if (containsLocationData(itemStack)) {
			val (x, y, z, dim) = getLocationData(itemStack)
			if (dim == player.worldObj.provider.dimensionId) {
				if (isValidPowerLocation(player.worldObj, x, y, z))
					infoListAsString.add(s"§2[VALID]§r X: §2$x§r Y: §2$y§r Z: §2$z§r")
				else
					infoListAsString.add(s"§4[INVALID]§r X: §2$x§r Y: §2$y§r Z: §2$z§r")
			}
			else
				infoListAsString.add(s"§9[UNKNOWN]§r X: §2$x§r Y: §2$y§r Z: §2$z§r")
		}
		if (containsPlayerData(itemStack)) {
			val name: String = getPlayerData(itemStack).getName
			infoListAsString.add(s"Player Name: §2$name§r")
		}
	}

	/**
	 * Does the card contain location data?
	 * @param itemstack card to check
	 * @return whether the card has location data
	 */
	def containsLocationData(itemstack: ItemStack): Boolean = {
		if (itemstack.getTagCompound != null) itemstack.getTagCompound.hasKey(NBT_LOCATIONDATA) else false
	}

	/**
	 * Does the card contain player data?
	 * @param itemstack card to check
	 * @return whether the card has player data
	 */
	def containsPlayerData(itemstack: ItemStack): Boolean = {
		if (itemstack.getTagCompound != null) itemstack.getTagCompound.hasKey(NBT_PLAYERDATA) else false
	}

	/**
	 * gets the location data on the card
	 * @param itemstack card to check
	 * @return Tuple of X,Y,Z,Dim
	 */
	def getLocationData(itemstack: ItemStack): Tuple4[Int, Int, Int, Int] = {
		val loc = itemstack.getTagCompound.getIntArray(NBT_LOCATIONDATA)
		new Tuple4(loc(0), loc(1), loc(2), loc(3))
	}

	/**
	 * Gets the player data off the card
	 * @param itemstack card to check
	 * @return Game profile
	 */
	def getPlayerData(itemstack: ItemStack): GameProfile = NBTUtil.func_152459_a(itemstack.getTagCompound.getCompoundTag(NBT_LOCATIONDATA))

	/**
	 * @param itemStack ItemStack to check the type of.
	 *
	 * @return Card type based on the item stack's damage value.
	 */
	def getCardType(itemStack: ItemStack): CardType = CardType(itemStack.getItemDamage)

	/**
	 * Returns true if the location given is an active Extractor
	 * @param world Dimension to check in
	 * @param x X coord
	 * @param y Y coord
	 * @param z Z coord
	 * @return True if valid. False if Invalid (different machine or nonexistant)
	 */
	def isValidPowerLocation(world: World, x: Int, y: Int, z: Int): Boolean =
		world.getTileEntity(x, y, z) match {
			case m: TileEntityExtractor => true
			case _ => false
		}

	/**
	 * On the use of the card
	 * @param p_77648_1_ Itemstack being used
	 * @param p_77648_2_ Player using the item
	 * @param p_77648_3_ World item being used in
	 * @param p_77648_4_ x xCoord for block
	 * @param p_77648_5_ y yCoord for block
	 * @param p_77648_6_ z xCoord for block
	 * @param p_77648_7_
	 * @param p_77648_8_
	 * @param p_77648_9_
	 * @param p_77648_10_
	 * @return
	 */
	override def onItemUse(p_77648_1_ : ItemStack, p_77648_2_ : EntityPlayer, p_77648_3_ : World, p_77648_4_ : Int, p_77648_5_ : Int, p_77648_6_ : Int, p_77648_7_ : Int, p_77648_8_ : Float, p_77648_9_ : Float, p_77648_10_ : Float): Boolean = getCardType(p_77648_1_) match {
		case CardType.Power =>
			if (isValidPowerLocation(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) {
				setLocationData(p_77648_1_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_3_.provider.dimensionId)
				true
			}
			else
				false
	}

	def setLocationData(itemstack: ItemStack, x: Int, y: Int, z: Int, dim: Int): Unit = {
		if (itemstack.getTagCompound == null)
			itemstack.setTagCompound(new NBTTagCompound)
		itemstack.getTagCompound.setIntArray(NBT_LOCATIONDATA, Array(x, y, z, dim))
	}
}
