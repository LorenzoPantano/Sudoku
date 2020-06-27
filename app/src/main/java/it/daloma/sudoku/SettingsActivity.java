package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    public static final String HIGHLIGHT_SELECTED = "highlightSelected";
    public static final String SHOW_ELAPSED_TIME = "showElapsedTime";
    public static final String AUTOVALIDATE = "autovalidateEveryNumber";
    public static final String SOUNDS = "sounds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new SettingsFragment())
                .commit();
    }
}
