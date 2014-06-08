package com.minalien.mffs.items

import net.minecraft.item.{ItemStack, Item}
import com.minalien.mffs.core.MFFSCreativeTab
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraft.creativetab.CreativeTabs
import com.minalien.mffs.items.cards.CardType.CardType
import com.minalien.mffs.items.cards.CardType
import net.minecraft.entity.player.EntityPlayer
import com.minalien.core.nbt.NBTUtility

/**
 * Cards are used for various purposes in MFFS - primarily, linking machines.
 */
object ItemCard extends Item {
	setCreativeTab(MFFSCreativeTab)
	setUnlocalizedName("card")
	setMaxStackSize(16)
	setHasSubtypes(true)

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
	}

	/**
	 * @param itemStack ItemStack to check the type of.
	 *
	 * @return Card type based on the item stack's damage value.
	 */
	def getCardType(itemStack: ItemStack): CardType = CardType(itemStack.getItemDamage)
}
