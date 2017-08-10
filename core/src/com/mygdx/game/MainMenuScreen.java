package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {

    /*
     * This class implements a Main menu screen
     * 
     * It only shows a background color and text
     */

    private final Dungeon game;

    private OrthographicCamera camera;

    public int level;

    private String text;

    public MainMenuScreen(final Dungeon gam, int l, String t) {
        game = gam;
        level = l;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
        text = t;
    }

    @Override
    public void show() {
        // TODO Autogenerierte Methode

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // game.font.setColor(1, 0, 0, 1);
        game.font.setColor(0.1f, 0.7f, 0.6f, 0.9f);
        game.font.draw(game.batch, text, 500, 600);
        game.batch.end();

        // if the game isn't won yet (level <= 5) -> start new level
        if (Gdx.input.isTouched() && level < 6 && level > 0) {
            game.setScreen(new GameScreen(game, level));

        }
        // if the game is won or player died and you want to quit -> quit application
        if (Gdx.input.isTouched() && level > 5 || Gdx.input.isKeyPressed(Keys.Q)) {
            dispose();
            System.exit(-1);
        }
        // if player died and you want to try again -> restart level 1
        if (Gdx.input.isTouched() && level == -1) {
            level = 1;
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Autogenerierte Methode

    }

    @Override
    public void pause() {
        // TODO Autogenerierte Methode

    }

    @Override
    public void resume() {
        // TODO Autogenerierte Methode

    }

    @Override
    public void hide() {
        // TODO Autogenerierte Methode

    }

    @Override
    public void dispose() {
        // TODO Autogenerierte Methode
    }

}
