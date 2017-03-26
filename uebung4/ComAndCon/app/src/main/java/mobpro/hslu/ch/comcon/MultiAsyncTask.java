package mobpro.hslu.ch.comcon;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daniel on 26.03.2017.
 */

public class MultiAsyncTask extends AsyncTask<URL, String, Void> {
    private final long WAIT_TIME_MILLIS = 3000;
    private final Context mainActivity;
    private String[] filmTitles = new String[4];
    private int counter = 0;

    public MultiAsyncTask(Context mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(URL... urls) {
        InputStream in = null;
        try {
            for (URL url : urls) {
                in = openHttpConnection(url);
                String text = readText(in);
                Thread.sleep(WAIT_TIME_MILLIS);
                publishProgress(text);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } finally {
            closeStream(in);
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        filmTitles[counter] = values[0];
        Toast.makeText(mainActivity.getApplicationContext(),filmTitles[counter]+ " geladen",Toast.LENGTH_SHORT).show();
        counter++;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainActivity);
        dialogBuilder
                .setTitle("Film Titel")
                .setItems(filmTitles,null)
                .show();
    }

    private InputStream openHttpConnection(URL url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return httpURLConnection.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private String readText(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    private boolean closeStream(InputStream in){
        try {
            in.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
