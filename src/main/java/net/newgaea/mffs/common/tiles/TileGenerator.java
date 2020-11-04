package net.newgaea.mffs.common.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.init.MFFSTiles;
import net.newgaea.mffs.common.inventory.GeneratorContainer;
import net.newgaea.mffs.common.storage.MFFSEnergyStorage;

import java.util.concurrent.atomic.AtomicInteger;

public class TileGenerator extends TileMFFS  implements ITickableTileEntity, INamedContainerProvider {

    private LazyOptional<IItemHandler> fuelHandler = LazyOptional.of(this::createFuelHandler);
    private LazyOptional<IItemHandler> monazitHandler = LazyOptional.of(this::createMonazitHandler);
    private LazyOptional<IItemHandler> jointHandler = LazyOptional.of(this::createJointHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergyStorage);

    private int burnTime;
    private int cookTime;
    private int cookTimeTotal=200;
    private int recipesUsed;


    public int getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public int getRecipesUsed() {
        return recipesUsed;
    }

    public void setRecipesUsed(int recipesUsed) {
        this.recipesUsed = recipesUsed;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getCookTimeTotal() {
        return cookTimeTotal;
    }

    public void setCookTimeTotal(int cookTimeTotal) {
        this.cookTimeTotal = cookTimeTotal;
    }


    private <T> IEnergyStorage createEnergyStorage() {
        return new MFFSEnergyStorage(10000,0);
    }
    private <T> IItemHandler createJointHandler() {
        return new CombinedInvWrapper((IItemHandlerModifiable)fuelHandler.cast(),(IItemHandlerModifiable)monazitHandler.cast());
    }

    private <T> IItemHandler createMonazitHandler() {
        return new ItemStackHandler(1){
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() == MFFSItems.MONAZIT_CRYSTAL.get();
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if(stack.getItem() != MFFSItems.MONAZIT_CRYSTAL.get())
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };
    }

    protected <T> IItemHandler createFuelHandler() {
        return new ItemStackHandler(1){
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return AbstractFurnaceTileEntity.isFuel(stack);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if(!AbstractFurnaceTileEntity.isFuel(stack))
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void tick() {



        if (this.isBurning()) {
            --this.burnTime;
        }
        if (!this.world.isRemote) {
            energy.ifPresent(iEnergyStorage -> {
                fuelHandler.ifPresent(iFuelHandler -> monazitHandler.ifPresent(iMonazitHandler-> {
                    boolean flag = isBurning();
                    boolean flag1 = false;
                    ItemStack itemstack = iFuelHandler.getStackInSlot(0);
                    if (this.isBurning() || !itemstack.isEmpty() && !iMonazitHandler.getStackInSlot(0).isEmpty()) {
                        if (!this.isBurning() && iMonazitHandler.getStackInSlot(0).getItem() == MFFSItems.MONAZIT_CRYSTAL.get()) {
                            this.burnTime = ForgeHooks.getBurnTime(itemstack);
                            this.recipesUsed = this.burnTime;
                            if (this.isBurning()) {
                                flag1 = true;

                                if (itemstack.hasContainerItem())
                                    ((ItemStackHandler) iFuelHandler).setStackInSlot(0, itemstack.getContainerItem());
                                else if (!itemstack.isEmpty()) {
                                    Item item = itemstack.getItem();
                                    itemstack.shrink(1);
                                    if (itemstack.isEmpty()) {
                                        ((ItemStackHandler) iFuelHandler).setStackInSlot(0, itemstack.getContainerItem());
                                    }
                                }

                            }
                        }
                        if (this.isBurning()) {
                            if(iMonazitHandler.getStackInSlot(0).getItem()==MFFSItems.MONAZIT_CRYSTAL.get()) {
                                energy.ifPresent(e -> {
                                    ((MFFSEnergyStorage) e).addEnergy(MFFSConfig.GENERATOR_GENERATE.get() / 200);
                                    if(e.getEnergyStored()<e.getMaxEnergyStored())
                                        ++this.cookTime;
                                });
                            }
                            if (this.cookTime == this.cookTimeTotal) {
                                this.cookTime = 0;
                                this.cookTimeTotal = 200;
                                flag1 = true;
                                ItemStack monazit=iMonazitHandler.getStackInSlot(0);
                                if(!monazit.isEmpty())
                                {
                                    monazit.shrink(1);
                                    if (monazit.isEmpty()) {
                                        ((ItemStackHandler) iMonazitHandler).setStackInSlot(0, itemstack.getContainerItem());
                                    }
                                }
                            }
                        } else {
                            this.cookTime = 0;
                        }
                    }
                    else if (!this.isBurning() && this.cookTime > 0) {
                        this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
                    }

                    if (flag != this.isBurning()) {
                        flag1 = true;
                    }

                    if (flag1) {
                        this.markDirty();
                    } }));
            });
            sendOutPower();
        }

    }

    private void sendOutPower() {
        energy.ifPresent(energy-> {
            AtomicInteger capacity=new AtomicInteger(energy.getEnergyStored());
            if(capacity.get()>0)
            {
                for(Direction direction : Direction.values())
                {
                    TileEntity te=world.getTileEntity(pos.offset(direction));
                    if(te!=null)
                    {
                        boolean doContinue=te.getCapability(CapabilityEnergy.ENERGY,direction).map(handler ->
                        {
                            if (handler.canReceive())
                            {
                                int recieved = handler.receiveEnergy(Math.min(capacity.get(),100),false);
                                capacity.addAndGet(-recieved);
                                ((MFFSEnergyStorage)energy).consumeEnergy(recieved);
                                markDirty();
                                return capacity.get()>0;

                            } else {
                                return true;
                            }
                        }).orElse(true);
                        if(!doContinue) {
                            return;
                        }
                    }
                }
            }
        });
    }


    public TileGenerator() {
        super(MFFSTiles.GENERATOR.get());
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        if (compound.contains("fuel")) {
            CompoundNBT invTag = compound.getCompound("fuel");
            fuelHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        }

        if(compound.contains("monazit")) {
            CompoundNBT invTag = compound.getCompound("monazit");
            monazitHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        }
        if(compound.contains("energy")) {
            CompoundNBT energyTag = compound.getCompound("energy");
            energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(energyTag));
        }

        burnTime = compound.getInt("burnTime");

        recipesUsed = compound.getInt("recipesUsed");
        cookTime = compound.getInt("cookTime");
        cookTimeTotal = compound.getInt("cookTimeTotal");
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        fuelHandler.ifPresent(h -> {
            CompoundNBT invTag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("fuel", invTag);
        });
        monazitHandler.ifPresent(h -> {
            CompoundNBT invTag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("monazit", invTag);
        });
        energy.ifPresent(h-> {
            CompoundNBT energyTag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", energyTag);
        });
        compound.putInt("recipesUsed",recipesUsed);
        compound.putInt("burnTime",burnTime);
        compound.putInt("cookTime",cookTime);
        compound.putInt("cookTimeTotal",cookTimeTotal);
        return compound;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new GeneratorContainer(id,world,pos,playerInventory,playerEntity);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            switch (side)
            {

                case DOWN:
                    return fuelHandler.cast();
                case UP:
                    return monazitHandler.cast();
                default:
                    return jointHandler.cast();
            }
        }
        else if(cap == CapabilityEnergy.ENERGY)
            return energy.cast();

        return super.getCapability(cap, side);
    }
}
