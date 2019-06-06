package com.jordanrifaey.galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Missile {
    private Sprite sprite;
    private Body body;
    private String type = "MISSILE";

    public Missile(Texture texture, World world, float getXCenterLocation) {
        sprite = new Sprite(texture, 232, 213, 3, 6);
        sprite.setOrigin(0f,0f);
        sprite.setScale(0.7f);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(sprite.getX(), sprite.getY());
        body = world.createBody(bodyDef);
        body.setUserData(this);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = .1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("bodies/bodies.json"));
        loader.attachFixture(body,"Missile",fixtureDef,sprite.getBoundingRectangle().width);
        body.setTransform(getXCenterLocation - (sprite.getWidth() / 2),10.1f,0f);
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        body.applyForceToCenter(0,10000,true);
    }

    public void update(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setRotation((float)Math.toDegrees((double)body.getAngle()));
    }

}
