package com.example.kaddcoder.brigadaap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap googleMap;
    private AlertDialog popup;
    private EditText nom_zona;
    private RadioButton easy_button;
    private RadioButton med_button;
    private RadioButton hard_button;
    private RadioGroup radio_group_difficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        CameraUpdate startPoint = CameraUpdateFactory.newLatLng(new LatLng(41.119257, 1.245467));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

        googleMap.moveCamera(startPoint);
        googleMap.animateCamera(zoom);

        this.googleMap = googleMap;

        googleMap.setOnMapClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {

        showPopup(googleMap, latLng);


    }

    public void showPopup(GoogleMap googleMap, LatLng latLng) {

        final GoogleMap googleMap1 = googleMap;
        final LatLng latLng1 = latLng;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.addzone_popup, null);
        builder.setView(customLayout);

        RelativeLayout zone_popup = customLayout.findViewById(R.id.pop_up);

        zone_popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(nom_zona.isFocused()) {
                        nom_zona.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(customLayout.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

        nom_zona = (EditText) customLayout.findViewById(R.id.nom_zona);
        radio_group_difficulty = (RadioGroup) customLayout.findViewById(R.id.dificultat);
        easy_button = (RadioButton) customLayout.findViewById(R.id.radioButton_easy);
        med_button = (RadioButton) customLayout.findViewById(R.id.radioButton_medium);
        hard_button = (RadioButton) customLayout.findViewById(R.id.radioButton_hard);

        radio_group_difficulty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(nom_zona.isFocused()){
                    nom_zona.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(customLayout.getWindowToken(), 0);
                }else {
                    if((easy_button.isChecked() || med_button.isChecked() || hard_button.isChecked()) && !TextUtils.isEmpty(nom_zona.getText().toString().trim())) {
                        popup.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                    }else {
                        popup.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                    }

                }
            }
        });


        nom_zona.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if((easy_button.isChecked() || med_button.isChecked() || hard_button.isChecked()) && !TextUtils.isEmpty(nom_zona.getText().toString().trim())) {
                    popup.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                }else {
                    popup.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                googleMap1.addMarker(new MarkerOptions()
                        .position(latLng1)
                        .title("hola")
                        .snippet("que")
                        .rotation((float) -15.0)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );

            }
        });

        builder.setNegativeButton(R.string.cancell, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });



        popup = builder.create();
        popup.show();
        popup.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


    }
}
