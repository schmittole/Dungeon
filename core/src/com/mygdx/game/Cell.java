package com.mygdx.game;

public class Cell implements Comparable<Cell> {

    /*
     * This class is needed for the A*-algorithm
     */

    // x postion
    protected int x;

    // y postion
    protected int y;

    // the three costs needed for the a*-algorithm
    // (total estimated costs)
    protected float fCost;

    // (costs needed till now)
    protected float gCost;

    // (estimated costs needed to target)
    protected float hCost;

    // predecessor for the current shortest path with cell
    protected Cell predecessor;

    public Cell(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
        fCost = 0;
    }

    @Override
    public int compareTo(Cell c) {
        if (fCost > c.fCost) {
            return 1;
        } else if (fCost < c.fCost) {
            return -1;
        }
        return 0;
    }
}
