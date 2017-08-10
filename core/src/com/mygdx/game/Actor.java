package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Actor extends GameObject {

    // an offSet for the postion
    protected int offSetX;

    protected int offSetY;

    protected int health = 100;

    public Actor(Vector2 p, Texture i, int offSetX, int offSetY) {
        super(p, i, offSetX, offSetY);
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void dispose() {
        img.dispose();
    }

}
