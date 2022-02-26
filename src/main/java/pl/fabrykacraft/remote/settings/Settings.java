package pl.fabrykacraft.remote.settings;

import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {


    @Override
    protected int getConfigVersion() {
        return 1;
    }

    public static Integer PORT;


    private static void init() {
        PORT = getInteger("Port");
    }
}
