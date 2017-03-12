package uidemo.mobpro.hslu.ch.uidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewsDemoActivity extends AppCompatActivity {
    private  TextView logLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views_demo);

        logLabel = (TextView) findViewById(R.id.viewsDemo_logLabel);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.viewsDemo_ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                logLabel.setText("Neue Bewertung: " + rating);
            }
        });
    }
}
