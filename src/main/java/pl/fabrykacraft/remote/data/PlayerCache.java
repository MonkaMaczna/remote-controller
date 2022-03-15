package pl.fabrykacraft.remote.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerCache {

    // Instance of cache
    private static PlayerCache instance;

    // Hashmap of the cashe with UUID key
    private static final Map<UUID, PlayerCache> cacheMap = new HashMap<>();

    @Getter @Setter
    Boolean isClickingActivated;



    private PlayerCache(final String uuid) {

        // Sets the default values if not set
        setIsClickingActivated(false);
    }



    public static PlayerCache getCache(final Player player) {
        return getCache(player.getUniqueId());
    }


    public static PlayerCache getCache(final UUID uuid) {
        PlayerCache cache = cacheMap.get(uuid);

        if (cache == null) {
            cache = new PlayerCache(uuid.toString());

            cacheMap.put(uuid, cache);
        }

        return cache;
    }
}