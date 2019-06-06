package com.jordanrifaey.galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
    public Sprite sprite;
    public Body body;
    private float topSpeed = 25;
    private float accelerationSpeed = 250;
    private String type = "PLAYER";
    public boolean alive = true;

    public Player(Texture texture, World world) {
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("bodies/bodies.json"));

        sprite = new Sprite(texture, 81, 22, 9, 10);
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(playerBodyDef);
        body.setUserData(this);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = .5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;
        //fixtureDef.friction = 0f;
        loader.attachFixture(body, "Player", fixtureDef, sprite.getWidth());
        body.setTransform(new Vector2(30, 0), body.getAngle());
        body.setGravityScale(0f);
        body.applyForceToCenter(50000,0,false);
        sprite.setOrigin(0f,0f);
    }

    public void update(SpriteBatch spriteBatch, OrthographicCamera cam) {
        sprite.draw(spriteBatch);
        if (body.getPosition().x > 0 && (body.getPosition().x + sprite.getWidth()) < cam.viewportWidth) {
            body.applyForceToCenter(Gdx.input.getAccelerometerX() * -accelerationSpeed, 0f, true);
            if (body.getLinearVelocity().x >= topSpeed)
                body.setLinearVelocity(topSpeed, 0);
            if (body.getLinearVelocity().x <= -topSpeed)
                body.setLinearVelocity(-topSpeed, 0);
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x * -1, 0);
        }

        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setRotation((float)Math.toDegrees((double)body.getAngle()));

    }

    public void die() {
        alive = false;
    }

    public float getXCenterLocation() {
        return body.getPosition().x + (sprite.getWidth() / 2);
    }

}
