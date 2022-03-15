package pl.fabrykacraft.remote.web;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.EntityUtil;
import org.mineacademy.fo.jsonsimple.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Model {


    @Data
    class BlockMove {
        private String direction;
        private String world;
        private String z;
        private String y;
        private String x;

    }

    public Location moveBlock(String direction, Location location) {

        Block block = location.getBlock();
        Location locationToMove = location;
        switch (direction) {
            case "up":
                locationToMove.add(0, 1, 0);
                break;
            case "down":
                locationToMove.add(0, -1, 0);
                break;
            case "left":
                locationToMove.add(0,0, -1);
                break;
            case "right":
                locationToMove.add(0, 0, 1);
                break;
            case "forward":
                locationToMove.add(1, 0, 0);
                break;
            case "back":
                locationToMove.add(-1, 0, 0);
                break;
        }
        Common.runLater(() -> {
            Material material = block.getType();
            block.setType(Material.AIR);
            locationToMove.getBlock().setType(material);




        });
        return locationToMove;
    }
    public Location digBlock(Location location, Player player) {
        Common.runLater(() -> {
            if (player == null) {
                Block block = location.getBlock();
                if (!block.getType().isAir()) {
                    block.breakNaturally();
                }
            } else {


                Block block = location.getBlock();
                if (!block.getType().isAir()) {

                    EntityUtil.dropItem(player.getLocation(), new ItemStack(block.getType(), 1), (item) -> {
                        item.setCustomName("Dropped item");
                    });
                    block.setType(Material.AIR);
                }
            }
        });
        return location;
    }

    /**
     * Gets the map of material names around the location on X and Z axis with 3x3 size
     *
     * @param location
     * @return surroundings
     */
    public JSONObject getSurroundings(Location location) {
        Map<String, String> surroundings = new HashMap<>();

        int a = 1;
        for (int x = location.getBlockX() - 1; x < location.getBlockX() + 2; x++) {
            for (int z = location.getBlockZ() - 1; z < location.getBlockZ() + 2; z++) {
                Location locationToGetMaterial = new Location(location.getWorld(), x, location.getBlockY(), z);
                Material material = locationToGetMaterial.getBlock().getType();
                surroundings.put(Integer.toString(a), material.name());
                a++;
            }
        }
        JSONObject jsonObject = new JSONObject(surroundings);



        return jsonObject;
    }

}