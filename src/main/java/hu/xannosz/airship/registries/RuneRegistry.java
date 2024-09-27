package hu.xannosz.airship.registries;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.xannosz.airship.util.CopyUtil.copy;

public class RuneRegistry extends SavedData {
	public static RuneRegistry INSTANCE = null;

	private final List<RuneData> smallRunes = new ArrayList<>();

	public void registerRune(int dimension, BlockPos position, String id) {
		smallRunes.add(new RuneData(dimension, position, id));
		setDirty();
	}

	public void deleteRune(int dimension, BlockPos position) {
		smallRunes.removeIf(runeData -> runeData.dimension() == dimension && runeData.position().equals(position));
		setDirty();
	}

	public Map<BlockPos, String> searchForRunes(int dimension, BlockPos position, int radius, int yRadius) {
		Map<BlockPos, String> result = new HashMap<>();

		for (RuneData runeData : smallRunes) {
			if (dimension == runeData.dimension()) {
				if (runeData.position().getX() > position.getX() - radius && runeData.position().getX() < position.getX() + radius) {
					if (runeData.position().getZ() > position.getZ() - radius && runeData.position().getZ() < position.getZ() + radius) {
						if (runeData.position().getY() > position.getY() - yRadius && runeData.position().getY() < position.getY() + yRadius) {
							result.put(copy(runeData.position()), runeData.id());
						}
					}
				}
			}
		}

		return result;
	}

	@Override
	public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
		ListTag runeList = new ListTag();
		for (RuneData rune : smallRunes) {
			CompoundTag itemTag = new CompoundTag();
			itemTag.putInt("dimensionCode", rune.dimension());
			itemTag.putInt("runePos.x", rune.position().getX());
			itemTag.putInt("runePos.y", rune.position().getY());
			itemTag.putInt("runePos.z", rune.position().getZ());
			itemTag.putString("id", rune.id());
			runeList.add(itemTag);
		}
		tag.put("smallRunes", runeList);
		return tag;
	}

	public static RuneRegistry create() {
		if (INSTANCE == null) {
			INSTANCE = new RuneRegistry();
		}
		return INSTANCE;
	}

	@SuppressWarnings("ConstantConditions")
	public static RuneRegistry load(CompoundTag tag) {
		RuneRegistry registry = create();
		ListTag runeList = (ListTag) tag.get("smallRunes");
		for (int i = 0; i < runeList.size(); i++) {
			CompoundTag sTag = runeList.getCompound(i);
			RuneData runeData = new RuneData(sTag.getInt("dimensionCode"),
					new BlockPos(sTag.getInt("runePos.x"),
							sTag.getInt("runePos.y"),
							sTag.getInt("runePos.z")), sTag.getString("id"));
			registry.smallRunes.add(runeData);
		}
		return registry;
	}
}
