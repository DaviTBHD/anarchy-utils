
package com.anarchyutils.client;

import net.fabricmc.api.ClientModInitializer;

public class AnarchyUtilsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        System.out.println("Anarchy Utils iniciado!");

        boolean witherMute = true;
        float bellVolume = 3.0f;

        System.out.println("Wither Mute: " + witherMute);
        System.out.println("Bell Volume: " + bellVolume);
    }
}
