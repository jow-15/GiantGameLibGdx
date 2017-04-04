package org.escoladeltreball.giant;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by iam07264872
 */
public class CloudController {

    private World world;

    private Array<Cloud> clouds = new Array<Cloud>();

    private final float DISTANCE_BETWEEN_CLOUDS = 250f;

    private  float minX, maxX;

    private float lastCloudPositionY;

    private Random random = new Random();

    private float cameraY;


    public CloudController(World world) {
        this.world = world;
        minX = GameInfo.H_WIDTH - 150;
        maxX = GameInfo.H_WIDTH + 150;
        createClouds();
        positionClouds(true);
    }

    void createClouds() {
        for(int i = 0; i<2; i++){
            clouds.add(new Cloud(world, "DarkCloud"));
        }
        int index = 1;
        for(int i = 0; i<6; i++){
            clouds.add(new Cloud(world, "Cloud" + index));
            index++;
            if(index==4) index=1;
        }

        clouds.shuffle();

    }

    public void positionClouds(boolean firstTimeArranging){

        while(clouds.get(0).getCloudName() == "DarkCloud"){
            clouds.shuffle();
        }
        float positionY = 0;

        if(firstTimeArranging){
            positionY=GameInfo.H_HEIGHT;
        }else{
            positionY = lastCloudPositionY;
        }

        int controlX = 0;
        for(Cloud c : clouds){

            if(c.getX() == 0 && c.getY() == 0){
                float tempX = 0;

                if(controlX == 0){
                    tempX = randomBetweenNumbers(maxX - 100, maxX);
                    controlX = 1;
                }else if(controlX == 1){
                    tempX = randomBetweenNumbers(minX + 50, minX);
                    controlX = 0;
                }
                c.setSpritePosition(tempX, positionY);

                positionY -= DISTANCE_BETWEEN_CLOUDS;
                lastCloudPositionY= positionY;
            }


        }


    }

    public void drawClouds(SpriteBatch batch){

        for(Cloud c : clouds){
            batch.draw(c, c.getX(), c.getY());
        }
    }

    public void createAndArrangeNewClouds(){
        for(int i = 0; i< clouds.size; i++){
            if(clouds.get(i).getY() - GameInfo.H_HEIGHT > cameraY){
                clouds.get(i).getTexture().dispose();
                clouds.removeIndex(i);
            }
        }

        if(clouds.size ==4){
            createClouds();

            positionClouds(false);
        }
    }


    public void setCameraY(float cameraY){
        this.cameraY = cameraY;
    }
    private float randomBetweenNumbers(float min, float max){
        return random.nextFloat() * (max - min) + min;
    }

    public Player  positionThePlayer(Player player){
        player = new Player(world,clouds.get(0).getX()+50, clouds.get(0).getY()+100);
        return player;
    }
}
