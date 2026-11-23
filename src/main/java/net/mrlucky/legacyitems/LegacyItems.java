package net.mrlucky.legacyitems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LegacyItems {
    public static final String MOD_ID = "legacyitems";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private record Entry(String name, String block, Map<String, String> state) {
        Entry(String name, String block) {
            this(name, block, Map.of());
        }
    }

    private static final List<Entry> ENTRIES = List.of(
            new Entry("water", "water"),
            new Entry("flowing_water", "water", Map.of("level", "1")),
            new Entry("lava", "lava"),
            new Entry("flowing_lava", "lava", Map.of("level", "1")),
            new Entry("fire", "fire"),
            new Entry("nether_portal", "nether_portal"),
            new Entry("end_portal", "end_portal"),
            new Entry("end_gateway", "end_gateway", Map.of("drag", "false")),
            new Entry("bubble_column", "bubble_column", Map.of("drag", "false")),
            new Entry("crops", "wheat", Map.of("age", "1")),
            new Entry("carrots", "carrots", Map.of("age", "1")),
            new Entry("potatoes", "potatoes", Map.of("age", "1")),
            new Entry("double_stone_slab", "stone_slab", Map.of("type", "double")),
            new Entry("double_wooden_slab", "oak_slab", Map.of("type", "double")),
            new Entry("oak_wall_sign", "oak_wall_sign", Map.of("facing", "north")),
            new Entry("piston_head", "piston_head", Map.of("type", "normal")),
            new Entry("sticky_piston_head", "piston_head", Map.of("type", "sticky")),
            new Entry("repeater_on", "repeater", Map.of("powered", "true", "delay", "1")),
            new Entry("repeater_off", "repeater", Map.of("powered", "false", "delay", "1")),
            new Entry("redstone_torch_off", "redstone_torch", Map.of("lit", "false")),
            new Entry("reeds", "sugar_cane"),
            new Entry("locked_chest", "chest")
    );

    private static final List<Entry> DIRECT = List.of(
            new Entry("bed_foot", "red_bed", Map.of("part", "foot")),
            new Entry("bed_head", "red_bed", Map.of("part", "head")),
            new Entry("oak_door_lower", "oak_door", Map.of("half", "lower", "hinge", "left")),
            new Entry("iron_door_lower", "iron_door", Map.of("half", "lower", "hinge", "left")),
            new Entry("oak_door_upper", "oak_door", Map.of("half", "upper", "hinge", "left")),
            new Entry("iron_door_upper", "iron_door", Map.of("half", "upper", "hinge", "left")),
            new Entry("sign", "oak_sign", Map.of())
    );

    private static int registered;

    public static void registerAll() {
        List<String> missing = new ArrayList<>();
        for (Entry entry : ENTRIES) {
            register(entry, missing);
        }
        for (Entry entry : DIRECT) {
            register(entry, missing);
        }
        registerAir();
        if (!missing.isEmpty()) {
            LOGGER.error("Legacy block items: no block registered for {}", missing);
        }
        LOGGER.info("Legacy block items: {} items forward ported", registered);
    }

    public static int count() {
        return registered;
    }

    private static void register(Entry entry, List<String> missing) {
        Block block = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", entry.block()));
        if (block == null) {
            missing.add(entry.block());
            return;
        }

        ResourceKey<Item> key = key(entry.name());
        Item.Properties properties = new Item.Properties().setId(key).useBlockDescriptionPrefix();
        if (!entry.state().isEmpty()) {
            properties = properties.component(DataComponents.BLOCK_STATE, new BlockItemStateProperties(entry.state()));
        }
        if (entry.name().equals("locked_chest")) {
            properties = properties.component(DataComponents.BLOCK_ENTITY_DATA, lockedChestData());
        }

        Item item = DIRECT.contains(entry)
                ? new DirectPlaceItem(block, entry.state(), properties)
                : new BlockItem(block, properties);
        Registry.register(BuiltInRegistries.ITEM, key, item);
        registered++;
    }

    private static TypedEntityData<BlockEntityType<?>> lockedChestData() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", "minecraft:chest");
        CompoundTag lock = new CompoundTag();
        lock.putString("items", "minecraft:air");
        tag.put("lock", lock);
        BlockEntityType<?> chestType = BuiltInRegistries.BLOCK_ENTITY_TYPE
                .getValue(Identifier.fromNamespaceAndPath("minecraft", "chest"));
        return TypedEntityData.of(chestType, tag);
    }

    private static void registerAir() {
        ResourceKey<Item> key = key("air");
        Registry.register(BuiltInRegistries.ITEM, key,
                new Item(new Item.Properties().setId(key).useBlockDescriptionPrefix()));
        registered++;
    }

    private static ResourceKey<Item> key(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }
}
