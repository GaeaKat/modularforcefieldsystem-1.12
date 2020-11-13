package net.newgaea.mffs.common.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IntArray;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.client.gui.screens.CapacitorScreen;
import net.newgaea.mffs.client.gui.screens.GeneratorScreen;
import net.newgaea.mffs.common.inventory.CapacitorContainer;
import net.newgaea.mffs.common.inventory.GeneratorContainer;
import net.newgaea.mffs.common.libs.LibContainer;

import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSContainer {
    public static void init()
    {

    }


    public static final RegistryEntry<ContainerType<GeneratorContainer>> GENERATOR=MFFSInit.REGISTRATE.object(LibContainer.GENERATOR)
            .container((type,windowId,inventory) -> new GeneratorContainer(type,windowId,inventory.player,new ItemStackHandler(1) {
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
            },new ItemStackHandler(1) {
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
            },new IntArray(6)),()-> GeneratorScreen::new).register();


    public static final RegistryEntry<ContainerType<CapacitorContainer>> CAPACITOR = MFFSInit.REGISTRATE.object(LibContainer.CAPACITOR)
            .container(
                    (type,windowId,playerInv) ->
                            new CapacitorContainer(type,windowId,playerInv.player), ()->CapacitorScreen::new).register();
}
