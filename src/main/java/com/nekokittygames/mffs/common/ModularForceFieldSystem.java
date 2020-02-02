/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    Thunderdark - initial implementation
 */

package com.nekokittygames.mffs.common;

import static com.nekokittygames.mffs.common.ModularForceFieldSystem.MODID;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.nekokittygames.mffs.common.block.ModBlocks;
import com.nekokittygames.mffs.common.item.ModItems;
import com.nekokittygames.mffs.common.tileentity.TileEntityControlSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.network.PacketTileHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ItemLayerModel.Loader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = MODID, name = "Modular ForceField System", version = "4.2.0", dependencies = "after:enderio;after:ic2;after:thermalexpansion")
public class ModularForceFieldSystem {

	public static final String MODID="modularforcefieldsystem";
	public static CreativeTabs MFFSTab;

	public static int MFFSRENDER_ID = 2908;
	public static SimpleNetworkWrapper networkWrapper;




	public static int MonazitOreworldamount = 4;
	public static int MonazitOreSmeltAmount=4;
	public static int forceFieldBlockCost;
	public static int forceFieldBlockCreateModifier;
	public static int forceFieldBlockZapperModifier;

	public static int forceFieldTransportCost;
	public static int forceFieldMaxBlocksPerTick;

	public static boolean forceFieldRemoveOnlyWaterAndLava;

	public static boolean influencedByOtherMods;
	public static boolean adventureMapMode;

	public static boolean ic2Found = false;
	public static boolean ee3Found = false;
	public static boolean buildcraftFound = false;
	public static boolean thermalExpansionFound = false;
	public static boolean enderIoFound = false;
	public static boolean computercraftFound = false;
	public static boolean appliedEnergisticsFound = false;

	public static boolean enableIC2Recipes = true;
	public static boolean enableTERecipes = true;
	public static boolean enableAEGrindStoneRecipe = true;

	public static int grindRecipeOutput = 8;
	public static int grindRecipeCost = 16;

	public static int graphicsStyle = 1;

	public static boolean showZapperParticles;
	public static boolean enableUUMatterForcicium;
	public static boolean enableChunkLoader = true;

	public static int ForciciumWorkCycle;
	public static int ForciciumCellWorkCycle;
	public static int ExtractorPassForceEnergyGenerate;

	public static int DefenceStationKillForceEnergy;
	public static int DefenceStationSearchForceEnergy;
	public static int DefenceStationScannForceEnergy;

	public static boolean DefenceStationNPCScannsuppressnotification;

	public static Configuration MFFSconfig;

	public static String Admin;
	public static String VersionLocal;
	public static String VersionRemote;
	public static String VersionRemoteURL;

	@SidedProxy(clientSide = "com.nekokittygames.mffs.client.ClientProxy", serverSide = "com.nekokittygames.mffs.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance(MODID)
	public static ModularForceFieldSystem instance;
	public static boolean enableEIRecipes;
	public  static Logger log;
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws Exception {
		log=event.getModLog();
		initIC2Plugin();
		initBuildcraftPlugin();
		initEE3Plugin();
		initThermalExpansionPlugin();
		initEnderIoPlugin();
		initComputerCraftPlugin();
		initAEPlugin();


		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);
		networkWrapper=NetworkRegistry.INSTANCE.newSimpleChannel("MFFS");
		networkWrapper.registerMessage(PacketTileHandler.class,PacketTileHandler.class,nextID(), Side.SERVER);
		MFFSconfig = new Configuration(event.getSuggestedConfigurationFile());
		event.getModMetadata().version = Versioninfo.curentversion();
		try {
			MFFSconfig.load();
			MFFSTab = new MFFSCreativeTab(CreativeTabs.getNextID(), "MFFS");

			Property prop_VersionremoteUrl = MFFSconfig
					.get(Configuration.CATEGORY_GENERAL, "VersionremoteUrl",
							"https://bitbucket.org/SeargeDP/modularforcefieldsystem/downloads/versioninfo");
			prop_VersionremoteUrl.setComment("URL to MFFS VersionInfo file");
			VersionRemoteURL = prop_VersionremoteUrl.getString();

			Property chunckloader_prop = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "Chunkloader", true);
			chunckloader_prop.setComment("Set this to false to turn off the MFFS Chuncloader ability");
			enableChunkLoader = chunckloader_prop.getBoolean(true);

			Property DefSationNPCScannoti = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"DefenceStationNPCScannnotification", false);
			DefSationNPCScannoti.setComment("Set this to true to turn off the DefenceStation notification is in NPC Mode");
			DefenceStationNPCScannsuppressnotification = DefSationNPCScannoti
					.getBoolean(false);

