package hu.xannosz.airship.blockentity;

import com.mojang.datafixers.util.Pair;
import hu.xannosz.airship.network.GetSmallRuneData;
import hu.xannosz.airship.network.ModMessages;
import hu.xannosz.airship.network.SmallRuneData;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.RuneRegistry;
import hu.xannosz.airship.registries.ShipData;
import hu.xannosz.airship.screen.RuneMenu;
import hu.xannosz.airship.util.ButtonId;
import hu.xannosz.airship.util.ButtonUser;
import hu.xannosz.airship.util.Config;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static hu.xannosz.airship.util.Constants.REAL_RADIUS;
import static hu.xannosz.airship.util.RuneUtil.*;
import static hu.xannosz.airship.util.ShipUtils.*;

@Slf4j
public class SmallRuneBlockEntity extends BlockEntity implements MenuProvider, ButtonUser {

	//client side too!!!
	@Getter
	@Setter
	private SmallRuneData runeData = new SmallRuneData(getBlockPos());
	//CLIENT ONLY
	@Setter
	private boolean isOpened = false;
	private int clock = 0;
	private List<Pair<Pair<BlockPos, Integer>, String>> runes = new ArrayList<>();

	private String id = "";
	@Getter
	private boolean isEnabled = true;
	private int runeStart = 0;
	private Pair<BlockPos, Integer> near;

