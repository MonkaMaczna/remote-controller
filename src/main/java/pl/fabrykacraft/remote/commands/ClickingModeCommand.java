package pl.fabrykacraft.remote.commands;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import pl.fabrykacraft.remote.data.PlayerCache;

public class ClickingModeCommand extends SimpleCommand {
    public ClickingModeCommand(String label) {
        super(label);
        setPermission(null);
    }

    @Override
    protected void onCommand() {

        PlayerCache cache = PlayerCache.getCache(getPlayer());

        if (cache.getIsClickingActivated()) {
            // Turned off mode
            cache.setIsClickingActivated(false);

        } else {
            // Turned on mode
            cache.setIsClickingActivated(true);
        }
        Common.tell(getPlayer(), "&a&l[REMOTE] &bYou have turned " + (cache.getIsClickingActivated() ? "on. Click on a block!" : "off"));
    }
}
