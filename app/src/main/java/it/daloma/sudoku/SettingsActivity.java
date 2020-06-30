package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String HIGHLIGHT_SELECTED = "highlightSelected";
    public static final String SHOW_ELAPSED_TIME = "showElapsedTime";
    public static final String AUTOVALIDATE = "autovalidateEveryNumber";
    public static final String SOUNDS = "sounds";

    private ImageButton imgbtnBackSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new SettingsFragment())
                .commit();

        imgbtnBackSettings = findViewById(R.id.imgbtnSettingsBack);
        imgbtnBackSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.imgbtnSettingsBack){
            onBackPressed();
        }
    }
}
