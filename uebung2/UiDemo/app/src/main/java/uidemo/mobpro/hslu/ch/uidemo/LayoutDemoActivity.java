package uidemo.mobpro.hslu.ch.uidemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by daniel on 07.03.2017.
 */

public final class LayoutDemoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent.hasExtra("layout")) {
            String layout = getIntent().getStringExtra("layout");
            if(layout.equals("linear")) {
                setContentView(R.layout.layoutdemo_linearlayout);
            }
            if(layout.equals("relative")) {
                setContentView(R.layout.layoutdemo_relativelayout);
            }
        }

    }
}
