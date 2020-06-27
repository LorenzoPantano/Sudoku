package it.daloma.sudoku.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import it.daloma.sudoku.R;
import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;
import it.daloma.sudoku.models.SudokuModel;

public class SudokuBoardView extends View {

    private static final String TAG = "SUDOKUVIEWGRID";

    //Paints
    private Paint thinPaint;
    private Paint thickPaint;
    private Paint selectedRowColPaint;
    private Paint selectedCellPaint;
    private Paint textPaint;
    private Paint textPaintHighlighted;

    //Values
    private static int width;
    private static int height;
    private static final int padding = 64;
    private static final int SIZE = 9;
    private static final int SQRT_SIZE = 3;
    private float cellSizePixels;

    //Model
    private SudokuModel sudokuModel = null;

    public SudokuBoardView(Context context) {
        super(context);
        init(null);
    }

    public SudokuBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SudokuBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SudokuBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        //Paint per righe e colonne selezionate
        selectedRowColPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedRowColPaint.setColor(ContextCompat.getColor(getContext(), R.color.pantone_classic_blue_75opacity));
        selectedRowColPaint.setStyle(Paint.Style.FILL);

        //Paint per LA cella selezionata
        selectedCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedCellPaint.setColor(ContextCompat.getColor(getContext(), R.color.pantone_classic_blue));
        selectedCellPaint.setStyle(Paint.Style.FILL);

        //Paint per numeri non evidenziati
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(50);
        textPaint.setColor(Color.BLACK);

        //Paint per numeri evidenziati
        textPaintHighlighted = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintHighlighted.setTextSize(50);
        textPaintHighlighted.setColor(Color.WHITE);

        //Dimensioni della griglia e delle celle
        width = getResources().getDisplayMetrics().widthPixels;  //Dim. assoluta dello schermo
        height = getResources().getDisplayMetrics().heightPixels;
        cellSizePixels = (float) ((width - 2*padding)/ SIZE);

    }

    public void setSudokuModel(SudokuModel model) {
        this.sudokuModel = model;
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
        if (sudokuModel.isNewGame()) {
            drawInitialBoard(canvas, sudokuModel.getBoard());
        }
        fillCells(canvas, sudokuModel.getSelectedRow(), sudokuModel.getSelectedCol());
        fillCellWithInput(canvas,sudokuModel.getBoard());
    }

    public void cleanCells() {
        invalidate();
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

    public void drawInitialBoard(Canvas canvas, Board board) {
        for (Cell cell: board.getCellList()) {
            int value = cell.getValue();
            String valueText = Integer.toString(value);
            int row = cell.getRow();
            int col = cell.getCol();

            Rect textBounds = new Rect();
            textPaint.getTextBounds(valueText, 0, valueText.length(), textBounds);
            float textWidth = textPaint.measureText(valueText);
            float textHeight = textBounds.height();

            canvas.drawText((value == 0 ? "" : valueText), (col* cellSizePixels + cellSizePixels/2 - textWidth/2 + padding),
                    (row*cellSizePixels + cellSizePixels/2 + thickPaint.getStrokeWidth() + padding), textPaint);
        }
    }

    public void fillCellWithInput(Canvas canvas, Board board) {

        if (sudokuModel.getSelectedCol() == -1 || sudokuModel.getSelectedRow() == -1) return;
        for (Cell cell: board.getCellList()) {
            int value = cell.getValue();
            String valueText = Integer.toString(value);
            int row = cell.getRow();
            int col = cell.getCol();

            Rect textBounds = new Rect();
            textPaint.getTextBounds(valueText, 0, valueText.length(), textBounds);
            float textWidth = textPaint.measureText(valueText);
            float textHeight = textBounds.height();
            boolean selectedCell = (row == sudokuModel.getSelectedRow() && col == sudokuModel.getSelectedCol());
            canvas.drawText((value == 0 ? "" : valueText), (col*cellSizePixels + cellSizePixels/2 - textWidth/2 + padding),
                    (row*cellSizePixels + cellSizePixels/2 + thickPaint.getStrokeWidth() + padding), selectedCell ? textPaintHighlighted : textPaint);
        }
    }

    protected void fillCells(Canvas canvas, int selectedRow, int selectedCol) {
        if (selectedCol == -1 || selectedRow == -1) return;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (row == selectedRow && col == selectedCol) {
                    highlightCell(canvas, row, col, selectedCellPaint);
                } else if (row == selectedRow || col == selectedCol) {
                    fillCell(canvas, row, col, selectedRowColPaint);
                }
            }
        }
    }

    private void highlightCell(Canvas canvas, int row, int col, Paint selectedCellPaint) {
        canvas.drawCircle(col*cellSizePixels + cellSizePixels/2 + padding, row*cellSizePixels +cellSizePixels/2 + padding,
                cellSizePixels/2 + thickPaint.getStrokeWidth(), selectedCellPaint);

    }

    protected void fillCell(Canvas canvas, int row, int col, Paint paint) {
        canvas.drawRect(col*cellSizePixels + padding, row*cellSizePixels + padding,
                (col + 1)*cellSizePixels + padding + thinPaint.getStrokeWidth(), (row + 1)*cellSizePixels + padding + thinPaint.getStrokeWidth(), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int possibleSelectedRow = (int )((event.getY() - padding)/ cellSizePixels);
            int possibleSelectedCol = (int )((event.getX() - padding)/ cellSizePixels);
            sudokuModel.setSelectedRow(possibleSelectedRow);
            sudokuModel.setSelectedCol(possibleSelectedCol);
            Log.d(TAG, "onTouchEvent: SELECTED ROW: " + sudokuModel.getSelectedRow());
            Log.d(TAG, "onTouchEvent: SELECTED COL: " + sudokuModel.getSelectedCol());
            if (possibleSelectedCol != -1 && possibleSelectedRow != -1) {
               invalidate();
            }
            return true;
        }
        else return false;
    }
}
