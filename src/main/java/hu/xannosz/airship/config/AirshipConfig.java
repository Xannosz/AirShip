package hu.xannosz.airship.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AirshipConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final ForgeConfigSpec.ConfigValue<Integer> CRYSTAL_ENERGY_PER_THOUSAND_BLOCK;
	public static final ForgeConfigSpec.ConfigValue<Integer> ENERGY_PER_CRYSTAL;
	public static final ForgeConfigSpec.ConfigValue<Integer> ENDER_ENGINE_SPEED;
	public static final ForgeConfigSpec.ConfigValue<Integer> ENERGY_HOLDER_SEND_ENERGY;
	public static final ForgeConfigSpec.ConfigValue<Integer> WARP_PERTURBATION;
	public static final ForgeConfigSpec.ConfigValue<Integer> GENERATION_PERTURBATION;
	public static final ForgeConfigSpec.ConfigValue<Integer> RUNE_RANGE;
	public static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_SHIP_RADIUS;
	public static final ForgeConfigSpec.ConfigValue<Integer> RADAR_SCAN_RADIUS;

	static {
		BUILDER.push("Configs for Poor man's airship mod");

		CRYSTAL_ENERGY_PER_THOUSAND_BLOCK = BUILDER.comment("Crystal energy per thousand block")
				.defineInRange("crystalEnergyPerThousandBlock", 4, 1, 20);
		ENERGY_PER_CRYSTAL = BUILDER.comment("Energy per crystal")
				.defineInRange("energyPerCrystal", 1000, 50, 6000);
		ENDER_ENGINE_SPEED = BUILDER.comment("Ender engine speed")
				.defineInRange("enderEngineSpeed", 5000, 1000, 20000);
		ENERGY_HOLDER_SEND_ENERGY = BUILDER.comment("Energy holder send energy")
				.defineInRange("energyHolderSendEnergy", 8, 1, 80);
		WARP_PERTURBATION = BUILDER.comment("Warp perturbation")
				.defineInRange("warpPerturbation", 50, 5, 500);
		GENERATION_PERTURBATION = BUILDER.comment("Generation perturbation")
				.defineInRange("generationPerturbation", 2500, 500, 6000);
		RUNE_RANGE = BUILDER.comment("Rune range")
				.defineInRange("runeRange", 300, 100, 1500);
		DEFAULT_SHIP_RADIUS = BUILDER.comment("Default ship radius")
				.defineInRange("defaultShipRadius", 500, 150, 1000);
		RADAR_SCAN_RADIUS = BUILDER.comment("Radar scan radius")
				.defineInRange("radarScanRadius", 5000, 500, 15000);

		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
