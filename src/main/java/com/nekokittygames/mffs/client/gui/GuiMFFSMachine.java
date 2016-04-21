package com.nekokittygames.mffs.client.gui;

import com.nekokittygames.mffs.common.inventory.MFFSMachineContainer;
import com.nekokittygames.mffs.common.tiles.MFFSTile;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.GuiColor;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentTabSlotHolder;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentLongText;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTab;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import elec332.core.util.StatCollector;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import scala.collection.immutable.$colon$colon;
import scala.collection.immutable.List$;

import java.awt.*;
import java.util.UUID;

/**
 * Created by Katrina on 19/04/2016.
 */
public abstract class GuiMFFSMachine<T extends MFFSMachineContainer > extends GuiBase<T> {


    protected MFFSTile tileEntity;
    protected UUID network=null;
    public GuiMFFSMachine(T inventory, MFFSTile tileEntity, int width, int height, String name) {
        super(inventory, width, height, name);
        this.tileEntity=tileEntity;
        addRightTabs(rightTabs());
        addLeftTabs(leftTabs());


    }


    @Override
    public void addLeftTabs(GuiTabCollection tabs) {
        super.addLeftTabs(tabs);
        if(tileEntity!=null)
        {
            scala.collection.immutable.List<BaseComponent> components= List$.MODULE$.empty();
            scala.collection.immutable.List<BaseComponent>  result=new $colon$colon(new GuiComponentText(GuiColor.YELLOW + StatCollector.translateToLocal("mffs.text.information"), 10, 7),components);
            result=new $colon$colon<BaseComponent>(new GuiComponentLongText(10,20,tileEntity.getDescription(),100,65,50),result);
            tabs.addReverseTab(result,120,100,new Color(130,0,0),new ItemStack(tileEntity.getBlockType()));
        }
    }

    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        super.addRightTabs(tabs);
        if(tileEntity!=null)
        {
            scala.collection.immutable.List<BaseComponent> components= List$.MODULE$.empty();
            scala.collection.immutable.List<BaseComponent>  result=new $colon$colon(new GuiComponentText(GuiColor.ORANGE+ StatCollector.translateToLocal("mffs.text.upgrades"),26,6),components);
            tabs.addTab(result,80,65,new Color(120,168,96),new ItemStack(Items.arrow));
            GuiTab tab=tabs.getTabs().head();
            int rows=(tileEntity.getUpgradeSlots()%2)+1;
            for(int i=0;i<rows;i++) {
                for (int j = 0; j < 2; j++) {
                    if(i+j*2<tileEntity.getUpgradeSlots())
                        tab.addChild(new GuiComponentTabSlotHolder(200+10+18*j,23+18*i,18,18,tab,inventory().getSlot(i+j*2),200+18*j,23+18*i));

                }
            }

        }
    }
}
