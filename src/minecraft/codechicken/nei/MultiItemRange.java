package codechicken.nei;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;

/**
 * A container class for multiple Item Ranges.
 *
 */
public class MultiItemRange
{
    public boolean isItemInRange(int itemid, int damage)
    {
        return false;
    }
    
    public String toString()
    {
		return "";
    }
    
    /**
     * Constructs a {@link MultiItemRange} from the specified string.
     * @param rangestring
     */
    public MultiItemRange(String rangestring)
    {
    }
    
    public MultiItemRange()
    {
    }
    
    public MultiItemRange add(Collection<?> ranges)
    {                
        return this;
    }
    
    public MultiItemRange add(MultiItemRange range)
    {
        return this;
    }
    
    public MultiItemRange add(int itemID)
    {
        return this;
    }
    
    public MultiItemRange add(int itemID, int damageStart, int damageEnd)
    {
	    return this;
    }
    
    public MultiItemRange add(int itemIDFirst, int itemIDLast)
    {
	    return this;
    }    

    public MultiItemRange add(Item item, int damageStart, int damageEnd)
    {
	    return this;
    }
    
    public MultiItemRange add(Block block, int damageStart, int damageEnd)
    {
        return add(block.blockID, damageStart, damageEnd);
    }
    
    public MultiItemRange add(Item item)
    {
        return add(item.itemID);
    }
    
    public MultiItemRange add(Block block)
    {
        return add(block.blockID);
    }
    
    public MultiItemRange add(ItemStack item)
    {
        if(item.getItem().isDamageable())
        {
            return add(item.itemID);
        }
        else
        {
            return add(item.itemID, item.getItemDamage(), item.getItemDamage());
        }
    }
    
    public int getNumSlots()
    {
        return 0;
    }
    
    public void slotClicked(int slot, int button, boolean doubleclick)
    {
    }
    
    public void hideAllItems()
    {
    }
    
    public void showAllItems()
    {
    }
    
    public int getWidth()
    {
        return 18;
    }
    
    public void resetHashes()
    {
    }
    
    public void addItemIfInRange(int item, int damage, NBTTagCompound compound)
    {
    }
    
    public byte state;
    
    protected int lastslotclicked = -1;
    protected long lastslotclicktime;
}
