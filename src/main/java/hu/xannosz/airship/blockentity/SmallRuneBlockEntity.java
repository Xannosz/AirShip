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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.xannosz.airship.util.RuneUtil.searchForSafeLandPosition;
import static hu.xannosz.airship.util.RuneUtil.useRune;
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
	private BlockPos land = null;

	private String id = "";
	@Getter
	private boolean isEnabled = true;

	public SmallRuneBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.SMALL_RUNE_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		tag.putString("small_rune.id", id);
		tag.putBoolean("small_rune.enabled", isEnabled);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		id = nbt.getString("small_rune.id");
		isEnabled = nbt.getBoolean("small_rune.enabled");
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
		if (buttonId.equals(ButtonId.LAND)) {
			if (land != null && isInShipDimension(level)) {
				final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
				ServerLevel world = toLevel(shipData.getDimensionCode(), level);
				useRune((ServerLevel) level, getBlockPos(), world, land);
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
			if (clock == 0) {
				clock = 5;

				runeData.setId(id);
				runeData.setEnabled(isEnabled);
				runeData.setLandEnabled(isInShipDimension(level));

				land = null;
				runes = new ArrayList<>();
				runes.addAll(getRunesInShip());
				//runes.addAll(getRunesInOtherShip());
				runes.addAll(getRunesInRealWorld());

				int runeStart = 0;
				if (runes.size() > runeStart) {
					runeData.setRune1(runes.get(runeStart).getSecond());
				}
				if (runes.size() > runeStart + 1) {
					runeData.setRune2(runes.get(runeStart + 1).getSecond());
				}
				if (runes.size() > runeStart + 2) {
					runeData.setRune3(runes.get(runeStart + 2).getSecond());
				}
				if (runes.size() > runeStart + 3) {
					runeData.setRune4(runes.get(runeStart + 3).getSecond());
				}
				if (runes.size() > runeStart + 4) {
					runeData.setRune5(runes.get(runeStart + 4).getSecond());
				}
				if (runes.size() > runeStart + 5) {
					runeData.setRune6(runes.get(runeStart + 5).getSecond());
				}
				if (runes.size() > runeStart + 6) {
					runeData.setRune7(runes.get(runeStart + 6).getSecond());
				}
				if (runes.size() > runeStart + 7) {
					runeData.setRune8(runes.get(runeStart + 7).getSecond());
				}
				if (runes.size() > runeStart + 8) {
					runeData.setRune9(runes.get(runeStart + 8).getSecond());
				}
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
		RuneRegistry.INSTANCE.searchForRunes(
				0, shipData.getSWCore(), shipData.getRadius(), 1000).forEach(
				(blockPos, runeId) -> {
					if (!runeId.equals(id)) {
						runes.add(new Pair<>(new Pair<>(blockPos, 0), runeId));
					}
				}
		);

		return runes;
	}

	private Map<BlockPos, String> getRunesInOtherShip() {
		return new HashMap<>();
	}

	private List<Pair<Pair<BlockPos, Integer>, String>> getRunesInRealWorld() {
		final List<Pair<Pair<BlockPos, Integer>, String>> runes = new ArrayList<>();

		ServerLevel world;
		BlockPos core = null;
		if (!isInShipDimension(level)) {
			world = (ServerLevel) level;
			core = getBlockPos();
		} else {
			final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			world = toLevel(shipData.getDimensionCode(), level);
			for (int y = 400; y > 0; y--) {
				BlockPos pos = new BlockPos((int) shipData.getRWCoreX(), y, (int) shipData.getRWCoreZ());
				if (!world.getBlockState(pos).getBlock().equals(Blocks.AIR) &&
						!world.getBlockState(pos).getBlock().equals(Blocks.VOID_AIR)) {
					core = pos;
					break;
				}
			}
			if (core == null) {
				return runes;
			} else {
				land = searchForSafeLandPosition(core, world, 8, 4);//TODO calculate in time
			}
		}


/*		for (int x = core.getX() - RUNE_RANGE; x < core.getX() + RUNE_RANGE; x++) {
			for (int y = core.getY() - RUNE_RANGE; y < core.getY() + RUNE_RANGE; y++) {
				for (int z = core.getZ() - RUNE_RANGE; z < core.getZ() + RUNE_RANGE; z++) {
					if (world.getBlockEntity(new BlockPos(x, y, z)) instanceof RuneBlockEntity runeBlockEntity) {
						runes.add(new Pair<>(runeBlockEntity.id, new BlockPos(x, y, z)));
					}
				}
			}
		}*/

		return runes;
	}

	public void responseToServer(ServerPlayer player) {
		ModMessages.sendToPlayer(runeData, player);
	}
}
