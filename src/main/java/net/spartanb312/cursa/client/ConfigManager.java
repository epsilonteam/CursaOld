package net.spartanb312.cursa.client;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.gui.renderers.ClickGUIRenderer;
import net.spartanb312.cursa.gui.Panel;
import net.spartanb312.cursa.gui.renderers.HUDEditorRenderer;
import net.spartanb312.cursa.hud.HUDModule;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.module.modules.player.FakePlayer;
import net.spartanb312.cursa.setting.Setting;
import com.google.gson.*;
import net.spartanb312.cursa.setting.settings.*;
import net.spartanb312.cursa.utils.ListUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigManager {

    private static final String CONFIG_PATH = "Cursa/config/";
    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();

    private final File CLIENT_FILE = new File(CONFIG_PATH + "Cursa_Client.json");
    private final File GUI_FILE = new File(CONFIG_PATH + "Cursa_GUI.json");
    private final File MODULE_FILE = new File(CONFIG_PATH + "Cursa_Module.json");

    private final List<File> configList = ListUtil.listOf(CLIENT_FILE, GUI_FILE, MODULE_FILE);

    boolean shouldSave = false;

    public void shouldSave() {
        shouldSave = true;
    }

    public void onInit() {
        configList.forEach(it -> {
            if (!it.exists()) {
                shouldSave();
            }
        });
        if (shouldSave) saveAll();
    }

    public void saveModule() {
        try {
            if (!MODULE_FILE.exists()) {
                MODULE_FILE.getParentFile().mkdirs();
                try {
                    MODULE_FILE.createNewFile();
                } catch (Exception ignored) {
                }
            }
            JsonObject father = new JsonObject();
            for (Module module : ModuleManager.getModules()) {
                JsonObject jsonModule = new JsonObject();
                jsonModule.addProperty("Enabled", module.isEnabled());
                if (module instanceof HUDModule) {
                    HUDModule hudModule = ((HUDModule) module);
                    jsonModule.addProperty("HUD_X", hudModule.x);
                    jsonModule.addProperty("HUD_Y", hudModule.y);
                    jsonModule.addProperty("HUD_WIDTH", hudModule.width);
                    jsonModule.addProperty("HUD_HEIGHT", hudModule.height);
                }
                if (!module.getSettings().isEmpty()) {
                    for (Setting<?> setting : module.getSettings()) {
                        if (setting instanceof BooleanSetting) {
                            jsonModule.addProperty(setting.getName(), ((BooleanSetting) setting).getValue());
                        }
                        if (setting instanceof IntSetting) {
                            jsonModule.addProperty(setting.getName(), ((IntSetting) setting).getValue());
                        }
                        if (setting instanceof FloatSetting) {
                            jsonModule.addProperty(setting.getName(), ((FloatSetting) setting).getValue());
                        }
                        if (setting instanceof DoubleSetting) {
                            jsonModule.addProperty(setting.getName(), ((DoubleSetting) setting).getValue());
                        }
                        if (setting instanceof ModeSetting) {
                            jsonModule.addProperty(setting.getName(), ((ModeSetting) setting).getValue());
                        }
                        if (setting instanceof BindSetting) {
                            jsonModule.addProperty(setting.getName(), ((BindSetting) setting).getValue().getKeyCode());
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
                    Module module = ModuleManager.getModuleByName(entry.getKey());
                    if (module != null) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        boolean enabled = jsonMod.get("Enabled").getAsBoolean();
                        if (module.isEnabled() && !enabled) module.disable();
                        if (module.isDisabled() && enabled) module.enable();
                        if (!module.getSettings().isEmpty()) {
                            trySet(module, jsonMod);
                        }
                        if (module instanceof HUDModule) {
                            HUDModule hudModule = ((HUDModule) module);
                            hudModule.x = jsonMod.get("HUD_X").getAsInt();
                            hudModule.y = jsonMod.get("HUD_Y").getAsInt();
                            hudModule.width = jsonMod.get("HUD_WIDTH").getAsInt();
                            hudModule.height = jsonMod.get("HUD_HEIGHT").getAsInt();
                        }
                    }
                }
            } catch (IOException e) {
                Cursa.log.info("Error while loading module config");
                e.printStackTrace();
            }
        }
    }

    public void safeLoadModule() {
        if (MODULE_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(MODULE_FILE));
                JsonObject moduleJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : moduleJason.entrySet()) {
                    Module module = ModuleManager.getModuleByName(entry.getKey());
                    if (module != null) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        boolean enabled = jsonMod.get("Enabled").getAsBoolean();
                        if (module.isEnabled() && !enabled) module.disable();
                        if (module.isDisabled() && enabled) module.enable();
                        if (Cursa.MODULE_BUS.isRegistered(module)) {
                            synchronized (Cursa.MODULE_BUS.getModules()) {
                                if (!module.getSettings().isEmpty()) {
                                    trySet(module, jsonMod);
                                }
                            }
                        } else if (!module.getSettings().isEmpty()) {
                            trySet(module, jsonMod);
                        }
                        if (module instanceof HUDModule) {
                            HUDModule hudModule = ((HUDModule) module);
                            hudModule.x = jsonMod.get("HUD_X").getAsInt();
                            hudModule.y = jsonMod.get("HUD_Y").getAsInt();
                            hudModule.width = jsonMod.get("HUD_WIDTH").getAsInt();
                            hudModule.height = jsonMod.get("HUD_HEIGHT").getAsInt();
                        }
                    }
                }
            } catch (IOException e) {
                Cursa.log.info("Error while loading module config");
                e.printStackTrace();
            }
        }
    }

    public void saveGUI() {
        try {
            if (!GUI_FILE.exists()) {
                GUI_FILE.getParentFile().mkdirs();
                try {
                    GUI_FILE.createNewFile();
                } catch (Exception ignored) {
                }
            }
            JsonObject father = new JsonObject();
            List<Panel> panels = new ArrayList<>(ClickGUIRenderer.instance.panels);
            panels.addAll(HUDEditorRenderer.instance.panels);
            for (Panel panel : panels) {
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
                    Panel panel = ClickGUIRenderer.instance.getPanelByName(entry.getKey());
                    if (panel == null) panel = HUDEditorRenderer.instance.getPanelByName(entry.getKey());
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
            if (!CLIENT_FILE.exists()) {
                CLIENT_FILE.getParentFile().mkdirs();
                try {
                    CLIENT_FILE.createNewFile();
                } catch (Exception ignored) {
                }
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
        stuff.addProperty("ChatSuffix", Cursa.CHAT_SUFFIX);
        stuff.addProperty("FakePlayerName", FakePlayer.customName);
        father.add("Client", stuff);
    }

    private void trySet(Module mods, JsonObject jsonMod) {
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
            Cursa.CHAT_SUFFIX = json.get("ChatSuffix").getAsString();
            FakePlayer.customName = json.get("FakePlayerName").getAsString();
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
            } else if (setting instanceof BindSetting) {
                ((BindSetting) setting).getValue().setKeyCode(jsonMod.get(setting.getName()).getAsInt());
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

    public static void safeLoadAll() {
        getInstance().loadClient();
        getInstance().loadGUI();
        getInstance().safeLoadModule();
    }

    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public static void init() {
        instance = new ConfigManager();
        instance.onInit();
        loadAll();
    }
}
