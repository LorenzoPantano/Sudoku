package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import it.daloma.sudoku.model.SudokuModel;
import it.daloma.sudoku.model.SudokuViewModel;

public class GameActivity extends AppCompatActivity {

    //Controls Buttons
    Button[] buttons = new Button[10];
    private static final int[] BUTTON_IDS = {
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.buttonCancel
    };
    boolean[] isPressed = new boolean[10];
    NumbersOnClickListener numbersOnClickListener;

    //Model
    SudokuViewModel sudokuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Controls Setup
        numbersOnClickListener = new NumbersOnClickListener();
        for (int i = 0; i < 10; i++) {
            buttons[i] = findViewById(BUTTON_IDS[i]);
            buttons[i].setOnClickListener(numbersOnClickListener);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) buttons[i].getLayoutParams();
            params.width = getResources().getDisplayMetrics().widthPixels / 7;
            params.height = params.width;
            buttons[i].setLayoutParams(params);
        }

        //Model Setup
        sudokuViewModel = ViewModelProviders.of(this).get(SudokuViewModel.class);

    }

    private class NumbersOnClickListener implements View.OnClickListener {


        private static final String TAG = "NUMBERS_ONCLICK";

        public NumbersOnClickListener() {
            for (int i = 0; i < 9; i++) {
                isPressed[i] = false;
            }
        }

        void setIsPressed(int number, boolean pressed) {
            isPressed[number] = pressed;
        }

        boolean getIsPressed(int number) {
            return isPressed[number];
        }

        @Override
        public void onClick(View v) {

            Log.w(TAG, "onClick: Clicked");

            switch (v.getId()) {
                case R.id.button1:
                    //Se è già "premuto"
                    if (!getIsPressed(0)) {
                        Log.w(TAG, "BUTTON 1 PRESSED");
                        press(buttons[0], 0);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 1 DEPRESSED");
                        dePress(buttons[0], 0);
                        break;
                    }

                case R.id.button2:
                    if (!getIsPressed(1)) {
                        Log.w(TAG, "BUTTON 2 PRESSED");
                        press(buttons[1], 1);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 2 DEPRESSED");
                        dePress(buttons[1], 1);
                        break;
                    }

                case R.id.button3:
                    if (!getIsPressed(2)) {
                        Log.w(TAG, "BUTTON 3 PRESSED");
                        press(buttons[2], 2);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 3 DEPRESSED");
                        dePress(buttons[2], 2);
                        break;
                    }

                case R.id.button4:
                    if (!getIsPressed(3)) {
                        Log.w(TAG, "BUTTON 4 PRESSED");
                        press(buttons[3], 3);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 4 DEPRESSED");
                        dePress(buttons[3], 3);
                        break;
                    }

                case R.id.button5:
                    if (!getIsPressed(4)) {
                        Log.w(TAG, "BUTTON 5 PRESSED");
                        press(buttons[4], 4);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 5 DEPRESSED");
                        dePress(buttons[4], 4);
                        break;
                    }

                case R.id.button6:
                    if (!getIsPressed(5)) {
                        Log.w(TAG, "BUTTON 6 PRESSED");
                        press(buttons[5], 5);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 6 DEPRESSED");
                        dePress(buttons[5], 5);
                        break;
                    }

                case R.id.button7:
                    if (!getIsPressed(6)) {
                        Log.w(TAG, "BUTTON 7 PRESSED");
                        press(buttons[6], 6);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 7 DEPRESSED");
                        dePress(buttons[6], 6);
                        break;
                    }

                case R.id.button8:
                    if (!getIsPressed(7)) {
                        Log.w(TAG, "BUTTON 8 PRESSED");
                        press(buttons[7], 7);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 8 DEPRESSED");
                        dePress(buttons[7], 7);
                        break;
                    }

                case R.id.button9:
                    if (!getIsPressed(8)) {
                        Log.w(TAG, "BUTTON 9 PRESSED");
                        press(buttons[8], 8);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON 9 DEPRESSED");
                        dePress(buttons[8], 8);
                        break;
                    }

                case R.id.buttonCancel:
                    if (!getIsPressed(9)) {
                        Log.w(TAG, "BUTTON CANCEL PRESSED");
                        press(buttons[9], 9);
                        break;
                    }
                    else {
                        Log.w(TAG, "BUTTON CANCEL DEPRESSED");
                        dePress(buttons[9], 9);
                        break;
                    }
            }

        }
    }

    public void press(Button button, int number) {
        button.setTextColor(Color.WHITE);
        button.setBackground(getDrawable(R.drawable.circular_button_pressed));
        numbersOnClickListener.setIsPressed(number,true);
        for (int i = 0; i < 10; i++) {
            if (i == number) continue;
            dePress(buttons[i], i);
        }
    }

    public void dePress(Button button, int number) {
        button.setTextColor(getColor(R.color.pantone_classic_blue));
        button.setBackground(getDrawable(R.drawable.circular_button_nonpressed));
        numbersOnClickListener.setIsPressed(number, false);
    }
}
