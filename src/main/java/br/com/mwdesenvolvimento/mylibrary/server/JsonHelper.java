package br.com.mwdesenvolvimento.mylibrary.server;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mindware on 25/03/15.
 */
public class JsonHelper {
  private static final String TAG = JsonHelper.class.getSimpleName();
  private static final String MODELS_PACKAGE = "mwdesenvolvimento.com.br.waitermobo.models.";
  private JsonHelper() {};

  public static <T> void populateList(Activity activity, List<T> lst,
                                      int list, int itemLayout, Class<? extends ArrayAdapter> adapterClazz) {
    try {
      ListView lw = (ListView) activity.findViewById(list);
      ArrayAdapter<T> adp;
      Constructor<? extends ArrayAdapter> c =
          adapterClazz.getConstructor(Context.class, int.class, List.class);
      adp = c.newInstance(activity.getBaseContext(), itemLayout, lst);
      lw.setAdapter(adp);
    } catch(Exception e) {
      Log.e(TAG, "Exception", e);
    }
  }

  public static <T> void sortList(List<T> lst, Class<T> clazz) {
    if(Comparable.class.isAssignableFrom(clazz)) {
      List<Comparable<T>> sortedList = new ArrayList<>();
      for(T t : lst) sortedList.add((Comparable<T>) t);
      Comparator<Comparable<T>> comp = new Comparator<Comparable<T>>() {
        @Override
        public int compare(Comparable<T> lhs, Comparable<T> rhs) {
          return lhs.compareTo((T) rhs);
        }
      };
      Collections.sort(sortedList, comp);
      lst.clear();
      for(Comparable<T> c : sortedList) lst.add((T) c);
    }
  }

  public static <T> void populateList(Activity activity, Class<T> modelClazz,
    int list, int itemLayout, Class<? extends ArrayAdapter> adapterClazz) {
    try {
      Dao<T, Integer> dao = Initializer.INSTANCE.getDAOHelper().getDao(modelClazz);
      List<T> lst = dao.queryForAll();
      sortList(lst, modelClazz);
      ListView lw = (ListView) activity.findViewById(list);
      ArrayAdapter<T> adp;
      Constructor<? extends ArrayAdapter> c =
          adapterClazz.getConstructor(Context.class, int.class, List.class);
      adp = c.newInstance(activity.getBaseContext(), itemLayout, lst);
      lw.setAdapter(adp);
    } catch(Exception e) {
      Log.e(TAG, "Exception", e);
    }
  }

  public static <T> T getRow(String result, Class<T> clazz, boolean persist) {
    T t = null;
    try {
      JSONObject jsonObj = new JSONObject(result);
      t = clazz.newInstance();
      Method[] methods = clazz.getDeclaredMethods();
      for (Method m : methods) {
        String nameM = m.getName();
        if (nameM.startsWith("set")) {
          // Set's parameter type
          Class pt = m.getParameterTypes()[0];
          String fld = nameM.substring(3, 4).toLowerCase() + nameM.substring(4);
          try {
            if(jsonObj.isNull(fld)) {
              continue;
            } else if(fld.endsWith("Collection")) {
              Log.d(TAG, "COLLECTION!");
              String cap = fld.substring(0, 1).toUpperCase() + fld.substring(1, fld.length() - 10);
              String className = MODELS_PACKAGE + cap;
              Class<?> recursiveClazz = Class.forName(className);
              List<Object> collection = new ArrayList<>();
              JSONArray fld2 = jsonObj.getJSONArray(fld);
              for (int i = 0; i < fld2.length(); i++) {
                Object o = getRow(fld2.getString(i), recursiveClazz, persist);
                collection.add(o);
                Log.d(TAG, o.toString());
              }
              m.invoke(t, collection);
            } else if(fld.endsWith("Date")) {
              String fld2 = jsonObj.getString(fld);
              int year = Integer.parseInt(fld2.substring(0, 4));
              int month = Integer.parseInt(fld2.substring(5, 7)) - 1;
              int day = Integer.parseInt(fld2.substring(8, 10));
              int hour = Integer.parseInt(fld2.substring(11, 13));
              int minute = Integer.parseInt(fld2.substring(14, 16));
              int second = Integer.parseInt(fld2.substring(17, 19));
              Calendar c = Calendar.getInstance();
              c.set(year, month, day, hour, minute, second);
              m.invoke(t, c.getTime());
            } else {
              Object fld2 = jsonObj.get(fld);
              if (fld2 instanceof String) {
                m.invoke(t, toObject(pt, (String) fld2));
              } else if (fld2 instanceof Boolean ||
                      fld2 instanceof Byte ||
                      fld2 instanceof Short ||
                      fld2 instanceof Integer ||
                      fld2 instanceof Long ||
                      fld2 instanceof Float ||
                      fld2 instanceof Double) {
                m.invoke(t, fld2);
              } else {
                String cap = fld.substring(0, 1).toUpperCase() + fld.substring(1);
                String className = MODELS_PACKAGE + cap;
                Class<?> recursiveClazz = Class.forName(className);
                Object object = getRow(jsonObj.getString(fld), recursiveClazz, persist);
                m.invoke(t, object);
              }
            }
          } catch (JSONException e) {
            Log.e(TAG, "Exception", e);
          }
        }
      }
      if(persist) {
        Dao<T, Integer> dao = Initializer.INSTANCE.getDAOHelper().getDao(clazz);
        dao.createOrUpdate(t);
      }
    } catch(Exception e) {
      Log.e(TAG, "exception", e);
    }
    return t;
  }

  /**
   * This Method has been created to automatically build Model
   * Objects from PHP server requests;
   * @param result
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> List<T> getRows(String result, Class<T> clazz, boolean persist) throws JSONException, JsonServerException {
    List<T> list = new ArrayList<>();
    //try {
      JSONObject query = new JSONObject(result);
      if (!query.getBoolean("success")) throw new JsonServerException();

      JSONArray data = query.getJSONArray("data");
      for (int i = 0; i < data.length(); i++) {
        list.add(getRow(data.getString(i), clazz, persist));
      }
    //} catch(Exception e) {
    //  Log.e(TAG, "Exception", e);
    //}
    return list;
  }

  public static Object toObject( Class clazz, String value ) {
    if( Boolean.class == clazz || boolean.class == clazz) return Boolean.valueOf(value);
    else if( Byte.class == clazz || byte.class == clazz) return Byte.valueOf(value);
    else if( Short.class == clazz || short.class == clazz) return Short.valueOf(value);
    else if( Integer.class == clazz || int.class == clazz) return Integer.valueOf(value);
    else if( Long.class == clazz || long.class == clazz) return Long.valueOf(value);
    else if( Float.class == clazz || float.class == clazz) return Float.valueOf(value);
    else if( Double.class == clazz || double.class == clazz) return Double.valueOf(value);
    return value;
  }
}
