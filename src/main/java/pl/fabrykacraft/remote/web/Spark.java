package pl.fabrykacraft.remote.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.jsonsimple.JSONObject;
import pl.fabrykacraft.remote.settings.Settings;
import spark.Filter;

import static spark.Spark.*;

import static spark.SparkBase.port;

public class Spark {

    private static final int HTTP_BAD_REQUEST = 400;



    @Data
    static class MoveBlockPayload {
        private String direction;
        private String world;
        private String z;
        private String y;
        private String x;


        public boolean isValid() {
            return direction != null && world != null && Valid.isDecimal(z) && Valid.isDecimal(y) && Valid.isDecimal(x);
        }
    }

    @Data
    static class LocationPayload {
        private String z;
        private String y;
        private String x;
        private String world;


        public boolean isValid() {
            return world != null && Valid.isDecimal(z) && Valid.isDecimal(y) && Valid.isDecimal(x);
        }
    }
    @Data
    static class LocationPayloadPlayer {
        private String z;
        private String y;
        private String x;
        private String world;
        private String playerName;


        public boolean isValid() {
            return world != null && Valid.isDecimal(z) && Valid.isDecimal(y) && Valid.isDecimal(x);
        }
    }




    public static void initializeSpark() {
        Model model = new Model();

        staticFileLocation("/public");

        port(Settings.PORT);


        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
        });

        get("/", (request, response) -> {
            response.redirect("index.html");
            return null;
        });


        // TODO Fix response with "material" and use it in index.html
        post("/post/move", (request, response) -> {

            try {
                ObjectMapper mapper = new ObjectMapper();
                MoveBlockPayload creation = mapper.readValue(request.body(), MoveBlockPayload.class);



                if (!creation.getX().contains(".")) {
                    creation.setX(creation.getX() + ".0");
                }
                if (!creation.getY().contains(".")) {
                    creation.setY(creation.getY() + ".0");
                }
                if (!creation.getZ().contains(".")) {
                    creation.setZ(creation.getZ() + ".0");
                }

                if (!creation.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }



                Location location = new Location(Bukkit.getServer().getWorld(creation.getWorld()), Double.parseDouble(creation.getX()), Double.parseDouble(creation.getY()), Double.parseDouble(creation.getZ()));
                Location movedLocation = model.moveBlock(creation.getDirection(), location);
                response.status(200);
                response.type("application/json");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("x", movedLocation.getBlockX());
                jsonObject.put("y", movedLocation.getBlockY());
                jsonObject.put("z", movedLocation.getBlockZ());
                jsonObject.put("material", movedLocation.getBlock().getType().name());
                return jsonObject.toString();

            } catch (JsonParseException jpe) {
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
        });
        post("/post/break", (request, response) -> {
              try {
                  ObjectMapper mapper = new ObjectMapper();
                  LocationPayloadPlayer creation = mapper.readValue(request.body(), LocationPayloadPlayer.class);


                  if (!creation.getX().contains(".")) {
                      creation.setX(creation.getX() + ".0");
                  }
                  if (!creation.getY().contains(".")) {
                      creation.setY(creation.getY() + ".0");
                  }
                  if (!creation.getZ().contains(".")) {
                      creation.setZ(creation.getZ() + ".0");
                  }


                  if (!creation.isValid()) {
                      response.status(HTTP_BAD_REQUEST);
                      return "";
                  }
                  Location location = new Location(Bukkit.getServer().getWorld(creation.getWorld()), Double.parseDouble(creation.getX()), Double.parseDouble(creation.getY()), Double.parseDouble(creation.getZ()));
                  model.digBlock(location, Bukkit.getPlayer(creation.getPlayerName()));
                  response.status(200);
                  response.type("application/json");
                  return true;
              } catch (JsonParseException jpe) {
                  Common.log(jpe.getMessage());
                  response.status(HTTP_BAD_REQUEST);
                  return "";
              }
        });

        get("/get/surroundings", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                LocationPayload creation = mapper.readValue(request.body(), LocationPayload.class);

                if (!creation.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }
                Location location = new Location(Bukkit.getServer().getWorld(creation.getWorld()), Double.parseDouble(creation.getX()), Double.parseDouble(creation.getY()), Double.parseDouble(creation.getZ()));

                JSONObject jsonObject = model.getSurroundings(location);
                response.status(200);
                response.type("application/json");
                return jsonObject.toString();
            } catch (JsonParseException jpe) {
                Common.log(jpe.getMessage());
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
        });

        get("/get/check", (request, response) -> {
            response.status(200);
            response.type("application/json");

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("status", "Connection acquired");
            return jsonObject.toString();
        });

        get("/get/data", (request, response) -> {
            JSONObject jsonObject = new JSONObject();
            Common.runLater(() -> {
                jsonObject.put("Worlds", Bukkit.getServer().getWorlds());
                jsonObject.put("Players", Bukkit.getServer().getOnlinePlayers());
            });
            response.status(200);
            response.type("application/json");
            return jsonObject.toString();
        });
    }

}
