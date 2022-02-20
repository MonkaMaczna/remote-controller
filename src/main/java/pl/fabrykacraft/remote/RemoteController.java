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

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.post;
import static spark.SparkBase.port;
import static spark.route.HttpMethod.post;


public class RemoteController extends SimplePlugin {




    @Override
    protected void onPluginStart() {

        initializeSpark();
        Common.logFramed("&aController Loaded. \n&bGo to https://remote-controller-web.vercel.app/ to remote control block!");



    }

    @Override
    protected void onPluginStop() {
        
    }

    private static final int HTTP_BAD_REQUEST = 400;

    interface Validable {
        boolean isValid();
    }

    @Data
    static class NewBlockPayload {
        private String direction;
        private String world;
        private String z;
        private String y;
        private String x;


        public boolean isValid() {
            return direction != null && world != null;
        }
    }
    public static class Model {
        private Map blocks = new HashMap<>();

        @Data
        class BlockMove {
            private String direction;
            private String world;
            private String z;
            private String y;
            private String x;


        }
        public Location moveBlock(String direction, Location location) {
            BlockMove blockMove = new BlockMove();
            blockMove.setWorld(location.getWorld().getName());
            blockMove.setDirection(direction);

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
                    locationToMove.add(0,0, 1);
                    break;
                case "right":
                    locationToMove.add(0, 0, -1);
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
                Common.log("Moved a block!");



            });
            return locationToMove;
        }

    }
    /*
    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
     */




    public static void initializeSpark() {
        Model model = new Model();

        port(25567);
        post("/post/move", (request, response) -> {
            Common.log("Got request");
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewBlockPayload creation = mapper.readValue(request.body(), NewBlockPayload.class);
                Common.log("Read values");
                if (!creation.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    Common.log("&cBad request - is not valid");
                    return "";
                }
                Location location = new Location(Bukkit.getServer().getWorld(creation.getWorld()), Double.parseDouble(creation.getX()), Double.parseDouble(creation.getY()), Double.parseDouble(creation.getZ()));
                Common.log("Got location");
                model.moveBlock(creation.getDirection(), location);
                response.status(200);
                response.type("application/json");
                String locationToReturn = location.getX() + "," + location.getY() + "," + location.getZ();
                return  locationToReturn;

            } catch (JsonParseException jpe) {
                Common.log("JsonParseException");
                Common.log("Cause: " + jpe.getCause());
                Common.log("Message: " + jpe.getMessage());
                Common.log("Stacktrace: " + Arrays.toString(jpe.getStackTrace()));
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
        });
    }
}
