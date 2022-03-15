package pl.fabrykacraft.remote.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleComponent;
import pl.fabrykacraft.remote.data.PlayerCache;
import pl.fabrykacraft.remote.settings.Settings;

public class ClickBlockEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock() != null && PlayerCache.getCache(event.getPlayer()).getIsClickingActivated()) {
            String link = "http://" + ( Bukkit.getIp() == "" ? "localhost" : Bukkit.getIp()) + ":" + Settings.PORT + "/index.html?";
            String x = "x=" + event.getClickedBlock().getLocation().getX();
            String y = "y=" + event.getClickedBlock().getLocation().getY();
            String z = "z=" + event.getClickedBlock().getLocation().getZ();
            String world = "world=" + event.getClickedBlock().getWorld().getName();
            link = link + x + "&" + y + "&" + z + "&" + world;
            SimpleComponent.of("&a&l[REMOTE] &bLink to manipulate this block is: " + link).onHover("&dClick here to open this link").onClickOpenUrl(link).send(event.getPlayer());

        }
    }
}
