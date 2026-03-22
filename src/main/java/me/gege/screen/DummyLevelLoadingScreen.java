package me.gege.screen;

import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.util.math.MatrixStack;

public class DummyLevelLoadingScreen extends LevelLoadingScreen {
    public DummyLevelLoadingScreen() {
        super(new WorldGenerationProgressTracker(11));
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        this.renderBackgroundTexture(0);
    }
}
