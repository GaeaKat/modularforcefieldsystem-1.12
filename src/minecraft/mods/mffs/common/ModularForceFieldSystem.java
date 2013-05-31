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

package mods.mffs.common;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import mods.mffs.common.block.*;
import mods.mffs.common.event.EE3Event;
import mods.mffs.common.item.*;
import mods.mffs.common.localization.LocalizationManager;
import mods.mffs.common.modules.*;
import mods.mffs.common.multitool.*;
import mods.mffs.common.options.*;
import mods.mffs.common.tileentity.TileEntityForceField;
import mods.mffs.common.tileentity.TileEntityMachines;
import mods.mffs.network.client.ForceFieldClientUpdatehandler;
import mods.mffs.network.client.NetworkHandlerClient;
import mods.mffs.network.server.ForceFieldServerUpdatehandler;
import mods.mffs.network.server.NetworkHandlerServer;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.logging.Level;

@Mod(modid = "ModularForceFieldSystem", name = "Modular ForceField System", version = "@VERSION@", dependencies = "after:ThermalExpansion")
@NetworkMod(versionBounds = "[@VERSION@]", clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerClient.class), serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerServer.class))
public class ModularForceFieldSystem {

	public static CreativeTabs MFFSTab;

	public static int MFFSRENDER_ID = 2908;

	public static Block MFFSCapacitor;
	public static Block MFFSProjector;
	public static Block MFFSDefenceStation;
	public static Block MFFSFieldblock;
	public static Block MFFSExtractor;
	public static Block MFFSMonazitOre;
	public static Block MFFSForceEnergyConverter;
	public static Block MFFSSecurtyStorage;
	public static Block MFFSSecurtyStation;
	public static Block MFFSControlSystem;

	public static Item MFFSitemForcicumCell;
	public static Item MFFSitemForcicium;
	public static Item MFFSitemForcePowerCrystal;

	public static Item MFFSitemdensifiedForcicium;
	public static Item MFFSitemdepletedForcicium;

	public static Item MFFSitemFocusmatix;
	public static Item MFFSitemSwitch;
	public static Item MFFSitemWrench;

	public static Item MFFSitemFieldTeleporter;
	public static Item MFFSitemMFDidtool;
	public static Item MFFSitemMFDdebugger;
	public static Item MFFSitemcardempty;
	public static Item MFFSitemfc;
	public static Item MFFSItemIDCard;
	public static Item MFFSAccessCard;
	public static Item MFFSItemSecLinkCard;
	public static Item MFFSitemManuelBook;
	public static Item MFFSitemInfinitePowerCard;
	public static Item MFFSitemDataLinkCard;

	public static Item MFFSitemupgradeexctractorboost;
	public static Item MFFSitemupgradecaprange;
	public static Item MFFSitemupgradecapcap;

	public static Item MFFSProjectorTypsphere;
	public static Item MFFSProjectorTypkube;
	public static Item MFFSProjectorTypwall;
	public static Item MFFSProjectorTypdeflector;
	public static Item MFFSProjectorTyptube;
	public static Item MFFSProjectorTypcontainment;
	public static Item MFFSProjectorTypAdvCube;
	public static Item MFFSProjectorTypdiagowall;

	public static Item MFFSProjectorOptionZapper;
	public static Item MFFSProjectorOptionSubwater;
	public static Item MFFSProjectorOptionDome;
	public static Item MFFSProjectorOptionCutter;
	public static Item MFFSProjectorOptionMoobEx;
	public static Item MFFSProjectorOptionDefenceStation;
	public static Item MFFSProjectorOptionForceFieldJammer;
	public static Item MFFSProjectorOptionCamouflage;
	public static Item MFFSProjectorOptionFieldFusion;

	public static Item MFFSProjectorFFDistance;
	public static Item MFFSProjectorFFStrenght;

	public static int MonazitOreworldamount = 4;

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

