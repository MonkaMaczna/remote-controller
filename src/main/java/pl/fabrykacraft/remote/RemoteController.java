package pl.fabrykacraft.remote;


import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;
import pl.fabrykacraft.remote.commands.ClickingModeCommand;
import pl.fabrykacraft.remote.events.ClickBlockEvent;
import pl.fabrykacraft.remote.settings.Settings;
import pl.fabrykacraft.remote.web.Spark;


import java.util.Arrays;
import java.util.List;


public class RemoteController extends SimplePlugin {




    @Override
    protected void onPluginStart() {

        Spark.initializeSpark();
        registerEvents(new ClickBlockEvent());
        registerCommand(new ClickingModeCommand("clicking"));
        Common.logFramed("&aController Loaded. \n&bGo to http://" + Bukkit.getIp() + ":" + Settings.PORT + "/ to remote control block!");




    }

    @Override
    protected void onPluginStop() {
        spark.Spark.stop();
    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Settings.class);
    }
}
