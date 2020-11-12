package net.newgaea.mffs.common.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.newgaea.mffs.common.init.MFFSTiles;
import net.newgaea.mffs.common.inventory.CapacitorContainer;
import net.newgaea.mffs.common.inventory.GeneratorContainer;
import net.newgaea.mffs.common.storage.MFFSEnergyStorage;

public class TileCapacitor extends TileNetwork implements INamedContainerProvider {

    private boolean master;

    public TileCapacitor() {
        super(MFFSTiles.CAPACITOR.get());
    }

    public boolean isMaster() {
        return master;
    }

    public TileCapacitor setMaster(boolean master) {
        this.master = master;
        return this;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CapacitorContainer(id,world,pos,playerInventory,playerEntity);
    }
}