	@SidedProxy(clientSide = "mods.mffs.client.ClientProxy", serverSide = "mods.mffs.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance("ModularForceFieldSystem")
	public static ModularForceFieldSystem instance;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {

		initIC2Plugin();
		initBuildcraftPlugin();
		initEE3Plugin();
		initThermalExpansionPlugin();
		initComputerCraftPlugin();
		initAEPlugin();

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);

		if (ee3Found)
			MinecraftForge.EVENT_BUS.register(new EE3Event());

		TickRegistry.registerScheduledTickHandler(
				new ForceFieldClientUpdatehandler(), Side.CLIENT);
		TickRegistry.registerScheduledTickHandler(
				new ForceFieldServerUpdatehandler(), Side.SERVER);

		MFFSconfig = new Configuration(event.getSuggestedConfigurationFile());
		event.getModMetadata().version = Versioninfo.curentversion();
		try {
			MFFSconfig.load();
			MFFSTab = new MFFSCreativeTab(CreativeTabs.getNextID(), "MFFS");

			Property prop_VersionremoteUrl = MFFSconfig
					.get(Configuration.CATEGORY_GENERAL, "VersionremoteUrl",
							"https://bitbucket.org/SeargeDP/modularforcefieldsystem/downloads/versioninfo");
			prop_VersionremoteUrl.comment = "URL to MFFS VersionInfo file";
			VersionRemoteURL = prop_VersionremoteUrl.getString();

			Property chunckloader_prop = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "Chunkloader", true);
			chunckloader_prop.comment = "Set this to false to turn off the MFFS Chuncloader ability";
			enableChunkLoader = chunckloader_prop.getBoolean(true);

			Property DefSationNPCScannoti = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"DefenceStationNPCScannnotification", false);
			DefSationNPCScannoti.comment = "Set this to true to turn off the DefenceStation notification is in NPC Mode";
			DefenceStationNPCScannsuppressnotification = DefSationNPCScannoti
					.getBoolean(false);

