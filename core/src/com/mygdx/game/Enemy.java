package com.mygdx.game;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Actor {

    // the map to move on. consisting of 0 == wall and 1 == blank
    protected int[][] map;

    // the current int position relating to the map
    protected Vector2 curIntPos;

    protected int speed;

    protected int stepFrequency;

    protected int stepCounter;

    public Enemy(Vector2 p, Texture i, int[][] map, Vector2 curIntPos, int offSetX, int offSetY) {
        super(p, i, offSetX, offSetY);
        this.map = map;
        this.curIntPos = curIntPos;
        setIntPos();
    }

    protected void moveDown() {
        pos.y -= speed;
        rec.setPosition(pos);
    }

    protected void moveUp() {
        pos.y += speed;
        rec.setPosition(pos);
    }

    protected void moveRight() {
        pos.x += speed;
        rec.setPosition(pos);
    }

    protected void moveLeft() {
        pos.x -= speed;
        rec.setPosition(pos);
    }

    // sets the current int postion (curIntPos) according to the current vector position
    protected void setIntPos() {
        curIntPos.x = (int) (((pos.x - offSetX)) / img.getWidth());
        curIntPos.y = (int) (((pos.y - offSetY)) / img.getHeight());
    }

    public void move() {

    }

    public void incStepCounter() {
        stepCounter++;
    }

    public int getStepFrequency() {
        return stepFrequency;
    }

    public int getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(int i) {
        stepCounter = i;
    }

    public LinkedList<Cell> move(Vector2 pPos) {
        // TODO Autogenerierte Methode
        return null;
    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
