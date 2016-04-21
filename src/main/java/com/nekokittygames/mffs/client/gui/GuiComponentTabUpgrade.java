package com.nekokittygames.mffs.client.gui;

import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.display.GuiTab;
import net.minecraft.inventory.Slot;

/**
 * Created by Katrina on 19/04/2016.
 */
public class GuiComponentTabUpgrade extends BaseComponent {

    private int width;
    private int height;
    private GuiTab tab;
    private Slot[] slots;
    private int slotX;
    private int slotY;
    private int cols;

    public GuiComponentTabUpgrade(int xPos, int yPos,int width,int height,GuiTab tab,Slot[] slots,int slotX,int slotY,int cols) {
        super(xPos, yPos);
        this.width=width;
        this.height=height;
        this.tab=tab;
        this.slots=slots;
        this.slotX=slotX;
        this.slotY=slotY;
        this.cols =cols;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void render(int guiLeft, int guiTop, int mouseX, int mouseY) {

        if(tab.areChildrenActive())
        {

            int rows=(slots.length%cols)+1;
            for(int i=0;i<rows;i++) {
                for (int j = 0; j < cols; j++) {
                    slots[(i * rows) + j].xDisplayPosition = slotX + 19 * j;

                    slots[(i * rows) + j].yDisplayPosition = slotY + 19 * i;
                }
            }
        }
        else
        {
            for(int i=0;i<slots.length;i++)
            {
                slots[i].xDisplayPosition=-1000;
                slots[i].yDisplayPosition=-1000;
            }
        }
    }

    @Override
    public void renderOverlay(int guiLeft, int guiTop, int mouseX, int mouseY) {

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
