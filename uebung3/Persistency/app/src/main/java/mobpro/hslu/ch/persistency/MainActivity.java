package mobpro.hslu.ch.persistency;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final String PREFS_NAME = "MyPrivatePrefsFile";
    public static final String COUNTER_KEY = "ResumeCounterKey";
    private final String FILE_NAME ="myTextFile.txt";
    private static SharedPreferences defaultSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final int newResumeCount = sharedPreferences.getInt(COUNTER_KEY, 0) + 1;
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COUNTER_KEY, newResumeCount);
        editor.apply();

        TextView resumeCounter = (TextView) findViewById(R.id.ResumeCounter);
        resumeCounter.setText("MainActivity.onResume() wurde seit der Installation dieser App "
                + newResumeCount + " mal aufgerufen.");

        printTeaSettings();
    }

    public void showTeaPreferences(View v) {
        Intent intent = new Intent(this, AppPreferenceActivity.class);
        startActivity(intent);
    }

    private void printTeaSettings() {
        boolean teaWithSugar = defaultSharedPreferences.getBoolean("teaWithSugar", true);
        String teaSweetener = defaultSharedPreferences.getString("teaSweetener", "Rohrzucker");
        String teaPreferred = defaultSharedPreferences.getString("teaPreferred", "Nestea");
        TextView currentTea = (TextView) findViewById(R.id.currentTea);


        if(teaWithSugar == false) {
            currentTea.setText("Ich trinke am liebsten " + teaPreferred);
        } else {
            currentTea.setText("Ich trinke am liebsten " + teaPreferred + " mit " + getValueFromKey(teaSweetener) + " gesüsst");
        }
    }

    public void setTeaDefault(View v) {
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.putBoolean("teaWithSugar", false);
        editor.putString("teaSweetener", "Rohrzucker");
        editor.putString("teaPreferred", "Nestea");
        editor.apply();
        printTeaSettings();
    }

    public String getValueFromKey(String key) {
        String[] keys = getResources().getStringArray(R.array.teaSweetenerValues);
        String[] values = getResources().getStringArray(R.array.teaSweetener);
        int i = 0;
        while(i < keys.length) {
            if(keys[i].equals(key)) {
                return values[i];
            }
            i++;
        }
        return "";
    }

    private void saveExtFileWithPermission() {
        int grant = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 25);
        } else {
            writeExternal();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case 24:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission " + permissions[0] + " denied! ", Toast.LENGTH_SHORT).show();
                } else {
                    loadExternal();
                }
                break;
            case 25:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission " + permissions[0] + " denied! ", Toast.LENGTH_SHORT).show();
                } else {
                    writeExternal();
                }
                break;
        }
    }

    public void saveFile(View v) {
        CheckBox cb = (CheckBox) findViewById(R.id.fileSystem_external);
        if(cb.isChecked() && Environment.getExternalStorageState().equals("mounted")) {
            saveExtFileWithPermission();
        } else {
            writeInternal();
        }
    }

    public void loadFile(View v) {
        CheckBox cb = (CheckBox) findViewById(R.id.fileSystem_external);
        if(cb.isChecked() && Environment.getExternalStorageState().equals("mounted")) {
            loadExternal();
        } else {
            loadInternal();
        }
    }

    private void writeExternal() {
        EditText editText = (EditText) findViewById(R.id.fileSystem_text);
        String text = editText.getText().toString();
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/ordner");
            dir.mkdir();
            File file = new File(dir, FILE_NAME);
            FileOutputStream os = new FileOutputStream(file);
            String data = text;
            os.write(data.getBytes());
            os.close();
            Toast.makeText(this,"Written to sdcard storage",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
        }
    }

    private void writeInternal() {
        EditText editText = (EditText) findViewById(R.id.fileSystem_text);
        String text = editText.getText().toString();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(text);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            System.out.println("Written to local storage: " + text);
            Toast.makeText(this,"Written to local storage",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
                //fileOutputStream.close();
        }
    }

    private void loadInternal() {
        EditText editText = (EditText) findViewById(R.id.fileSystem_text);
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileInputStream fin = openFileInput(FILE_NAME);
            if (fin != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fin);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while (( line = bufferedReader.readLine() ) != null) {
                    lines.add(line);
                }
                fin.close();
            }
            if (lines.size()>0){
                editText.setText(lines.get(0));
            } else {
                System.out.println("NOTHING TO READ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExternal() {
        EditText editText = (EditText) findViewById(R.id.fileSystem_text);
        int grant = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (grant == PackageManager.PERMISSION_GRANTED) {
            try {
                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath()+"/ordner/");
                dir.mkdir();
                File file = new File(dir, FILE_NAME);
                FileInputStream fileIn = new FileInputStream(file);
                InputStreamReader InputRead = new InputStreamReader(fileIn);

                char[] inputBuffer = new char[100];
                String s = "";
                int charRead;

                while ((charRead = InputRead.read(inputBuffer)) > 0) {
                    String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                    s += readstring;
                }
                InputRead.close();

                editText.setText(s);
                System.out.println("Read from sdcard storage: " + s);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 24);
        }
    }
}
