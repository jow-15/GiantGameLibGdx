package org.escoladeltreball.giant;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by iam07264872
 */
public class GamePlay implements Screen, ContactListener {


    private GameMain game;

    private SpriteBatch batch;
    private Player player;

    private OrthographicCamera mainCamera;
    private Viewport gameViewPort;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private boolean doubleJump;

   private CloudController cloudController;

    private Sprite[] bgs;

    private float lastYPosition; //ofTheBackgrounds

    private int puntuacion;


    public GamePlay(GameMain game) {
        this.game = game;
        this.batch=game.getBatch();

        this.mainCamera = (new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT));
        mainCamera.position.set(GameInfo.H_WIDTH, GameInfo.H_HEIGHT, 0);

        gameViewPort = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);



        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM,
                GameInfo.HEIGHT / GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.H_HEIGHT, 0);


        debugRenderer = new Box2DDebugRenderer();


        world = new World(new Vector2(0, GameInfo.GRAVITY), true);
        this.world.setContactListener(this);

        this.puntuacion = 0;

        cloudController = new CloudController(world);
        player = cloudController.positionThePlayer(player);
        createBackgrounds();

    }

    void createBackgrounds(){
        bgs = new Sprite[3];

        for (int i = 0; i < bgs.length; i++) {
            bgs[i] = new Sprite(new Texture("Backgrounds/Game-BG.png"));
            bgs[i].setPosition(0, -(i * bgs[i].getHeight()));
            lastYPosition = Math.abs(bgs[i].getY());
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();


        drawBackgrounds();

        cloudController.drawClouds(batch);

        player.drawPlayer(batch);

        //bordes

        if (player.getBody().getPosition().x * GameInfo.PPM > GameInfo.WIDTH
                || player.getBody().getPosition().x*GameInfo.PPM < 0 - 20){
            gameover();
        }


        game.getBatch().end();

        //Cuadrado dibujado
       // debugRenderer.render(world, box2DCamera.combined);

        game.getBatch().setProjectionMatrix(mainCamera.combined);

        mainCamera.update();


        player.updatePlayer();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    private void update(float delta) {
        moveCamera();
        checkBackgroundsOutOfBounds();
        cloudController.setCameraY(mainCamera.position.y);
        cloudController.createAndArrangeNewClouds();

        playerControlls();

    }
    private void playerControlls() {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.getBody().applyLinearImpulse(new Vector2(-0.7f, 0),
                    player.getBody().getWorldCenter(), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                if(doubleJump){
                    player.getBody().applyLinearImpulse(new Vector2(0, 12f),
                            player.getBody().getWorldCenter(), true);
                    doubleJump = false;
                }

            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.getBody().applyLinearImpulse(new Vector2(0.7f, 0),
                    player.getBody().getWorldCenter(), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                if(doubleJump){
                    player.getBody().applyLinearImpulse(new Vector2(0, 12f),
                            player.getBody().getWorldCenter(), true);
                    doubleJump = false;
                }

            }


        }else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if(doubleJump){
                player.getBody().applyLinearImpulse(new Vector2(0, 12f),
                        player.getBody().getWorldCenter(), true);
                doubleJump = false;
            }

        }
    }

    private void moveCamera() {
        mainCamera.position.y -=2;
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

    }

    private void drawBackgrounds(){
        for (int i = 0; i <bgs.length ; i++) {
            batch.draw(bgs[i], bgs[i].getX(), bgs[i].getY());
        }
    }

    void checkBackgroundsOutOfBounds(){
        for (int i = 0; i <bgs.length ; i++) {
            if(bgs[i].getY() - bgs[i].getHeight()/2f -15> mainCamera.position.y){
                float newPosition = bgs[i].getHeight() + lastYPosition;
                bgs[i].setPosition(0, -newPosition);
                lastYPosition = Math.abs(newPosition);
            }
        }
    }

    private Fixture lastBody;
    private boolean primeraNube = true;


    @Override
     public void beginContact(Contact contact) {
        doubleJump = true;

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

        if (secondBody.getUserData().equals("DarkCloud")) {
            gameover();
           // System.out.println("gris");
        } else if (secondBody.getUserData().equals("Cloud1") && !secondBody.equals(lastBody)) {
            lastBody = secondBody;
            if (!primeraNube) {
                puntuacion += 1;
            } else {
                primeraNube = false;
            }
            //System.out.println("blancas: "+puntuacion);
        }else if (secondBody.getUserData().equals("Cloud2") && !secondBody.equals(lastBody)) {
            lastBody = secondBody;
            if (!primeraNube) {
                puntuacion += 1;
            } else {
                primeraNube = false;
            }
            //System.out.println("blancas: "+puntuacion);
        }else if (secondBody.getUserData().equals("Cloud3") && !secondBody.equals(lastBody)) {
            lastBody = secondBody;
            if (!primeraNube) {
                puntuacion += 1;
            } else {
                primeraNube = false;
            }
            //System.out.println("blancas: "+puntuacion);
        }


        //System.out.println("The name of the first body "+ firstBody.getUserData());
    }
    public void gameover() {
        //Restaurar camara
        mainCamera.position.set(GameInfo.H_WIDTH,GameInfo.H_HEIGHT, 0);
        mainCamera.update();


        batch.setProjectionMatrix(mainCamera.combined);

        game.setScreen(new GameOver(game, puntuacion));
    }



    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
