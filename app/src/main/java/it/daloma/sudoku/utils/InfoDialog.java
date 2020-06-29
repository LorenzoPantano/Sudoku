package it.daloma.sudoku.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import it.daloma.sudoku.R;

public class InfoDialog {

    private Activity activity;
    private AlertDialog dialog;

    public InfoDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.info_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
    public void cancelOnTouchOutside(){
        dialog.setCanceledOnTouchOutside(true);
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}


