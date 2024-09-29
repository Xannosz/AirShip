package hu.xannosz.airship.util;

import hu.xannosz.airship.blockentity.SmallRuneBlockEntity;
import hu.xannosz.airship.dimension.DimensionTransporter;
import hu.xannosz.airship.network.ModMessages;
import hu.xannosz.airship.network.PlaySoundPacket;
import hu.xannosz.airship.registries.RuneRegistry;
import it.unimi.dsi.fastutil.longs.LongSet;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static hu.xannosz.airship.util.ShipUtils.toDimensionCode;

@Slf4j
@UtilityClass
public class RuneUtil {
	public static boolean canLandOnIt(BlockPos pos, ServerLevel level) {
		return !level.getBlockState(pos.below()).getBlock().equals(Blocks.AIR) &&
				level.getBlockState(pos).getBlock().equals(Blocks.AIR) &&
				level.getBlockState(pos.above()).getBlock().equals(Blocks.AIR);
	}

	public static BlockPos searchForSafeLandPosition(BlockPos pos, ServerLevel level, int radius, int yRadius) {
		List<BlockPos> lands = new ArrayList<>();
		for (int x = pos.getX() - radius; x < pos.getX() + radius; x++) {
			for (int y = pos.getY() - yRadius; y < pos.getY() + yRadius; y++) {
				for (int z = pos.getZ() - radius; z < pos.getZ() + radius; z++) {
					if (canLandOnIt(new BlockPos(x - 1, y, z - 1), level) &&
							canLandOnIt(new BlockPos(x - 1, y, z), level) &&
							canLandOnIt(new BlockPos(x - 1, y, z + 1), level) &&
							canLandOnIt(new BlockPos(x, y, z - 1), level) &&
							canLandOnIt(new BlockPos(x, y, z), level) &&
							canLandOnIt(new BlockPos(x, y, z + 1), level) &&
							canLandOnIt(new BlockPos(x + 1, y, z - 1), level) &&
							canLandOnIt(new BlockPos(x + 1, y, z), level) &&
							canLandOnIt(new BlockPos(x + 1, y, z + 1), level)) {
						lands.add(new BlockPos(x, y, z));
					}
				}
			}
		}
		if (lands.size() > 0) {
			return lands.get(new Random().nextInt(lands.size()));
		}
		return null;
	}

	public static Map<BlockPos, String> filterRunes(Map<BlockPos, String> runes, ServerLevel level) {
		Map<BlockPos, String> result = new HashMap<>();
		for (Map.Entry<BlockPos, String> rune : runes.entrySet()) {
			if (level.getBlockEntity(rune.getKey()) instanceof SmallRuneBlockEntity smallRuneBlockEntity) {
				if (smallRuneBlockEntity.isEnabled()) {
					result.put(rune.getKey(), rune.getValue());
				}
			} else {
				RuneRegistry.INSTANCE.deleteRune(toDimensionCode(level), rune.getKey());
			}
		}
		return result;
	}

