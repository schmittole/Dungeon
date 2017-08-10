package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class WayTile extends GameObject {

    /*
     * This class implements a WayTile It is the super class if Blank, Wall and Fire
     */

    public WayTile(Vector2 p, Texture i, int oX, int oY) {
        super(p, i, oX, oY);
    }

    @Override
    public void dispose() {
        img.dispose();
    }

}
