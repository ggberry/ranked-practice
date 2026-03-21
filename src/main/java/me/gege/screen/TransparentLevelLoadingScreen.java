package me.gege.screen;

import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.util.math.MatrixStack;

public class TransparentLevelLoadingScreen extends LevelLoadingScreen {
    public TransparentLevelLoadingScreen() {
        super(new WorldGenerationProgressTracker(11));
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
    }
}
