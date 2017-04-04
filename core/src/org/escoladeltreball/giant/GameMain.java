package org.escoladeltreball.giant;


import com.badlogic.gdx.Game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import sun.applet.Main;

public class GameMain extends Game {

	private SpriteBatch batch;

	public SpriteBatch getBatch() {
		return batch;
	}


	@Override
	public void create () {
        //Nomes un SPRITEBATCH per GAME, obj molt pesat

		batch = new SpriteBatch();

       setScreen(new GamePlay(this));


	}

	@Override
	public void render () {
		super.render();


       // Gdx.gl.glClearColor(1, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}


}
