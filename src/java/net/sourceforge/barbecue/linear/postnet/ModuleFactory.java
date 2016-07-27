package net.sourceforge.barbecue.linear.postnet;

import net.sourceforge.barbecue.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brendon Anderson
 */
public class ModuleFactory {

    public static final PostNetModule START_STOP = new PostNetModule(new int[]{1});

    private static final List KEYS = new ArrayList();
    private static final Map SET = new HashMap();

    static {
        initBaseSet();
    }

    private static void initBaseSet() {
        KEYS.add("0");
        SET.put("0", new PostNetModule(new int[]{1, 1, 0, 0, 0}));
        KEYS.add("1");
        SET.put("1", new PostNetModule(new int[]{0, 0, 0, 1, 1}));
        KEYS.add("2");
        SET.put("2", new PostNetModule(new int[]{0, 0, 1, 0, 1}));
        KEYS.add("3");
        SET.put("3", new PostNetModule(new int[]{0, 0, 1, 1, 0}));
        KEYS.add("4");
        SET.put("4", new PostNetModule(new int[]{0, 1, 0, 0, 1}));
        KEYS.add("5");
        SET.put("5", new PostNetModule(new int[]{0, 1, 0, 1, 0}));
        KEYS.add("6");
        SET.put("6", new PostNetModule(new int[]{0, 1, 1, 0, 0}));
        KEYS.add("7");
        SET.put("7", new PostNetModule(new int[]{1, 0, 0, 0, 1}));
        KEYS.add("8");
        SET.put("8", new PostNetModule(new int[]{1, 0, 0, 1, 0}));
        KEYS.add("9");
        SET.put("9", new PostNetModule(new int[]{1, 0, 1, 0, 0}));
    }

    public static Module getModule(String key) {
        PostNetModule module = null;
        module = (PostNetModule) SET.get(key);
        module.setSymbol(key);
        return module;
    }

    public static int getIndex(String key) {
        return KEYS.indexOf(key);
    }

    public static Module getModuleForIndex(int index) {
        return getModule((String) KEYS.get(index));
    }
}
