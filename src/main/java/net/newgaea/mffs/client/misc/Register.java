package net.newgaea.mffs.client.misc;

import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.items.ItemLinkCard;
import net.newgaea.mffs.common.libs.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID,value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class Register {


    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ItemModelsProperties.registerProperty(MFFSItems.LINK_CARD.get(),new ResourceLocation("mffs","link_status"),(stack,world,entity) -> {
            if(stack.getItem() instanceof ItemLinkCard) {
                ItemLinkCard item=(ItemLinkCard)stack.getItem();
                switch (item.getLinkType(stack,world,entity)) {
                    case None:
                        return 0;
                    case Link:
                        return 1;
                    case Creative:
                        return 2;
                }
            }
            return 1;
        });
    }
}
