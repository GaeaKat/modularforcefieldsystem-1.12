package net.newgaea.mffs.client.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.client.gui.buttons.ButtonSwitch;
import net.newgaea.mffs.common.inventory.ProjectorContainer;
import net.newgaea.mffs.common.inventory.slots.SlotItemHandlerToggle;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.network.messages.ToggleSwitchModeMessage;

public class ProjectorScreen extends ContainerScreen<ProjectorContainer> {

    private ResourceLocation GUI = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/projector.png");
    public ProjectorScreen(ProjectorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize=176;
        this.ySize=186;
    }

    @Override
    protected void init() {
        super.init();
        addButton(new ButtonSwitch(this, (width / 2) + 60, (height / 2) - 88, button -> MFFS.networkHandler.sendToggleSwitchMode(new ToggleSwitchModeMessage(container.getPos()))));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack,relX, relY, 0, 0, this.xSize, this.ySize);
        for (Slot slot : this.container.inventorySlots) {
            if (slot instanceof SlotItemHandlerToggle) {
                if (slot.isEnabled()) {
                    this.blit(matrixStack, relX + slot.xPos, relY + slot.yPos, 177, 143, 16, 16);
                }
            }
        }
        int i1 = (79 * this.getContainer().getCapacity() / 100);
        this.blit(matrixStack,relX+8,relY+91,176,0,i1+1,70);
    }
    @Override
    public void render(MatrixStack matrixStack,int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack,mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack,mouseX,mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }
}