	public SmallRuneBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.SMALL_RUNE_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		tag.putString("small_rune.id", id);
		tag.putBoolean("small_rune.enabled", isEnabled);
		tag.putInt("small_rune.runeStart", runeStart);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		id = nbt.getString("small_rune.id");
		isEnabled = nbt.getBoolean("small_rune.enabled");
		runeStart = nbt.getInt("small_rune.runeStart");
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.literal("Rune");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
		return new RuneMenu(containerId, inventory, this);
	}

	@Override
	public void executeButtonClick(ButtonId buttonId) {
		if (buttonId.equals(ButtonId.TOGGLE_RUNE_ENABLED)) {
			isEnabled = !isEnabled;
			setChanged();
		}
		if (buttonId.equals(ButtonId.PREVIOUS)) {
			runeStart -= 9;
			if (runeStart < 0) {
				runeStart = 0;
			}
			setChanged();
		}
		if (buttonId.equals(ButtonId.NEXT)) {
			runeStart += 9;
			if (runeStart > runes.size()) {
				runeStart -= 9;
				if (runeStart < 0) {
					runeStart = 0;
				}
			}
			setChanged();
		}
		if (buttonId.equals(ButtonId.LAND)) {
			if (isInShipDimension(level)) {
				BlockPos core = null;
				final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
				final ServerLevel world = toLevel(shipData.getDimensionCode(), level);
				for (int y = 400; y > 0; y--) {
					BlockPos pos = new BlockPos((int) shipData.getRWCoreX(), y, (int) shipData.getRWCoreZ());
					if (!world.getBlockState(pos).getBlock().equals(Blocks.AIR) &&
							!world.getBlockState(pos).getBlock().equals(Blocks.VOID_AIR)) {
						core = pos;
						break;
					}
				}
				if (core != null) {
					BlockPos land = searchForSafeLandPosition(core, world, 8, 4);
					if (land != null && isInShipDimension(level)) {
						useRune((ServerLevel) level, getBlockPos(), world, land);
					}
				}
			}
		}
		if (buttonId.equals(ButtonId.NEAR)) {
			if (near != null) {
				useRune((ServerLevel) level, getBlockPos(), toLevel(near.getSecond(), level), near.getFirst());
			}
		}
	}

	public void jumpToRune(String id) {
		runes.forEach((data) -> {
			if (data.getSecond().equals(id)) {
				ServerLevel world = toLevel(data.getFirst().getSecond(), level);
				useRune((ServerLevel) level, getBlockPos(), world, data.getFirst().getFirst());
			}
		});
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, SmallRuneBlockEntity blockEntity) {
		blockEntity.tick();
	}

	@SuppressWarnings("ConstantConditions")
	private void tick() {
		if (level.isClientSide()) {
			if (isOpened) {
				isOpened = false;
				if (clock == 0) {
					clock = 5;
					ModMessages.sendToServer(new GetSmallRuneData(getBlockPos()));
				}
				clock--;
			}
		} else {
			if (id.isEmpty()) {
				id = generateAlphaNumerical(10);
				RuneRegistry.INSTANCE.registerRune(toDimensionCode(level), getBlockPos(), id);
				setChanged();
			}
			if (runeStart > runes.size()) {
				runeStart -= 9;
				if (runeStart < 0) {
					runeStart = 0;
				}
			}
			if (clock == 0) {
				clock = 5;

				runeData.setId(id);
				runeData.setEnabled(isEnabled);
				runeData.setLandEnabled(isInShipDimension(level));

				runes = new ArrayList<>();
				near = null;
				runes.addAll(getRunesInShip());
				runes.addAll(getRunesInOtherShip());
				runes.addAll(getRunesInRealWorld());
			}

			if (runes.size() > runeStart) {
				runeData.setRune1(runes.get(runeStart).getSecond());
			} else {
				runeData.setRune1("");
			}
			if (runes.size() > runeStart + 1) {
				runeData.setRune2(runes.get(runeStart + 1).getSecond());
			} else {
				runeData.setRune2("");
			}
			if (runes.size() > runeStart + 2) {
				runeData.setRune3(runes.get(runeStart + 2).getSecond());
			} else {
				runeData.setRune3("");
			}
			if (runes.size() > runeStart + 3) {
				runeData.setRune4(runes.get(runeStart + 3).getSecond());
			} else {
				runeData.setRune4("");
			}
			if (runes.size() > runeStart + 4) {
				runeData.setRune5(runes.get(runeStart + 4).getSecond());
			} else {
				runeData.setRune5("");
			}
			if (runes.size() > runeStart + 5) {
				runeData.setRune6(runes.get(runeStart + 5).getSecond());
			} else {
				runeData.setRune6("");
			}
			if (runes.size() > runeStart + 6) {
				runeData.setRune7(runes.get(runeStart + 6).getSecond());
			} else {
				runeData.setRune7("");
			}
			if (runes.size() > runeStart + 7) {
				runeData.setRune8(runes.get(runeStart + 7).getSecond());
			} else {
				runeData.setRune8("");
			}
			if (runes.size() > runeStart + 8) {
				runeData.setRune9(runes.get(runeStart + 8).getSecond());
			} else {
				runeData.setRune9("");
			}

			clock--;
		}
	}

	private List<Pair<Pair<BlockPos, Integer>, String>> getRunesInShip() {
		final List<Pair<Pair<BlockPos, Integer>, String>> runes = new ArrayList<>();

		if (!isInShipDimension(level)) {
			return runes;
		}

		final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
		filterRunes(RuneRegistry.INSTANCE.searchForRunes(
				0, shipData.getSWCore(), shipData.getRadius(), 1000), toLevel(0, level))
				.forEach(
						(blockPos, runeId) -> {
							if (!runeId.equals(id)) {
								runes.add(new Pair<>(new Pair<>(blockPos, 0), runeId));
							}
						}
				);

		return runes;
	}

	private List<Pair<Pair<BlockPos, Integer>, String>> getRunesInOtherShip() {
		final List<Pair<Pair<BlockPos, Integer>, String>> runes = new ArrayList<>();

		int dist = 0;
		BlockPos core;
		if (isInShipDimension(level)) {
			final ShipData selfShipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			core = new BlockPos((int) selfShipData.getRWCoreX(), 100, (int) selfShipData.getRWCoreZ());
		} else {
			core = getBlockPos();
		}

		for (ShipData shipData : getNearShips()) {
			for (Map.Entry<BlockPos, String> entry : filterRunes(RuneRegistry.INSTANCE.searchForRunes(
					0, shipData.getSWCore(), shipData.getRadius(), 1000), toLevel(0, level)).entrySet()) {
				BlockPos blockPos = entry.getKey();
				if (near == null || core.distManhattan(new BlockPos((int) shipData.getRWCoreX(), 100, (int) shipData.getRWCoreZ())) < dist) {
					dist = core.distManhattan(new BlockPos((int) shipData.getRWCoreX(), 100, (int) shipData.getRWCoreZ()));
					near = new Pair<>(blockPos, 0);
				}
				String runeId = entry.getValue();
				runes.add(new Pair<>(new Pair<>(blockPos, 0), runeId));
			}
		}

		return runes;
	}

	private List<Pair<Pair<BlockPos, Integer>, String>> getRunesInRealWorld() {
		final List<Pair<Pair<BlockPos, Integer>, String>> runes = new ArrayList<>();

		boolean searchForNear;

		BlockPos core;
		int world;
		if (isInShipDimension(level)) {
			final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			world = shipData.getDimensionCode();
			core = new BlockPos((int) shipData.getRWCoreX(), 200, (int) shipData.getRWCoreZ());
			searchForNear = near == null;
		} else {
			world = toDimensionCode(level);
			core = getBlockPos();
			searchForNear = false;
		}

		int dist = 0;

		for (Map.Entry<BlockPos, String> entry : filterRunes(RuneRegistry.INSTANCE.searchForRunes(
				world, core, Config.RUNE_RANGE, 1000), toLevel(world, level)).entrySet()) {
			BlockPos blockPos = entry.getKey();
			String runeId = entry.getValue();
			if ((near == null || core.distManhattan(blockPos) < dist) && searchForNear) {
				dist = core.distManhattan(blockPos);
				near = new Pair<>(blockPos, world);
			}
			if (!runeId.equals(id)) {
				runes.add(new Pair<>(new Pair<>(blockPos, world), runeId));
			}
		}

		return runes;
	}

	private List<ShipData> getNearShips() {
		if (isInShipDimension(level)) {
			final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			List<ShipData> result = AirShipRegistry.INSTANCE.getShipsInRadius(
					new BlockPos((int) shipData.getRWCoreX(), 100, (int) shipData.getRWCoreZ()),
					shipData.getDimensionCode(), REAL_RADIUS);
			result.removeIf(s -> s.getSWCore().equals(shipData.getSWCore()));
			return result;
		} else {
			return AirShipRegistry.INSTANCE.getShipsInRadius(getBlockPos(), toDimensionCode(level), REAL_RADIUS);
		}
	}

	public void responseToServer(ServerPlayer player) {
		ModMessages.sendToPlayer(runeData, player);
	}
}
