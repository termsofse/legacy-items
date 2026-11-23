package net.mrlucky.legacyitems;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LegacyItemsEntrypoint implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("legacyitems");

    @Override
    public void onInitialize() {
        LOGGER.info("Legacy block items loaded, {} items forward ported", LegacyItems.count());
    }
}
