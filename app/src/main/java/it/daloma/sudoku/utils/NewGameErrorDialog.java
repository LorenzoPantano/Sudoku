package it.daloma.sudoku.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import it.daloma.sudoku.R;

public class NewGameErrorDialog {

    private Activity activity;
    private AlertDialog dialog;

    public NewGameErrorDialog(Activity activity) {
        this.activity = activity;
    }

    public void startErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.new_game_error_dialog, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
