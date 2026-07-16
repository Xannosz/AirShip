package hu.xannosz.airship;

import hu.xannosz.airship.client.ShipDetectorFunction;
import hu.xannosz.airship.client.ShipDimensionEffect;
import hu.xannosz.airship.item.ModItems;
import hu.xannosz.airship.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AirShip.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AirShipModEvents {
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onClientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(ModMenus.NAVIGATION_TABLE_MENU.get(), NavigationTableScreen::new);
		MenuScreens.register(ModMenus.RUNE_MENU.get(), RuneScreen::new);
		MenuScreens.register(ModMenus.LOCAL_RUNE_MENU.get(), LocalRuneScreen::new);
		MenuScreens.register(ModMenus.RADAR_MENU.get(), RadarScreen::new);
		event.enqueueWork(() -> ItemProperties.register(ModItems.SHIP_DETECTOR.get(),
				new ResourceLocation(AirShip.MOD_ID, "state"), new ShipDetectorFunction()));
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerDimensionEffect(RegisterDimensionSpecialEffectsEvent event) {
		event.register(new ResourceLocation(AirShip.MOD_ID, "ship_dimension"), ShipDimensionEffect.INSTANCE);
	}
}
