package br.com.mwdesenvolvimento.mylibrary.controllers.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import br.com.mwdesenvolvimento.mylibrary.R;

/**
 * Created by geppetto on 29/01/16.
 */
public class OnOffFragment extends Fragment {

  private ImageButton btn;
  private MyListener myListener;
  private boolean on = true;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    btn = (ImageButton) inflater.inflate(R.layout.fr_on_off, container, false);
    btn.setOnClickListener(myListener = new MyListener());
    return btn;
  }

  public OnOffFragment setImageResource(int resId) {
    btn.setImageResource(resId);
    return this;
  }

  public boolean isOn() {
    return on;
  }

  public OnOffFragment setOn(boolean on) {
    this.on = on;
    if(on) btn.setAlpha(1f);
    else   btn.setAlpha(.5f);
    return this;
  }

  public OnOffFragment setOnClickListener(final View.OnClickListener listener) {
    myListener.setListener(listener);
    return this;
  }

  private class MyListener implements View.OnClickListener {
    private View.OnClickListener listener;

    @Override
    public void onClick(View v) {
      setOn(!on);
      if(listener != null) listener.onClick(v);
    }

    public void setListener(View.OnClickListener listener) {
      this.listener = listener;
    }
  }
}
