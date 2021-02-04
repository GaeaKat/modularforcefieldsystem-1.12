package net.newgaea.mffs.client.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.client.gui.buttons.ButtonSwitch;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.inventory.ExtractorContainer;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.network.messages.ToggleSwitchModeMessage;

public class ExtractorScreen extends ContainerScreen<ExtractorContainer> {
    private ResourceLocation GUI = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/extractor.png");
    public ExtractorScreen(ExtractorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
        int workPowerSlider = (79*this.container.getWorkDone() / 100);
        blit(matrixStack,relX+49,relY+89,176,0,workPowerSlider,6);

        int workCycle = (32 * this.container.getWorkCycle()) / MFFSConfig.MONAZIT_WORK_CYCLE.get();
        blit(matrixStack,relX+73,relY+50,179,81,workCycle,32);

        int forceEnergy = (24 * this.container.getForceEnergyBuffer() / this.container.getMaxForceEnergyBuffer());
        blit(matrixStack,relX+137,relY+60,219,80,32,forceEnergy);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        //super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        this.font.drawString(matrixStack,"Force Energy", 5, 25, 0x404040);
        this.font.drawString(matrixStack,"Upgrades", 10, 50, 0x404040);
        this.font.drawString(matrixStack,container.getDeviceName(), 8, 9, 0x404040);
        this.font.drawString(matrixStack,"Extractor", 5, 35, 0x404040);
        this.font.drawString(matrixStack,
                String.valueOf(container.getForceEnergyBuffer() / 1000).concat(
                        "k"), 140, 89, 0x404040);

        this.font.drawString(matrixStack,
                String.valueOf(container.getWorkDone()).concat("%"), 23, 89,
                0x404040);
    }
}
