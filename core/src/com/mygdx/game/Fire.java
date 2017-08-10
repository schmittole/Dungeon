package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Fire extends WayTile {

    /*
     * This class implements a fire on the playing field
     */

    // texture array for the animation
    private Texture[] tArray = new Texture[4];

    // counter attributes for the animation
    private float animationCounter = 0;

    private float animationFrequency = 2;

    private Random rnd = new Random();

    public Fire(Vector2 p, Texture[] i, int oX, int oY) {
        super(p, i[0], oX, oY);
        tArray = i;
        animationFrequency = (float) (1 + 1.5 * Math.random());
    }

    public void setCurrentImg() {
        animationCounter++;
        if (animationCounter * Gdx.graphics.getDeltaTime() > animationFrequency) {
            animationFrequency = (float) (0.2 + 1.5 * Math.random());
            int i = rnd.nextInt(4);
            while (img == tArray[i]) {
                i = rnd.nextInt(4);
            }
            img = tArray[i];
            animationCounter = 0;
        }
    }

    @Override
    public void dispose() {
        for (Texture t : tArray) {
            t.dispose();
        }
    }
}
