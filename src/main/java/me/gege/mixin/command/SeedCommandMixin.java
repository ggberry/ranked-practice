package me.gege.mixin.command;

import com.mojang.brigadier.CommandDispatcher;
import me.gege.util.GeneralUtil;
import me.gege.util.SeedUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.SeedCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.SeedUtil.*;

/**
 * Overrides /seed command
 * If practicing: shows Overworld, Nether and End (same as overworld) seed.
 */

@Mixin(SeedCommand.class)
public class SeedCommandMixin {
    @Inject(at = @At("HEAD"), method = "register", cancellable = true)
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated, CallbackInfo ci) {
        dispatcher.register(
                CommandManager.literal("seed")
                        .requires(serverCommandSource -> !dedicated || serverCommandSource.hasPermissionLevel(2))
                        .executes(
                                commandContext -> {
                                    if (!isPracticing) {
                                        long seed = commandContext.getSource().getWorld().getSeed();
                                        Text text = GeneralUtil.seedCopyText(seed);
                                        commandContext.getSource().sendFeedback(new TranslatableText("commands.seed.success", text), false);

                                        return (int) seed;
                                    }

                                    Text overworldSeedText = new LiteralText("Overworld Seed: ").append(GeneralUtil.seedCopyText(overworldSeed));
                                    Text netherSeedText = new LiteralText("Nether Seed: ").append(GeneralUtil.seedCopyText(netherSeed));
                                    Text endSeedText = new LiteralText("End Seed: ").append(GeneralUtil.seedCopyText(overworldSeed));

                                    commandContext.getSource().sendFeedback(overworldSeedText, false);
                                    commandContext.getSource().sendFeedback(netherSeedText, false);
                                    commandContext.getSource().sendFeedback(endSeedText, false);

                                    return (int) overworldSeed;
                                }
                        )
        );

        ci.cancel();
    }
}
