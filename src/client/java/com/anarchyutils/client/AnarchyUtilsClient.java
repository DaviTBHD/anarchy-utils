
package com.anarchyutils.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class AnarchyUtilsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        SoundConfig.load();
        KeyMapping menuKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.anarchyutils.sound_menu", GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("anarchyutils", "sounds"))
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.consumeClick()) {
                if (client.gui.screen() == null) client.gui.setScreen(new SoundSettingsScreen());
            }
        });
    }
}
