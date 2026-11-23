package net.mrlucky.legacyitems;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import net.minecraft.util.datafix.fixes.ItemRenameFix;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LegacyForwardPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyItems.MOD_ID);

    private static final Map<Integer, String> NUMERIC = Map.ofEntries(
            Map.entry(0, "legacyitems:air"),
            Map.entry(26, "legacyitems:bed_foot"),
            Map.entry(34, "legacyitems:piston_head"),
            Map.entry(59, "legacyitems:crops"),
            Map.entry(60, "minecraft:farmland"),
            Map.entry(63, "legacyitems:sign"),
            Map.entry(64, "legacyitems:oak_door_lower"),
            Map.entry(68, "legacyitems:oak_wall_sign"),
            Map.entry(71, "legacyitems:iron_door_lower"),
            Map.entry(75, "legacyitems:redstone_torch_off"),
            Map.entry(83, "legacyitems:reeds"),
            Map.entry(93, "legacyitems:repeater_off"),
            Map.entry(94, "legacyitems:repeater_on"),
            Map.entry(95, "legacyitems:locked_chest"),
            Map.entry(209, "legacyitems:end_gateway")
    );

    private static final Map<String, String> RENAMES = Map.ofEntries(
            Map.entry("minecraft:water", "legacyitems:water"),
            Map.entry("minecraft:flowing_water", "legacyitems:flowing_water"),
            Map.entry("minecraft:lava", "legacyitems:lava"),
            Map.entry("minecraft:flowing_lava", "legacyitems:flowing_lava"),
            Map.entry("minecraft:fire", "legacyitems:fire"),
            Map.entry("minecraft:portal", "legacyitems:nether_portal"),
            Map.entry("minecraft:nether_portal", "legacyitems:nether_portal"),
            Map.entry("minecraft:end_portal", "legacyitems:end_portal"),
            Map.entry("minecraft:end_gateway", "legacyitems:end_gateway"),
            Map.entry("minecraft:bubble_column", "legacyitems:bubble_column"),
            Map.entry("minecraft:cocoa", "legacyitems:cocoa"),
            Map.entry("minecraft:carrots", "legacyitems:carrots"),
            Map.entry("minecraft:potatoes", "legacyitems:potatoes"),
            Map.entry("minecraft:crops", "legacyitems:crops"),
            Map.entry("minecraft:piston_head", "legacyitems:piston_head"),
            Map.entry("minecraft:double_stone_slab", "legacyitems:double_stone_slab"),
            Map.entry("minecraft:double_wooden_slab", "legacyitems:double_wooden_slab"),
            Map.entry("minecraft:sign", "legacyitems:sign"),
            Map.entry("minecraft:standing_sign", "legacyitems:sign"),
            Map.entry("minecraft:wall_sign", "legacyitems:oak_wall_sign"),
            Map.entry("minecraft:reeds", "legacyitems:reeds"),
            Map.entry("minecraft:locked_chest", "legacyitems:locked_chest"),
            Map.entry("minecraft:unpowered_repeater", "legacyitems:repeater_off"),
            Map.entry("minecraft:powered_repeater", "legacyitems:repeater_on"),
            Map.entry("minecraft:unlit_redstone_torch", "legacyitems:redstone_torch_off"),
            Map.entry("minecraft:mob_spawner", "minecraft:spawner"),
            Map.entry("minecraft:lit_furnace", "minecraft:furnace"),
            Map.entry("minecraft:melon_block", "minecraft:melon"),
            Map.entry("minecraft:speckled_melon", "minecraft:glistering_melon_slice")
    );

    private LegacyForwardPort() {
    }

    public static String numericName(int id) {
        return NUMERIC.get(id);
    }

    public static String rename(String id) {
        String namespaced = id.indexOf(':') < 0 ? "minecraft:" + id : id;
        return RENAMES.getOrDefault(namespaced, id);
    }

    public static void install(DataFixerBuilder builder) {
        Schema schema = builder.addSchema(1450, 1, NamespacedSchema::new);
        builder.addFixer(ItemRenameFix.create(schema, "Legacy block items forward port", LegacyForwardPort::rename));
        LOGGER.info("Legacy block items: name fix installed at schema 1450, {} numeric ids and {} names forward ported",
                NUMERIC.size(), RENAMES.size());
    }
}
