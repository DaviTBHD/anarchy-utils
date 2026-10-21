package com.anarchyutils;

public final class BellSoundSettings {
    public static int rangeBlocks = 256;

    private BellSoundSettings() {}

    public static boolean isBellSound(String path) {
        return path.equals("block.bell.use") || path.equals("block.bell.resonate");
    }
}
