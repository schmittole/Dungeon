package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Blank extends WayTile {

    /*
     * A WayTile to walk on (without getting harm)
     */

    public Blank(Vector2 p, Texture i, int oX, int oY) {
        super(p, i, oX, oY);
    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
