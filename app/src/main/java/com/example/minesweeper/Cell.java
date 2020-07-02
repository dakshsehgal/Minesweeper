package com.example.minesweeper;

import android.content.Context;

public class Cell extends androidx.appcompat.widget.AppCompatButton {
    private boolean bomb;
    private int neighbors;
    private boolean checkedForNeighbors;


    public Cell(Context context) {
        super(context);
        bomb = false;
        checkedForNeighbors = false;
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public int getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(int neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isCheckedForNeighbors() {
        return checkedForNeighbors;
    }

    public void setCheckedForNeighbors(boolean checkedForNeighbors) {
        this.checkedForNeighbors = checkedForNeighbors;
    }


}
