package hu.xannosz.airship;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.blockentity.ModBlockEntities;
import hu.xannosz.airship.command.AirshipCommand;
import hu.xannosz.airship.item.ModItems;
import hu.xannosz.airship.network.ModMessages;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.RuneRegistry;
import hu.xannosz.airship.screen.ModMenus;
import hu.xannosz.airship.util.TeleportHandler;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.ConfigCommand;

@Mod(AirShip.MOD_ID)
public class AirShip {

	public static final String MOD_ID = "xannosz_airship";

	public AirShip() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ModBlocks.BLOCKS.register(bus);
		ModItems.ITEMS.register(bus);
		ModBlockEntities.BLOCK_ENTITIES.register(bus);
		ModMenus.MENUS.register(bus);
		ModCreativeModeTab.CREATIVE_MODE_TABS.register(bus);

		bus.addListener(this::commonSetup);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		ModMessages.register();
	}

	@Mod.EventBusSubscriber(modid = AirShip.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeEvents {
		@SubscribeEvent
		public static void onLivingUpdate(LivingEvent.LivingTickEvent e) {
			TeleportHandler.onLivingTick(e);
		}

		@SubscribeEvent
		@SuppressWarnings("resource")
		public static void onServerStartingEvent(ServerStartingEvent event) {
			event.getServer().overworld().getDataStorage().computeIfAbsent(AirShipRegistry::load, AirShipRegistry::create, "airship_registry");
			event.getServer().overworld().getDataStorage().computeIfAbsent(RuneRegistry::load, RuneRegistry::create, "rune_registry");
		}

		@SubscribeEvent
		public static void onCommandsRegister(RegisterCommandsEvent event) {
			new AirshipCommand(event.getDispatcher());
			ConfigCommand.register(event.getDispatcher());
		}
	}
}
