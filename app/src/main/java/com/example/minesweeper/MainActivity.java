package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static int colNo = 10;
    private static int rowNo = 10;
    private static int bombChance = 3;

    private TableLayout table;
    public Cell[][] cells;
    private Random random;
    private int counter, bombCount;
    private TextView bombs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.cells = new Cell[rowNo][colNo];
        random = new Random();

        bombs = findViewById(R.id.bombCounter);
        table = findViewById(R.id.cellTable);
        Button reset = findViewById(R.id.resetter);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTable();
                bombs.setText("Bombs left: ");
            }
        });

        createTable();
    }

    public void createTable() {
        counter = 0;
        bombCount = 0;

        table.removeAllViews();

        for (int i = 0; i < rowNo; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
            row.setWeightSum(colNo);
            row.setId(i);
            row.setGravity(Gravity.CENTER);

            table.addView(row);

            for (int j = 0; j < colNo; j++) {
                TableRow.LayoutParams trParam = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                trParam.weight = 1;
                Cell cell = new Cell(this);
                cell.setLayoutParams(trParam);
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cell cell = (Cell) v;
                        if (counter == 0) {

                            for (int i = 0; i < rowNo; i++) {
                                for (int j = 0; j < colNo; j++) {
                                    if (random.nextInt(10) < bombChance) {
                                        bombCount++;
                                        cells[i][j].setBomb(true);
                                    }
                                }
                            }
                            if (cell.isBomb()) {
                                bombCount--;
                                cell.setBomb(false);
                            }
                            counter++;
                            initializeNeighbors();
                        }


                        if (cell.isBomb()) {
                            try {

                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Game over!", Toast.LENGTH_LONG).show();
                            createTable();
                        } else {
                            cell.setText("" + cell.getNeighbors());
                            cell.setBackgroundColor(Color.WHITE);
                            cell.setEnabled(false);
                            if (cell.getNeighbors() == 0) {
                                revealZeros(cell);
                            }
                        }
                        if (checkVictory()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "You win!", Toast.LENGTH_LONG).show();
                            createTable();
                        }

                        bombs.setText("Bombs left: " + bombCount);


                    }

                });
                cell.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Cell cell = (Cell) v;

                        if (cell.getText() == "F") {
                            cell.setText("");
                            bombCount++;

                        } else {
                            cell.setText("F");
                            bombCount--;

                        }
                        bombs.setText("Bombs left: " + bombCount);
                        return true;
                    }
                });

                this.cells[i][j] = cell;
                row.addView(cell);
            }
        }
    }

    public int noOfNeighbors(Cell cell) {
        int row = 0, col = 0, sum = 0;

        for (int i = 0; i < rowNo; i++) {
            for (int j = 0; j < colNo; j++) {

                if (cells[i][j].equals(cell)) {
                    col = j;
                    row = i;

                }
            }
        }

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (row == 0 & i == -1 || row == rowNo - 1 & i == 1 || col == 0 & j == -1 || col == colNo - 1 & j == 1) {

                } else if (cells[row + i][col + j].isBomb()) {
                    sum++;
                }
            }
        }

        return sum;
    }

    public boolean checkVictory() {
        int checkSum = 0;
        for (int i = 0; i < rowNo; i++) {
            for (int j = 0; j < colNo; j++) {
                if (!cells[i][j].getText().equals("") || cells[i][j].isBomb()) {
                    checkSum++;
                }
            }
        }

        return checkSum == rowNo * colNo;
    }

    public void initializeNeighbors() {
        for (int i = 0; i < rowNo; i++) {
            for (int j = 0; j < colNo; j++) {
                cells[i][j].setNeighbors(noOfNeighbors(cells[i][j]));
            }
        }
    }

    public void revealZeros(Cell cell) {
        int row = 0;
        int col = 0;
        cell.setCheckedForNeighbors(true);

        for (int i = 0; i < rowNo; i++) {
            for (int j = 0; j < colNo; j++) {
                if (cells[i][j].equals(cell)) {
                    col = j;
                    row = i;

                }
            }
        }

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {

                if (row == 0 & i == -1 || row == rowNo - 1 & i == 1 || col == 0 & j == -1 || col == colNo - 1 & j == 1) {

                } else {
                    if (!cells[row + i][col + j].isCheckedForNeighbors()) {
                        cells[row + i][col + j].setText("" + cells[row + i][col + j].getNeighbors());
                        cells[row + i][col + j].setBackgroundColor(Color.WHITE);
                        cells[row + i][col + j].setEnabled(false);

                        if (cells[row + i][col + j].getNeighbors() == 0) {
                            revealZeros(cells[row + i][col + j]);
                        }
                    }

                }
            }
        }


    }
}