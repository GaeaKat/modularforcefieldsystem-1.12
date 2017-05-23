package com.nekokittygames.mffs.common.guide;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.api.util.TextHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageItemStack;
import amerifrance.guideapi.page.PageText;
import amerifrance.guideapi.page.reciperenderer.ShapedRecipesRenderer;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.ProjectorTyp;
import com.nekokittygames.mffs.common.RecipesFactory;
import com.nekokittygames.mffs.common.modules.*;
import com.nekokittygames.mffs.common.options.ItemProjectorOptionBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;


import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.nekokittygames.mffs.common.guide.GuideUtils.addRecipes;

/**
 * Created by katsw on 24/11/2016.
 */
public class LightGuideBook {

    public static Book book;
    public static void MakeBook()
    {
        book=new Book();

        book.setTitle("MFFS Guide");
        book.setWelcomeMessage("Welcome to MFFS");
        book.setDisplayName("MFFS Guide");
        book.setColor(Color.BLUE);
        book.setRegistryName("mffsGuide");

        GameRegistry.register(book);
        GameRegistry.addShapelessRecipe(GuideAPI.getStackFromBook(book),new ItemStack(Items.BOOK),new ItemStack(ModularForceFieldSystem.MFFSitemForcicium));
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            GuideAPI.setModel(book);
    }

    public static void AddPages() {
        ArrayList<IPage> generalAboutPages = new ArrayList<IPage>();
        generalAboutPages.addAll(PageHelper.pagesForLongText(I18n.format("mffs.guide.general.about")));
        ArrayList<IPage> generalForciumPages=new ArrayList<IPage>();
        generalForciumPages.add(new PageItemStack("mffs.guide.general.forcium.1",new ItemStack(ModularForceFieldSystem.MFFSitemForcicium)));
        generalForciumPages.add(new PageFurnaceRecipe("oreMonazit"));
        EntryItemStack generalForcium=new EntryItemStack(generalForciumPages,"mffs.guide.general.forciumEntry",new ItemStack(ModularForceFieldSystem.MFFSitemForcicium));

        ArrayList<IPage> generalForcefieldsPages=new ArrayList<IPage>();
        generalForcefieldsPages.add(new PageText( "mffs.guide.general.forcefield.1"));
        generalForcefieldsPages.add(new PageText( "mffs.guide.general.forcefield.2"));

        EntryItemStack generalForcefields=new EntryItemStack(generalForcefieldsPages,"mffs.guide.general.forcefieldEntry",new ItemStack(ModularForceFieldSystem.MFFSFieldblock));

        ArrayList<IPage> generalMachinesPages=new ArrayList<IPage>();
        generalMachinesPages.add(new PageItemStack("mffs.guide.general.machines.1",new ItemStack(ModularForceFieldSystem.MFFSProjector)));
        generalMachinesPages.add(new PageText("mffs.guide.general.machines.2"));
        EntryItemStack generalMachines = new EntryItemStack(generalMachinesPages,"mffs.guide.general.machinesEntry",new ItemStack(ModularForceFieldSystem.MFFSProjector));

        EntryItemStack generalAbout=new EntryItemStack(generalAboutPages,"mffs.guide.general.aboutEntry",new ItemStack(ModularForceFieldSystem.MFFSCapacitor));

        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        entries.put(new ResourceLocation("modularforcefieldsystem", "generalAbout"), generalAbout);
        entries.put(new ResourceLocation("modularforcefieldsystem","generalForcium"),generalForcium);
        entries.put(new ResourceLocation("modularforcefieldsystem","generalForcefield"),generalForcefields);
        entries.put(new ResourceLocation("modularforcefieldsystem","generalMachines"),generalMachines);
        CategoryItemStack general=new CategoryItemStack(entries,"mffs.guide.general.category",new ItemStack(ModularForceFieldSystem.MFFSitemForcicium));
        CategoryItemStack moduleCategory = makeModules();
        CategoryItemStack optionsCategory = makeOptions();
        CategoryItemStack machinesCategory=makeMachines();


        book.addCategory(general);
        book.addCategory(machinesCategory);
        book.addCategory(moduleCategory);
        book.addCategory(optionsCategory);

    }

