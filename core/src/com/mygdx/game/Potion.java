package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Potion extends GameObject {

    /*
     * This class implements a potion. It can be collected by the player
     */

    public Potion(Vector2 p, Texture i, int oX, int oY) {
        super(p, i, oX, oY);
    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
