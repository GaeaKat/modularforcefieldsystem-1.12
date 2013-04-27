package mods.mffs.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Interface for an item that allows the item to be used to teleport through
 * MFFS Forcefields.
 * 
 * @author Mina
 */
public interface IFieldTeleporter {
	/**
	 * Called after the forcefield has verified that player has the required
	 * security right to teleport through.
	 * 
	 * @param stack
	 *            Item stack being checked.
	 * @param teleportCost
	 *            Teleportation Cost (in FE), to provide cost scaling based on
	 *            MFFS configuration. Default cost is 10,000 FE.
	 * @param player
	 *            Player attempting to teleport.
	 * 
	 * @return Whether or not the item allows the player to teleport through
	 *         (for instance, has the required power).
	 */
	public boolean canFieldTeleport(EntityPlayer player, ItemStack stack,
			int teleportCost);

	/**
	 * Called after a successful field teleport to allow the item to handle
	 * damage. Note that if you want to send a message to the player (such as
	 * the "transmission success" message of the multitool teleporter, you
	 * should do it here.)
	 * 
	 * @param stack
	 *            Item stack used.
	 * @param teleportCost
	 *            Teleportation Cost (in FE), to provide cost scaling based on
	 *            MFFS configuration. Default cost is 10,000 FE.
	 * @param player
	 *            Player who attempted to teleport.
	 */
	public void onFieldTeleportSuccess(EntityPlayer player, ItemStack stack,
			int teleportCost);

	/**
	 * Called after a failed attempt to teleport through a forcefield. Note that
	 * if you want to send a message to the player (such as the
	 * "transmission failed" message of the multitool teleporter, you should do
	 * it here.)
	 * 
	 * @param stack
	 *            Item stack used.
	 * @param teleportCost
	 *            Teleportation Cost (in FE), to provide cost scaling based on
	 *            MFFS configuration. Default cost is 10,000 FE.
	 * @param player
	 *            Player who attempted to teleport.
	 */
	public void onFieldTeleportFailed(EntityPlayer player, ItemStack stack,
			int teleportCost);
}
