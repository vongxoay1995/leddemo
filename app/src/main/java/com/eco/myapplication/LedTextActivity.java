package com.eco.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;

import com.eco.myapplication.databinding.ActivityLedTextBinding;


public class LedTextActivity extends AppCompatActivity {
    ActivityLedTextBinding binding;
    //LEDView ledView;
    String mes = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLedTextBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        //this.ledView = new LEDView(this);
        binding.previewText.setInfor(this);
        binding.previewText.setMessage(binding.edtIput.getText().toString());
        listener();
        binding.edtIput.addTextChangedListener(messageWatcher);
    }
    TextWatcher messageWatcher = new TextWatcher() { // from class: oops.ledscroller.MainActivity.16
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            mes = "";
            mes = editable.toString();
            binding.previewText.setMessage(mes);
        }
    };
  /*  private void startScroll() {
        if (scroller.scroll!= null && !scroller.isSCroll) {
            scroller.isSCroll = true;
            scroller.paddingStart = scroller.scroll.getCurrX();
            scroller.scroll.abortAnimation();
        }
        scroller.scroll();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAN", "onResume: ");
       this.binding.previewText.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.binding.previewText.pause();
    }

    private void listener() {

    }
}