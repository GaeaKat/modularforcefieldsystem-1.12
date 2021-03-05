package net.newgaea.mffs.client.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.client.gui.buttons.ButtonSwitch;
import net.newgaea.mffs.client.gui.buttons.PowerModeSwitch;
import net.newgaea.mffs.common.inventory.CapacitorContainer;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.network.messages.TogglePowerLinkModeMessage;
import net.newgaea.mffs.common.network.messages.ToggleSwitchModeMessage;

public class CapacitorScreen extends ContainerScreen<CapacitorContainer> {
    private ResourceLocation GUI = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/capacitor.png");

    public CapacitorScreen(CapacitorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize=176;
        this.ySize=207;
    }

    @Override
    protected void init() {
        super.init();
        addButton(new ButtonSwitch(this,(width / 2) + 65, (height / 2) - 100, button -> MFFS.networkHandler.sendToggleSwitchMode(new ToggleSwitchModeMessage(container.getPos()))));
        addButton(new PowerModeSwitch(this,(width / 2) + 20, (height / 2) - 28, button -> MFFS.networkHandler.sendTogglePowerLinkMode(new TogglePowerLinkModeMessage(container.getPos()))));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        //super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        this.font.drawString(matrixStack,"Force Energy Capacitor",8,25,0x404040);
        this.font.drawString(matrixStack,this.getContainer().getDeviceName(),8,8,0x404040);
        this.font.drawString(matrixStack,"FE: "+String.valueOf(this.getContainer().getAvailablePower()),8,100,0x404040);
        this.font.drawString(matrixStack,"Power Uplink:",8,80,0x404040);
        this.font.drawString(matrixStack,"Transmit Range",8,60,0x404040);
        this.font.drawString(matrixStack,(new StringBuilder()).append(" ").append(this.getContainer().getTransmitRange()).toString(),90,60,0x404040);
        this.font.drawString(matrixStack,"Linked Devices:",8,43,0x404040);
        this.font.drawString(matrixStack,(new StringBuilder()).append(" ").append(this.getContainer().getLinkedDevices()).toString(),90,45,0x404040);
    }

    @Override
    public void render(MatrixStack matrixStack,int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack,mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack,mouseX,mouseY);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack,relX, relY, 0, 0, this.xSize, this.ySize);
        int i1 = (79 * this.getContainer().getPercentageStorageCapacity() / 100);
        this.blit(matrixStack,relX+8,relY+112,176,0,i1+1,79);
    }
}
