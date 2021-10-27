package com.pinkmoon.flux.ui.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.pinkmoon.flux.R;


public class SettingsFragment extends Fragment {

    // widgets
    private TextInputLayout etUserCanvasToken;
    private TextView tvCheck;
    private RadioGroup rgCanvasSyncFreq;
    private Switch switchDarkMode;

    // local vars

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO define database ViewModels here (from settings table, not created yet)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        defineViews(v);
        setOnClickListeners(v);

        return v;
    }

    private void defineViews(View v) {
        etUserCanvasToken = v.findViewById(R.id.et_fragment_settings_canvas_token);
        tvCheck = v.findViewById(R.id.tv_fragment_settings_save_token);
        rgCanvasSyncFreq = v.findViewById(R.id.rg_fragment_settings_group);
        switchDarkMode = v.findViewById(R.id.switch_fragment_settings_dark_mode);
    }

    @SuppressLint("NonConstantResourceId")
    private void setOnClickListeners(View v) {
        // foar t0k3n editTextz!!!1!
        etUserCanvasToken.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!verifyTextIsNotEmpty(charSequence.toString())){
                    tvCheck.setVisibility(View.VISIBLE);
                }else{
                    tvCheck.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        tvCheck.setOnClickListener(l -> {
            Toast.makeText(getContext(),
                    etUserCanvasToken.getEditText().getText().toString().trim(),
                    Toast.LENGTH_SHORT).show();
        });

        // foar radio button group!!1!
        int selectedRadBtnId = rgCanvasSyncFreq.getCheckedRadioButtonId();
        RadioButton radBtn = v.findViewById(selectedRadBtnId);

        if(radBtn != null){
            switch (radBtn.getId()){
                case R.id.rad_btn_fragment_settings_daily:
                    // TODO update local database with 'D' user selected setting
                    break;
                case R.id.rad_btn_fragment_settings_weekly:
                    // TODO update local database with 'W' user selected setting
                    break;
                case R.id.rad_btn_fragment_settings_never:
                    // TODO update local database with 'N' user selected setting
                    break;
            }
        }

        // foar dark m0dez toggl3 switch!!1
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // TODO update local database with whatever the user wants for dark mode
            }
        });
    }

    private boolean verifyTextIsNotEmpty(String text){
        return text.trim().isEmpty();
    }
}