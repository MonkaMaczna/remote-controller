package pl.fabrykacraft.remote;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;
import pl.fabrykacraft.remote.settings.Settings;


import java.util.Arrays;
import java.util.List;


public class RemoteController extends SimplePlugin {




    @Override
    protected void onPluginStart() {

        Spark.initializeSpark();
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
