package org.escoladeltreball.giant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


/**
 * Created by iam07264872
 */
public class GameOver implements Screen {
    private GameMain game;
    private SpriteBatch batch;

    private String puntuacion;
    private Sprite bg;

    private Sprite restart;

    BitmapFont font;


    public GameOver(GameMain game, long puntuacion) {
        this.puntuacion = String.valueOf(puntuacion);

       FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
       FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
       parameter.size = 50;
       font = generator.generateFont(parameter);

        this.game = game;
        this.batch = game.getBatch();
        this.bg = new Sprite(new Texture("Backgrounds/Highscore-BG.png"));

        this.restart = new Sprite(new Texture("Buttons/MainMenuButtons/PressSpace.png"));


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {



        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(bg, 0, 0);

        //Layout para la puntuación de nubes blancas
        GlyphLayout Marcador = new GlyphLayout(font, puntuacion);


        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        font.draw(batch, puntuacion, GameInfo.H_WIDTH - Marcador.width / 2,  GameInfo.H_HEIGHT-100);

        batch.draw(restart,  GameInfo.H_WIDTH - restart.getWidth() / 2,  GameInfo.H_HEIGHT-330);

        playAgain();
        batch.end();

    }

    private void playAgain() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new GamePlay(game));
        }
    }

    /**
     * @param width
     * @param height
     */
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
}
