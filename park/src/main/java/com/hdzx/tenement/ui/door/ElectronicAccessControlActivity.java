package com.hdzx.tenement.ui.door;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.hdzx.tenement.R;
import com.hdzx.tenement.component.SwitchView;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.PreferencesUtils;

/**
 * Created by anchendong on 15/7/28.
 * 电子门禁
 */
public class ElectronicAccessControlActivity extends Activity {

    private SwitchView switchViewStartUp = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_door_electronicaccesscontrol);
        switchViewStartUp = (SwitchView) this.findViewById(R.id.sw_door_startup);
        switchViewStartUp.setOnText(null);
        boolean isOn = PreferencesUtils.getInstance().takeBoolean(ElectronicAccessControlActivity.this, Contants.PREFERENCES_KEY.door_status.name());
        switchViewStartUp.setState(isOn);
        switchViewStartUp.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                PreferencesUtils.getInstance().saveBoolean(ElectronicAccessControlActivity.this, Contants.PREFERENCES_KEY.door_status.name(), true);
                switchViewStartUp.toggleSwitch(true);
            }

            @Override
            public void toggleToOff() {
                PreferencesUtils.getInstance().saveBoolean(ElectronicAccessControlActivity.this, Contants.PREFERENCES_KEY.door_status.name(), false);
                switchViewStartUp.toggleSwitch(false);
            }
        });

    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_door_electronicaccesscontrol_back:
                finish();
                break;
            default:
                break;
        }
    }
}