			Property zapperParticles = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "renderZapperParticles",
					true);
			zapperParticles.setComment("Set this to false to turn off the small smoke particles present around TouchDamage enabled ForceFields.");
			showZapperParticles = zapperParticles.getBoolean(true);

			Property uumatterForciciumprop = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "uumatterForcicium", true);
			uumatterForciciumprop.setComment("Add TechReborn UU-Matter Recipes for Forcicium");
			enableUUMatterForcicium = uumatterForciciumprop.getBoolean(true);

			Property monazitWorldAmount = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "MonazitOreWorldGen", 4);
			monazitWorldAmount.setComment("Controls the size of the ore node that Monazit Ore will generate in");
			MonazitOreworldamount = monazitWorldAmount.getInt(4);

			Property adminList = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"ForceFieldMaster", "nobody");
			adminList.setComment("Add users uuids to this list to give them admin permissions split by ;");
			Admin = adminList.getString();

			Property influencedByOther = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "influencedbyothermods",
					true);
			influencedByOther.setComment("Should MFFS be influenced by other mods. e.g. ICBM's EMP");
			influencedByOtherMods = influencedByOther.getBoolean(true);

			Property ffRemoveWaterLavaOnly = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"forcefieldremoveonlywaterandlava", false);
			ffRemoveWaterLavaOnly.setComment("Should forcefields only remove water and lava when sponge is enabled?");
			forceFieldRemoveOnlyWaterAndLava = ffRemoveWaterLavaOnly
					.getBoolean(false);

			Property feTransportCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "forcefieldtransportcost",
					10000);
			feTransportCost.setComment("How much FE should it cost to transport through a field?");
			forceFieldTransportCost = feTransportCost.getInt(10000);

			Property feFieldBlockCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "forcefieldblockcost", 1);
			feFieldBlockCost.setComment("How much upkeep FE cost a default ForceFieldblock per second");
			forceFieldBlockCost = feFieldBlockCost.getInt(1);

			Property BlockCreateMod = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"forcefieldblockcreatemodifier", 10);
			BlockCreateMod.setComment("Energy need for create a ForceFieldblock (forcefieldblockcost*forcefieldblockcreatemodifier)");
			forceFieldBlockCreateModifier = BlockCreateMod.getInt(10);

			Property ffZapperMod = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"forcefieldblockzappermodifier", 2);
			ffZapperMod.setComment("Energy need multiplier used when the zapper option is installed");
			forceFieldBlockZapperModifier = ffZapperMod.getInt(2);

			Property maxFFGenPerTick = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"forcefieldmaxblockpeerTick", 5000);
			maxFFGenPerTick.setComment("How many field blocks can be generated per tick?");
			forceFieldMaxBlocksPerTick = maxFFGenPerTick.getInt(5000);

			Property fcWorkCycle = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "ForceciumWorkCylce", 250);
			fcWorkCycle.setComment("WorkCycle amount of Forcecium inside a Extractor");
			ForciciumWorkCycle = fcWorkCycle.getInt(250);

			Property fcCellWorkCycle = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "ForceciumCellWorkCylce",
					230);
			fcCellWorkCycle.setComment("WorkCycle amount of Forcecium Cell inside a Extractor");
			ForciciumCellWorkCycle = fcCellWorkCycle.getInt(230);

			Property extractorPassFEGen = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"ExtractorPassForceEnergyGenerate", 12000);
			extractorPassFEGen.setComment("How many ForceEnergy generate per WorkCycle");
			ExtractorPassForceEnergyGenerate = extractorPassFEGen.getInt(12000);

			ExtractorPassForceEnergyGenerate = (ExtractorPassForceEnergyGenerate / 4000) * 4000;

			Property defStationKillCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"DefenceStationKillForceEnergy", 10000);
			defStationKillCost.setComment("How much FE does the AreaDefenseStation when killing someone");
			DefenceStationKillForceEnergy = defStationKillCost.getInt(10000);

			Property defStationSearchCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"DefenceStationSearchForceEnergy", 1000);
			defStationSearchCost.setComment("How much FE does the AreaDefenseStation when searching someone for contraband");
			DefenceStationSearchForceEnergy = defStationSearchCost.getInt(1000);

			Property defStationScannCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"DefenceStationScannForceEnergy", 10);
			defStationScannCost.setComment("How much FE does the AreaDefenseStation when Scann for Targets (amount * range / tick)");
			DefenceStationScannForceEnergy = defStationScannCost.getInt(10);

			Property Adventuremap = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "adventuremap", false);
			Adventuremap.setComment("Set MFFS to AdventureMap Mode Extractor need no Forcecium and ForceField have no click damage");
			adventureMapMode = Adventuremap.getBoolean(false);

			Property ic2Recipes = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "enableIC2Recipes", true);
			ic2Recipes.setComment("Set to false to disable IndustrialCraft 2 recipes for MFFS machines.");
			enableIC2Recipes = ic2Recipes.getBoolean(true);

			Property teRecipes = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"enableTERecipes", true);
			teRecipes.setComment("Set to false to disable Thermal Expansion recipes for MFFS machines.");
			enableTERecipes = teRecipes.getBoolean(true);

			Property eiRecipes = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"enableEIRecipes", true);
			eiRecipes.setComment("Set to false to disable Ender IO recipes for MFFS machines.");
			enableEIRecipes= eiRecipes.getBoolean(true);

			Property grindRecipe = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"enableAEGrindStoneRecipe", true);
			grindRecipe.setComment("Set to false to disable the Applied Energistics Grind Stone recipe for MFFS " +
					"machines.");
			enableAEGrindStoneRecipe = grindRecipe.getBoolean(true);
			Property monazitSmeltOutput=MFFSconfig.get(Configuration.CATEGORY_GENERAL,"monazitOreSmeltAmount",4);
			monazitSmeltOutput.setComment("Amount of Forcium to generate per Monazit ore when smelting");
			MonazitOreSmeltAmount=monazitSmeltOutput.getInt(4);

			Property grindOutput = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"grindRecipeOutput", 8);
			grindOutput.setComment("Amount of Forcicium to generate per Monazit ore in the AE Grind Stone.");
			grindRecipeOutput = grindOutput.getInt(8);

			Property grindCost = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"grindRecipeCost", 12);
			grindCost.setComment("Number of clicks on the Applied Energistics Grind Stone Crank that must be " +
					"registered to turn Monazit into Forcicium.");
			grindRecipeCost = grindCost.getInt(12);

			Property controlSystemRange = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "controlSystemRange",
					TileEntityControlSystem.MACHINE_RANGE);
			controlSystemRange.setComment("Range (in meters) a block must be within for the control system to be able " +
					"to access its GUI.");
			TileEntityControlSystem.MACHINE_RANGE = controlSystemRange.getInt(TileEntityControlSystem.MACHINE_RANGE);

			// Machines + Blocks



		} catch (Exception e) {
			log.error(
					"ModularForceFieldSystem has a problem loading its configuration!",e);
			System.out.println(e.getMessage());
		} finally {
			MFFSconfig.save();
		}

		VersionLocal = Versioninfo.curentversion();
		VersionRemote = Versioninfo.newestversion();

		//GameRegistry.register(MFFSMonazitOre);
		//GameRegistry.register(new ItemBlock(MFFSMonazitOre),MFFSMonazitOre.getRegistryName());
		//proxy.setupClientBlock(MFFSMonazitOre, LibBlockNames.MONAZIT_ORE);
		//GameRegistry.register(MFFSFieldblock);
        //GameRegistry.register(new ItemBlock(MFFSFieldblock),MFFSFieldblock.getRegistryName());
        //proxy.setupClientFieldBlock(MFFSFieldblock,LibBlockNames.FORCE_FIELD);
		//MFFSFieldblock.setCreativeTab(MFFSTab);
		//GameRegistry.registerTileEntity(TileEntityForceField.class,
				//"MFFSForceField");
		//proxy.setupClientBlock(MFFSFieldblock, LibBlockNames.FORCE_FIELD);

		proxy.registerTileEntitySpecialRenderer();


	}
	private static int packetId = 0;

	private int nextID() {
		return packetId++;
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent evt) {

		NetworkRegistry.INSTANCE.registerGuiHandler(instance,proxy);
		GameRegistry.registerWorldGenerator(new MFFSWorldGenerator(), 0);

		OreDictionary.registerOre("dustMonazit",
				ModItems.FORCICIUM);
		OreDictionary.registerOre("oreMonazit",
				ModBlocks.MONAZIT_ORE);
		MFFSMaschines.initialize();
		ProjectorTyp.initialize();
		ProjectorOptions.initialize();

		MFFSRecipes.init();
		proxy.addBookPages();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent evt) {

		ForgeChunkManager.setForcedChunkLoadingCallback(instance,
				new MFFSChunkloadCallback());
	}

	public class MFFSChunkloadCallback implements
			ForgeChunkManager.OrderedLoadingCallback {
		@Override
		public void ticketsLoaded(List<Ticket> tickets, World world) {
			for (Ticket ticket : tickets) {
				int MaschineX = ticket.getModData().getInteger("MaschineX");
				int MaschineY = ticket.getModData().getInteger("MaschineY");
				int MaschineZ = ticket.getModData().getInteger("MaschineZ");
				TileEntityMachines Machines = (TileEntityMachines) world
						.getTileEntity(new BlockPos(MaschineX,MaschineY,MaschineZ));
				Machines.forceChunkLoading(ticket);

			}
		}

		@Override
		public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world,
				int maxTicketCount) {
			List<Ticket> validTickets = Lists.newArrayList();
			for (Ticket ticket : tickets) {
				int MaschineX = ticket.getModData().getInteger("MaschineX");
				int MaschineY = ticket.getModData().getInteger("MaschineY");
				int MaschineZ = ticket.getModData().getInteger("MaschineZ");

				TileEntity tileEntity = world.getTileEntity(new BlockPos(MaschineX,MaschineY,MaschineZ));
				if (tileEntity instanceof TileEntityMachines) {
					validTickets.add(ticket);
				}
			}
			return validTickets;
		}

	}

	public void initComputerCraftPlugin() {
		log.info("Loading module for ComputerCraft");
		
		if(net.minecraftforge.fml.common.Loader.isModLoaded("computercraft")) {
			computercraftFound = true;
		} else {
			log.info("Module not loaded: ComputerCraft not found");
		}
	}

	public void initBuildcraftPlugin() {

		log.info("Loading module for Buildcraft");

		if(net.minecraftforge.fml.common.Loader.isModLoaded("buildcraft")) {
			buildcraftFound = true;

		} else {
			log.info("Module not loaded: Buildcraft not found");

		}
	}

	public void initThermalExpansionPlugin() {

		log.info("Loading module for ThermalExpansion");

		if(net.minecraftforge.fml.common.Loader.isModLoaded("thermalexpansion")) {
			thermalExpansionFound = true;

		} else {
			log.info("Module not loaded: ThermalExpansion not found");

		}
	}

	public void initEnderIoPlugin() {

		log.info("Loading module for EnderIo");

		if(net.minecraftforge.fml.common.Loader.isModLoaded("enderio")) {
			enderIoFound = true;

		} else {
			log.info("Module not loaded: EnderIo not found");

		}
	}
	public void initEE3Plugin() {

		log.info("Loading module for EE3");

		if(net.minecraftforge.fml.common.Loader.isModLoaded("ee3")) {
			ee3Found = true;

		} else {
			log.info("Module not loaded: EE3 not found");

		}
	}

	public void initIC2Plugin() {

		log.info("Loading module for IC2");

		if(net.minecraftforge.fml.common.Loader.isModLoaded("ic2")) {
			ic2Found = true;

		} else {
			log.info("Module not loaded: IC2 not found");

		}
	}

	public void initAEPlugin() {
		log.info("Loading module for Applied Energistics");

		if(net.minecraftforge.fml.common.Loader.isModLoaded("appeng")) {
			appliedEnergisticsFound = true;
		} else {
			log.info("Module not loaded: Applied Energistics not found");
		}
	}

}