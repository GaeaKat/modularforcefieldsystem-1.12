package com.nekokittygames.mffs.common.item;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.modules.*;
import com.nekokittygames.mffs.common.multitool.*;
import com.nekokittygames.mffs.common.options.*;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

import static com.nekokittygames.mffs.common.Functions.Null;

@GameRegistry.ObjectHolder(ModularForceFieldSystem.MODID)
public class ModItems {
    public static final ItemForcicium FORCICIUM=Null();
    public static final ItemAccessCard ACCESS_CARD=Null();
    public static final ItemCapacitorUpgradeCapacity UPGRADE_CAPACITY=Null();
    public static final ItemCapacitorUpgradeRange UPGRADE_RANGE=Null();
    public static final ItemCardDataLink DATALINK_CARD=Null();
    public static final ItemCardEmpty EMPTY_CARD=Null();
    public static final ItemCardPersonalID PERSONAL_ID=Null();
    public static final ItemCardPower POWER_CARD=Null();
    public static final ItemCardPowerLink POWERLINK_CARD=Null();
    public static final ItemCardSecurityLink SECURITYLINK_CARD=Null();
    public static final ItemExtractorUpgradeBooster EXTRACTOR_UPGRADE_BOOSTER=Null();
    public static final ItemForcePowerCrystal FORCEPOWER_CRYSTAL=Null();
    public static final ItemForcicumCell FORCICIUM_CELL=Null();
    public static final ItemProjectorFieldModulatorDistance PROJECTOR_DISTANCE=Null();
    public static final ItemProjectorFieldModulatorStrength PROJECTOR_STRENGTH=Null();
    public static final ItemProjectorFocusMatrix PROJECTOR_FOCUS_MATRIX=Null();
    public static final ItemProjectorModuleAdvCube MODULE_ADVCUBE=Null();
    public static final ItemProjectorModuleContainment MODULE_CONTAINMENT=Null();
    public static final ItemProjectorModuleCube MODULE_CUBE=Null();
    public static final ItemProjectorModuleDeflector MODULE_DEFLECTOR=Null();
    public static final ItemProjectorModuleDiagonalWall MODULE_DIAGONAL_WALL=Null();
    public static final ItemProjectorModuleSphere MODULE_SPHERE=Null();
    public static final ItemProjectorModuleTube MODULE_TUBE=Null();
    public static final ItemProjectorModuleWall MODULE_WALL=Null();
    public static final ItemDebugger MULTITOOL_DEBUGGER=Null();
    public static final ItemFieldtransporter MULTITOOL_TRANSPORTER=Null();
    public static final ItemPersonalIDWriter MULTITOOL_ID_WRITER=Null();
    public static final ItemSwitch MULTITOOL_SWITCH=Null();
    public static final ItemWrench MULTITOOL_WRENCH=Null();
    public static final ItemProjectorOptionBlockBreaker OPTION_BLOCK_BREAKER=Null();
    public static final ItemProjectorOptionCamoflage OPTION_CAMOFLAGE=Null();
    public static final ItemProjectorOptionDefenseStation OPTION_DEFENSE_STATION=Null();
    public static final ItemProjectorOptionFieldFusion OPTION_FIELD_FUSION=Null();
    public static final ItemProjectorOptionFieldManipulator OPTION_FIELD_MANIPULATOR=Null();
    public static final ItemProjectorOptionForceFieldJammer OPTION_FIELD_JAMMER=Null();
    public static final ItemProjectorOptionMobDefence OPTION_MOB_DEFENCE=Null();
    public static final ItemProjectorOptionSponge OPTION_SPONGE=Null();
    public static final ItemProjectorOptionTouchDamage OPTION_TOUCH_DAMAGE=Null();
    public static final ItemProjectorOptionLight OPTION_LIGHT=Null();

    @Mod.EventBusSubscriber(modid = ModularForceFieldSystem.MODID)
    public static class RegistrationHandler {
        public static final Set<Item> ITEMS = new HashSet<Item>();

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final Item[] items={
                    new ItemForcicium(),
                    new ItemAccessCard(),
                    new ItemCapacitorUpgradeCapacity(),
                    new ItemCapacitorUpgradeRange(),
                    new ItemCardDataLink(),
                    new ItemCardEmpty(),
                    new ItemCardPersonalID(),
                    new ItemCardPower(),
                    new ItemCardPowerLink(),
                    new ItemCardSecurityLink(),
                    new ItemExtractorUpgradeBooster(),
                    new ItemForcePowerCrystal(),
                    new ItemForcicumCell(),
                    new ItemProjectorFieldModulatorDistance(),
                    new ItemProjectorFieldModulatorStrength(),
                    new ItemProjectorFocusMatrix(),
                    new ItemProjectorModuleAdvCube(),
                    new ItemProjectorModuleContainment(),
                    new ItemProjectorModuleCube(),
                    new ItemProjectorModuleDeflector(),
                    new ItemProjectorModuleDiagonalWall(),
                    new ItemProjectorModuleSphere(),
                    new ItemProjectorModuleTube(),
                    new ItemProjectorModuleWall(),
                    new ItemDebugger(),
                    new ItemFieldtransporter(),
                    new ItemPersonalIDWriter(),
                    new ItemSwitch(),
                    new ItemWrench(),
                    new ItemProjectorOptionBlockBreaker(),
                    new ItemProjectorOptionCamoflage(),
                    new ItemProjectorOptionDefenseStation(),
                    new ItemProjectorOptionFieldFusion(),
                    new ItemProjectorOptionFieldManipulator(),
                    new ItemProjectorOptionForceFieldJammer(),
                    new ItemProjectorOptionMobDefence(),
                    new ItemProjectorOptionSponge(),
                    new ItemProjectorOptionTouchDamage(),
                    new ItemProjectorOptionLight()
            };
            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final Item item : items) {
                registry.register(item);
                ITEMS.add(item);
            }
        }
        }

    }
