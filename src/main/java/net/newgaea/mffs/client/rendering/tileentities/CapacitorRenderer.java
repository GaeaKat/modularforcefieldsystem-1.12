package net.newgaea.mffs.client.rendering.tileentities;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.newgaea.mffs.common.tiles.TileCapacitor;

public class CapacitorRenderer extends TileEntityRenderer<TileCapacitor> {
    public CapacitorRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileCapacitor tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        switch (tileEntityIn.getSide()) {
            case DOWN:
                break;
            case UP:
                matrixStackIn.translate(1,1,0);
                matrixStackIn.rotate(new Quaternion(new Vector3f(1,0,0),180,true));
                matrixStackIn.rotate(new Quaternion(new Vector3f(0,1,0),180,true));
                break;
            case NORTH:
                matrixStackIn.translate(1,1,1);
                matrixStackIn.rotate(new Quaternion(new Vector3f(0,1,0),180,true));
                matrixStackIn.rotate(new Quaternion(new Vector3f(1,0,0),90,true));
                break;
            case SOUTH:
                matrixStackIn.translate(0,1,0);
                matrixStackIn.rotate(new Quaternion(new Vector3f(0,0,1),0,true));
                matrixStackIn.rotate(new Quaternion(new Vector3f(1,0,0),90,true));
                break;
            case WEST:
                matrixStackIn.translate(1,1,0);
                matrixStackIn.rotate(new Quaternion(new Vector3f(0,1,0),-90,true));
                matrixStackIn.rotate(new Quaternion(new Vector3f(1,0,0),90,true));
                break;

            case EAST:
                matrixStackIn.translate(0,1,1);
                matrixStackIn.rotate(new Quaternion(new Vector3f(0,1,0),90,true));
                matrixStackIn.rotate(new Quaternion(new Vector3f(1,0,0),90,true));
                break;

        }
        float dx = 1F / 16;
        float dz = 1F / 16;
        float displayWidth = 1 - 2F / 16;
        float displayHeight = 1 - 2F / 16;
        matrixStackIn.translate(dx+displayWidth / 2,1.01f,dz+displayHeight/2);
        matrixStackIn.rotate(new Quaternion(new Vector3f(1,0,0),-90,true));
        FontRenderer fontRenderer = this.renderDispatcher.fontRenderer;
        int maxWidth = 1;
        String header = "MFFS Capacitor";
        maxWidth = Math.max(fontRenderer.getStringWidth(header),maxWidth);
        maxWidth+=4;
        int lineHeight=fontRenderer.FONT_HEIGHT+1;
        int requiredHeight = lineHeight * 1;
        float scaleX = displayWidth / maxWidth;
        float scaleY=displayHeight / requiredHeight;
        float scale = Math.min(scaleX,scaleY);
        matrixStackIn.scale(scale,-scale,scale);
        int offsetX;
        int offsetY;
        int realHeight = (int) Math.floor(displayHeight/scale);
        int realWidth = (int)Math.floor(displayWidth/scale);

        if(scaleX < scaleY) {
            offsetX = 2;
            offsetY = (realHeight - requiredHeight) /2;
        } else {
            offsetX = (realWidth - maxWidth) / 2 + 2;
            offsetY = 0;
        }
        fontRenderer.drawString(matrixStackIn,header,offsetX - realWidth/2,1 + offsetY - realHeight / 2 + -2 * lineHeight,1);
        fontRenderer.drawString(matrixStackIn,"capacity: ",offsetX - realWidth / 2, 1 + offsetY - realHeight / 2 + 0 * lineHeight, 1);
        fontRenderer.drawString(matrixStackIn,
                String.valueOf(tileEntityIn.getPercentageStorageCapacity())
                        .concat(" % "),
                offsetX
                        + realWidth
                        / 2
                        - offsetX
                        - fontRenderer.getStringWidth(String.valueOf(
                        tileEntityIn.getPercentageStorageCapacity())
                        .concat(" % ")), offsetY - realHeight / 2
                        - 0 * lineHeight, 1);
        fontRenderer.drawString(matrixStackIn ,"range: ", offsetX - realWidth / 2, 1
                + offsetY - realHeight / 2 + 1 * lineHeight, 1);
        fontRenderer.drawString(matrixStackIn,
                String.valueOf( tileEntityIn.getTransmitRange()),
                offsetX
                        + realWidth
                        / 2
                        - offsetX
                        - fontRenderer.getStringWidth(String
                        .valueOf(tileEntityIn.getTransmitRange())),
                offsetY - realHeight / 2 + 1 * lineHeight, 1);
        fontRenderer.drawString(matrixStackIn,
                "linked device: ", offsetX - realWidth / 2,
                1 + offsetY - realHeight / 2 + 2 * lineHeight, 1);
        fontRenderer.drawString(matrixStackIn,
                String.valueOf(tileEntityIn.getLinkedDevices()),
                offsetX
                        + realWidth
                        / 2
                        - offsetX
                        - fontRenderer.getStringWidth(String
                        .valueOf(tileEntityIn.getLinkedDevices())),
                offsetY - realHeight / 2 + 2 * lineHeight, 1);
        matrixStackIn.pop();
    }
}
