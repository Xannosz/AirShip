package hu.xannosz.airship;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AirShip.MOD_ID);

	public static final RegistryObject<CreativeModeTab> AIRSHIP_TAB = CREATIVE_MODE_TABS.register("airship_tab",
			() -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.RUDDER.get()))
					.title(Component.translatable("creative_tab." + AirShip.MOD_ID + ".airship_tab"))
					.displayItems((pParameters, pOutput) -> {
						pOutput.accept(ModItems.RUNE_STONE.get());
						ModItems.BLOCK_ITEMS.forEach(
								i -> pOutput.accept(i.get())
						);
					})
					.build());
}
