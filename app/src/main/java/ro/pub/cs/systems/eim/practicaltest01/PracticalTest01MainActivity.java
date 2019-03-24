package ro.pub.cs.systems.eim.practicaltest01;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
import java.security.Permissions;

import javax.xml.transform.Result;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private Button pressMe;
    private Button pressMeToo;
    private Button nextActivity;
    private EditText pressMeText;
    private EditText pressMeTooText;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver();
    private IntentFilter intentFilter;
    private Button callButton;

    public static final int MAIN_ACTIVITY_REQUEST_CODE = 1;
    public static final int THRESHOLD = 7;
    private boolean serviceStarted = false;
    private boolean acceptedCallPerm = false;

    protected void thresholdMetCheck()
    {
        int a = Integer.parseInt(String.valueOf(pressMeText.getText()));
        int b = Integer.parseInt(String.valueOf(pressMeTooText.getText()));

        if(a + b > THRESHOLD && !serviceStarted)
        {
            Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
            intent.putExtra("press_me_count", String.valueOf(pressMeText.getText()));
            intent.putExtra("press_me_too_count", String.valueOf(pressMeTooText.getText()));
            getApplicationContext().startService(intent);
            Toast.makeText(this, "Started service", Toast.LENGTH_LONG).show();
            serviceStarted = true;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        Toast.makeText(this, "Stopped service", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.actionTypes[0]);

        pressMe = findViewById(R.id.press_me_button);
        pressMeToo = findViewById(R.id.press_me_too_button);
        pressMeText = findViewById(R.id.press_me_text);
        pressMeTooText = findViewById(R.id.press_me_too_text);
        nextActivity = findViewById(R.id.nextActivityButton);
        callButton = findViewById(R.id.call);

        if(savedInstanceState != null)
        {
            String savedPressMe = savedInstanceState.getString("press_me");
            String savedPressMeToo = savedInstanceState.getString("press_me_too");


            if(savedPressMe != null)
            {
                pressMeText.setText(savedPressMe);
            }

            if(savedPressMeToo != null)
            {
                pressMeTooText.setText(savedPressMeToo);
            }
        }

        pressMe.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                thresholdMetCheck();
                String value = String.valueOf(pressMeText.getText());
                int newValue = Integer.parseInt(value) + 1;
                pressMeText.setText(Integer.toString(newValue));

            }
        });


        pressMeToo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                thresholdMetCheck();
                String value = String.valueOf(pressMeTooText.getText());
                int newValue = Integer.parseInt(value) + 1;
                pressMeTooText.setText(Integer.toString(newValue));
            };
        });

        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.parseInt(String.valueOf(pressMeText.getText())) + Integer.parseInt(String.valueOf(pressMeTooText.getText()));
                Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                intent.putExtra("press_count_total", Integer.toString(total));
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(PracticalTest01MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PracticalTest01MainActivity.this,  new String[]{Manifest.permission.CALL_PHONE}, Constants.CALL_PERM);
                }
                else
                {
                    if(acceptedCallPerm)
                    {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:072492271"));
                        startActivity(intent);
                    }

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(permissions[0] == Manifest.permission.CALL_PHONE && grantResults[0] == 0)
        {
            if(requestCode == Constants.CALL_PERM) {
                acceptedCallPerm = true;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:0724922741"));
                startActivity(intent);
            }
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PracticalTest01MainActivity.MAIN_ACTIVITY_REQUEST_CODE)
        {
            Toast.makeText(this, "Activity returned with" + resultCode, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("press_me", String.valueOf(pressMeText.getText()));
        outState.putString("press_me_too", String.valueOf(pressMeTooText.getText()));
        Log.d("TAG", String.valueOf(outState));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
