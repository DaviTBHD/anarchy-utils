package com.anarchyutils.client;

import com.anarchyutils.BellSoundSettings;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class SoundConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("anarchyutils-sounds.properties");

    private SoundConfig() {}

    public static void load() {
        if (!Files.exists(FILE)) return;
        Properties values = new Properties();
        try (var reader = Files.newBufferedReader(FILE)) {
            values.load(reader);
            float wither = Float.parseFloat(values.getProperty("witherVolume", "0"));
            float bell = Float.parseFloat(values.getProperty("bellVolume", "3"));
            int range = Integer.parseInt(values.getProperty("bellRangeBlocks", "128"));
            if (!Float.isFinite(wither) || !Float.isFinite(bell)) throw new IllegalArgumentException("Invalid volume");
            SoundManager.witherVolume = Math.clamp(wither, 0f, 1f);
            SoundManager.bellVolume = Math.clamp(bell, 0f, 3f);
            BellSoundSettings.rangeBlocks = Math.clamp(range, 16, 256);
        } catch (IOException | IllegalArgumentException error) {
            LoggerFactory.getLogger("anarchyutils").warn("Nao foi possivel carregar as configuracoes de som", error);
        }
    }

    public static void save() {
        Properties values = new Properties();
        values.setProperty("witherVolume", Float.toString(SoundManager.witherVolume));
        values.setProperty("bellVolume", Float.toString(SoundManager.bellVolume));
        values.setProperty("bellRangeBlocks", Integer.toString(BellSoundSettings.rangeBlocks));
        try {
            Files.createDirectories(FILE.getParent());
            try (var writer = Files.newBufferedWriter(FILE)) {
                values.store(writer, "Anarchy Utils - sounds");
            }
        } catch (IOException error) {
            LoggerFactory.getLogger("anarchyutils").error("Nao foi possivel salvar as configuracoes de som", error);
        }
    }
}
