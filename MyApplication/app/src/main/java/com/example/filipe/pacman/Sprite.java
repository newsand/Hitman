package com.example.filipe.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Random;

/**
 * Created by Filipe on 6/5/2015.
 */
public class Sprite  {

    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
    private int currentFrame = 0;
    private Bitmap bmp;
    private int xSpeed = 5;
    private int ySpeed = 5;
    private static final int MAX_SPEED = 10;
    private GameView gameView;
    private int x = 0;
    private int y = 0;
    private int width;//largura do frame
    private int height;//altura do frame
    public boolean colided=false;
    public boolean Sensitive=false;
    public boolean Alive=true;
    public int HP;
    private MediaPlayer mp;
    private boolean hitedPrincess=false;

    public void setHitedPrincess(boolean hitedPrincess) {
        this.hitedPrincess = hitedPrincess;
    }

    public Sprite(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        Random rnd = new Random();
        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height);
        xSpeed = rnd.nextInt(MAX_SPEED * 2)-MAX_SPEED;
        ySpeed = rnd.nextInt(MAX_SPEED * 2)-MAX_SPEED;
        HP =2;
        Alive=true;
    }

    public void update()
    {
        if(HP <1)
        {
            Alive=false;
            Log.d("testing", "morto:" + HP);
        }

        if(Alive=false)
        {
            Log.d("testing","sprite paralizado");
            return;
        }
        else
        {
            UpdateAliveSprite();
        }



    }

    private void UpdateAliveSprite() {
        if (Sensitive)
        {
            xSpeed = Global.globalX*(-10);
            ySpeed = Global.globalY *10;
            if (colided)
            {
                if(hitedPrincess){
                    mp = MediaPlayer.create(gameView.getContext(), R.raw.victory);
                    Log.d("testing", "victory song");
                    hitedPrincess=false;
                }
                else{
                    mp = MediaPlayer.create(gameView.getContext(), R.raw.hit);
                }
                mp.start();
                Random rnd = new Random();

                int xModifier = rnd.nextInt(MAX_SPEED * 10)-MAX_SPEED;
                int yModifier = rnd.nextInt(MAX_SPEED * 10)-MAX_SPEED;
                if((x+xModifier)<(gameView.getWidth() - width)&&((x+xModifier)>0))
                    x+=xModifier;
                else
                    x-=xModifier;
                if((y+xModifier)<(gameView.getHeight() - height)&&((y+xModifier)>0))
                    y+=xModifier;
                else
                    y-=yModifier;

                colided=false;
            }else
            {
                if (x > gameView.getWidth() - width - xSpeed || x + xSpeed < 0) {
                    xSpeed = -xSpeed;
                }


                if (y > gameView.getHeight() - height - ySpeed || y + ySpeed < 0) {
                    ySpeed = -ySpeed;
                }
            }

            x = x + xSpeed;
            y = y + ySpeed;
        }
        else
        {

            if (colided)
            {
                Random rnd = new Random();

                int xModifier = rnd.nextInt(MAX_SPEED * 4)-MAX_SPEED;
                int yModifier = rnd.nextInt(MAX_SPEED * 4)-MAX_SPEED;
                if((x+xModifier)<(gameView.getWidth() - width)&&((x+xModifier)>0))
                    x+=xModifier;
                else
                    x-=xModifier;
                if((y+xModifier)<(gameView.getHeight() - height)&&((y+xModifier)>0))
                    y+=xModifier;
                else
                    y-=yModifier;
                xSpeed = rnd.nextInt(MAX_SPEED * 2)-MAX_SPEED;
                ySpeed = rnd.nextInt(MAX_SPEED * 2)-MAX_SPEED;
                colided=false;
            }else
            {
                if (x > gameView.getWidth() - width - xSpeed || x + xSpeed < 0) {
                    xSpeed = -xSpeed;
                }


                if (y > gameView.getHeight() - height - ySpeed || y + ySpeed < 0) {
                    ySpeed = -ySpeed;
                }
            }

            x = x + xSpeed;
            y = y + ySpeed;

        }
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    public void onDraw(Canvas canvas)
    {
        update();
        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);// quadrado dentro do arquivo de imagem
        Rect dst = new Rect(x, y, x + width, y + height);// quadrado na tela
        canvas.drawBitmap(bmp, src, dst, null);
    }
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    public int getAnimationRow()
    {
        double moveResult =(Math.atan2(xSpeed,ySpeed)/(Math.PI/2)+2);
        int direction = (int)Math.round(moveResult)%BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }
    public Rect getBounds() {
        return new Rect(x, y, width, height);
    }
    public void inColision(Sprite other) {
        //return .intersects(getBounds());
        if( Rect.intersects(other.getBounds(),this.getBounds()))
        {
            Log.d("testing", "inColision hitted");
            colided=true;
            other.colided=true;
        }
        Log.d("testing", "inColision got called");
    }
    public boolean collidesWith(Sprite obj) {

        if (obj.y <= this.y + this.height &&
                obj.y + obj.height >= this.y    &&
                obj.x + obj.width >= this.x &&
                obj.x <= this.x + this.width){

            colided=true;
            obj.colided=true;
            this.HP--;
            obj.HP--;
            Log.d("testing", "collidesWith hitted hero lives: "+ this.HP);
            return true;
        }
        return false;
    }

}
