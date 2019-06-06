package com.jordanrifaey.galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Enemy {
    private Sprite sprite;
    public Body body;
    private String type = "ENEMY";
    public boolean alive = true;
    private World world;
    private Sound die;

    public Enemy(Texture texture, World world) {
        this.world = world;
        sprite = new Sprite(texture, 60, 84, 8, 8);
        sprite.setOrigin(0f, 0f);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(sprite.getX(), sprite.getY());
        body = world.createBody(bodyDef);
        body.setUserData(this);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = .5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;
        //fixtureDef.friction = 0f;
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("bodies/bodies.json"));
        loader.attachFixture(body, "Enemy", fixtureDef, sprite.getWidth());
        body.setTransform(10f + ((float)Math.random() * 50f), 90f, -4.71239f);
        body.applyForceToCenter(0, -5000f + ((float)Math.random() * -30000f), true);
        die = Gdx.audio.newSound(Gdx.files.internal("kill.mp3"));
    }

    public void update(SpriteBatch spriteBatch) {
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setRotation((float) Math.toDegrees((double) body.getAngle()));
        sprite.draw(spriteBatch);
        if (sprite.getY() < -sprite.getHeight()) {
            destroy();
        }
    }

    public void destroy() {
        alive = false;
        //world.destroyBody(body);
        die.play();
    }

}
