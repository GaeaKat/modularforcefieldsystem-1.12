package com.minalien.mffs.world

import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.init.Blocks
import com.minalien.mffs.blocks.BlockMonazitOre
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.World
import java.util.Random

/**
 * Responsible for generating Monazit Ore within the world.
 */
object MonazitOreWorldGenerator extends IWorldGenerator {
	override def generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider,
	                      chunkProvider: IChunkProvider) {
		val shiftedChunkX = chunkX << 4
		val shiftedChunkZ = chunkZ << 4

		val monazitOreGenerator = new WorldGenMinable(BlockMonazitOre, 0, 4, Blocks.stone)

		for(vein <- 1 to 6) {
			val x = shiftedChunkX + random.nextInt(16)
			val y = 14 + random.nextInt(50)
			val z = shiftedChunkZ + random.nextInt(16)

			monazitOreGenerator.generate(world, random, x, y, z)
		}
	}
}
