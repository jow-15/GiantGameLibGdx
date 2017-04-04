package org.escoladeltreball.giant;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by iam07264872
 */
public class Cloud extends Sprite {
    private World world;
    private Body body;
    private String cloudName;

    public Cloud(World world, String cloud/*, float x, float y*/){
        super(new Texture("Clouds/"+cloud+ ".png"));
        this.world = world;
        this.cloudName = cloud;
      //  setPosition(GameInfo.H_WIDTH, GameInfo.H_HEIGHT / 2f - 130);
       // createBody();
    }

    void createBody(){
        BodyDef bodyDef = new BodyDef(); //body definition, /dynamic, static, cinematic
        //static, not affected by any force, it will not move by anything
        //kinematic, not affected by gravity, but affected by other forces
        //dynamic, affected by gravity and other forces
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set((getX() + getWidth()/2f -50 ) / GameInfo.PPM
                ,( getY() +getHeight()/2f) / GameInfo.PPM); //Setted  at the top of this doc

        body = world.createBody(bodyDef); //This will create the body with the body definition
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth()/2 -10) / GameInfo.PPM, (getHeight() / 2) / GameInfo.PPM);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1; //The mass of the player

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(cloudName);
        fixture.setSensor(false); //Permet que el Player l'atravessi


        shape.dispose(); //dispose are in MainMenu

    }
    public void setSpritePosition(float x, float y){
        setPosition(x,y);
        createBody();
    }

    public String getCloudName() {
        return cloudName;
    }
}
