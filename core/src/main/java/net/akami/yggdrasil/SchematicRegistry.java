package net.akami.yggdrasil;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.world.schematic.Schematic;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SchematicRegistry {

    private static Map<String, Schematic> registry = new HashMap<>();

    public static void register(String key, InputStream stream) throws IOException {
        DataContainer schematicData = DataFormats.NBT.readFrom(stream);
        Schematic schematic = DataTranslators.LEGACY_SCHEMATIC.translate(schematicData);
        registry.put(key, schematic);
    }

    public static Schematic get(String key) {
        return registry.get(key);
    }
}
