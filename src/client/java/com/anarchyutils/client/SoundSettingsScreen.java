package com.anarchyutils.client;

import com.anarchyutils.BellSoundSettings;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.function.DoubleConsumer;

public final class SoundSettingsScreen extends Screen {
    private float previousWitherVolume = 1f;

    public SoundSettingsScreen() {
        super(Component.literal("Anarchy Utils"));
    }

    @Override
    protected void init() {
        int x = width / 2 - 150;
        int y = height / 2 - 102;
        addRenderableWidget(Button.builder(muteLabel(), button -> {
            if (SoundManager.witherVolume > 0) {
                previousWitherVolume = SoundManager.witherVolume;
                SoundManager.witherVolume = 0;
            } else {
                SoundManager.witherVolume = previousWitherVolume;
            }
            rebuildWidgets();
        }).bounds(x + 12, y + 32, 276, 20).build());
        addRenderableWidget(new SettingSlider(x + 12, y + 56, "Wither", 0, 100,
            SoundManager.witherVolume * 100, "%", v -> SoundManager.witherVolume = (float) v / 100));
        addRenderableWidget(Button.builder(bellLabel(), button -> {
            SoundManager.bellVolume = SoundManager.bellVolume > 0 ? 0 : 3;
            rebuildWidgets();
        }).bounds(x + 12, y + 103, 276, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Restaurar padroes"), button -> {
            SoundManager.witherVolume = 0;
            SoundManager.bellVolume = 3;
            BellSoundSettings.rangeBlocks = 256;
            rebuildWidgets();
        }).bounds(x + 12, y + 184, 134, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Concluido"), button -> onClose())
            .bounds(x + 154, y + 184, 134, 20).build());
    }

    private Component muteLabel() {
        return Component.literal("Silenciar Wither: " + (SoundManager.witherVolume == 0 ? "LIGADO" : "DESLIGADO"));
    }

    private Component bellLabel() {
        return Component.literal("Sino global: " + (SoundManager.bellVolume > 0 ? "LIGADO" : "DESLIGADO"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int x = width / 2 - 150;
        int y = height / 2 - 102;
        graphics.fill(x, y, x + 300, y + 216, 0xF019202D);
        graphics.fill(x, y, x + 300, y + 3, 0xFF52D6BD);
        graphics.centeredText(font, title, width / 2, y + 13, 0xFF52D6BD);
        graphics.text(font, "SINO", x + 12, y + 88, 0xFFE0C982);
        graphics.centeredText(font, "Multiplayer: alcance limitado pelo servidor.", width / 2, y + 155, 0xFFB5BFCD);
        graphics.centeredText(font, "Configuracoes salvas ao fechar.", width / 2, y + 168, 0xFFB5BFCD);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        SoundConfig.save();
        super.removed();
    }

    private final class SettingSlider extends AbstractSliderButton {
        private final String label;
        private final int min;
        private final int max;
        private final String suffix;
        private final DoubleConsumer setter;

        SettingSlider(int x, int y, String label, int min, int max, double current, String suffix, DoubleConsumer setter) {
            super(x, y, 276, 20, Component.empty(), (current - min) / (max - min));
            this.label = label;
            this.min = min;
            this.max = max;
            this.suffix = suffix;
            this.setter = setter;
            updateMessage();
        }

        private int amount() { return (int) Math.round(min + value * (max - min)); }

        @Override
        protected void updateMessage() { setMessage(Component.literal(label + ": " + amount() + suffix)); }

        @Override
        protected void applyValue() {
            setter.accept(amount());
            // Keep the mute toggle in sync without interrupting a slider drag.
            for (var child : SoundSettingsScreen.this.children()) {
                if (child instanceof Button button && button.getMessage().getString().startsWith("Silenciar Wither:")) {
                    button.setMessage(muteLabel());
                }
            }
        }
    }
}
