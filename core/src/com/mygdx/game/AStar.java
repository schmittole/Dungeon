package com.mygdx.game;

import java.util.LinkedList;
import java.util.PriorityQueue;

import com.badlogic.gdx.math.Vector2;

public class AStar {

    /*
     * This class implements an A*-algorithm.
     * 
     * It will find the shortest path from the current enemy position to the current player postion
     * 
     * pseudo code: https://de.wikipedia.org/wiki/A*-Algorithmus
     */
    private PriorityQueue<Cell> open;

    private PriorityQueue<Cell> closed;

    private Cell[][] cellMap;

    private Cell current;

    public AStar(int[][] map) {
        open = new PriorityQueue<Cell>();
        closed = new PriorityQueue<Cell>();
        cellMap = new Cell[map.length][map.length];
        // initialize the cellMap
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
                    cellMap[i][j] = null;
                } else {
                    cellMap[i][j] = new Cell(j, i);
                }
            }
        }
    }

    public LinkedList<Cell> find(Vector2 ePos, Vector2 pPos) {
        // set all costs for the cells
        setAllHCosts(ePos);
        setAllFCost();
        setAllGCost();
        // set fCosts of starting cell
        cellMap[(int) ePos.y][(int) ePos.x].fCost = 0;
        open.add(cellMap[(int) ePos.y][(int) ePos.x]);

        LinkedList<Cell> path = new LinkedList<Cell>();

        while (!open.isEmpty()) {
            current = open.poll();
            // target has been found
            if (current.x == pPos.x && current.y == pPos.y) {
                Cell toGo = cellMap[(int) pPos.y][(int) pPos.x];
                while (toGo != null && (current.x != ePos.x || current.y != ePos.y)) {
                    // add all cells to path
                    path.add(toGo);
                    toGo = toGo.predecessor;
                }
                return path;
            }
            closed.add(current);
            expand(current);
        }
        return path;
    }

    private void setAllHCosts(Vector2 ePos) {
        for (int i = 0; i < cellMap.length; i++) {
            for (int j = 0; j < cellMap.length; j++) {
                if (cellMap[i][j] != null) {
                    // hCost function = distance function
                    cellMap[i][j].hCost = (float) Math.sqrt((j - ePos.x) * (j - ePos.x) + (i - ePos.y) * (i - ePos.y));
                }
            }
        }
    }

    private void expand(Cell c) {
        // check all neighbours
        if (c.y - 1 > -1) {
            check(cellMap[c.y - 1][c.x]);
        }
        if (c.x + 1 < cellMap.length - 1) {
            check(cellMap[c.y][c.x + 1]);
        }
        if (c.y + 1 < cellMap.length - 1) {
            check(cellMap[c.y + 1][c.x]);
        }
        if (c.x - 1 > -1) {
            check(cellMap[c.y][c.x - 1]);
        }
    }

    private void check(Cell successor) {
        if (closed.contains(successor) || successor == null) {
            return;
        }
        float tentative_g = current.gCost + 1;
        if (open.contains(successor) && tentative_g >= successor.gCost) {
            return;
        }
        // set new values: predecessor, gCost, fCost
        successor.predecessor = current;
        successor.gCost = tentative_g;
        float f = tentative_g + successor.hCost;
        successor.fCost = f;
        // update open list
        if (!open.contains(successor)) {
            open.add(successor);
        }
    }

    private void setAllGCost() {
        for (int i = 0; i < cellMap.length; i++) {
            for (int j = 0; j < cellMap.length; j++) {
                if (cellMap[j][i] != null) {
                    cellMap[j][i].gCost = 0;
                }
            }
        }
    }

    private void setAllFCost() {
        for (int i = 0; i < cellMap.length; i++) {
            for (int j = 0; j < cellMap.length; j++) {
                if (cellMap[j][i] != null) {
                    cellMap[j][i].fCost = 0;
                }
            }
        }
    }
}