	public static void useRune(ServerLevel sourceLevel, BlockPos source, ServerLevel targetLevel, BlockPos target) {
		try {
			final AbsoluteRectangleData sourceData = new AbsoluteRectangleData();
			sourceData.setNorthWestCorner(new BlockPos(source.getX() - 1, source.getY() - 1, source.getZ() - 1));
			sourceData.setSouthEastCorner(new BlockPos(source.getX() + 2, source.getY() + 2, source.getZ() + 2));
			sourceData.setStructureSize(new Vec3i(5, 5, 5));

			final AbsoluteRectangleData targetData = new AbsoluteRectangleData();
			targetData.setNorthWestCorner(new BlockPos(target.getX() - 1, target.getY() - 1, target.getZ() - 1));
			targetData.setSouthEastCorner(new BlockPos(target.getX() + 2, target.getY() + 2, target.getZ() + 2));
			targetData.setStructureSize(new Vec3i(5, 5, 5));

			final Vec3 sourceAdditional = new Vec3(target.getX() - source.getX(), target.getY() - source.getY(), target.getZ() - source.getZ());
			final Vec3 targetAdditional = new Vec3(source.getX() - target.getX(), source.getY() - target.getY(), source.getZ() - target.getZ());

			//save entities
			final Map<Entity, Vec3> sourceEntities = getEntities(Collections.singletonList(sourceData), sourceAdditional, sourceLevel);
			final Map<Entity, Vec3> targetEntities = getEntities(Collections.singletonList(targetData), targetAdditional, targetLevel);
			//save players
			final Map<ServerPlayer, Vec3> sourcePlayers = getPlayers(Collections.singletonList(sourceData), sourceAdditional, sourceLevel);
			final Map<ServerPlayer, Vec3> targetPlayers = getPlayers(Collections.singletonList(targetData), targetAdditional, targetLevel);
			//get chunks
			final Map<LevelChunk, Boolean> sourceChunks = getChunks(Collections.singletonList(sourceData), sourceAdditional, sourceLevel);
			final Map<LevelChunk, Boolean> targetChunks = getChunks(Collections.singletonList(targetData), targetAdditional, targetLevel);

			//sound effect
			playSoundEffect(source, sourcePlayers, sourceLevel.getPlayers((serverPlayer -> true)));
			playSoundEffect(target, targetPlayers, targetLevel.getPlayers((serverPlayer -> true)));
			//effects on player
			addEffectsToPlayers(sourcePlayers);
			addEffectsToPlayers(targetPlayers);

			//force load chunks
			forceLoadChunks(sourceChunks, sourceLevel);
			forceLoadChunks(targetChunks, targetLevel);

			//update chunks (reload structure)
			updateChunks(sourceChunks);
			updateChunks(targetChunks);
			//teleport entities
			teleportEntities(sourceEntities, sourceLevel, targetLevel);
			teleportEntities(targetEntities, targetLevel, sourceLevel);
			//teleport players
			teleportPlayers(sourcePlayers, sourceLevel, targetLevel);
			teleportPlayers(targetPlayers, targetLevel, sourceLevel);
			//update chunks (reload entities)
			updateChunks(sourceChunks);
			updateChunks(targetChunks);

			//reset chunks force load
			resetChunkForceLoad(sourceChunks, sourceLevel);
			resetChunkForceLoad(targetChunks, targetLevel);

			//save chunks
			saveChunks(sourceChunks);
			saveChunks(targetChunks);
		} catch (Exception ex) {
			log.error("Exception during rune usage", ex);
		}
	}

	public static Map<Entity, Vec3> getEntities(List<AbsoluteRectangleData> rectangles, Vec3 additional, ServerLevel level) {

		final Map<Entity, Vec3> result = new HashMap<>();

		level.getEntities().getAll().forEach(
				entity -> {
					Vec3 position = entity.getPosition(0.1f);
					if (isOnTheShip(rectangles, position)) {
						result.put(entity, position.add(additional));
					}
				}
		);

		return result;
	}

	public static Map<ServerPlayer, Vec3> getPlayers(List<AbsoluteRectangleData> rectangles, Vec3 additional, ServerLevel level) {
		final Map<ServerPlayer, Vec3> result = new HashMap<>();

		level.getPlayers(
				player -> {
					Vec3 position = player.getPosition(0.1f);
					return isOnTheShip(rectangles, position);
				}
		).forEach(player -> {
			Vec3 position = player.getPosition(0.1f);
			result.put(player, position.add(additional));
		});

		return result;
	}

	private static boolean isOnTheShip(List<AbsoluteRectangleData> rectangles, Vec3 position) {
		for (AbsoluteRectangleData rectangleData : rectangles) {
			boolean inBox;
			inBox = rectangleData.getNorthWestCorner().getX() <= position.x + 0.5f;
			inBox &= rectangleData.getSouthEastCorner().getX() >= position.x - 0.5f;
			inBox &= rectangleData.getNorthWestCorner().getY() <= position.y + 0.5f;
			inBox &= rectangleData.getSouthEastCorner().getY() >= position.y - 0.5f;
			inBox &= rectangleData.getNorthWestCorner().getZ() <= position.z + 0.5f;
			inBox &= rectangleData.getSouthEastCorner().getZ() >= position.z - 0.5f;

			if (inBox) {
				return true;
			}
		}
		return false;
	}

