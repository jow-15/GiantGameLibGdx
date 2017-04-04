package org.escoladeltreball.giant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by iam07264872
 */
public class MainMenu implements Screen, ContactListener {
    private GameMain game;

    private Texture bg;

    private Player player;

    private World world;

    private Cloud cloud;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;


    public MainMenu(GameMain game) {
        this.game = game;

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM,
                GameInfo.HEIGHT / GameInfo.PPM); //widht and heigt position of the camera
        box2DCamera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f, 0); //x, y ,z

        debugRenderer = new Box2DDebugRenderer();


        world = new World(new Vector2(0, -9.8f), true); //gravity
                                                        // /f - float
        //                                              //true - the bodies are not moving just ignore him, best for the processor

       world.setContactListener(this);
        bg = new Texture("Backgrounds/Game-BG.png");

        player = new Player(world,
                GameInfo.H_WIDTH,
                GameInfo.H_HEIGHT +250);


        cloud = new Cloud(world, "Cloud1");

    }

    void update(float dt){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.getBody().applyLinearImpulse(new Vector2(-0.1f,0),
                    player.getBody().getWorldCenter(), true);
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.getBody().applyForce(new Vector2(+5,0),
                    player.getBody().getWorldCenter(), true);
        }else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.getBody().applyLinearImpulse(new Vector2(+0, 1),
                   player.getBody().getWorldCenter(), true);

            //player.getBody().applyForce(new Vector2(+0, 50),
            //       player.getBody().getWorldCenter(), true);


        }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.getBody().applyLinearImpulse(new Vector2(+0, -1),
                    player.getBody().getWorldCenter(), true);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        player.updatePlayer();

        Gdx.gl.glClearColor(0, 0,1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();



        game.getBatch().draw(bg, 0, 0);
        game.getBatch().draw(player, player.getX(),
                player.getY() - player.getHeight() / 2 );


        game.getBatch().draw(cloud, cloud.getX(), cloud.getY());

        game.getBatch().end();

        debugRenderer.render(world, box2DCamera.combined); //What the camara see -> Prjction MAtrix

        world.step(Gdx.graphics.getDeltaTime(),6,2);

    }

    @Override
    public void resize(int width, int height) {

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
        bg.dispose();
        player.getTexture().dispose();

    }

    @Override
    public void beginContact(Contact contact) {

        Fixture firstBody, secondBody;
        //firstbody player
        //secondbody cloud

        if(contact.getFixtureA().getUserData().equals("Player")){
            firstBody = contact.getFixtureA();
            secondBody = contact.getFixtureB();
        }else{
            firstBody = contact.getFixtureB();
            secondBody = contact.getFixtureA();
        }
        System.out.println("The name of the firs body "+ firstBody.getUserData());
    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("END CONTACT");

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
