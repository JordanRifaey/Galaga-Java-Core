package com.jordanrifaey.galaga;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;

public class PlayScreen implements Screen {

    private OrthographicCamera cam;
    private ShapeRenderer sr;
    private ExtendViewport viewport;
    private SpriteBatch spriteBatch;
    private Texture texture;
    private long fireNext;
    private World world;
    private Sound intro;
    private Player player;
    private ArrayList<Missile> missiles;
    private Array<Enemy> enemies;
    private Sound fire;
    private Sound gameOverSound;
    private Box2DDebugRenderer debugRenderer;
    private boolean gameOver = false;
    private boolean gameOverSoundPlayed = false;
    private Game game;

    public PlayScreen(Game game) {
        this.game = game;
        sr = new ShapeRenderer();
        cam = new OrthographicCamera();
        viewport = new ExtendViewport(50, 50, cam);
        texture = new Texture("textureTransparent.png");
        world = new World(new Vector2(0f, 0f), true);
        world.setContactListener(new CollisionListener(texture, world));
        player = new Player(texture, world);
        missiles = new ArrayList<Missile>();
        enemies = new Array<Enemy>(5);
        spriteBatch = new SpriteBatch();
        intro = Gdx.audio.newSound(Gdx.files.internal("intro.mp3"));
        fire = Gdx.audio.newSound(Gdx.files.internal("fire.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOver.mp3"));
        debugRenderer = new Box2DDebugRenderer(true, false, false, false, false, false);
        intro.play();
        enemies.add(new Enemy(texture, world));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, cam.combined);
        world.step(1f / 60f, 6, 2);

        if (Gdx.input.isTouched() && (System.currentTimeMillis() > fireNext) && !gameOver) {
            missiles.add(new Missile(texture, world, player.getXCenterLocation()));
            fireNext = System.currentTimeMillis() + 250;
            fire.play();
        }

        spriteBatch.begin();
        player.update(spriteBatch, cam);
        for (int i = 0; i < enemies.size; i++) {
            if (enemies.get(i) != null) {
                enemies.get(i).update(spriteBatch);
                if (!enemies.get(i).alive) {
                    world.destroyBody(enemies.get(i).body);
                    enemies.add(new Enemy(texture, world));
                    if (enemies.size < 5)
                        enemies.add(new Enemy(texture, world));
                    enemies.get(i).alive = true;
                }
            }
        }
        if (!player.alive) {
            gameOver = true;
            if (!gameOverSoundPlayed) {
                gameOverSound.play();
                gameOverSoundPlayed = true;
            }
            game.dispose();
            BitmapFont font = new BitmapFont();
            font.getData().setScale(0.4f);
            font.draw(spriteBatch, "Game Over!", 10, 30);
        }

        for (Missile missile : missiles) {
            missile.update(spriteBatch);
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        spriteBatch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        sr.dispose();
        spriteBatch.dispose();
        texture.dispose();
        world.dispose();
    }
}
