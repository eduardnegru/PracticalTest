package ro.pub.cs.systems.eim.practicaltest01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private Button pressMe;
    private Button pressMeToo;
    private Button nextActivity;
    private EditText pressMeText;
    private EditText pressMeTooText;

    public static final int MAIN_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        pressMe = findViewById(R.id.press_me_button);
        pressMeToo = findViewById(R.id.press_me_too_button);
        pressMeText = findViewById(R.id.press_me_text);
        pressMeTooText = findViewById(R.id.press_me_too_text);
        nextActivity = findViewById(R.id.nextActivityButton);


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

                String value = String.valueOf(pressMeText.getText());
                int newValue = Integer.parseInt(value) + 1;
                pressMeText.setText(Integer.toString(newValue));

            }
        });


        pressMeToo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
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
}
