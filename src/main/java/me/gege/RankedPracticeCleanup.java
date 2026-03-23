package me.gege;

import me.gege.updater.AutoUpdater;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class RankedPracticeCleanup implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        AutoUpdater.deleteOld();
    }
}
