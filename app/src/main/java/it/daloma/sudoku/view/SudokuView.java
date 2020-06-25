package it.daloma.sudoku.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class SudokuView extends View {

    //Attributes
    private static final String TAG = "SUDOKUVIEWGRID";

    //Paints
    private Paint thinPaint;
    private Paint thickPaint;
    private Paint selectedCellPaint;

    //Values
    private static int width;
    private static int height;
    private static final int padding = 64;
    private static final int SIZE = 9;
    private static final int SQRT_SIZE = 3;
    private float cellSizePixels;
    public int selectedRow = -1;
    public int selectedCol = -1;

    //GestureDetector
    private GestureDetector gestureDetector;


    public SudokuView(Context context) {
        super(context);
        init(null);
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(@Nullable AttributeSet attributeSet) {

        //Paint più sottile per celle
        thinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thinPaint.setStrokeWidth(2);
        thinPaint.setStyle(Paint.Style.STROKE);
        thinPaint.setColor(Color.BLACK);

        //Paint più spesso per contorni
        thickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thickPaint.setStrokeWidth(10);
        thickPaint.setStyle(Paint.Style.STROKE);
        thickPaint.setColor(Color.BLACK);

        //Paint per celle selezionate
        selectedCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedCellPaint.setColor(Color.BLUE);
        selectedCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //Dimensioni della griglia e delle celle
        width = getResources().getDisplayMetrics().widthPixels;  //Dim. assoluta dello schermo
        height = getResources().getDisplayMetrics().heightPixels;
        cellSizePixels = (float) ((width - 2*padding)/ SIZE);

        //GestureDetector
        gestureDetector = new GestureDetector(SudokuView.this.getContext(), new GestureDetectorListener());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Outside Border
        canvas.drawRoundRect(padding,padding, width - padding, width - padding, 20, 20, thickPaint);
        //Cells
        drawLines(canvas);
        fillCells(canvas);
    }

    protected void drawLines(Canvas canvas){

        //Linee Verticali
        for (int i = 0; i < SIZE; i++) {
            //Linea più spessa ogni 3 linee (la prima viene saltata perché c'è già il bordo)
            if (i == 0) continue;
            if (i % SQRT_SIZE == 0) {
                canvas.drawLine( i*cellSizePixels + padding, padding, i*cellSizePixels + padding, width - padding, thickPaint);
            } else {
                canvas.drawLine( i*cellSizePixels + padding, padding, i*cellSizePixels + padding, width - padding, thinPaint);
            }
        }

        //Linee Orizzontali
        for (int i = 0; i < SIZE; i++) {
            //Linea più spessa ogni 3 linee (la prima viene saltata perché c'è già il bordo)
            if (i == 0) continue;
            if (i % SQRT_SIZE == 0) {
                canvas.drawLine( padding, i*cellSizePixels + padding, width - padding, i*cellSizePixels + padding, thickPaint);
            } else {
                canvas.drawLine(padding, i * cellSizePixels + padding, width - padding, i * cellSizePixels + padding, thinPaint);
            }
        }
    }

    //Ad ogni longPress sceglie quale riga e colonna deve essere selezionata
    protected void fillCells(Canvas canvas) {
        if (selectedCol == -1 || selectedRow == -1) return;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (row == selectedRow && col == selectedCol) {
                    fillCell(canvas, row, col, selectedCellPaint);
                } else if (row == selectedRow || col == selectedCol) {
                    fillCell(canvas, row, col, selectedCellPaint);
                }
            }
        }

    }

    protected void fillCell(Canvas canvas, int row, int col, Paint paint) {
        canvas.drawRect(col*cellSizePixels + padding, row*cellSizePixels + padding, (col + 1)*cellSizePixels + padding + 8, (row + 1)*cellSizePixels + padding + 8, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            return gestureDetector.onTouchEvent(event);
        } else return false;
    }

    private class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public void onLongPress(MotionEvent e) {
            selectedRow = (int )((e.getY() - padding)/ cellSizePixels);
            selectedCol = (int )((e.getX() - padding)/ cellSizePixels);
            Log.d(TAG, "onTouchEvent: " + selectedRow);
            Log.d(TAG, "onTouchEvent: " + selectedCol);
            postInvalidate();
            super.onLongPress(e);
        }


    }

}