    private static CategoryItemStack makeMachines() {

        ArrayList<IPage> extractorPages=new ArrayList<IPage>();
        extractorPages.add(new PageItemStack("mffs.guide.machines.extractor.1",ModularForceFieldSystem.MFFSExtractor));
        extractorPages.add(new PageText("mffs.guide.machines.extractor.2"));
        addRecipes(extractorPages,ModularForceFieldSystem.MFFSExtractor);
        EntryItemStack entryExtractor=new EntryItemStack(extractorPages,"mffs.guide.machines.extractor",new ItemStack(ModularForceFieldSystem.MFFSExtractor));
        ArrayList<IPage> capacitorPages=new ArrayList<IPage>();
        capacitorPages.add(new PageItemStack("mffs.guide.machines.capacitor.1",ModularForceFieldSystem.MFFSCapacitor));
        capacitorPages.add(new PageText("mffs.guide.machines.capacitor.2"));
        capacitorPages.add(new PageText("mffs.guide.machines.capacitor.3"));
        addRecipes(capacitorPages,ModularForceFieldSystem.MFFSCapacitor);
        EntryItemStack entryCapacitor=new EntryItemStack(capacitorPages,"mffs.guide.machines.capacitor",new ItemStack(ModularForceFieldSystem.MFFSCapacitor));

        ArrayList<IPage> projectorPages=new ArrayList<IPage>();
        projectorPages.add(new PageItemStack("mffs.guide.machines.projector.1",ModularForceFieldSystem.MFFSProjector));
        projectorPages.add(new PageText("mffs.guide.machines.projector.2"));
        addRecipes(projectorPages,ModularForceFieldSystem.MFFSProjector);

        EntryItemStack entryProjector=new EntryItemStack(projectorPages,"mffs.guide.machines.projector",new ItemStack(ModularForceFieldSystem.MFFSProjector));


        Map<ResourceLocation,EntryAbstract> machines=new LinkedHashMap<ResourceLocation, EntryAbstract>();
        machines.put(new ResourceLocation("modularforcefieldsystem","machineExtractor"),entryExtractor);
        machines.put(new ResourceLocation("modularforcefieldsystem","machineCapacitor"),entryCapacitor);
        machines.put(new ResourceLocation("modularforcefieldsystem","machineProjector"),entryProjector);
        return new CategoryItemStack(machines,"mffs.guide.machines",new ItemStack(ModularForceFieldSystem.MFFSCapacitor));
    }

    private static CategoryItemStack makeOptions() {
        // Options
        ArrayList<IPage> BlockBreakerPages=new ArrayList<IPage>();
        BlockBreakerPages.add(new PageItemStack("mffs.guide.options.blockBreaker.1", ModularForceFieldSystem.MFFSProjectorOptionCutter));
        worksWith(BlockBreakerPages, (ItemProjectorOptionBase) ModularForceFieldSystem.MFFSProjectorOptionCutter);
        addRecipes(BlockBreakerPages,ModularForceFieldSystem.MFFSProjectorOptionCutter);
        EntryItemStack entryBlockBreaker=new EntryItemStack(BlockBreakerPages,"mffs.guide.options.blockBreaker",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionCutter));


        ArrayList<IPage> CamoflagePages=new ArrayList<IPage>();
        CamoflagePages.add(new PageItemStack("mffs.guide.options.camoflage.1",ModularForceFieldSystem.MFFSProjectorOptionCamouflage));
        worksWith(CamoflagePages, (ItemProjectorOptionBase) ModularForceFieldSystem.MFFSProjectorOptionCamouflage);
        addRecipes(CamoflagePages,ModularForceFieldSystem.MFFSProjectorOptionCamouflage);

