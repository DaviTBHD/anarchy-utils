
package com.anarchyutils.client;

public class SoundManager {

    // Som do Wither: 0% a 100%
    public static float witherVolume = 0.0f;

    // Som do sino: 0% a 300%
    public static float bellVolume = 3.0f;

    // Distancia maxima do som do sino, em blocos, no cliente.

    public static boolean isBellSound(String soundId) {
        return com.anarchyutils.BellSoundSettings.isBellSound(soundId);
    }

    public static boolean isWitherSound(String soundId) {
        return soundId.startsWith("entity.wither.");
    }

    public static float getVolume(String soundId) {

        if (isWitherSound(soundId)) {
            return Math.max(0.0f, Math.min(witherVolume, 1.0f));
        }

        if (isBellSound(soundId)) {
            return Math.max(0.0f, Math.min(bellVolume, 3.0f));
        }

        return 1.0f;
    }
    
public static float applyVolume(String soundId, float originalVolume) {
    float multiplier = getVolume(soundId);

    return originalVolume * multiplier;
}

}
