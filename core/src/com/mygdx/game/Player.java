package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player extends Actor {

    /*
     * This class implements the Player
     */

    // speed to move with
    private int stepSize = 50;

    // current int position of the player (important for the chaser (enemy))
    protected Vector2 curIntPos;

    // counter attributes for the fire damage -> time has to pass between two fire damages
    private int fireDamageFrequency = 20;

    private int fireDamageCounter = fireDamageFrequency + 1;

    // counter attributes for the enemy damage -> time has to pass between two enemy damages
    private int enemyDamageFrequency = 20;

    private int enemyDamageCounter = enemyDamageFrequency + 1;

    // life points added while collecting a potion
    private int potionLife = 10;

    // counter attributes for choosing the right step sound
    private int stepSoundCounter;

    private int stepSoundFrequency = 10;

    // step sounds
    private Sound step1;

    private Sound step2;

    private Sound step;

    // sound played if player gets harmed
    private Sound harm;

    protected boolean alive;

    private int fastStep = 85;

    private int slowStep = 50;

    public Player(Vector2 p, Texture i, int[][] map, int offSetX, int offSetY) {
        super(p, i, offSetX, offSetY);
        this.offSetX = offSetX;
        this.offSetY = offSetY;
        curIntPos = new Vector2();
        harm = Gdx.audio.newSound(Gdx.files.internal("breathing.mp3"));
        step1 = Gdx.audio.newSound(Gdx.files.internal("step1.mp3"));
        step2 = Gdx.audio.newSound(Gdx.files.internal("step2.mp3"));
        step = step1;
        alive = true;
        setIntPos();
    }

    // set speed whether player is running
    public void setRun(boolean run) {
        if (run) {
            stepSize = fastStep;
        } else {
            stepSize = slowStep;
        }
    }

    public void moveUp() {
        pos.y += stepSize * Gdx.graphics.getDeltaTime();
        rec.setPosition(pos);
        setIntPos();
    }

    public void moveDown() {
        pos.y -= stepSize * Gdx.graphics.getDeltaTime();
        rec.setPosition(pos);
        setIntPos();
    }

    public void moveLeft() {
        pos.x -= stepSize * Gdx.graphics.getDeltaTime();
        rec.setPosition(pos);
        setIntPos();
    }

    public void moveRight() {
        pos.x += stepSize * Gdx.graphics.getDeltaTime();
        rec.setPosition(pos);
        setIntPos();
    }

    private void setIntPos() {
        curIntPos.x = (int) (pos.x - offSetX) / img.getWidth();
        curIntPos.y = (int) ((pos.y - offSetY) / img.getHeight());
        stepSoundCounter++;
        if (stepSoundCounter > stepSoundFrequency) {
            step.play();
            if (step == step1) {
                step = step2;
            } else {
                step = step1;
            }
            stepSoundCounter = 0;
        }
    }

    public void getFireDamage() {
        fireDamageCounter++;
        if (fireDamageCounter > fireDamageFrequency) {
            health -= 10;
            fireDamageCounter = 0;
            if (health < 0) {
                health = 0;
            }
            harm.play();
        }
        if (health == 0) {
            alive = false;
        }
    }

    public boolean overlaps(Enemy e) {
        return rec.overlaps(e.getRec());
    }

    public void getPotion() {
        health += potionLife;
    }

    public void getEnemyDamage() {
        enemyDamageCounter++;
        if (enemyDamageCounter > enemyDamageFrequency) {
            health -= 10;
            enemyDamageCounter = 0;
            if (health < 0) {
                health = 0;
            }
            harm.play();
        }
        if (health == 0) {
            alive = false;
        }
    }

    @Override
    public void dispose() {
        img.dispose();
        harm.dispose();
        step.dispose();
        step1.dispose();
        step2.dispose();
    }

}
