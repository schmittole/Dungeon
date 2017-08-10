package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Dungeon extends Game {

    public SpriteBatch batch;

    public BitmapFont font;

    public int level = 1;

    // music for the game
    private Music caveMusic;

    // lava sound in background
    private Music lava;

    private String text = "Are you ready to enter the Dungeon? \n\n\n\nW = up\nS = down\nA = left\nD = right\n\nJ = run \n\n\nThere are 5 level\n\n\n\nTap anywhere to begin";

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        setText();
        caveMusic = Gdx.audio.newMusic(Gdx.files.internal("CaveLevel.mp3"));
        lava = Gdx.audio.newMusic(Gdx.files.internal("lava-loop.mp3"));
        if (level == 1) {
            caveMusic.setLooping(true);
            caveMusic.play();
            lava.setLooping(true);
            lava.setVolume(0.4f);
            lava.play();
        }
        setScreen(new MainMenuScreen(this, level, text));
    }

    // set the right text for the current level/situation
    private void setText() {
        if (level == 1) {
            text = "Are you ready to enter the Dungeon? \n\n\n\nW = up\nS = down\nA = left\nD = right\n\nJ = run \n\n\nThere are 5 level\n\n\n\nMouseclick to start\n\n\n\n\n\n\nPress Q to quit";
        } else if (level == 2 || level == 3 || level == 4) {
            text = "You've found your way out of the Dungeon! \n  Can you escape the Dungeon in level " + level
                            + "?\n\n\n\nMouseclick to start\n\n\n\n\n\n\nPress Q to quit";
        } else if (level == 5) {
            text = "You've found your way out of the Dungeon! \n  Can you escape the last Dungeon?\n\n\n\nMouseclick to start\n\n\n\n\n\n\nPress Q to quit";
        } else if (level == 6) {
            text = "You made it! \n    You don't have to meander anymore!  \n\n\n\n Mouseclick or press Q to exit.";
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        caveMusic.dispose();
        lava.dispose();
    }

}
