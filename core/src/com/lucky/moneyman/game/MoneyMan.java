package com.lucky.moneyman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class MoneyMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture man[];
	int manstate;
	int pause=0;
	float gravity = 0.2f;
	float velocity = 0;
	int many=0;
	int score=0;
	BitmapFont font;
	int gamestate =0;
	Texture dizzy;

	//creating coin objects

	ArrayList<Integer> coinXs =new ArrayList<Integer>();
	ArrayList<Integer> coinYs =new ArrayList<Integer>();

	ArrayList<Rectangle> coinRectangles =new ArrayList<Rectangle>();
    ArrayList<Rectangle> bombRectangles =new ArrayList<Rectangle>();

    Rectangle manRectangle;
    ArrayList<Integer> bombXs =new ArrayList<Integer>();
	ArrayList<Integer> bombYs =new ArrayList<Integer>();
	Texture coin;
	int coincount;
	Random random;

	Texture bomb;
	int bombcount;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man =new Texture[4];
		man[0]= new Texture("run1.png");
        man[1]= new Texture("run4.png");
        man[2]= new Texture("dizzy1.png");
        man[3]= new Texture("dead.png");
    many=Gdx.graphics.getHeight()/2;
    dizzy =new Texture("dead.png");
        coin =new Texture("dollar.png");
        bomb =new Texture("bomb.png");
        random =new Random();

        font =new BitmapFont();
        font.setColor(Color.GREEN);
        font.getData().setScale(10);
	}

	public void makeCoin(){

		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());


	}
	public void Makebomb(){
		float height= random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int) height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
     if(gamestate==1){
    //game is live
         if(bombcount<150){
             bombcount++;
         }
         else{
             bombcount=0;
             Makebomb();
         }
         bombRectangles.clear();
         for (int j=0;j<bombXs.size();j++){
             batch.draw(bomb,bombXs.get(j),bombYs.get(j));
             bombXs.set(j,bombXs.get(j)-8);
             bombRectangles.add(new Rectangle(bombXs.get(j),bombYs.get(j),bomb.getWidth(),bomb.getHeight()));
         }

         //coins
         if(coincount<100){

             coincount++;
         }
         else{
             coincount=0;
             makeCoin();
         }

         coinRectangles.clear();

         for(int i=0;i<coinXs.size();i++){
             batch.draw(coin,coinXs.get(i),coinYs.get(i));
             coinXs.set(i,coinXs.get(i)-5);
             coinRectangles.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));

         }



         if(Gdx.input.justTouched()){
             velocity = -10;

         }


         if(pause<8){
             pause++;
         }
         else {
             pause=0;
             if (manstate < 2) {
                 manstate++;
             } else {
                 manstate = 0;
             }

         }
         velocity = velocity + gravity;
         many-=velocity;

         if(many <=0 ){
             many=0;
         }
         if(many>Gdx.graphics.getHeight()){
             many =Gdx.graphics.getHeight();
         }



       }
     else if(gamestate==0){
         //Waiting to start
         if(Gdx.input.justTouched()){
             gamestate =1;

         }

     }
     else if (gamestate==2){
         //game over

         if(Gdx.input.justTouched()){
             gamestate =1;
             many =Gdx.graphics.getHeight()/2;
             score=0;
             velocity=0;
             coinXs.clear();
             coinYs.clear();
             coinRectangles.clear();
             coincount =0;
             bombRectangles.clear();
             bombXs.clear();
             bombYs.clear();
             bombcount =0;

         }
     }
//bombs

        if(gamestate ==2) {
            batch.draw(dizzy,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        }
        else{
            batch.draw(man[manstate], Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, many);

        }


		manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manstate].getWidth()/2,many,man[manstate].getWidth(),man[manstate].getHeight());

		for(int i=0;i<coinRectangles.size();i++)
        {
            if(Intersector.overlaps(manRectangle,coinRectangles.get(i))){
                Gdx.app.log("Coin","collision");
                score++;
                coinRectangles.remove(i);
                coinXs.remove(i);
                coinYs.remove(i);
                break;
            }
        }
        for(int i=0;i<bombRectangles.size();i++)
        {
            if(Intersector.overlaps(manRectangle,bombRectangles.get(i))){
                Gdx.app.log("bomb","collision");
                gamestate =2;

            }
        }

        font.draw(batch,String.valueOf(score),100,200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
