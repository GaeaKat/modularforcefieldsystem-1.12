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

import com.nekokittygames.mffs.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class MFFSWorldGenerator implements IWorldGenerator {


	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		chunkX = chunkX << 4;
		chunkZ = chunkZ << 4;

		WorldGenMinable worldGenMinable = new WorldGenMinable(
				ModBlocks.MONAZIT_ORE.getDefaultState(), ModularForceFieldSystem.MonazitOreworldamount + 1);

		for (int i = 0; i < ModularForceFieldSystem.MonazitOreworldamount + 1; i++) {
			int x = chunkX + random.nextInt(16);
			int y = random.nextInt(80) + 0;
			int z = chunkZ + random.nextInt(16);

			int randAmount = random
					.nextInt(ModularForceFieldSystem.MonazitOreworldamount + 1);

			worldGenMinable.generate(world, random, new BlockPos(x, y, z));
		}
	}
}