			Property zapperParticles = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "renderZapperParticles",
					true);
			zapperParticles.comment = "Set this to false to turn off the small smoke particles present around TouchDamage enabled ForceFields.";
			showZapperParticles = zapperParticles.getBoolean(true);

			Property uumatterForciciumprop = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "uumatterForcicium", true);
			uumatterForciciumprop.comment = "Add IC2 UU-Matter Recipes for Forcicium";
			enableUUMatterForcicium = uumatterForciciumprop.getBoolean(true);

			Property monazitWorldAmount = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "MonazitOreWorldGen", 4);
			monazitWorldAmount.comment = "Controls the size of the ore node that Monazit Ore will generate in";
			MonazitOreworldamount = monazitWorldAmount.getInt(4);

			Property adminList = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"ForceFieldMaster", "nobody");
			adminList.comment = "Add users to this list to give them admin permissions split by ;";
			Admin = adminList.getString();

			Property influencedByOther = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "influencedbyothermods",
					true);
			influencedByOther.comment = "Should MFFS be influenced by other mods. e.g. ICBM's EMP";
			influencedByOtherMods = influencedByOther.getBoolean(true);

			Property ffRemoveWaterLavaOnly = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"forcefieldremoveonlywaterandlava", false);
			ffRemoveWaterLavaOnly.comment = "Should forcefields only remove water and lava when sponge is enabled?";
			forceFieldRemoveOnlyWaterAndLava = ffRemoveWaterLavaOnly
					.getBoolean(false);

			Property feTransportCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "forcefieldtransportcost",
					10000);
			feTransportCost.comment = "How much FE should it cost to transport through a field?";
			forceFieldTransportCost = feTransportCost.getInt(10000);

			Property feFieldBlockCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "forcefieldblockcost", 1);
			feFieldBlockCost.comment = "How much upkeep FE cost a default ForceFieldblock per second";
			forceFieldBlockCost = feFieldBlockCost.getInt(1);

			Property BlockCreateMod = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"forcefieldblockcreatemodifier", 10);
			BlockCreateMod.comment = "Energy need for create a ForceFieldblock (forcefieldblockcost*forcefieldblockcreatemodifier)";
			forceFieldBlockCreateModifier = BlockCreateMod.getInt(10);

			Property ffZapperMod = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"forcefieldblockzappermodifier", 2);
			ffZapperMod.comment = "Energy need multiplier used when the zapper option is installed";
			forceFieldBlockZapperModifier = ffZapperMod.getInt(2);

			Property maxFFGenPerTick = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"forcefieldmaxblockpeerTick", 5000);
			maxFFGenPerTick.comment = "How many field blocks can be generated per tick?";
			forceFieldMaxBlocksPerTick = maxFFGenPerTick.getInt(5000);

			Property fcWorkCycle = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "ForceciumWorkCylce", 250);
			fcWorkCycle.comment = "WorkCycle amount of Forcecium inside a Extractor";
			ForciciumWorkCycle = fcWorkCycle.getInt(250);

			Property graphics = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"GraphicStyle", 1);
			graphics.comment = "Graphics style to use. 0 = original (16x16), 1 = new (32x32)";
			graphicsStyle = graphics.getInt(1);

			Property fcCellWorkCycle = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "ForceciumCellWorkCylce",
					230);
			fcCellWorkCycle.comment = "WorkCycle amount of Forcecium Cell inside a Extractor";
			ForciciumCellWorkCycle = fcCellWorkCycle.getInt(230);

			Property extractorPassFEGen = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"ExtractorPassForceEnergyGenerate", 12000);
			extractorPassFEGen.comment = "How many ForceEnergy generate per WorkCycle";
			ExtractorPassForceEnergyGenerate = extractorPassFEGen.getInt(12000);

			ExtractorPassForceEnergyGenerate = (ExtractorPassForceEnergyGenerate / 4000) * 4000;

			Property defStationKillCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"DefenceStationKillForceEnergy", 10000);
			defStationKillCost.comment = "How much FE does the AreaDefenseStation when killing someone";
			DefenceStationKillForceEnergy = defStationKillCost.getInt(10000);

			Property defStationSearchCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"DefenceStationSearchForceEnergy", 1000);
			defStationSearchCost.comment = "How much FE does the AreaDefenseStation when searching someone for contraband";
			DefenceStationSearchForceEnergy = defStationSearchCost.getInt(1000);

			Property defStationScannCost = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL,
					"DefenceStationScannForceEnergy", 10);
			defStationScannCost.comment = "How much FE does the AreaDefenseStation when Scann for Targets (amount * range / tick)";
			DefenceStationScannForceEnergy = defStationScannCost.getInt(10);

			Property Adventuremap = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "adventuremap", false);
			Adventuremap.comment = "Set MFFS to AdventureMap Mode Extractor need no Forcecium and ForceField have no click damage";
			adventureMapMode = Adventuremap.getBoolean(false);

			Property ic2Recipes = MFFSconfig.get(
					Configuration.CATEGORY_GENERAL, "enableIC2Recipes", true);
			ic2Recipes.comment = "Set to false to disable IndustrialCraft 2 recipes for MFFS machines.";
			enableIC2Recipes = ic2Recipes.getBoolean(true);

			Property teRecipes = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"enableTERecipes", true);
			teRecipes.comment = "Set to false to disable Thermal Expansion recipes for MFFS machines.";
			enableTERecipes = teRecipes.getBoolean(true);

			Property grindRecipe = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"enableAEGrindStoneRecipe", true);
			grindRecipe.comment = "Set to false to disable the Applied Energistics Grind Stone recipe for MFFS " +
					"machines.";
			enableAEGrindStoneRecipe = grindRecipe.getBoolean(true);

			Property grindOutput = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"grindRecipeOutput", 8);
			grindOutput.comment = "Amount of Forcicium to generate per Monazit ore in the AE Grind Stone.";
			grindRecipeOutput = grindOutput.getInt(8);

			Property grindCost = MFFSconfig.get(Configuration.CATEGORY_GENERAL,
					"grindRecipeCost", 12);
			grindCost.comment = "Number of clicks on the Applied Energistics Grind Stone Crank that must be " +
					"registered to turn Monazit into Forcicium.";
			grindRecipeCost = grindCost.getInt(12);

			// Machines + Blocks

			MFFSForceEnergyConverter = new BlockConverter(MFFSconfig
					.getBlock("MFFSForceEnergyConverter",
							DefaultProps.block_Converter_ID).getInt(
							DefaultProps.block_Converter_ID))
					.setUnlocalizedName("MFFSForceEnergyConverter");
			MFFSExtractor = new BlockExtractor(MFFSconfig.getBlock(
					"MFFSExtractor", DefaultProps.block_Extractor_ID).getInt(
					DefaultProps.block_Extractor_ID))
					.setUnlocalizedName("MFFSExtractor");
			MFFSMonazitOre = new BlockMonazitOre(MFFSconfig.getBlock(
					"MFFSMonazitOre", DefaultProps.block_MonazitOre_ID).getInt(
					DefaultProps.block_MonazitOre_ID))
					.setUnlocalizedName("MFFSMonazitOre");
			MFFSDefenceStation = new BlockAreaDefenseStation(MFFSconfig
					.getBlock("MFFSDefenceStation",
							DefaultProps.block_DefenseStation_ID).getInt(
							DefaultProps.block_DefenseStation_ID))
					.setUnlocalizedName("MFFSDefenseStation");
			MFFSCapacitor = new BlockCapacitor(MFFSconfig.getBlock(
					"MFFSCapacitor", DefaultProps.block_Capacitor_ID).getInt(
					DefaultProps.block_Capacitor_ID))
					.setUnlocalizedName("MFFSCapacitor");
			MFFSProjector = new BlockProjector(MFFSconfig.getBlock(
					"MFFSProjector", DefaultProps.block_Projector_ID).getInt(
					DefaultProps.block_Projector_ID))
					.setUnlocalizedName("MFFSProjector");
			MFFSFieldblock = new BlockForceField(MFFSconfig.getBlock(
					"MFFSFieldblock", DefaultProps.block_Field_ID).getInt(
					DefaultProps.block_Field_ID));
			MFFSSecurtyStorage = new BlockSecurtyStorage(MFFSconfig.getBlock(
					"MFFSSecurtyStorage", DefaultProps.block_SecureStorage_ID)
					.getInt(DefaultProps.block_SecureStorage_ID))
					.setUnlocalizedName("MFFSSecureStorage");
			MFFSSecurtyStation = new BlockAdvSecurtyStation(MFFSconfig
					.getBlock("MFFSSecurtyStation",
							DefaultProps.block_SecurityStation_ID).getInt(
							DefaultProps.block_SecurityStation_ID))
					.setUnlocalizedName("MFFSSecurityStation");
			MFFSControlSystem = new BlockControlSystem(MFFSconfig.getBlock(
					"MFFSControlSystem", DefaultProps.block_ControlSystem)
					.getInt(DefaultProps.block_ControlSystem))
					.setUnlocalizedName("MFFSControlSystem");

			// Items
			MFFSProjectorFFDistance = new ItemProjectorFieldModulatorDistance(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorFFDistance",
							DefaultProps.item_AltDistance_ID).getInt(
							DefaultProps.item_AltDistance_ID))
					.setUnlocalizedName("itemProjectorFFDistance");
			MFFSProjectorFFStrenght = new ItemProjectorFieldModulatorStrength(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorFFStrength",
							DefaultProps.item_AltStrength_ID).getInt(
							DefaultProps.item_AltStrength_ID))
					.setUnlocalizedName("itemProjectorFFStrength");
			MFFSitemFocusmatix = new ItemProjectorFocusMatrix(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemPorjectorFocusmatrix",
							DefaultProps.item_FocusMatrix_ID).getInt(
							DefaultProps.item_FocusMatrix_ID))
					.setUnlocalizedName("itemProjectorFocusMatrix");
			MFFSitemForcePowerCrystal = new ItemForcePowerCrystal(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemForcePowerCrystal",
							DefaultProps.item_FPCrystal_ID).getInt(
							DefaultProps.item_FPCrystal_ID))
					.setUnlocalizedName("itemForcePowerCrystal");
			MFFSitemForcicium = new ItemForcicium(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemForcicium",
					DefaultProps.item_Forcicium_ID).getInt(
					DefaultProps.item_Forcicium_ID))
					.setUnlocalizedName("itemForcicium");
			MFFSitemForcicumCell = new ItemForcicumCell(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemForcicumCell",
					DefaultProps.item_ForciciumCell_ID).getInt(
					DefaultProps.item_ForciciumCell_ID))
					.setUnlocalizedName("itemForciciumCell");

			// Modules
			MFFSProjectorTypdiagowall = new ItemProjectorModuleDiagonalWall(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorModulediagonallyWall",
							DefaultProps.item_ModDiag_ID).getInt(
							DefaultProps.item_ModDiag_ID))
					.setUnlocalizedName("itemProjectorModuleDiagonalWall");
			MFFSProjectorTypsphere = new ItemProjectorModuleSphere(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTypsphere",
							DefaultProps.item_ModSphere_ID).getInt(
							DefaultProps.item_ModSphere_ID))
					.setUnlocalizedName("itemProjectorModuleSphere");
			MFFSProjectorTypkube = new ItemProjectorModuleCube(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTypkube",
							DefaultProps.item_ModCube_ID).getInt(
							DefaultProps.item_ModCube_ID))
					.setUnlocalizedName("itemProjectorModuleCube");
			MFFSProjectorTypwall = new ItemProjectorModuleWall(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTypwall",
							DefaultProps.item_ModWall_ID).getInt(
							DefaultProps.item_ModWall_ID))
					.setUnlocalizedName("itemProjectorModuleWall");
			MFFSProjectorTypdeflector = new ItemProjectorModuleDeflector(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTypdeflector",
							DefaultProps.item_ModDeflector_ID).getInt(
							DefaultProps.item_ModDeflector_ID))
					.setUnlocalizedName("itemProjectorModuleDeflector");
			MFFSProjectorTyptube = new ItemProjectorModuleTube(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTyptube",
							DefaultProps.item_ModTube_ID).getInt(
							DefaultProps.item_ModTube_ID))
					.setUnlocalizedName("itemProjectorModuleTube");
			MFFSProjectorTypcontainment = new ItemProjectorModuleContainment(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorModuleContainment",
							DefaultProps.item_ModContainment_ID).getInt(
							DefaultProps.item_ModContainment_ID))
					.setUnlocalizedName("itemProjectorModuleContainment");
			MFFSProjectorTypAdvCube = new ItemProjectorModuleAdvCube(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorModuleAdvCube",
							DefaultProps.item_ModAdvCube_ID).getInt(
							DefaultProps.item_ModAdvCube_ID))
					.setUnlocalizedName("itemProjectorModuleAdvCube");

			// Options
			MFFSProjectorOptionZapper = new ItemProjectorOptionTouchDamage(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemupgradeprozapper",
							DefaultProps.item_OptTouchHurt_ID).getInt(
							DefaultProps.item_OptTouchHurt_ID))
					.setUnlocalizedName("itemProjectorOptionZapper");
			MFFSProjectorOptionSubwater = new ItemProjectorOptionSponge(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemupgradeprosubwater",
							DefaultProps.item_OptSponge_ID).getInt(
							DefaultProps.item_OptSponge_ID))
					.setUnlocalizedName("itemProjectorOptionSponge");
			MFFSProjectorOptionDome = new ItemProjectorOptionFieldManipulator(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemupgradeprodome",
							DefaultProps.item_OptManipulator_ID).getInt(
							DefaultProps.item_OptManipulator_ID))
					.setUnlocalizedName("itemProjectorOptionDome");
			MFFSProjectorOptionCutter = new ItemProjectorOptionBlockBreaker(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemUpgradeprocutter",
							DefaultProps.item_OptBlockBreaker_ID).getInt(
							DefaultProps.item_OptBlockBreaker_ID))
					.setUnlocalizedName("itemProjectorOptionCutter");
			MFFSProjectorOptionDefenceStation = new ItemProjectorOptionDefenseStation(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptiondefencestation",
							DefaultProps.item_OptDefense_ID).getInt(
							DefaultProps.item_OptDefense_ID))
					.setUnlocalizedName("itemProjectorOptionDefenseStation");
			MFFSProjectorOptionMoobEx = new ItemProjectorOptionMobDefence(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptionMoobEx",
							DefaultProps.item_OptMobDefense_ID).getInt(
							DefaultProps.item_OptMobDefense_ID))
					.setUnlocalizedName("itemProjectorOptionMobKiller");
			MFFSProjectorOptionForceFieldJammer = new ItemProjectorOptionForceFieldJammer(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptionFFJammer",
							DefaultProps.item_OptJammer_ID).getInt(
							DefaultProps.item_OptJammer_ID))
					.setUnlocalizedName("itemProjectorOptionFFJammer");
			MFFSProjectorOptionCamouflage = new ItemProjectorOptionCamoflage(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptionCamoflage",
							DefaultProps.item_OptCamouflage_ID).getInt(
							DefaultProps.item_OptCamouflage_ID))
					.setUnlocalizedName("itemProjectorOptionCamouflage");
			MFFSProjectorOptionFieldFusion = new ItemProjectorOptionFieldFusion(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptionFieldFusion",
							DefaultProps.item_OptFusion_ID).getInt(
							DefaultProps.item_OptFusion_ID))
					.setUnlocalizedName("itemProjectorOptionFieldFusion");

			// Cards
			MFFSitemcardempty = new ItemCardEmpty(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemcardempty",
					DefaultProps.item_BlankCard_ID).getInt(
					DefaultProps.item_BlankCard_ID))
					.setUnlocalizedName("itemCardEmpty");
			MFFSitemfc = new ItemCardPowerLink(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemfc",
					DefaultProps.item_CardPowerLink_ID).getInt(
					DefaultProps.item_CardPowerLink_ID))
					.setUnlocalizedName("itemCardPowerLink");
			MFFSItemIDCard = new ItemCardPersonalID(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemIDCard",
					DefaultProps.item_CardPersonalID_ID).getInt(
					DefaultProps.item_CardPersonalID_ID))
					.setUnlocalizedName("itemCardID");
			MFFSItemSecLinkCard = new ItemCardSecurityLink(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemSecLinkCard",
					DefaultProps.item_CardSecurityLink_ID).getInt(
					DefaultProps.item_CardSecurityLink_ID))
					.setUnlocalizedName("itemCardSecurityLink");
			MFFSitemInfinitePowerCard = new ItemCardPower(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemInfinitePower",
					DefaultProps.item_infPowerCard_ID).getInt(
					DefaultProps.item_infPowerCard_ID))
					.setUnlocalizedName("itemCardInfinitePower");
			MFFSAccessCard = new ItemAccessCard(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemAccessCard",
					DefaultProps.item_CardAccess_ID).getInt(
					DefaultProps.item_CardAccess_ID))
					.setUnlocalizedName("itemCardAccess");
			MFFSitemDataLinkCard = new ItemCardDataLink(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemCardDataLink",
					DefaultProps.item_CardDataLink_ID).getInt(
					DefaultProps.item_CardDataLink_ID))
					.setUnlocalizedName("itemCardDataLink");

			// MultiTools
			MFFSitemWrench = new ItemWrench(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemWrench",
					DefaultProps.item_MTWrench_ID).getInt(
					DefaultProps.item_MTWrench_ID))
					.setUnlocalizedName("itemMultiToolWrench");
			MFFSitemSwitch = new ItemSwitch(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemSwitch",
					DefaultProps.item_MTSwitch_ID).getInt(
					DefaultProps.item_MTSwitch_ID))
					.setUnlocalizedName("itemMultiToolSwitch");
			MFFSitemFieldTeleporter = new ItemFieldtransporter(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM, "itemForceFieldsync",
							DefaultProps.item_MTFieldTransporter_ID).getInt(
							DefaultProps.item_MTFieldTransporter_ID))
					.setUnlocalizedName("itemMultiToolFieldTransporter");
			MFFSitemMFDidtool = new ItemPersonalIDWriter(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "ItemMFDIDwriter",
					DefaultProps.item_MTIDWriter_ID).getInt(
					DefaultProps.item_MTIDWriter_ID))
					.setUnlocalizedName("itemMultiToolIDWriter");
			MFFSitemMFDdebugger = new ItemDebugger(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemMFDdebugger",
					DefaultProps.item_MTDebugger_ID).getInt(
					DefaultProps.item_MTDebugger_ID))
					.setUnlocalizedName("itemMultiToolDebugger");
			MFFSitemManuelBook = new ItemManualBook(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemManuelBook",
					DefaultProps.item_MTManual_ID).getInt(
					DefaultProps.item_MTManual_ID))
					.setUnlocalizedName("itemMultiToolManual");

			// Upgrades
			MFFSitemupgradeexctractorboost = new ItemExtractorUpgradeBooster(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemextractorbooster",
							DefaultProps.item_upgradeBoost_ID).getInt(
							DefaultProps.item_upgradeBoost_ID))
					.setUnlocalizedName("itemExtractorUpgradeBooster");
			MFFSitemupgradecaprange = new ItemCapacitorUpgradeRange(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemupgradecaprange",
							DefaultProps.item_upgradeRange_ID).getInt(
							DefaultProps.item_upgradeRange_ID))
					.setUnlocalizedName("itemCapacitorUpgradeRange");
			MFFSitemupgradecapcap = new ItemCapacitorUpgradeCapacity(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM, "itemupgradecapcap",
							DefaultProps.item_upgradeCap_ID).getInt(
							DefaultProps.item_upgradeCap_ID))
					.setUnlocalizedName("itemCapacitorUpgradeCapacity");

		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e,
					"ModularForceFieldSystem has a problem loading its configuration!");
			System.out.println(e.getMessage());
		} finally {
			MFFSconfig.save();
		}

		VersionLocal = Versioninfo.curentversion();
		VersionRemote = Versioninfo.newestversion();
	}

	@Init
	public void load(FMLInitializationEvent evt) {

		GameRegistry.registerBlock(MFFSMonazitOre, "MFFSMonazitOre");
		GameRegistry.registerBlock(MFFSFieldblock, "MFFSFieldblock");
		GameRegistry.registerTileEntity(TileEntityForceField.class,
				"MFFSForceField");

		MFFSMaschines.initialize();
		ProjectorTyp.initialize();
		ProjectorOptions.initialize();

		NetworkRegistry.instance().registerGuiHandler(instance, proxy);

		proxy.registerTileEntitySpecialRenderer();

		GameRegistry.registerWorldGenerator(new MFFSWorldGenerator());

		LocalizationManager.loadLanguages();

		OreDictionary.registerOre("ForciciumItem",
				MFFSitemForcicium);
		OreDictionary.registerOre("MonazitOre",
				MFFSMonazitOre);
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent evt) {

		MFFSRecipes.init();

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
						.getBlockTileEntity(MaschineX, MaschineY, MaschineZ);
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

				TileEntity tileEntity = world.getBlockTileEntity(MaschineX,
						MaschineY, MaschineZ);
				if (tileEntity instanceof TileEntityMachines) {
					validTickets.add(ticket);
				}
			}
			return validTickets;
		}

	}

	public void initComputerCraftPlugin() {
		System.out.println("[ModularForcefieldSystem] Loading module for ComputerCraft");

		try {
			Class.forName("dan200.ComputerCraft");
			computercraftFound = true;
		} catch(Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: ComputerCraft not found");
		}
	}

	public void initBuildcraftPlugin() {

		System.out
				.println("[ModularForceFieldSystem] Loading module for Buildcraft");

		try {

			Class.forName("buildcraft.core.Version");
			buildcraftFound = true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: Buildcraft not found");

		}
	}

	public void initThermalExpansionPlugin() {

		System.out
				.println("[ModularForceFieldSystem] Loading module for ThermalExpansion");

		try {

			Class.forName("thermalexpansion.ThermalExpansion");
			thermalExpansionFound = true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: ThermalExpansion not found");

		}
	}

	public void initEE3Plugin() {

		System.out.println("[ModularForceFieldSystem] Loading module for EE3");

		try {

			Class.forName("com.pahimar.ee3.event.ActionRequestEvent");
			ee3Found = true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: EE3 not found");

		}
	}

	public void initIC2Plugin() {

		System.out.println("[ModularForceFieldSystem] Loading module for IC2");

		try {

			Class.forName("ic2.core.IC2");
			ic2Found = true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: IC2 not found");

		}
	}

	public void initAEPlugin() {
		System.out.println("[ModularForceFieldSystem] Loading module for Applied Energistics");

		try {
			Class.forName("appeng.common.AppEng");
			appliedEnergisticsFound = true;
		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: Applied Energistics not found");
		}
	}

}