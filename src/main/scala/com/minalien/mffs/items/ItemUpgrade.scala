package com.minalien.mffs.items

import com.minalien.mffs.api.EnumUpgradeType.EnumUpgradeType
import net.minecraft.item.Item

/**
 * Created by Katrina on 14/01/2015.
 */
abstract class ItemUpgrade extends Item {
    def getTypeUpgrade():EnumUpgradeType

}
