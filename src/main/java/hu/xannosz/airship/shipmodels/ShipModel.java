package hu.xannosz.airship.shipmodels;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class ShipModel {
	protected List<Pair<BlockPos, BlockState>> blocks = new ArrayList<>();

	protected BlockPos pivot = new BlockPos(0, 0, 0);
	protected Map<Character, BlockState> blockStates = new HashMap<>();

	protected void writeBlock(List<List<String>> layers) {
		writeBlock(layers, pivot.getX(), pivot.getY(), pivot.getZ());
	}

	protected void writeBlock(List<List<String>> layers, int startX, int startY, int startZ) {
		for (int i = 0; i < layers.size(); i++) {
			writeLayer(startY + i, layers.get(i), startX, startZ);
		}
	}

	protected void writeLayer(int y, List<String> lines, int startX, int startZ) {
		for (int i = 0; i < lines.size(); i++) {
			writeLine(startX, y, startZ + i, lines.get(i));
		}
	}

	protected void writeLine(int startX, int y, int z, String line) {
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) != ' ') {
				blocks.add(new Pair<>(new BlockPos(startX + i, y, z),
						blockStates.get(line.charAt(i))));
			}
		}
	}
}
