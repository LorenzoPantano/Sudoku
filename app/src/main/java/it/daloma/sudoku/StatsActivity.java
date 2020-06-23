package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StatsActivity extends AppCompatActivity {

    private Button[] diffButtons = new Button[3];
    private static final int[] BUTTONS_ID = {
            R.id.btnEasy,
            R.id.btnMedium,
            R.id.btnHard
    };
    private int selectedButton = 0;
    StatsActivityClickListener statsActivityClickListener = new StatsActivityClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //Buttons
        for (int i = 0; i < 3; i++) {
            diffButtons[i] = findViewById(BUTTONS_ID[i]);
            diffButtons[i].setOnClickListener(statsActivityClickListener);
        }
    }

    private class StatsActivityClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnEasy:
                    if (selectedButton == 0) break;
                    else {
                        selectedButton = 0;
                        diffButtons[0].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_white));
                        diffButtons[0].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.pantone_classic_blue)));

                        for (int i = 1; i < 3; i++) {
                            diffButtons[i].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_black));
                            diffButtons[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.background_white)));
                        }
                        break;
                    }

                case R.id.btnMedium:
                    if (selectedButton == 1) break;
                    else {
                        selectedButton = 1;
                        diffButtons[1].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_white));
                        diffButtons[1].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.pantone_classic_blue)));

                        for (int i = 0; i < 3; i++) {
                            if (i == 1) continue;
                            diffButtons[i].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_black));
                            diffButtons[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.background_white)));
                        }
                        break;
                    }

                case R.id.btnHard:
                    if (selectedButton == 2) break;
                    else {
                        selectedButton = 2;
                        diffButtons[2].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_white));
                        diffButtons[2].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.pantone_classic_blue)));

                        for (int i = 0; i < 2; i++) {
                            diffButtons[i].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_black));
                            diffButtons[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.background_white)));
                        }
                        break;
                    }


            }
        }
    }
}
