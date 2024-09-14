package hu.xannosz.airship.screen;

import hu.xannosz.airship.AirShip;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AirShip.MOD_ID);

	public static final RegistryObject<MenuType<NavigationTableMenu>> NAVIGATION_TABLE_MENU =
			registerMenuType(NavigationTableMenu::new, "navigation_table_menu");
	public static final RegistryObject<MenuType<RuneMenu>> RUNE_MENU =
			registerMenuType(RuneMenu::new, "rune_menu");

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
		return MENUS.register(name, () -> IForgeMenuType.create(factory));
	}
}
