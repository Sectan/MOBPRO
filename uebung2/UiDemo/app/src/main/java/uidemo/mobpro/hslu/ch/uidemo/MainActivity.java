package uidemo.mobpro.hslu.ch.uidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int counter;
    private static final String KEY_COUNTER = "KEY_COUNTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button radioButton1 = (Button) findViewById(R.id.radioButton1);
        radioButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showLinearLayout(v);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putInt(KEY_COUNTER, counter);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        counter = savedInstanceState.getInt(KEY_COUNTER);
    }

    public void showLinearLayout(View v) {
        Intent intent = new Intent(this, LayoutDemoActivity.class);
        intent.putExtra("layout", "linear");
        startActivity(intent);
    }

    public void showRelativeLayout(View v) {
        Intent intent = new Intent(this, LayoutDemoActivity.class);
        intent.putExtra("layout", "relative");
        startActivity(intent);
    }

    public void showViewsDemos(View w) {
        Intent intent = new Intent(this, ViewsDemoActivity.class);
        startActivity(intent);
    }

    public void showCounter(View v) {
        counter++;
        Toast.makeText(getApplicationContext(), Integer.toString(counter), Toast.LENGTH_SHORT).show();
    }
}
