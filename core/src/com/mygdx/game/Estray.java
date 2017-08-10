package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Estray extends Enemy {

    /*
     * This class implements an enemy which is randomly strolling around
     */

    // counter attributes for the beam function
    private int beamCounter;

    private int beamFrequency;

    private Random rnd = new Random();

    private Sound beam;

    // array for the images
    private Texture[] tArray;

    // counter attributes for the animation function
    private float animationCounter = 0;

    private float animationFrequency = 2;

    public Estray(Vector2 p, Texture i[], int[][] map, Vector2 curIntPos, int offSetX, int offSetY) {
        super(p, i[0], map, curIntPos, offSetX, offSetY);
        speed = 16;
        stepFrequency = 10;
        stepCounter = 0;
        tArray = i;
        beamFrequency = rnd.nextInt(100) + 50;
        beam = Gdx.audio.newSound(Gdx.files.internal("beam.mp3"));
        setIntPos();
    }

    @Override
    public void move() {
        beamCounter++;
        if (beamCounter > beamFrequency) {
            beamCounter = 0;
            beamFrequency = rnd.nextInt(100) + 100;
            beam();
            return;
        }
        boolean moved = false;
        int i = rnd.nextInt(4);
        // randomly chosing a direction to move to. but: the estray HAS to move during one frame
        while (!moved) {
            i = rnd.nextInt(4);
            if (i == 0 && curIntPos.y + 1 < map.length - 1 && map[(int) curIntPos.y + 1][(int) curIntPos.x] == 1) {
                moveUp();
                moved = true;
            } else if (i == 1 && curIntPos.y - 1 > -1 && map[(int) curIntPos.y - 1][(int) curIntPos.x] == 1) {
                moveDown();
                moved = true;
            } else if (i == 2 && curIntPos.x + 1 < map.length - 1 && map[(int) curIntPos.y][(int) curIntPos.x + 1] == 1) {
                moveRight();
                moved = true;
            } else if (i == 3 && curIntPos.x - 1 > -1 && map[(int) curIntPos.y][(int) curIntPos.x - 1] == 1) {
                moveLeft();
                moved = true;
            }
        }
        setIntPos();
    }

    private void beam() {
        // find a blank field on the map and set current psotion to this field
        int x = rnd.nextInt(map.length - 1);
        int y = rnd.nextInt(map.length - 1);
        while (map[y][x] != 1) {
            x = rnd.nextInt(map.length - 1);
            y = rnd.nextInt(map.length - 1);
        }
        pos.x = x * img.getHeight() + offSetX;
        pos.y = y * img.getHeight() + offSetY;
        setIntPos();
        beam.setVolume(beam.play(), 0.2f);
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
        beam.dispose();
    }

    public void act() {
        incStepCounter();
        if (getStepCounter() > getStepFrequency()) {
            move();
            setStepCounter(0);
        }
    }
}
