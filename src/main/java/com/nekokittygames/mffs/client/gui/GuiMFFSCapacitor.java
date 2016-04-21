package com.nekokittygames.mffs.client.gui;

import com.dyonovan.neotech.utils.ClientUtils;
import com.nekokittygames.mffs.common.inventory.MFFSCapacitorContainer;
import com.nekokittygames.mffs.common.inventory.MFFSMachineContainer;
import com.nekokittygames.mffs.common.tiles.TileCapacitor;
import com.teambr.bookshelf.api.jei.drawables.GuiComponentPowerBarJEI;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.GuiColor;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentTabSlotHolder;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBarGradient;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTab;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import elec332.core.util.StatCollector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BossInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import scala.collection.immutable.List$;
import scala.collection.immutable.$colon$colon;
import scala.collection.mutable.ArrayBuffer;
import scala.collection.mutable.ArrayBuffer$;
import scala.collection.mutable.Builder;
import scala.xml.dtd.impl.Base;

/**
 * Created by Katrina on 19/04/2016.
 */
public class GuiMFFSCapacitor extends GuiMFFSMachine<MFFSCapacitorContainer>{


    public GuiMFFSCapacitor(InventoryPlayer player, TileCapacitor capacitor, int width, int height, String name) {
        super(new MFFSCapacitorContainer(player,capacitor),capacitor, width, height, name);
    }




    @Override
    public void addComponents() {
        this.components();

        BaseComponent comp=new GuiComponentPowerBarGradient(8,18,18,60,new Color(255,0,0)){

            public BaseComponent Init()
            {
                addColor(new Color(255, 150, 0));
                addColor(new Color(255, 255, 0));
                return this;
            }

            @Override
            public int getEnergyPercent(int scale) {
                GlStateManager.enableBlend();
                if(tileEntity!=null)
                {
                    TileCapacitor cap= (TileCapacitor) tileEntity;
                    return cap.getEnergyStored(null)*scale/((TileCapacitor) tileEntity).getMaxEnergyStored(null);
                }
                return 0;
            }

            @Override
            public ArrayBuffer<String> getDynamicToolTip(int mouseX, int mouseY) {
                Builder<String, ArrayBuffer<String>> builder = ArrayBuffer$.MODULE$.newBuilder();
                if(tileEntity!=null) {
                    TileCapacitor cap = (TileCapacitor) tileEntity;

                    builder.$plus$eq(GuiColor.ORANGE + StatCollector.translateToLocal("mffs.text.redstoneFlux"));
                    builder.$plus$eq(ClientUtils.formatNumber(cap.getEnergyStored(null))+" / "+ClientUtils.formatNumber(cap.getMaxEnergyStored(null))+" RF");
                }
                return builder.result();

            }
        }.Init();
        Builder<BaseComponent,ArrayBuffer<BaseComponent>> builder=ArrayBuffer$.MODULE$.newBuilder();
        builder.$plus$eq(comp);
        this.components_$eq(builder.result());
    }
}
