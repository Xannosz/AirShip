package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.util.ShipDirection;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static hu.xannosz.airship.util.Config.*;
import static hu.xannosz.airship.util.ShipUtils.*;

@Slf4j
@Getter
public class CoreBlockEntity extends BlockEntity {

	private int speed = 0;
	private ShipDirection direction = ShipDirection.N;
	@Setter
	private String name = generateName();
	@Getter
	private int enderEnergy = 0;
	@Getter
	private int necessaryEnderEnergy = 0;
	private boolean isEnderEngineOn = false;

	public CoreBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.CORE_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		tag.putInt("core.speed", speed);
		tag.putInt("core.direction", direction.getCode());
		tag.putString("core.name", name);
		tag.putInt("core.enderEnergy", enderEnergy);
		tag.putInt("core.necessaryEnderEnergy", necessaryEnderEnergy);
		tag.putBoolean("core.isEnderEngineOn", isEnderEngineOn);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		speed = nbt.getInt("core.speed");
		direction = ShipDirection.fromCode(nbt.getInt("core.direction"));
		name = nbt.getString("core.name");
		enderEnergy = nbt.getInt("core.enderEnergy");
		necessaryEnderEnergy = nbt.getInt("core.necessaryEnderEnergy");
		isEnderEngineOn = nbt.getBoolean("core.isEnderEngineOn");
	}

	public void left() {
		direction = direction.left();
		setChanged();
	}

	public void right() {
		direction = direction.right();
		setChanged();
	}

	public void faster() {
		switch (speed) {
			case 0 -> speed = 5;
			case 5 -> speed = 10;
			case 10 -> speed = 50;
			case 50, 100 -> speed = 100;
		}
		setChanged();
	}

	public void slower() {
		switch (speed) {
			case 0, 5 -> speed = 0;
			case 10 -> speed = 5;
			case 50 -> speed = 10;
			case 100 -> speed = 50;
		}
		setChanged();
	}

	public int increaseCrystalEnergy(int energy) {
		if (energy + enderEnergy <= necessaryEnderEnergy) {
			enderEnergy += energy;
			setChanged();
			return energy;
		} else {
			int consumedEnergy = necessaryEnderEnergy - enderEnergy;
			enderEnergy = necessaryEnderEnergy;
			setChanged();
			return consumedEnergy;
		}
	}

	public void toggleEnderEngine() {
		if (isEnderEngineOn) {
			enderEnergy = 0;
			necessaryEnderEnergy = 0;
		}
		isEnderEngineOn = !isEnderEngineOn;
		setChanged();
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, CoreBlockEntity blockEntity) {
		blockEntity.tick();
	}

	@SuppressWarnings("ConstantConditions")
	private void tick() {
		if (level.isClientSide() || !isInShipDimension(level)) {
			return;
		}

		if (isEnderEngineOn) {
			necessaryEnderEnergy = speed * 5 * CRYSTAL_ENERGY_PER_THOUSAND_BLOCK;
			enderEnergy = Math.min(enderEnergy, necessaryEnderEnergy);
			if (enderEnergy == necessaryEnderEnergy && speed > 0) {
				AirShipRegistry.INSTANCE.updatePosition(direction.getX() * speed * ENDER_ENGINE_SPEED + new Random().nextInt(-speed * WARP_PERTURBATION, speed * WARP_PERTURBATION),
						direction.getZ() * speed * ENDER_ENGINE_SPEED + new Random().nextInt(-speed * WARP_PERTURBATION, speed * WARP_PERTURBATION),
						getBlockPos());
				enderEnergy = 0;
				necessaryEnderEnergy = 0;
				isEnderEngineOn = false;
			}
			setChanged();
		}

		if (speed > 0) {
			AirShipRegistry.INSTANCE.updatePosition((direction.getX() * speed) / 20,
					(direction.getZ() * speed) / 20, getBlockPos());
		}
	}

	private String generateName() {
		return generateLetters(3) + "-" + generateDigits(2);
	}
}