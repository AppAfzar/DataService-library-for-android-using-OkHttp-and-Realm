package appafzar.dataservice.web.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import appafzar.dataservice.helper.Convert;
import appafzar.dataservice.helper.Log;
import appafzar.dataservice.helper.Tools;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 *
 * @param <T>
 */
public class PreferenceModel<T> {

    //region Property
    public static int MODE_UPDATE = 1;
    public static int MODE_SYNC = 2;
    private SharedPreferences sharedPreferences;
    private Class<T> typeParameterClass;
    private String keyPrefix;

    public PreferenceModel(Context context, Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
        keyPrefix = typeParameterClass.getSimpleName().toLowerCase() + "_";
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.i("PreferenceModel run!");
    }
    //endregion

    //region Data Manipulation

    public static boolean writeBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        Log.d(key + " = " + value + " Write in shared preference.");
        editor.apply();
        return true;
    }

    public static boolean readBoolean(Context context, String key) {
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        return sf.getBoolean(key, false);
    }

    /**
     * Read all field of entity from shared preferences.
     *
     * @return Filled entity.
     */
    public T read() {
        try {
            T t = typeParameterClass.newInstance();
            Log.i("prefer", "PreferenceModel: start reading " + typeParameterClass.getSimpleName() + " data.");
            Map<String, ?> allEntries = sharedPreferences.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                try {
                    if (!entry.getKey().startsWith(keyPrefix)) continue;
                    Field field = t.getClass().getDeclaredField(removeKeyPrefix(entry.getKey()));
                    field.setAccessible(true);
                    Class<?> fieldClass = field.getType();
                    if (Boolean.class.equals(fieldClass) || Boolean.TYPE.equals(fieldClass)) {
                        field.set(t, entry.getValue() == "true");
                    } else {
                        field.set(t, fieldClass.cast(entry.getValue()));
                    }
                    Log.i("prefer", entry.getKey() + " = " + entry.getValue() + " read from shared preference.");
                } catch (Exception ex) {
                    Log.e(String.valueOf(ex));
                }
            }
            return t;
        } catch (Exception ex) {
            Log.e(String.valueOf(ex));
            return null;
        }
    }

    /**
     * for reading int type data
     *
     * @param key key to read data
     * @return saved value for key
     */
    public int readInt(String key) {
        return sharedPreferences.getInt(addKeyPrefix(key), 0);
    }

    /**
     * for reading string type data
     *
     * @param key key to read data
     * @return saved value for key
     */
    public String readString(String key) {
        return sharedPreferences.getString(addKeyPrefix(key), null);
    }

    /**
     * write an entity class in sync mode
     *
     * @param t an entity class
     * @return Saved successfully or not.
     */
    public boolean write(T t) {
        return write(MODE_SYNC, t);
    }

    /**
     * Save each field of entity to shared preferences.
     *
     * @param t Entity for saveLocal.
     * @return Saved successfully or not.
     */
    public boolean write(int mode, T t) {
        try {
            for (Field field : Convert.getStandardFields(t.getClass())) {
                field.setAccessible(true);
                try {
                    String fieldName = field.getName();
                    putData(mode, fieldName, field.get(t));
                } catch (Exception ex) {
                    Log.d(String.valueOf(ex));
                }
            }
            return true;

        } catch (Exception ex) {
            Log.e(String.valueOf(ex));
            return false;
        }
    }

    /**
     * Delete all saved field of entity.
     *
     * @return Deleted successfully or not.
     */
    public boolean deleteAll() {
        try {
            T t = typeParameterClass.newInstance();
            List<String> keys = new ArrayList<>();
            for (Field field : Convert.getStandardFields(t.getClass())) {
                keys.add(field.getName());
            }
            return delete(keys);
        } catch (Exception ex) {
            Log.e(String.valueOf(ex));
            return false;
        }
    }

    /**
     * Delete a key.
     *
     * @param key key.
     * @return Deleted successfully or not.
     */
    public boolean delete(String key) {
        return delete(Collections.singletonList(key));
    }

    /**
     * Delete saved data by list of keys.
     *
     * @param keys List of keys.
     * @return Deleted successfully or not.
     */
    public boolean delete(List<String> keys) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (String key : keys) {
                editor.remove(addKeyPrefix(key));
                Log.d(key, " is Removed from Shared preference.");
            }
            editor.apply();
            return true;
        } catch (Exception ex) {
            Log.e(String.valueOf(ex));
            return false;
        }
    }
    //endregion

    //region Helper
    private String addKeyPrefix(String keyName) {
        if (Tools.isNullOrEmpty(keyPrefix)) return keyName;
        return keyPrefix + keyName;
    }

    private String removeKeyPrefix(String keyName) {
        if (Tools.isNullOrEmpty(keyPrefix)) return keyName;
        if (keyName.startsWith(keyPrefix)) return keyName.replace(keyPrefix, "");
        return keyPrefix;
    }

    /**
     * Save single value.
     *
     * @param key   A key name.
     * @param value Value of the key.
     * @return Saved successfully or not.
     */
    private boolean putData(int mode, String key, Object value) {
        try {
            if (value == null) {
                if (mode == MODE_SYNC) delete(key);
                return true;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            Class aClass = value.getClass();

            key = addKeyPrefix(key);
            if (Boolean.class.equals(aClass) || Boolean.TYPE.equals(aClass))
                editor.putBoolean(key, (boolean) value);
            else if (Integer.class.equals(aClass) || Integer.TYPE.equals(aClass))
                editor.putInt(key, (int) value);
            else if (Long.class.equals(aClass) || Long.TYPE.equals(aClass))
                editor.putLong(key, (long) value);
            else if (Float.class.equals(aClass) || Float.TYPE.equals(aClass))
                editor.putFloat(key, (float) value);
            else if (String.class.equals(aClass))
                editor.putString(key, (String) value);
            else
                throw new Exception("Can not saveLocal this type '" + aClass.getName() + "'.");

            Log.d(key + " = " + value + " Write in shared preference.");
            editor.apply();

            return true;
        } catch (Exception ex) {
            Log.e("rrrr", String.valueOf(ex));
            return false;
        }
    }

    /**
     * Save int value to shared preferences.
     *
     * @param value int for saveLocal.
     * @param key   refer key.
     * @return Saved successfully or not.
     */
    public boolean write(String key, int value) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            key = addKeyPrefix(key);
            editor.putInt(key, value);
            editor.apply();
            return true;
        } catch (Exception ex) {
            Log.e(ex);
            return false;
        }
    }

    /**
     * Save object value to shared preferences.
     *
     * @param value object for saveLocal.
     * @param key   refer key.
     * @return Saved successfully or not.
     */
    public boolean write(String key, Object value) {
        return putData(MODE_SYNC, key, value);
    }
    //endregion

}
