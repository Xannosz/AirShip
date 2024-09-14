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
import java.util.List;
import java.util.Random;

import static hu.xannosz.airship.util.Constants.SHIP_Y_MAX;
import static hu.xannosz.airship.util.Constants.SHIP_Y_MIN;
import static hu.xannosz.airship.util.RuneUtil.canLandOnIt;
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
	private List<Pair<String, BlockPos>> runes = new ArrayList<>();
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
				RuneRegistry.INSTANCE.registerRune(toDimensionCode(level),getBlockPos(),id);
				setChanged();
			}
			if (clock == 0) {
				clock = 5;

				runeData.setId(id);
				runeData.setEnabled(isEnabled);
				runeData.setLandEnabled(isInShipDimension(level));

				land = null;
				runes = new ArrayList<>();
				//runes.addAll(getRunesInShip());
				//runes.addAll(getRunesInOtherShip());
				runes.addAll(getRunesInRealWorld());
			}
			clock--;
		}
	}

	private List<Pair<String, BlockPos>> getRunesInShip() {
		final List<Pair<String, BlockPos>> runes = new ArrayList<>();

		if (!isInShipDimension(level)) {
			return runes;
		}

		final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);

		for (int x = shipData.getSWCore().getX() - shipData.getRadius();
			 x < shipData.getSWCore().getX() + shipData.getRadius(); x++) {
			for (int y = SHIP_Y_MIN; y < SHIP_Y_MAX; y++) {
				for (int z = shipData.getSWCore().getZ() - shipData.getRadius();
					 z < shipData.getSWCore().getZ() + shipData.getRadius(); z++) {
					if (level.getBlockEntity(new BlockPos(x, y, z)) instanceof SmallRuneBlockEntity smallRuneBlockEntity) {
						runes.add(new Pair<>(smallRuneBlockEntity.id, new BlockPos(x, y, z)));
					}
				}
			}
		}

		return runes;
	}

	private List<Pair<String, BlockPos>> getRunesInOtherShip() {
		return new ArrayList<>();
	}

	private List<Pair<String, BlockPos>> getRunesInRealWorld() {
		List<Pair<String, BlockPos>> runes = new ArrayList<>();

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
				List<BlockPos> lands = new ArrayList<>(); //TODO calculate in time
				for (int x = core.getX() - 8; x < core.getX() + 8; x++) {
					for (int y = core.getY() - 4; y < core.getY() + 4; y++) {
						for (int z = core.getZ() - 8; z < core.getZ() + 8; z++) {
							if (canLandOnIt(new BlockPos(x, y, z), world)) {
								lands.add(new BlockPos(x, y, z));
							}
						}
					}
				}
				if (lands.size() > 0) {
					land = lands.get(new Random().nextInt(lands.size()));
				}
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
