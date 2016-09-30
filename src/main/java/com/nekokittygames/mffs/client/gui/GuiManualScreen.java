package com.nekokittygames.mffs.client.gui;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;


public class GuiManualScreen extends GuiContainer {
	private int page = 0;
	private int maxpage;

	private List<String> pages = new ArrayList<String>();

	public GuiManualScreen(Container par1Container) {
		super(par1Container);
		generateIndex();
		maxpage = pages.size() - 1;
		xSize = 256;
		ySize = 216;
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, (width / 2) + 90, (height / 2) + 80,
				22, 16, "-->"));
		buttonList.add(new GuiButton(1, (width / 2) - 110, (height / 2) + 80,
				22, 16, "<--"));
		super.initGui();

	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 0) {
			if (page < maxpage) {
				page++;
			} else {
				page = 0;
			}
		}
		if (guibutton.id == 1) {
			if (page > 0) {
				page--;
			} else {
				page = pages.size() - 1;
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/GuiManual.png"));
		int w = (width - 256) / 2;
		int k = (height - 216) / 2;
		drawTexturedModalRect(w, k, 0, 0, 256, 216);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

		fontRendererObj.drawString("ModularForceFieldSystem Guide", 20, 15,
				0xFFFFFF);
		getcontent(page);
		fontRendererObj.drawString("Page [" + this.page + "] :" + pages.get(page),
				45, 193, 0xFFFFFF);
	}

	private void generateIndex() {
		pages.clear();
		pages.add("Table of Content");
		pages.add("Changelog");
		pages.add("Version Check");
		pages.add("Monazit/Forcicium/-Cell");
		pages.add("Card Overview(1)");
		pages.add("Card Overview(2)");
	}

	private void getcontent(int page) {

		RenderHelper.enableGUIStandardItemLighting();

		switch (page) {
		case 0:
			fontRendererObj.drawString("Table of Contents", 90, 45, 0xFFFFFF);
			for (int p = 0; p < pages.size(); p++) {
				fontRendererObj.drawString("[" + p + "]: " + pages.get(p), 20,
						65 + (p * 10), 0xFFFFFF);
			}
			break;
		case 1:
			fontRendererObj.drawString("Changelog V2.2.8.3.6", 90, 45, 0xFFFFFF);
			fontRendererObj.drawString("fix Coverter Powerloop", 20, 65, 0xFFFFFF);
			fontRendererObj.drawString("fix Textur Bug ", 20, 75, 0xFFFFFF);
			fontRendererObj.drawString("change ForceField -> ", 20, 85, 0xFFFFFF);
			fontRendererObj.drawString("touch damage system", 20, 95, 0xFFFFFF);

			break;
		case 2:
			fontRendererObj.drawString("Versions Check", 90, 45, 0xFFFFFF);
			fontRendererObj.drawString("Current Version: "
					+ ModularForceFieldSystem.VersionLocal, 20, 65, 0xFFFFFF);
			fontRendererObj.drawString("Newest Version : "
					+ ModularForceFieldSystem.VersionRemote, 20, 75, 0xFFFFFF);
			break;
		case 3:
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSMonazitOre), 30,
					45);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemForcicium),
					30, 65);
			itemRender
					.renderItemIntoGUI(
							new ItemStack(
									ModularForceFieldSystem.MFFSitemForcicumCell),
							30, 85);

			fontRendererObj.drawString("Monazit Ore (Block/WorldGen)", 60, 50,
					0xFFFFFF);
			fontRendererObj.drawString("Forcicium (Item/for Crafting)", 60, 70,
					0xFFFFFF);
			fontRendererObj.drawString("Forcicium Cell (Item/from Crafting)", 60,
					90, 0xFFFFFF);

			fontRendererObj.drawString("Monazite can be found between 80 and 0",
					20, 105, 0xFFFFFF);
			fontRendererObj.drawString("Use furnace to get 4 Forcicium", 20, 115,
					0xFFFFFF);
			fontRendererObj.drawString("Use IC Macerator to get 8 Forcicium", 20,
					125, 0xFFFFFF);
			fontRendererObj.drawString("Forcicium Cell can store 1kForcicium", 20,
					135, 0xFFFFFF);
			fontRendererObj.drawString("if in hand right click to activate", 20,
					145, 0xFFFFFF);
			fontRendererObj.drawString("when active remove Forcicium from  ", 20,
					155, 0xFFFFFF);
			fontRendererObj.drawString("Player Inventory and stores it", 20, 165,
					0xFFFFFF);
			break;
		case 4:

			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
					15, 45);
			fontRendererObj.drawString(
					"Card <blank> get from Crafting stackable ", 35, 45,
					0xFFFFFF);
			fontRendererObj.drawString("up to 16 need for create all Cards ", 35,
					55, 0xFFFFFF);

			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemfc), 15, 95);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSCapacitor), 35,
					105);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
					140, 105);
			fontRendererObj.drawString("Card <Power Link> get from right click",
					35, 95, 0xFFFFFF);
			fontRendererObj.drawString("Capacitor with a ", 55, 110, 0xFFFFFF);

			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSItemSecLinkCard),
					15, 145);
			fontRendererObj.drawString("Card <Security Station Link> get from",
					35, 145, 0xFFFFFF);
			fontRendererObj.drawString("right click", 35, 160, 0xFFFFFF);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSSecurtyStation),
					90, 155);
			fontRendererObj.drawString("SecurityStation with a ", 110, 160,
					0xFFFFFF);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
					220, 155);

			break;
		case 5:

			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSAccessCard), 15,
					45);
			fontRendererObj.drawString("Card <Access license> create inside a  ",
					35, 45, 0xFFFFFF);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSSecurtyStation),
					35, 55);
			fontRendererObj.drawString("SecurityStation with a", 55, 60, 0xFFFFFF);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
					170, 55);

			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSItemIDCard), 15,
					85);
			fontRendererObj.drawString("Card <Personal ID> create with help  ",
					35, 85, 0xFFFFFF);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemMFDidtool),
					35, 100);
			fontRendererObj.drawString("MultiTool right click create self", 55,
					98, 0xFFFFFF);
			fontRendererObj.drawString("or left click for another Player", 55,
					110, 0xFFFFFF);

			itemRender
					.renderItemIntoGUI(

							new ItemStack(
									ModularForceFieldSystem.MFFSitemDataLinkCard),
							15, 125);
			fontRendererObj.drawString("Card <Data Link> create with help  ", 35,
					125, 0xFFFFFF);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemMFDidtool),
					35, 140);
			fontRendererObj.drawString("MultiTool right click any ", 55, 138,
					0xFFFFFF);
			fontRendererObj.drawString("MFFS Maschine ", 55, 150, 0xFFFFFF);

			itemRender.renderItemIntoGUI(
					new ItemStack(
							ModularForceFieldSystem.MFFSitemInfinitePowerCard),
					15, 165);
			fontRendererObj.drawString("Admin Infinite Force Energy Card", 35,
					170, 0xFFFFFF);

			break;
		}
		RenderHelper.disableStandardItemLighting();
	}

}
