package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {

    /*
     * This is the super class of every game object:
     * 
     * Actor -> Player, Enemy -> Chaser, Estray
     * 
     * WayTile -> Blank, Wall, Fire
     * 
     * Potion
     */

    protected Vector2 pos;

    protected Texture img;

    protected Rectangle rec;

    protected int offSetX;

    protected int offSetY;

    public GameObject(Vector2 p, Texture i, int oX, int oY) {
        offSetX = oX;
        offSetY = oY;
        setPos(p);
        setImg(i);
        rec = new Rectangle();
        rec.setPosition(pos);
        rec.setSize(img.getWidth() - img.getWidth() / 4, img.getHeight() - img.getHeight() / 4);
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Texture getImg() {
        return img;
    }

    public void setImg(Texture img) {
        this.img = img;
    }

    public Rectangle getRec() {
        return rec;
    }

    public void dispose() {
        img.dispose();
    }
}
