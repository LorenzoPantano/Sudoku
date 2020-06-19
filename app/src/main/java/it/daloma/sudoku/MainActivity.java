package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton imgbtnInfo, imgbtnSettings, imgbtnMenu;
    private InfoDialogFragment infoDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sudoku Generator
        SudokuGenerator.getInstace();

        imgbtnInfo = findViewById(R.id.imgbtnInfo);
        imgbtnMenu = findViewById(R.id.imgbtnMenu);
        imgbtnSettings = findViewById(R.id.imgbtnSettings);

        imgbtnInfo.setOnClickListener(new MainActivityButtonsListener());
        imgbtnSettings.setOnClickListener(new MainActivityButtonsListener());
        imgbtnMenu.setOnClickListener(new MainActivityButtonsListener());

    }

    public static class InfoDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
            LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
            builder.setView(layoutInflater.inflate(R.layout.info_dialog, null));
            return builder.create();
        }
    }

    private class MainActivityButtonsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgbtnInfo) {
                infoDialogFragment = new InfoDialogFragment();
                infoDialogFragment.show(getSupportFragmentManager(), "infoDialog");
            }
        }
    }


}
