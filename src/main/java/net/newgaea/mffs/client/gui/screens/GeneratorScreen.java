package net.newgaea.mffs.client.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.newgaea.mffs.client.gui.widgets.VerticalGuage;
import net.newgaea.mffs.common.inventory.GeneratorContainer;
import net.newgaea.mffs.common.libs.LibMisc;

public class GeneratorScreen extends ContainerScreen<GeneratorContainer> {
    private ResourceLocation GUI = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/generator.png");
    private ResourceLocation FORGE_GUAGE=new ResourceLocation(LibMisc.MOD_ID,"textures/gui/widgets/forge_guage.png");
    private VerticalGuage guage;
    public GeneratorScreen(GeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack,int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack,mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack,mouseX,mouseY);
    }

    @Override
    protected void init() {
        super.init();
        guage=new VerticalGuage(this.guiLeft+110,this.guiTop+20,14,42,1,0,17,0,14,42,FORGE_GUAGE);
        guage.setMax(this.container.getMaxEnergy()).setCurrent(this.container.getEnergy());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack,relX, relY, 0, 0, this.xSize, this.ySize);
        int i=this.getGuiLeft();
        int j=this.getGuiTop();
        if (this.container.func_217061_l()) {
            int k = this.container.getBurnLeftScaled();
            this.blit(matrixStack,i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.container.getCookProgressionScaled();
        this.blit(matrixStack,i + 79, j + 34, 176, 14, l + 1, 16);
        guage.setCurrent(this.container.getEnergy());
        guage.render(matrixStack,1,1,1);
    }


}