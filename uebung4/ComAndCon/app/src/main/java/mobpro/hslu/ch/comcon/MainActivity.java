package mobpro.hslu.ch.comcon;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Thread demoThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadHttpDemosActivity(View view) {
        startActivity(new Intent(this, HttpDemosActivity.class));
    }

    public void freeze7Seconds (View view) {
        try {
            Thread.sleep(7000);
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void demoThreadStarten(View view) {
        final Button button = (Button) view;
        if ((demoThread == null) || !(demoThread.isAlive())) {
            demoThread = createWaitThread(button);
            demoThread.start();
            button.setText("*[DemoThread läuft...]");
        } else {
            Toast.makeText(this, "*DemoThread läuft schon!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private Thread createWaitThread(final Button button) {
        return new Thread("hsluDemoThread") {
            @Override
            public void run() {
                final Runnable doneRunnable = new Runnable() {
                    @Override
                    public void run() {
                        button.setText("*DemoThread starten");
                    }
                };
                try {
                    Thread.sleep(7000);
                    MainActivity.this.runOnUiThread(doneRunnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void startMultiAsyncTask(View view) {
        AsyncTask asyncTask = new MultiAsyncTask(this);
        try {
            URL[] urls = new URL[4];
            for(int i=0;i<4;i++){
                urls[i] = new URL("http://wherever.ch/hslu/title"+i+".txt");
            }
            asyncTask.execute(urls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
