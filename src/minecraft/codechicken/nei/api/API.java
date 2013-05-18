package codechicken.nei.api;

import codechicken.nei.MultiItemRange;
import cpw.mods.fml.common.ICraftingHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the main class that handles item property configuration.
 * WARNING: DO NOT access this class until the world has been loaded
 * These methods should be called from INEIConfig implementors
 */
public class API
{
    /**
     * Register a new Crafting Recipe handler;
     * @param handler The handler to register
     */
    public static void registerRecipeHandler(ICraftingHandler handler)
    {
    }
    
    /**
     * Add a gui to the default overlay renderer with the default position
     * {crafting, crafting2x2, smelting, fuel, brewing}
     */
    public static void registerGuiOverlay(Class<? extends GuiContainer> class1, String string)
    {
        registerGuiOverlay(class1, string, 5, 11);
    }
    
    /**
     * Add a gui to the default overlay renderer with an offset
     * {crafting, crafting2x2, smelting, fuel, brewing}
     * @param x x-offset
     * @param y y-offset
     */
    public static void registerGuiOverlay(Class<? extends GuiContainer> class1, String string, int x, int y)
    {
    }
    
    /**
     * Set the offset to be added to items to translate them into recipe coords on the actual gui, default is 5, 11. Primarily RecipeTransferRects
     * @param classz The class of your gui
     * @param x
     * @param y
     */
    public static void setGuiOffset(Class<? extends GuiContainer> classz, int x, int y)
    {
    }

    /**
     * Hide this item from the ItemPanel.
     * @param itemID The ItemID to hide.
     */
    public static void hideItem(int itemID)
    {
    }
    
    /**
     * Collection version of hideItem.
     * @param items A collection of ItemIDs to hide
     */
    public static void hideItems(Collection<Integer> items)
    {
    }
    
    /**
    * Add or replace the name normally shown on the item tooltip
    * @param itemID
    * @param itemDamage
    * @param name The name to set.
    */
    public static void setOverrideName(int itemID, int itemDamage, String name)
    {
    }
   
   /**
    * An advanced damage range setter, capable of handling multiple ranges. Removes the performance hit from simply searching from 0 - 32000.
    * Sets the item to have damages between the ranges specified by the pairs of ints in the int[]s
    * The int[] should have dimension of 2. The int[0] being the first damage and int[1] the last.
    * Damage ranges are inclusive. 
    * @param itemID The item to set the damage ranges for 
    * @param ranges An ArrayList of int[] pairs specifying the damage ranges.
    */
    public static void setItemDamageVariants(int itemID, ArrayList<int[]> ranges)
    {
    }
   
   /**
    * A simplified wrapper version for specific damage values. Potions, Spawn Eggs etc.
    * @param itemID
    * @param damages A list of Integers specifying the valid damage values.
    */
    public static void setItemDamageVariants(int itemID, Collection<Integer> damages)
    {
    }
    
    /**
     * Another simplified wrapper version of setItemDamageVariants. 
     * Simply supports searching from 0-maxDamage
     * Use of this function is not recommended for large damage values as there is a good hit on performance.
     * @param itemID
     * @param maxDamage the maximum damage to search to (inclusive)
     * Setting this to -1 will disable NEI's normal damage value based search.
     */
    public static void setMaxDamageException(int itemID, int maxDamage)
    {
        ArrayList<int[]> damageset = new ArrayList<int[]>();
        damageset.add(new int[]{0, maxDamage});
        setItemDamageVariants(itemID, damageset);
    }
    
    /**
     * Adds an item with a data compound. 
     * Use this for adding items to the panel that are different depending on their compounds not just their damages.
     * @param item an item with data
     */
    public static void addNBTItem(ItemStack item)
    {
    }
    
    /**
     * The all important function for mods wanting to add custom Item Subset tags
     * @param setname The name of the item range Eg. "Items.Tools.Hammers"
     * @param range A {@link MultiItemRange} specifying the items encompassed by this tag.
     */
    public static void addSetRange(String setname, MultiItemRange range)
    {
    }
    
    /**
     * Add more items to an existing Item Subset rather than replacing it
     * @param setname
     * @param range
     */
    public static void addToRange(String setname, MultiItemRange range)
    {
    }
    
    /**
     * Add a custom KeyBinding to be configured in the Controls menu.
     * @param ident An identifier for your key, eg "shoot"
     * @param desc The description of your key to be displayed in the Controls menu, eg "Previous Recipe"
     * @param defaultKey The default value, commonly obtained from {@link Keyboard}
     */
    public static void addKeyBind(String ident, String desc, int defaultKey)
    {
    }
    
    /**
     * Tells NEI not to perform any Fast Transfer operations on slots of a particular class
     * @param slotClass The class of slot to be exempted
     */
    public static void addFastTransferExemptSlot(Class<? extends Slot> slotClass)
    {
    }
}
