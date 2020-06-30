package it.daloma.sudoku.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import it.daloma.sudoku.R;

public class WinningDialog {

    private Activity activity;
    private AlertDialog dialog;

    public WinningDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.winning_dialog, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

}
