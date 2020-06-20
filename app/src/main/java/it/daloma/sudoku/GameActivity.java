package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button button1,button2,button3,button4,button5,button6,button7,button8,button9,buttonCancel;
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        buttonCancel = findViewById(R.id.buttonCancel);

        GridLayout.LayoutParams params = (GridLayout.LayoutParams) button1.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels / 7;
        params.height = params.width;
        button1.setLayoutParams(params);

        GridLayout.LayoutParams params2 = (GridLayout.LayoutParams) button2.getLayoutParams();
        params2.width = getResources().getDisplayMetrics().widthPixels / 7;
        params2.height = params2.width;
        button2.setLayoutParams(params2);

        GridLayout.LayoutParams params3 = (GridLayout.LayoutParams) button3.getLayoutParams();
        params3.width = getResources().getDisplayMetrics().widthPixels / 7;
        params3.height = params3.width;
        button3.setLayoutParams(params3);

        GridLayout.LayoutParams params4 = (GridLayout.LayoutParams) button4.getLayoutParams();
        params4.width = getResources().getDisplayMetrics().widthPixels / 7;
        params4.height = params4.width;
        button4.setLayoutParams(params4);

        GridLayout.LayoutParams params5 = (GridLayout.LayoutParams) button5.getLayoutParams();
        params5.width = getResources().getDisplayMetrics().widthPixels / 7;
        params5.height = params5.width;
        button5.setLayoutParams(params5);

        GridLayout.LayoutParams params6 = (GridLayout.LayoutParams) button6.getLayoutParams();
        params6.width = getResources().getDisplayMetrics().widthPixels / 7;
        params6.height = params6.width;
        button6.setLayoutParams(params6);

        GridLayout.LayoutParams params7 = (GridLayout.LayoutParams) button7.getLayoutParams();
        params7.width = getResources().getDisplayMetrics().widthPixels / 7;
        params7.height = params7.width;
        button7.setLayoutParams(params7);

        GridLayout.LayoutParams params8 = (GridLayout.LayoutParams) button8.getLayoutParams();
        params8.width = getResources().getDisplayMetrics().widthPixels / 7;
        params8.height = params8.width;
        button8.setLayoutParams(params8);

        GridLayout.LayoutParams params9 = (GridLayout.LayoutParams) button9.getLayoutParams();
        params9.width = getResources().getDisplayMetrics().widthPixels / 7;
        params9.height = params9.width;
        button9.setLayoutParams(params9);

        GridLayout.LayoutParams paramsCancel = (GridLayout.LayoutParams) buttonCancel.getLayoutParams();
        paramsCancel.width = getResources().getDisplayMetrics().widthPixels / 7;
        paramsCancel.height = paramsCancel.width;
        buttonCancel.setLayoutParams(paramsCancel);
    }
}
