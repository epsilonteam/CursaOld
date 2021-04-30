package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static club.eridani.cursa.utils.ListUtil.listOf;

public class ConfigManager {

    private static final String CONFIG_PATH = "Cursa/config/";

    private final File CLIENT_FILE = new File(CONFIG_PATH + "Cursa_Client.json");
    private final File FRIEND_FILE = new File(CONFIG_PATH + "Cursa_Friend.json");
    private final File GUI_FILE = new File(CONFIG_PATH + "Cursa_GUI.json");
    private final File MODULE_FILE = new File(CONFIG_PATH + "Cursa_Module.json");

    private final List<File> configList = listOf(CLIENT_FILE, FRIEND_FILE, GUI_FILE, MODULE_FILE);

    public void onInit() {
        configList.forEach(it -> {
            if (!it.exists()) {
                if (it.getParentFile().mkdirs()) {
                    try {
                        if (it.createNewFile()) {
                            Cursa.log.error("Created config files!");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Cursa.log.error("Can't create config files!");
                    }
                } else {
                    Cursa.log.error("Can't create config dirs!");
                }
            }
        });
        loadAll();
    }

    public void saveModule() {

    }

    public void loadModule() {

    }

    public void saveFriend() {

    }

    public void loadFriend() {

    }

    public void saveGUI() {

    }

    public void loadGUI() {

    }

    public void saveClient() {

    }

    public void loadClient() {

    }

    public static void loadAll() {
        getInstance().loadClient();
        getInstance().loadFriend();
        getInstance().loadGUI();
        getInstance().loadModule();
    }

    public static void saveAll() {
        getInstance().saveClient();
        getInstance().saveFriend();
        getInstance().saveGUI();
        getInstance().saveModule();
    }

    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public static void init() {
        if (instance == null) instance = new ConfigManager();
        instance.onInit();
    }
}
