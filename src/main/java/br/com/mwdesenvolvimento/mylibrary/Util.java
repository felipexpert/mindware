package br.com.mwdesenvolvimento.mylibrary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by geppetto on 05/08/15.
 */
public class Util {
  private Util() {}

  public static boolean eq(double x, double y) {
    // 1 / 128 -> EPSILON
    return Math.abs(x - y) < 0.0078125d;
  }
  public static boolean le(double x, double y) {
    return eq(x, y) || x < y;
  }
  public static boolean ge(double x, double y) {
    return eq(x, y) || x > y;
  }
  public static boolean lt(double x, double y) {
    return !eq(x, y) && x < y;
  }
  public static boolean gt(double x, double y) {
    return !eq(x, y) && x > y;
  }

  public static void putFragment(Activity activity, int id, Fragment frag) {
    FragmentManager fm = activity.getFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    ft.replace(id, frag);
    ft.commit();
    fm.executePendingTransactions();
  }

  public static Fragment findFragment(Activity activity, int id) {
    FragmentManager fm = activity.getFragmentManager();
    return fm.findFragmentById(id);
  }
}