	private static Map<LevelChunk, Boolean> getChunks(List<AbsoluteRectangleData> rectangles, Vec3 additional, ServerLevel level) {
		final Map<LevelChunk, Boolean> chunks = new HashMap<>();

		final LongSet forcedChunks = level.getForcedChunks();

		for (AbsoluteRectangleData rectangleData : rectangles) {
			for (int x = 0; x < rectangleData.getStructureSize().getX(); x++) {
				for (int z = 0; z < rectangleData.getStructureSize().getZ(); z++) {
					LevelChunk sourceChunk = level.getChunkAt(
							new BlockPos(rectangleData.getNorthWestCorner().getX() + x,
									rectangleData.getNorthWestCorner().getY(),
									rectangleData.getNorthWestCorner().getZ() + z));
					chunks.put(sourceChunk, forcedChunks.contains(sourceChunk.getPos().toLong()));

					LevelChunk targetChunk = level.getChunkAt(
							new BlockPos((int) (rectangleData.getNorthWestCorner().getX() + x + additional.x),
									(int) (rectangleData.getNorthWestCorner().getY() + additional.y),
									(int) (rectangleData.getNorthWestCorner().getZ() + z + additional.z)));
					chunks.put(targetChunk, forcedChunks.contains(targetChunk.getPos().toLong()));
				}
			}
		}

		return chunks;
	}

	private static void playSoundEffect(BlockPos pivotPointPosition, Map<ServerPlayer, Vec3> players, List<ServerPlayer> serverPlayers) {
		for (ServerPlayer player : serverPlayers) {
			ModMessages.sendToPlayer(new PlaySoundPacket(pivotPointPosition, players.containsKey(player)), player);
		}
	}

	private static void addEffectsToPlayers(Map<ServerPlayer, Vec3> players) {
		players.keySet().forEach(player -> { // 20 -> 1 second
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 16));
			player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 8));
		});
	}

	private static void forceLoadChunks(Map<LevelChunk, Boolean> chunks, ServerLevel level) {
		for (LevelChunk chunk : chunks.keySet()) {
			level.setChunkForced(chunk.getPos().x, chunk.getPos().z, true);
		}
	}

	private static void updateChunks(Map<LevelChunk, Boolean> chunks) {
		for (LevelChunk chunk : chunks.keySet()) {
			chunk.runPostLoad();
		}
	}

	private static void teleportEntities(Map<Entity, Vec3> entities, ServerLevel from, ServerLevel to) {

		entities.forEach(
				(entity, target) -> {
					if (!from.equals(to)) {
						entity.changeDimension(to, new DimensionTransporter(new BlockPos((int) target.x, (int) target.y, (int) target.z)));
					} else {
						entity.teleportTo(target.x, target.y, target.z);
					}
				}
		);
	}

	private static void teleportPlayers(Map<ServerPlayer, Vec3> players, ServerLevel from, ServerLevel to) {
		players.forEach(
				(player, target) -> {
					if (!from.equals(to)) {
						player.changeDimension(to, new DimensionTransporter(new BlockPos((int) target.x, (int) target.y, (int) target.z)));
					} else {
						player.teleportTo(target.x, target.y, target.z);
					}
				}
		);
	}

	private static void resetChunkForceLoad(Map<LevelChunk, Boolean> chunks, ServerLevel level) {
		chunks.forEach((chunk, isForced) -> level.setChunkForced(chunk.getPos().x, chunk.getPos().z, isForced));
	}

	private static void saveChunks(Map<LevelChunk, Boolean> chunks) {
		chunks.forEach((chunk, isForced) -> chunk.setUnsaved(true));
	}
}
