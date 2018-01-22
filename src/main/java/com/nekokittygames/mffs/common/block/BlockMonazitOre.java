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

package com.nekokittygames.mffs.common.block;

import java.util.Objects;
import java.util.Random;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.libs.LibBlockNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class BlockMonazitOre extends Block {


	public BlockMonazitOre() {
		super(Material.ROCK);
		setHardness(3.0F);
		setResistance(5.0F);
		setSoundType(SoundType.STONE);
		this.setRegistryName(ModularForceFieldSystem.MODID, LibBlockNames.MONAZIT_ORE);
		final ResourceLocation registryName = Objects.requireNonNull(this.getRegistryName());
		this.setUnlocalizedName(registryName.toString());
		setCreativeTab(ModularForceFieldSystem.MFFSTab);
	}


	@Override
	public int quantityDropped(Random par1Random) {
		return 1;
	}
}
