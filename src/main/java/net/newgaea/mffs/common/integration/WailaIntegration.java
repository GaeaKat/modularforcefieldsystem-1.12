package net.newgaea.mffs.common.integration;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.newgaea.mffs.common.blocks.BlockForceField;
import net.newgaea.mffs.common.blocks.BlockGenerator;

@WailaPlugin
public class WailaIntegration implements IWailaPlugin {


    @Override
    public void register(IRegistrar iRegistrar) {
        WailaGeneratorProvider provider = new WailaGeneratorProvider();
        iRegistrar.registerComponentProvider(provider, TooltipPosition.BODY, BlockGenerator.class);
        iRegistrar.registerBlockDataProvider(provider,BlockGenerator.class);
        WailaForcefieldProvider fprovider=new WailaForcefieldProvider();
        iRegistrar.registerComponentProvider(fprovider,TooltipPosition.BODY, BlockForceField.class);
        iRegistrar.registerBlockDataProvider(fprovider,BlockForceField.class);
    }
}
