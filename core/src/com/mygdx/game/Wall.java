package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Wall extends WayTile {

    /*
     * This class implements a Wall
     */

    public Wall(Vector2 p, Texture i, int oX, int oY) {
        super(p, i, oX, oY);
    }

    @Override
    public void dispose() {
        img.dispose();
    }

}
