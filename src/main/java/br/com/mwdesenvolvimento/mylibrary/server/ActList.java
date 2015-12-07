package br.com.mwdesenvolvimento.mylibrary.server;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;

/**
 * Created by mindware on 26/03/15.
 */
public class ActList <T, U extends ArrayAdapter> implements TaskListener  {
  private static final String TAG = ActList.class.getSimpleName();

  private Activity activity;
  private Class<T> clazz;
  private Class<U> adpClazz;
  private int idList;
  private int layoutItem;
  private boolean persist;

  public ActList(Activity activity, Class<T> clazz, Class<U> adpClazz, int idList, int layoutItem) {
    this(activity, clazz, adpClazz, idList, layoutItem, true);
  }

  public ActList(Activity activity, Class<T> clazz, Class<U> adpClazz, int idList, int layoutItem, boolean persist) {
    this.activity = activity;
    this.clazz = clazz;
    this.adpClazz = adpClazz;
    this.idList = idList;
    this.layoutItem = layoutItem;
    this.persist = persist;
    JsonHelper.populateList(activity, clazz, idList,
        layoutItem, adpClazz);
  }

  @Override
  public void act(String result, Task task, Exception e) {
    try {
      JsonHelper.getRows(result, clazz, persist);
      initList();
    } catch(JsonServerException | JSONException e2) {
      Log.e(TAG, "Exception", e2);
    }
  }

  public void initList() {
    JsonHelper.populateList(activity, clazz, idList,
            layoutItem, adpClazz);
  }
}