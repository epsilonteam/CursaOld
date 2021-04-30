package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.gui.Panel;
import club.eridani.cursa.module.CursaModule;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.*;
import com.google.gson.*;

import java.io.*;
import java.util.List;
import java.util.Map;

import static club.eridani.cursa.utils.ListUtil.listOf;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigManager {

    private static final String CONFIG_PATH = "Cursa/config/";
    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();

    private final File CLIENT_FILE = new File(CONFIG_PATH + "Cursa_Client.json");
    private final File GUI_FILE = new File(CONFIG_PATH + "Cursa_GUI.json");
    private final File MODULE_FILE = new File(CONFIG_PATH + "Cursa_Module.json");

    private final List<File> configList = listOf(CLIENT_FILE, GUI_FILE, MODULE_FILE);;

    boolean shouldSave = false;

    public void shouldSave(){
        shouldSave = true;
    }

    public void onInit() {
        configList.forEach(it -> {
            Cursa.log.error("Checking config files " + it.getName());
            if (!it.exists()) {
                shouldSave();
            }
        });
        if(shouldSave) saveAll();
        loadAll();
    }

    public void saveModule() {
        try {
            if(!MODULE_FILE.exists()){
                MODULE_FILE.getParentFile().mkdirs();
                try{
                    MODULE_FILE.createNewFile();
                } catch (Exception ignored){}
            }
            JsonObject father = new JsonObject();
            for (CursaModule module : ModuleManager.getModules()) {
                JsonObject jsonModule = new JsonObject();
                jsonModule.addProperty("Enabled", module.isEnabled());
                jsonModule.addProperty("Bind", module.keyCode);
                if (!module.getSettings().isEmpty()) {
                    for (Setting<?> value : module.getSettings()) {
                        if (value instanceof BooleanSetting) {
                            jsonModule.addProperty(value.getName(), ((BooleanSetting) value).getValue());
                        }
                        if (value instanceof IntSetting) {
                            jsonModule.addProperty(value.getName(), ((IntSetting) value).getValue());
                        }
                        if (value instanceof FloatSetting) {
                            jsonModule.addProperty(value.getName(), ((FloatSetting) value).getValue());
                        }
                        if (value instanceof DoubleSetting) {
                            jsonModule.addProperty(value.getName(), ((DoubleSetting) value).getValue());
                        }
                        if (value instanceof ModeSetting) {
                            jsonModule.addProperty(value.getName(), ((ModeSetting) value).getValue());
                        }
                    }
                }
                father.add(module.name, jsonModule);
            }
            PrintWriter saveJSon = new PrintWriter(new FileWriter(MODULE_FILE));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e) {
            Cursa.log.error("Error while saving module config!");
            e.printStackTrace();
        }
    }

    public void loadModule() {
        if (MODULE_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(MODULE_FILE));
                JsonObject moduleJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : moduleJason.entrySet()) {
                    CursaModule module = ModuleManager.getModuleByName(entry.getKey());
                    if (module != null) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        boolean enabled = jsonMod.get("Enabled").getAsBoolean();
                        if (module.isEnabled() && !enabled) module.disable();
                        if (module.isDisabled() && enabled) module.enable();
                        if (!module.getSettings().isEmpty()) {
                            trySet(module, jsonMod);
                        }
                        module.keyCode = jsonMod.get("Bind").getAsInt();
                    }
                }
            } catch (IOException e) {
                Cursa.log.info("Error while loading module config");
                e.printStackTrace();
            }
        } else {
            try {
                MODULE_FILE.createNewFile();
                saveModule();
            } catch (IOException ignore) {}
        }
    }

    public void saveGUI() {
        try {
            if(!GUI_FILE.exists()){
                GUI_FILE.getParentFile().mkdirs();
                try{
                    GUI_FILE.createNewFile();
                } catch (Exception ignored){}
            }
            JsonObject father = new JsonObject();
            for (Panel panel : GUIManager.guiRenderer.panels) {
                JsonObject jsonGui = new JsonObject();
                jsonGui.addProperty("X", panel.x);
                jsonGui.addProperty("Y", panel.y);
                jsonGui.addProperty("Extended", panel.extended);
                father.add(panel.category.categoryName, jsonGui);
            }
            PrintWriter saveJSon = new PrintWriter(new FileWriter(GUI_FILE));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e) {
            Cursa.log.error("Error while saving GUI config!");
            e.printStackTrace();
        }
    }

    public void loadGUI() {
        if (GUI_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(GUI_FILE));
                JsonObject guiJson = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJson.entrySet()) {
                    Panel panel = GUIManager.guiRenderer.getPanelByName(entry.getKey());
                    if (panel != null) {
                        JsonObject jsonGui = (JsonObject) entry.getValue();
                        panel.x = jsonGui.get("X").getAsInt();
                        panel.y = jsonGui.get("Y").getAsInt();
                        panel.extended = jsonGui.get("Extended").getAsBoolean();
                    }
                }
            } catch (IOException e) {
                Cursa.log.error("Error while loading GUI config!");
                e.printStackTrace();
            }
        }
    }

    public void saveClient() {
        try {
            if(!CLIENT_FILE.exists()){
                CLIENT_FILE.getParentFile().mkdirs();
                try{
                    CLIENT_FILE.createNewFile();
                } catch (Exception ignored){}
            }

            JsonObject father = new JsonObject();

            saveClientStuff(father);
            saveFriend(father);

            PrintWriter saveJSon = new PrintWriter(new FileWriter(CLIENT_FILE));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e) {
            Cursa.log.error("Error while saving client stuff!");
            e.printStackTrace();
        }
    }

    private void loadClient() {
        if (CLIENT_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(CLIENT_FILE));
                JsonObject guiJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJason.entrySet()) {
                    if (entry.getKey().equals("Client")) {
                        JsonObject json = (JsonObject) entry.getValue();
                        trySetClient(json);
                    } else if (entry.getKey().equals("Friends")) {
                        JsonArray array = (JsonArray) entry.getValue();
                        array.forEach(it -> FriendManager.getInstance().friends.add(it.getAsString()));
                    }
                }
            } catch (IOException e) {
                Cursa.log.error("Error while loading client stuff!");
                e.printStackTrace();
            }
        } else {
            try {
                CLIENT_FILE.createNewFile();
                saveClient();
            } catch (IOException ignore) {}
        }
    }

    private void saveFriend(JsonObject father) {
        JsonArray array = new JsonArray();
        FriendManager.getInstance().friends.forEach(array::add);
        father.add("Friends", array);
    }

    private void saveClientStuff(JsonObject father) {
        JsonObject stuff = new JsonObject();
        stuff.addProperty("CommandPrefix", CommandManager.cmdPrefix);
        father.add("Client", stuff);
    }

    private void trySet(CursaModule mods, JsonObject jsonMod) {
        try {
            for (Setting<?> setting : mods.getSettings()) {
                tryValue(mods.name, setting, jsonMod);
            }
        } catch (Exception e) {
            Cursa.log.error("Cant set value for module : " + mods.name + "!");
        }
    }

    private void trySetClient(JsonObject json) {
        try {
            CommandManager.cmdPrefix = json.get("CommandPrefix").getAsString();
        } catch (Exception e) {
            Cursa.log.error("Error while setting client!");
        }
    }

    private void tryValue(String name, Setting<?> setting, JsonObject jsonMod) {
        try {
            if (setting instanceof BooleanSetting) {
                ((BooleanSetting) setting).setValue(jsonMod.get(setting.getName()).getAsBoolean());
            } else if (setting instanceof DoubleSetting) {
                ((DoubleSetting) setting).setValue(jsonMod.get(setting.getName()).getAsDouble());
            } else if (setting instanceof IntSetting) {
                ((IntSetting) setting).setValue(jsonMod.get(setting.getName()).getAsInt());
            } else if (setting instanceof FloatSetting) {
                ((FloatSetting) setting).setValue(jsonMod.get(setting.getName()).getAsFloat());
            } else if (setting instanceof ModeSetting) {
                ((ModeSetting) setting).setValue(jsonMod.get(setting.getName()).getAsString());
            }
        } catch (Exception e) {
            Cursa.log.error("Cant set value for " + name + ",loaded default!Setting name:" + setting.getName());
        }
    }

    public static void loadAll() {
        getInstance().loadClient();
        getInstance().loadGUI();
        getInstance().loadModule();
    }

    public static void saveAll() {
        getInstance().saveClient();
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
