package com.mygdx.game;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Chaser extends Enemy {

    /*
     * This class implements an enemy which is capable to detect the curretn postion of the player and to chase him down
     */

    // instance to compute an A* algorithm
    private AStar star;

    // the images
    private Texture[] tArray = new Texture[4];

    // timer parameters to change the current image
    private float animationCounter = 0;

    private float animationFrequency = 2;

    private Random rnd = new Random();

    public Chaser(Vector2 p, Texture i[], int[][] map, Vector2 curIntPos, int offSetX, int offSetY) {
        super(p, i[0], map, curIntPos, offSetX, offSetY);
        setIntPos();
        speed = i[0].getHeight();
        stepFrequency = (int) (15 + Math.random() * 20);
        stepCounter = 0;
        star = new AStar(map);
        tArray = i;
        animationFrequency = (float) (1 + 1.5 * Math.random());
    }

    // this method is called every fram
    public void act(Vector2 pCurIntPos) {
        incStepCounter();
        if (getStepCounter() > getStepFrequency()) {
            move(pCurIntPos);
            setStepCounter(0);
        }
    }

    // with help of the a* this function will move the chaser to the next closer cell to the player
    @Override
    public LinkedList<Cell> move(Vector2 pPos) {
        star = new AStar(map);
        setIntPos();
        LinkedList<Cell> path = star.find(curIntPos, pPos);
        if (path.size() > 1) {
            goTo(path.get(path.size() - 2));
        }
        return path;
    }

    private void goTo(Cell cell) {
        if (cell.x > curIntPos.x) {
            moveRight();
        } else if (cell.x < curIntPos.x) {
            moveLeft();
        } else if (cell.y > curIntPos.y) {
            moveUp();
        } else if (cell.y < curIntPos.y) {
            moveDown();
        }
        setIntPos();
    }

    public void setCurrentImg() {
        animationCounter++;
        if (animationCounter * Gdx.graphics.getDeltaTime() > animationFrequency) {
            animationFrequency = (float) (0.2 + 1.5 * Math.random());
            int i = rnd.nextInt(tArray.length);
            while (img == tArray[i]) {
                i = rnd.nextInt(tArray.length);
            }
            img = tArray[i];
            animationCounter = 0;
        }
    }

    @Override
    public void dispose() {
        img.dispose();
        for (Texture t : tArray) {
            t.dispose();
        }
    }
}
