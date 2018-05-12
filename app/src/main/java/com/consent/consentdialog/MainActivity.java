package com.consent.consentdialog;

import android.Manifest;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sharelib.consent_dialog.ConsentDialog;
import com.sharelib.consent_dialog.PermissionItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * UI Widgets
     */
    private CheckBox mLocation, mStorage, mContacts, mCamera, mSMS, mCalendar;

    /**
     * AppCompatActivity Methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUIWidgets();
    }

    /**
     * Class Methods
     */
    private void initUIWidgets(){
        mLocation = findViewById(R.id.am_location);
        mCalendar = findViewById(R.id.am_calendar);
        mSMS = findViewById(R.id.am_sms);
        mStorage = findViewById(R.id.am_storage);
        mCamera = findViewById(R.id.am_camera);
        mContacts = findViewById(R.id.am_contacts);
        findViewById(R.id.am_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PermissionItem> permissionItems = onStartClick();
                int defaultTextColor = getColorFromRes(R.color.cardview_dark_background);
                int defaultBackgroundColor = getColorFromRes(android.R.color.white);
                ConsentDialog.newInstance(permissionItems, defaultTextColor, 14,
                        getString(R.string.consent_title), defaultTextColor, getString(R.string.next),
                        defaultBackgroundColor, defaultBackgroundColor, 18, 18,
                        getColorFromRes(R.color.colorAccent), new ConsentDialog.ConsentCallback() {
                            @Override
                            public void onNextClick(String[] permissions) {
                                Dexter.withActivity(MainActivity.this)
                                        .withPermissions(permissions)
                                        .withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                                List<PermissionGrantedResponse> list = report.getGrantedPermissionResponses();
                                                String stringToast;
                                                if(list.isEmpty()){
                                                    stringToast = getString(R.string.all_permissions_denied);
                                                } else if(!report.areAllPermissionsGranted()){
                                                    stringToast = Integer.toString
                                                            (list.size()) + " " +  getString(R.string.permissions_were_granted);
                                                } else {
                                                    stringToast = getString(R.string.all_permissions_granted);
                                                }
                                                Toast.makeText(MainActivity.this, stringToast, Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown
                                                    (List<PermissionRequest> permissions, PermissionToken token) {
                                                token.continuePermissionRequest();
                                            }
                                        }).check();
                            }
                        }).show(getSupportFragmentManager(), ConsentDialog.TAG);
            }
        });
    }

    private List<PermissionItem> onStartClick(){
        List<PermissionItem> list = new ArrayList<>();
        if(mLocation.isChecked()){
            PermissionItem item = new PermissionItem();
            item.setName(mLocation.getText().toString());
            item.setPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            item.setDrawableResource(R.drawable.location);
            list.add(item);
        }
        if(mContacts.isChecked()){
            PermissionItem item = new PermissionItem();
            item.setName(mLocation.getText().toString());
            item.setPermissions((new String[]{Manifest.permission.READ_CONTACTS}));
            item.setDrawableResource(R.drawable.location);
            list.add(item);
        }
        if(mStorage.isChecked()){
            PermissionItem item = new PermissionItem();
            item.setName(mLocation.getText().toString());
            item.setPermissions((new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}));
            item.setDrawableResource(R.drawable.location);
            list.add(item);
        }
        if(mCamera.isChecked()){
            PermissionItem item = new PermissionItem();
            item.setName(mLocation.getText().toString());
            item.setPermissions((new String[]{Manifest.permission.CAMERA}));
            item.setDrawableResource(R.drawable.location);
            list.add(item);
        }
        if(mSMS.isChecked()){
            PermissionItem item = new PermissionItem();
            item.setName(mLocation.getText().toString());
            item.setPermissions((new String[]{Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_MMS}));
            item.setDrawableResource(R.drawable.location);
            list.add(item);
        }
        if(mCalendar.isChecked()){
            PermissionItem item = new PermissionItem();
            item.setName(mLocation.getText().toString());
            item.setPermissions((new String[]{Manifest.permission.READ_CALENDAR}));
            item.setDrawableResource(R.drawable.location);
            list.add(item);
        }
        return list;
    }

    private int getColorFromRes(@ColorRes int colorRes){
        return ContextCompat.getColor(this, colorRes);
    }

}
