package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {

    /*
     * This class implements the game mechanics
     */

    private Random rnd = new Random();

    // instance of the game
    private Dungeon game;

    // attributes for the clipping of the size of the playing field
    private static final int MIN_SIZE = 3;

    private static final int MAX_SIZE = 15;

    // texture for blank waytile
    private Texture blank;

    // texture for wall waytile
    private Texture wall;

    // texture for fire
    private Texture fire1;

    // texture for fire
    private Texture fire2;

    // texture for fire
    private Texture fire3;

    // texture for fire
    private Texture fire4;

    // texture for potion
    private Texture potion;

    // texture for chaser
    private Texture blob1;

    // texture for chaser
    private Texture blob2;

    // texture for chaser
    private Texture blob3;

    // texture for chaser
    private Texture blob4;

    // texture for estray
    private Texture gnasher1;

    // texture for estray
    private Texture gnasher2;

    // texture array for chaser
    private Texture[] chaserImgArray;

    // texture array for estray
    private Texture[] gnasherImgArray;

    // texture array for fire
    private Texture[] fireImgArray;

    // map consisting of: 0 = wall, 1 = blank, -1 = fire, -2 = potion
    private int[][] mMapAbs;

    // possible offSet for the graphics
    private int offSetX = 0;

    private int offSetY = 0;

    private Array<GameObject> myObjects;

    private Array<Fire> fireList;

    private Array<Potion> potionList;

    private Array<Chaser> chasers;

    private Array<Estray> estrays;

    private Array<Wall> walls;

    private Array<Texture> textures;

    private Player player;

    private Vector3 color = new Vector3(0, 0, 0);

    // level
    private int level;

    // size of the current level
    private int size;

    // amount of random blanks to add to the map
    private int randomBlanks;

    // amount of fires to add to the map
    private int fires;

    // amount of potions to add to the map
    private int potions;

    // amount of chasers to add to the map
    private int chaserNr;

    // amount of estrays to add to the map
    private int estrayNr;

    // sound played if potion gets collected
    private Sound bell;

    // camera
    private OrthographicCamera camera;

    // exit out of the dungeon
    private Vector2 exit;

    private int width = 1200;

    private int height = 800;

    public GameScreen(Dungeon game, int level) {

        this.level = level;
        size = level * 3;
        chaserNr = size / 3;
        estrayNr = size / 3;
        randomBlanks = size * size + 3;
        fires = size * 4;
        potions = rnd.nextInt(size / 3) + 1;

        this.game = game;

        addTextures();

        exit = new Vector2(blank.getWidth() * size * 3 + offSetX, blank.getHeight() * size * 3 - 1 + offSetY);

        bell = Gdx.audio.newSound(Gdx.files.internal("bell.mp3"));
        // initialize the int map (consisting of 1=blank and 0=wall)
        mMapAbs = initMap(createLabyrinth(size));
        // add random blanks to transform the simple straight maze into a more appealing dungeon
        // (this causes possible randomly generated rooms, circles and
        // additional paths out of the dungeon)
        addRandomBlanks();
        // check for solo blanks and fill them up with wall
        // (this is because of the move function of the estray.
        // the estray HAS to make a step. if this is not possible (there is no blank to move to), there will be an
        // endless loop in the move function)
        checkForSoloBlanks();
        // add fire to the int map (-1=fire)
        addFire();
        // add fire to the int map (-2=potion)
        addPotions();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        myObjects = new Array<GameObject>();
        walls = new Array<Wall>();
        fireList = new Array<Fire>();
        potionList = new Array<Potion>();
        chasers = new Array<Chaser>();
        estrays = new Array<Estray>();

        buildLabyrinth();

        addActors();

    }

    private void addTextures() {
        textures = new Array<Texture>();
        blank = new Texture("blank.png");
        textures.add(blank);
        wall = new Texture("wall.png");
        textures.add(wall);
        blob1 = new Texture("blob_1.png");
        textures.add(blob1);
        blob2 = new Texture("blob_2.png");
        textures.add(blob2);
        blob3 = new Texture("blob_3.png");
        textures.add(blob3);
        blob4 = new Texture("blob_4.png");
        textures.add(blob4);
        fire1 = new Texture("black_hole_1.png");
        textures.add(fire1);
        fire2 = new Texture("black_hole_2.png");
        textures.add(fire2);
        fire3 = new Texture("black_hole_3.png");
        textures.add(fire3);
        fire4 = new Texture("black_hole_4.png");
        textures.add(fire4);
        potion = new Texture("block_timer_0.png");
        textures.add(potion);
        gnasher1 = new Texture("gnasher_1.png");
        textures.add(gnasher1);
        gnasher2 = new Texture("gnasher_2.png");
        textures.add(gnasher2);

        fireImgArray = new Texture[] {fire1, fire2, fire3, fire4};
        chaserImgArray = new Texture[] {blob1, blob2, blob3, blob4};
        gnasherImgArray = new Texture[] {gnasher1, gnasher2};
    }

    private void addActors() {
        player = new Player(new Vector2(offSetX + blank.getWidth(), offSetY), new Texture("kye.png"), mMapAbs, offSetX,
                        offSetY);
        myObjects.add(player);
        addChaser(chaserNr);
        myObjects.addAll(chasers);
        addEstray(estrayNr);
        myObjects.addAll(estrays);
    }

    private void buildLabyrinth() {
        // instantiate map objects according to the int map
        for (int i = 0; i < mMapAbs.length; i++) {
            for (int j = 0; j < mMapAbs.length; j++) {
                Vector2 vec = new Vector2(j * blank.getWidth(), i * blank.getHeight());
                if (mMapAbs[i][j] == 1) {
                    myObjects.add(new Blank(new Vector2(vec.x + offSetX, vec.y + offSetY), blank, offSetX, offSetY));
                } else if (mMapAbs[i][j] == 0) {
                    Wall w = new Wall(new Vector2(vec.x + offSetX, vec.y + offSetY), wall, offSetX, offSetY);
                    myObjects.add(w);
                    walls.add(w);
                } else if (mMapAbs[i][j] == -1) {
                    Fire f = new Fire(new Vector2(vec.x + offSetX, vec.y + offSetY), fireImgArray, offSetX, offSetY);
                    myObjects.add(f);
                    fireList.add(f);
                } else if (mMapAbs[i][j] == -2) {
                    Potion p = new Potion(new Vector2(vec.x + offSetX, vec.y + offSetY), potion, offSetX, offSetY);
                    myObjects.add(p);
                    potionList.add(p);
                }
            }
        }
    }

    // delete single blank waytiles
    private void checkForSoloBlanks() {
        for (int y = 1; y < mMapAbs.length - 1; y++) {
            for (int x = 1; x < mMapAbs.length - 1; x++) {
                if (mMapAbs[y][x] == 1 && mMapAbs[y][x + 1] != 1 && mMapAbs[y][x - 1] != 1 && mMapAbs[y - 1][x] != 1
                                && mMapAbs[y + 1][x] != 1) {
                    mMapAbs[y][x] = 0;
                }
            }
        }
    }

    // find a blank waytile and put a potion on it
    private void addPotions() {
        for (int i = 0; i < potions; i++) {
            int x = rnd.nextInt(mMapAbs.length);
            int y = rnd.nextInt(mMapAbs.length);
            while (x == 0 || x == mMapAbs.length - 1 || y == 0 || y == mMapAbs.length - 1 || mMapAbs[x][y] != 0) {
                x = rnd.nextInt(mMapAbs.length);
                y = rnd.nextInt(mMapAbs.length);
            }
            mMapAbs[x][y] = -2;
        }
    }

    // add fire to randomly chosen wall waytile (the edge of the labyrinth is excluded)
    private void addFire() {
        for (int i = 0; i < fires; i++) {
            int x = rnd.nextInt(mMapAbs.length);
            int y = rnd.nextInt(mMapAbs.length);
            while (x == 0 || x == mMapAbs.length - 1) {
                x = rnd.nextInt(mMapAbs.length);
            }
            while (y == 0 || y == mMapAbs.length - 1) {
                y = rnd.nextInt(mMapAbs.length);
            }
            if (mMapAbs[x][y] == 0) {
                mMapAbs[x][y] = -1;
            } else {
                continue;
            }
        }
    }

    // find a blank waytile and instantiate a chaser on it
    private void addChaser(int nr) {
        for (int i = 0; i < nr; i++) {
            int x = rnd.nextInt(mMapAbs.length - 1);
            int y = rnd.nextInt(mMapAbs.length - 1);
            while (mMapAbs[y][x] != 1) {
                x = rnd.nextInt(mMapAbs.length - 1);
                y = rnd.nextInt(mMapAbs.length - 1);
            }
            chasers.add(new Chaser(new Vector2(x * blob1.getHeight() + offSetX, y * blob1.getHeight() + offSetY),
                            chaserImgArray, mMapAbs, new Vector2(x, y), offSetX, offSetY));
        }

    }

    // find a blank waytile and instantiate an estray on it
    private void addEstray(int nr) {
        for (int i = 0; i < nr; i++) {
            int x = rnd.nextInt(mMapAbs.length - 1);
            int y = rnd.nextInt(mMapAbs.length - 1);
            while (mMapAbs[y][x] != 1) {
                x = rnd.nextInt(mMapAbs.length - 1);
                y = rnd.nextInt(mMapAbs.length - 1);
            }
            estrays.add(new Estray(new Vector2(x * gnasher1.getHeight() + offSetX, y * gnasher1.getHeight() + offSetY),
                            gnasherImgArray, mMapAbs, new Vector2(x, y), offSetX, offSetY));
        }
    }

    // add random blanks where no blanks are yet
    private void addRandomBlanks() {
        for (int i = 0; i < randomBlanks; i++) {
            int x = rnd.nextInt(mMapAbs.length);
            int y = rnd.nextInt(mMapAbs.length);
            while (x == 0 || x == mMapAbs.length - 1) {
                x = rnd.nextInt(mMapAbs.length);
            }
            while (y == 0 || y == mMapAbs.length - 1) {
                y = rnd.nextInt(mMapAbs.length);
            }
            if (mMapAbs[x][y] == 0) {
                mMapAbs[x][y] = 1;
            } else {
                continue;
            }
        }

    }

    // this method uses the class graph to create a labyrinth
    public int[][] createLabyrinth(int size) {
        if (size < GameScreen.MIN_SIZE || size > GameScreen.MAX_SIZE || size % 3 != 0) {
            throw new IllegalArgumentException();
        }
        Graph g = new Graph(size * size);
        // create graph with randomly chosen values for the edges
        for (int i = 0; i < size * size - size; i++) {
            if (i % size != size - 1) {
                g.insertEdge(i, i + 1, (int) (Math.random() * 10) + 1);
            }
            g.insertEdge(i, i + size, ((int) (Math.random() * 10)) + 1);
        }
        // create minimumSpanningTree with randomly with randomly chosen startNode
        int[][] l = g.createMinimumSpanningTree((int) (Math.random() * size)).getAdjacencyMatrix();
        int[][] laby = new int[size][size];
        for (int i = 0; i < l.length - size; i++) {
            if (l[i][i + 1] != -1) {
                // there is an exit to the left field of this field
                laby[i / size][i % size] += 2;
                // there is an exit to the right field of this field
                laby[i / size][i % size + 1] += 8;
            }
            if (l[i + size][i] != -1) {
                // there is an exit to the lower field of this field
                laby[i / size][i % size] += 4;
                // there is an exit to the upper field of this field
                laby[i / size + 1][i % size] += 1;
            }
        }
        return laby;
    }

    private int[][] initMap(int[][] a) {
        int[][] aAbs = new int[a.length * 3][a[0].length * 3];
        // set all fields in the int map to 0 (=wall)
        for (int i = 0; i < aAbs.length; i++) {
            for (int j = 0; j < aAbs[0].length; j++) {
                aAbs[i][j] = 0;
            }
        }

        // set a blank (0) or a wall (1) according to the fields of the map created by the minimum spanning tree
        // coding of the numbers from the mimum spanning tree:
        // 1 -> exit to the top
        // 8 -> exit to the right
        // 4 -> exit to the bottom
        // 2 -> exit to the left
        //
        // sum of these figures will give the right waytile decsion
        //
        // example:
        // if there is a 13 in the a array it will be mapped to:
        // 0 1 0
        // 0 1 1
        // 0 1 0
        // there are exits to the top, the bottom and to the right
        //
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                for (int p = i * 3; p < i * 3 + 3; p++) {
                    for (int q = j * 3; q < j * 3 + 3; q++) {
                        // set 1 (all!)
                        if (a[j][i] % 2 == 1 && p % 3 == 1 && q % 3 == 0) {
                            aAbs[p][q] = 1;
                        }
                        // set 2
                        if (a[j][i] == 2 && p % 3 == 2 && q % 3 == 1) {
                            aAbs[p][q] = 1;
                        }
                        // set 3
                        if (a[j][i] == 3 && p % 3 == 2 && q % 3 == 1) {
                            aAbs[p][q] = 1;
                        }
                        // set 4 and 5. mention set 1!
                        if ((a[j][i] == 4 || a[j][i] == 5) && p % 3 == 1 && q % 3 == 2) {
                            aAbs[p][q] = 1;
                        }
                        // set 6
                        if (a[j][i] == 6) {
                            if (p % 3 == 2 && q % 3 == 1) {
                                aAbs[p][q] = 1;
                            }
                            if (p % 3 == 1 && q % 3 == 2) {
                                aAbs[p][q] = 1;
                            }
                        }
                        // set 7
                        if (a[j][i] == 7) {
                            if (p % 3 == 2 && q % 3 == 1) {
                                aAbs[p][q] = 1;
                            }
                            if (p % 3 == 1 && q % 3 == 2) {
                                aAbs[p][q] = 1;
                            }
                            if (p % 3 == 1 && q % 3 == 0) {
                                aAbs[p][q] = 1;
                            }
                        }
                        // set 8 and 9. mention set 1!
                        if ((a[j][i] == 8 || a[j][i] == 9) && p % 3 == 0 && q % 3 == 1) {
                            aAbs[p][q] = 1;
                        }
                        // set 10 and 11. mention 1!
                        if (a[j][i] == 10 || a[j][i] == 11) {
                            if (p % 3 == 2 && q % 3 == 1) {
                                aAbs[p][q] = 1;
                            }
                            if (p % 3 == 0 && q % 3 == 1) {
                                aAbs[p][q] = 1;
                            }
                        }
                        // set 12 and 13. mention 1!
                        if (a[j][i] == 12 || a[j][i] == 13) {
                            if (p % 3 == 0 && q % 3 == 1) {
                                aAbs[p][q] = 1;
                            }
                            if (p % 3 == 1 && q % 3 == 2) {
                                aAbs[p][q] = 1;
                            }
                        }
                        // set 14 and 15. mention 1!
                        if (a[j][i] == 14 || a[j][i] == 15) {
                            if (p % 3 == 2 && q % 3 == 1) {
                                aAbs[p][q] = 1;
                            }
                            if (p % 3 == 1 && q % 3 == 2) {
                                aAbs[p][q] = 1;
                            }
                            if (p % 3 == 0 && q % 3 == 1) {
                                aAbs[p][q] = 1;
                            }
                        }
                        if (a[j][i] != 0 && p % 3 == 1 && q % 3 == 1) {
                            aAbs[p][q] = 1;
                        }
                    }
                }
            }
        }
        // add entry
        aAbs[0][1] = 1;
        // add exit
        aAbs[aAbs.length - 2][aAbs.length - 1] = 1;
        return aAbs;
    }

    // main loop of the game
    @Override
    public void render(float deltaTime) {

        if (!player.alive) {
            game.level = 1;
            game.setScreen(new MainMenuScreen(game, 1,
                            "You died! \n\n\n\nMouseclick to restart.\n\n\nOr push Q to exit."));
        }

        Gdx.gl.glClearColor(color.x, color.y, color.z, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        // draw all gameobjects
        // update the gameobjects (move enemies, check if player overlaps an enemy)
        for (GameObject o : myObjects) {
            game.batch.draw(o.getImg(), o.getPos().x, o.getPos().y);
            if (o instanceof Enemy) {
                if (player.overlaps((Enemy) o)) {
                    player.getEnemyDamage();
                }
            }
            if (o instanceof Chaser) {
                ((Chaser) o).act(player.curIntPos);
                ((Chaser) o).setCurrentImg();
            }
            if (o instanceof Estray) {
                ((Estray) o).act();
                ((Estray) o).setCurrentImg();
            }
        }
        game.font.setColor(0, 0.9f, 0.2f, 0.8f);
        game.font.draw(game.batch, "HEALTH: " + player.getHealth(), offSetX,
                        offSetY + (mMapAbs.length + 1) * blank.getHeight());
        game.font.setColor(0.1f, 0.7f, 0.6f, 0.9f);
        game.font.draw(game.batch, "LEVEL: " + level, offSetX + blank.getHeight() * 6, offSetY + (mMapAbs.length + 1)
                        * blank.getHeight());
        game.batch.end();

        // is the player running?
        if (Gdx.input.isKeyPressed(Keys.J)) {
            player.setRun(true);
        } else {
            player.setRun(false);
        }

        // move player up
        if (Gdx.input.isKeyPressed(Keys.W)) {
            Vector2 posNow = new Vector2(player.getPos().x, player.getPos().y);
            player.moveUp();
            if (playerInWall()) {
                player.setPos(posNow);
            }
        }

        // move player down
        if (Gdx.input.isKeyPressed(Keys.S) && player.getPos().y > offSetY) {
            Vector2 posNow = new Vector2(player.getPos().x, player.getPos().y);
            player.moveDown();
            if (playerInWall()) {
                player.setPos(posNow);
            }
        }

        // move player left
        if (Gdx.input.isKeyPressed(Keys.A) && player.getPos().x > offSetX) {
            Vector2 posNow = new Vector2(player.getPos().x, player.getPos().y);
            player.moveLeft();
            if (playerInWall()) {
                player.setPos(posNow);
            }
        }

        // move player right
        if (Gdx.input.isKeyPressed(Keys.D)) {
            Vector2 posNow = new Vector2(player.getPos().x, player.getPos().y);
            player.moveRight();
            if (playerInWall()) {
                player.setPos(posNow);
            }
        }

        // check if player is on fire
        if (playerInFire()) {
            player.getFireDamage();
        }
        // check if player is collecting a potion
        if (playerInPotion()) {
            player.getPotion();
            bell.setVolume(bell.play(), 0.1f);
        }
        // check if player escaped the dungeon
        if (player.getPos().x > exit.x) {
            game.level += 1;
            game.create();
        }
    }

    // give a potion to the player
    // fill potion space with blank waytile on the map
    private boolean playerInPotion() {
        for (Potion p : potionList) {
            if (p.rec.overlaps(player.rec)) {
                // add Blank with potion's position to laby
                myObjects.add(new Blank(p.getPos(), blank, offSetX, offSetY));
                // remove player from list
                myObjects.removeValue(player, true);
                // remove potion from list
                potionList.removeValue(p, true);
                // add player to list -> player will be drawn at the very end
                myObjects.add(player);
                return true;
            }
        }
        return false;
    }

    // check if player is on fire
    private boolean playerInFire() {
        for (Fire f : fireList) {
            f.setCurrentImg();
            if (f.getRec().overlaps(player.getRec())) {
                return true;
            }
        }
        return false;
    }

    // check if players position is overlapping a wall
    private boolean playerInWall() {
        for (Wall w : walls) {
            if (w.getRec().overlaps(player.getRec())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispose() {

        bell.dispose();
        for (GameObject o : myObjects) {
            o.dispose();
        }
        for (Texture t : textures) {
            t.dispose();
        }
        game.dispose();
    }

    @Override
    public void show() {
        // TODO Autogenerierte Methode

    }

    @Override
    public void resize(int width, int height) {
        // TODO Autogenerierte Methode

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // TODO Autogenerierte Methode

    }

}