        EntryItemStack entryCamoflage=new EntryItemStack(CamoflagePages,"mffs.guide.options.camoflage",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionCamouflage));

        // Defence station
        ArrayList<IPage> defenceStationPages=new ArrayList<IPage>();
        defenceStationPages.add(new PageItemStack("mffs.guide.options.defenceStation.1",ModularForceFieldSystem.MFFSProjectorOptionDefenceStation));
        worksWith(defenceStationPages, (ItemProjectorOptionBase) ModularForceFieldSystem.MFFSProjectorOptionDefenceStation);
        addRecipes(defenceStationPages,ModularForceFieldSystem.MFFSProjectorOptionDefenceStation);
        EntryItemStack entryDefenseStation=new EntryItemStack(defenceStationPages,"mffs.guide.options.defenceStation",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionDefenceStation));


        ArrayList<IPage> fieldFusionPages=new ArrayList<IPage>();
        fieldFusionPages.add(new PageItemStack("mffs.guide.options.fieldFusion.1",ModularForceFieldSystem.MFFSProjectorOptionFieldFusion));
        worksWith(fieldFusionPages, (ItemProjectorOptionBase) ModularForceFieldSystem.MFFSProjectorOptionFieldFusion);
        addRecipes(fieldFusionPages,ModularForceFieldSystem.MFFSProjectorOptionFieldFusion);
        EntryItemStack entryFieldFusion=new EntryItemStack(fieldFusionPages,"mffs.guide.options.fieldFusion",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionFieldFusion));

        ArrayList<IPage> fieldManipulatorPages=new ArrayList<IPage>();
        fieldManipulatorPages.add(new PageItemStack("mffs.guide.options.dome.1",ModularForceFieldSystem.MFFSProjectorOptionDome));
        worksWith(fieldManipulatorPages, (ItemProjectorOptionBase)ModularForceFieldSystem.MFFSProjectorOptionDome);
        addRecipes(fieldManipulatorPages,ModularForceFieldSystem.MFFSProjectorOptionDome);
        EntryItemStack entryDome=new EntryItemStack(fieldManipulatorPages,"mffs.guide.options.dome",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionDome));


        ArrayList<IPage> fieldJammerPages=new ArrayList<IPage>();
        fieldJammerPages.add(new PageItemStack("mffs.guide.options.jammer.1",ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer));
        worksWith(fieldJammerPages, (ItemProjectorOptionBase)ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer);
        addRecipes(fieldJammerPages,ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer);
        EntryItemStack entryJammer=new EntryItemStack(fieldJammerPages,"mffs.guide.options.jammer",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer));

        ArrayList<IPage> mobDefensePages=new ArrayList<IPage>();
        mobDefensePages.add(new PageItemStack("mffs.guide.options.mobDefense.1",ModularForceFieldSystem.MFFSProjectorOptionMoobEx));
        worksWith(mobDefensePages, (ItemProjectorOptionBase)ModularForceFieldSystem.MFFSProjectorOptionMoobEx);
        addRecipes(mobDefensePages,ModularForceFieldSystem.MFFSProjectorOptionMoobEx);
        EntryItemStack entryMobDefense=new EntryItemStack(mobDefensePages,"mffs.guide.options.mobDefense",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionMoobEx));

        ArrayList<IPage> spongePages=new ArrayList<IPage>();
        spongePages.add(new PageItemStack("mffs.guide.options.sponge.1",ModularForceFieldSystem.MFFSProjectorOptionSubwater));
        worksWith(spongePages, (ItemProjectorOptionBase)ModularForceFieldSystem.MFFSProjectorOptionSubwater);
        addRecipes(spongePages,ModularForceFieldSystem.MFFSProjectorOptionSubwater);
        EntryItemStack entrySponge=new EntryItemStack(spongePages,"mffs.guide.options.sponge",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionSubwater));

        ArrayList<IPage> zapPages=new ArrayList<IPage>();
        zapPages.add(new PageItemStack("mffs.guide.options.zap.1",ModularForceFieldSystem.MFFSProjectorOptionZapper));
        worksWith(zapPages, (ItemProjectorOptionBase)ModularForceFieldSystem.MFFSProjectorOptionZapper);
        addRecipes(zapPages,ModularForceFieldSystem.MFFSProjectorOptionZapper);
        EntryItemStack entryZap=new EntryItemStack(zapPages,"mffs.guide.options.zap",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionZapper));


        ArrayList<IPage> lightPages=new ArrayList<IPage>();
        lightPages.add(new PageItemStack("mffs.guide.options.light.1",ModularForceFieldSystem.MFFSProjectorOptionLight));
        worksWith(lightPages, (ItemProjectorOptionBase)ModularForceFieldSystem.MFFSProjectorOptionLight);
        addRecipes(lightPages,ModularForceFieldSystem.MFFSProjectorOptionLight);
        EntryItemStack entryLight=new EntryItemStack(lightPages,"mffs.guide.options.light",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionLight));

        Map<ResourceLocation,EntryAbstract> options=new LinkedHashMap<ResourceLocation, EntryAbstract>();
        options.put(new ResourceLocation("modularforcefieldsystem","optionBlockBreaker"),entryBlockBreaker);
        options.put(new ResourceLocation("modularforcefieldsystem","optionCamoflage"),entryCamoflage);
        options.put(new ResourceLocation("modularforcefieldsystem","optionDefenceSystem"),entryDefenseStation);
        options.put(new ResourceLocation("modularforcefieldsystem","optionFieldFusion"),entryFieldFusion);
        options.put(new ResourceLocation("modularforcefieldsystem","optionDome"),entryDome);
        options.put(new ResourceLocation("modularforcefieldsystem","optionJammer"),entryJammer);
        options.put(new ResourceLocation("modularforcefieldsystem","optionMobDefense"),entryMobDefense);
        options.put(new ResourceLocation("modularforcefieldsystem","optionSponge"),entrySponge);
        options.put(new ResourceLocation("modularforcefieldsystem","optionZapper"),entryZap);
        options.put(new ResourceLocation("modularforcefieldsystem","optionLight"),entryLight);

        return new CategoryItemStack(options,"mffs.guide.options",new ItemStack(ModularForceFieldSystem.MFFSProjectorOptionCutter));

    }

    private static void worksWith(ArrayList<IPage> blockBreakerPages, ItemProjectorOptionBase mffsProjectorOption) {
        ArrayList<String> info=new ArrayList<String>();
        if (ItemProjectorModuleWall.supportsOption(mffsProjectorOption))
            info.add(TextHelper.localizeEffect("mffs.guide.modules.wall"));
        if (ItemProjectorModuleWall.supportsOption(mffsProjectorOption))
            info.add(TextHelper.localizeEffect("mffs.guide.modules.diagWall"));
        if (ItemProjectorModuleDeflector.supportsOption(mffsProjectorOption))
            info.add(TextHelper.localizeEffect("mffs.guide.modules.deflector"));
        if (ItemProjectorModuleTube.supportsOption(mffsProjectorOption))
            info.add(TextHelper.localizeEffect("mffs.guide.modules.tube"));
        if (ItemProjectorModuleSphere.supportsOption(mffsProjectorOption))
            info.add(TextHelper.localizeEffect("mffs.guide.modules.sphere"));
        if (ItemProjectorModuleCube.supportsOption(mffsProjectorOption))
            info.add(TextHelper.localizeEffect("mffs.guide.modules.cube"));
        if (ItemProjectorModuleAdvCube.supportsOption(mffsProjectorOption))
            info.add(TextHelper.localizeEffect("mffs.guide.modules.advCube"));
        if (ItemProjectorModuleContainment.supportsOption(mffsProjectorOption))
            info.add(TextHelper.localizeEffect("mffs.guide.modules.containment"));

        String str=TextHelper.localizeEffect("mffs.guide.options.worksWith")+"\n";
        for(String temp:info)
        {
            str+= "* "+temp+"\n";
        }
        blockBreakerPages.add(new PageText(str));

    }




    private static CategoryItemStack makeModules() {
        // Modules :
        //Advanced Cube
        ArrayList<IPage> AdvCubePages=new ArrayList<IPage>();
        AdvCubePages.add(new PageItemStack("mffs.guide.modules.advCube.1", ModularForceFieldSystem.MFFSProjectorTypAdvCube));
        AdvCubePages.add(new PageText("mffs.guide.modules.advCube.2"));
        AdvCubePages.add(new PageText("mffs.guide.modules.advCube.3"));
        AdvCubePages.add(new PageIRecipe(RecipesFactory.GetRecipe(ModularForceFieldSystem.MFFSProjectorTypAdvCube,0)));
        EntryItemStack entryAdvCube=new EntryItemStack(AdvCubePages,"mffs.guide.modules.advCube",new ItemStack(ModularForceFieldSystem.MFFSProjectorTypAdvCube));


        //Containment
        ArrayList<IPage> ContainmentPages=new ArrayList<IPage>();
        ContainmentPages.add(new PageItemStack("mffs.guide.modules.containment.1",ModularForceFieldSystem.MFFSProjectorTypcontainment));
        ContainmentPages.add(new PageText("mffs.guide.modules.containment.2"));
        ContainmentPages.add(new PageText("mffs.guide.modules.containment.3"));
        ContainmentPages.add(new PageText("mffs.guide.modules.containment.4"));
        ContainmentPages.add(new PageIRecipe(RecipesFactory.GetRecipe(ModularForceFieldSystem.MFFSProjectorTypcontainment,0)));
        EntryItemStack entryContainment=new EntryItemStack(ContainmentPages,"mffs.guide.modules.containment",new ItemStack(ModularForceFieldSystem.MFFSProjectorTypcontainment));

        //Cube
        ArrayList<IPage> CubePages=new ArrayList<IPage>();
        CubePages.add(new PageItemStack("mffs.guide.modules.cube.1",ModularForceFieldSystem.MFFSProjectorTypCube));
        CubePages.add(new PageIRecipe(RecipesFactory.GetRecipe(ModularForceFieldSystem.MFFSProjectorTypCube,0)));
        EntryItemStack entryCube=new EntryItemStack(CubePages,"mffs.guide.modules.cube",new ItemStack(ModularForceFieldSystem.MFFSProjectorTypCube));

        //Defelctor
        ArrayList<IPage> DeflectorPages=new ArrayList<IPage>();
        DeflectorPages.add(new PageItemStack("mffs.guide.modules.deflector.1",ModularForceFieldSystem.MFFSProjectorTypdeflector));
        DeflectorPages.add(new PageText("mffs.guide.modules.deflector.2"));
        DeflectorPages.add(new PageText("mffs.guide.modules.deflector.3"));
        DeflectorPages.add(new PageIRecipe(RecipesFactory.GetRecipe(ModularForceFieldSystem.MFFSProjectorTypdeflector,0)));

        EntryItemStack entryDeflector=new EntryItemStack(DeflectorPages,"mffs.guide.modules.deflector",new ItemStack(ModularForceFieldSystem.MFFSProjectorTypdeflector));


        // Wall
        ArrayList<IPage> WallPages=new ArrayList<IPage>();
        WallPages.add(new PageItemStack("mffs.guide.modules.wall.1",ModularForceFieldSystem.MFFSProjectorTypwall));
        WallPages.add(new PageText("mffs.guide.modules.wall.2"));
        WallPages.add(new PageText("mffs.guide.modules.wall.3"));
        WallPages.add(new PageIRecipe(RecipesFactory.GetRecipe(ModularForceFieldSystem.MFFSProjectorTypwall,0)));
        EntryItemStack entryWall=new EntryItemStack(WallPages,"mffs.guide.modules.wall",new ItemStack(ModularForceFieldSystem.MFFSProjectorTypwall));

        // Diagonal Wall
        ArrayList<IPage> DiagWallPages=new ArrayList<IPage>();
        DiagWallPages.add(new PageItemStack("mffs.guide.modules.diagWall.1",ModularForceFieldSystem.MFFSProjectorTypdiagowall));
        DiagWallPages.add(new PageText("mffs.guide.modules.diagWall.2"));
        DiagWallPages.add(new PageText("mffs.guide.modules.diagWall.3"));
        DiagWallPages.add(new PageIRecipe(RecipesFactory.GetRecipe(ModularForceFieldSystem.MFFSProjectorTypdiagowall,0)));
        EntryItemStack entryDiagWall=new EntryItemStack(DiagWallPages,"mffs.guide.modules.diagWall",new ItemStack(ModularForceFieldSystem.MFFSProjectorTypdiagowall));

        // Sphere
        ArrayList<IPage> SpherePages=new ArrayList<IPage>();
        SpherePages.add(new PageItemStack("mffs.guide.modules.sphere.1",ModularForceFieldSystem.MFFSProjectorTypsphere));
        SpherePages.add(new PageText("mffs.guide.modules.sphere.2"));
        SpherePages.add(new PageIRecipe(RecipesFactory.GetRecipe(ModularForceFieldSystem.MFFSProjectorTypsphere,0)));
        EntryItemStack entrySphere=new EntryItemStack(SpherePages,"mffs.guide.modules.sphere",new ItemStack(ModularForceFieldSystem.MFFSProjectorTypsphere));


        // Tube
        ArrayList<IPage> TubePages=new ArrayList<IPage>();
        TubePages.add(new PageItemStack("mffs.guide.modules.tube.1",ModularForceFieldSystem.MFFSProjectorTyptube));
        TubePages.add(new PageText("mffs.guide.modules.tube.2"));
        TubePages.add(new PageIRecipe(RecipesFactory.GetRecipe(ModularForceFieldSystem.MFFSProjectorTyptube,0)));
        EntryItemStack entryTube=new EntryItemStack(TubePages,"mffs.guide.modules.tube",new ItemStack(ModularForceFieldSystem.MFFSProjectorTyptube));

        Map<ResourceLocation,EntryAbstract> modules=new LinkedHashMap<ResourceLocation, EntryAbstract>();

        modules.put(new ResourceLocation("modularforcefieldsystem","moduleCube"),entryCube);
        modules.put(new ResourceLocation("modularforcefieldsystem","moduleAdvCube"),entryAdvCube);
        modules.put(new ResourceLocation("modularforcefieldsystem","moduleContainment"),entryContainment);
        modules.put(new ResourceLocation("modularforcefieldsystem","moduleDeflector"),entryDeflector);
        modules.put(new ResourceLocation("modularforcefieldsystem","moduleWall"),entryWall);
        modules.put(new ResourceLocation("modularforcefieldsystem","moduleDiagWall"),entryDiagWall);
        modules.put(new ResourceLocation("modularforcefieldsystem","moduleSphere"),entrySphere);
        modules.put(new ResourceLocation("modularforcefieldsystem","moduleTube"),entryTube);
        return new CategoryItemStack(modules,"mffs.guide.modules",new ItemStack(ModularForceFieldSystem.MFFSProjectorTypAdvCube));
    }


}
