package com.example.filipe.pacman;

import android.graphics.Canvas;

/**
 * Created by Filipe on 6/5/2015.
 */

public class GameLoopThread extends Thread {
    static final long FPS = 10;
    private GameView view;

    public boolean isRunning() {
        return running;
    }

    private boolean running = false;

    public GameLoopThread(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;



        while (running) {
            Canvas runningCanvas = null;
            startTime = System.currentTimeMillis();
            try
            {
                runningCanvas = view.getHolder().lockCanvas();
                synchronized (view.getHolder())
                {
                    view.onDraw(runningCanvas);
                }
            } finally
            {
                if (runningCanvas != null) {
                    view.getHolder().unlockCanvasAndPost(runningCanvas);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            view.colisionCheck();
            try
            {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }
}