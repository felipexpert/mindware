package br.com.mwdesenvolvimento.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by geppetto on 30/01/16.
 */
public class Misc {
  private Misc() {}

  public static boolean hideKeyboard(Activity activity) {
    // Closing virtual keyboard
    View currentFocused = activity.getCurrentFocus();
    if(currentFocused != null) {
      InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(currentFocused.getWindowToken(), 0);
      return true;
    }
    return false;
  }

  /**
   * Use this to close all activities and exit the app beautifully
   * @param activity
   * @param mainActivityClass
   */
  public static void closeAllActivities(Activity activity, Class<? extends Activity> mainActivityClass) {
    Intent intent = new Intent(activity, mainActivityClass);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("EXIT", true);
    activity.startActivity(intent);
  }
}
