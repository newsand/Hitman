package com.example.filipe.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private Sprite hero;
    private Sprite princess;
    private List<Sprite> sprites = new ArrayList<Sprite>();

    public GameView(final Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d("testing", "SURFACE DESTROYED");
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {}
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();



            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    private void createSprites() {
        sprites.add(createSprite(R.drawable.villain));
        sprites.add(createSprite(R.drawable.villain));
        sprites.add(createSprite(R.drawable.villain));
        sprites.add(createSprite(R.drawable.villain));
        sprites.add(createSprite(R.drawable.villain));
        sprites.add(createSprite(R.drawable.villain2));
        sprites.add(createSprite(R.drawable.villain2));
        sprites.add(createSprite(R.drawable.villain2));
        sprites.add(createSprite(R.drawable.villain2));
        sprites.add(createSprite(R.drawable.villain2));
        sprites.add(createSprite(R.drawable.hero2));
        sprites.add(createSprite(R.drawable.hero2));
        sprites.add(createSprite(R.drawable.hero2));
        sprites.add(createSprite(R.drawable.hero2));
        hero=createSprite(R.drawable.hero);
        hero.Sensitive=true;
        hero.HP =6;
        princess=createSprite(R.drawable.princess);
        Global.globalConstructed=true;
        Log.d("testing", "sprites created");
    }

    private void destroySprites() {
        synchronized (getHolder()) {
            if (sprites.size()>0){
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    sprites.remove(sprite);
                }
            }
        }
        Global.globalConstructed=false;
    }
    private Sprite createSprite(int resource) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(this,bmp);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        for (Sprite sprite : sprites) {
            sprite.onDraw(canvas);
        }
        hero.onDraw(canvas);
        princess.onDraw(canvas);
    }

    public void colisionCheck()
    {
        for (Sprite sprite : sprites)
        {
            hero.collidesWith(sprite);
        }
        if(hero.collidesWith(princess))
        {
            Log.d("testing", "hittedPricness");
            hero.setHitedPrincess(true);
            destroySprites();
        }
        if(hero.HP <0)
        {
            Log.d("testing", "hero dead: "+hero.HP);

            destroySprites();
        }

    }
    public boolean onTouchEvent(MotionEvent event) {
        if(Global.globalConstructed)
        {
            if(!gameLoopThread.isRunning())
            {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

        }
        else
        {
            Log.d("testing", "to create sprites");
            createSprites();
        }
        Log.d("testing", "eventtouch");

        return super.onTouchEvent(event);
    }

}