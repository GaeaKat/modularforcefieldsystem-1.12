package net.newgaea.mffs.client.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.newgaea.mffs.common.inventory.ProjectorContainer;
import net.newgaea.mffs.common.libs.LibMisc;

public class ProjectorScreen extends ContainerScreen<ProjectorContainer> {

    private ResourceLocation GUI = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/projector.png");
    public ProjectorScreen(ProjectorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize=176;
        this.ySize=186;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack,relX, relY, 0, 0, this.xSize, this.ySize);
    }
}
