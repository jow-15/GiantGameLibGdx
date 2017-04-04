package org.escoladeltreball.giant;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by iam07264872
 */
public class Player extends Sprite {

    private World world;
    private Body body; //The actual body of the player(body, mess, shape)

    //our custom funcionatilly for our player
    public Player(World world, float x, float y) {
        super(new Texture("Player/Player1_sg.png"));


        //Centra el personaje este
        this.world = world;
        setPosition(x - this.getWidth() / 2,
                y - this.getHeight() / 2);
        createBody();
    }

    void createBody() {
        BodyDef bodyDef = new BodyDef(); //body definition, /dynamic, static, cinematic
        //static, not affected by any force, it will not move by anything
        //kinematic, not affected by gravity, but affected by other forces
        //dynamic, affected by gravity and other forces
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / GameInfo.PPM
                , getY() / GameInfo.PPM); //Setted  at the top of this doc

        body = world.createBody(bodyDef); //This will create the body with the body definition
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2 - 20) / GameInfo.PPM, (getHeight() / 2 -10) / GameInfo.PPM);
        body.setFixedRotation(true);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        //fixtureDef.density = 1; //The mass of the player
        fixtureDef.density = 11f; // mass of the body
        fixtureDef.friction = 0.2f;
        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData("Player");
        fixture.setSensor(false);

        shape.dispose(); //dispose are in MainMenu


    }

    public void updatePlayer() {
        this.setPosition(body.getPosition().x * GameInfo.PPM,
                body.getPosition().y * GameInfo.PPM);
    }

    public Body getBody() {
        return body;
    }


    public void drawPlayer(SpriteBatch batch) {
        batch.draw(this.getTexture(), this.getX() + getWidth() / 2f,
                this.getY() - getHeight() / 2f);

    }

}