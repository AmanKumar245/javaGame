package main;

import entity.Player;
import object.SuperObject;
import tile.TileManger;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;

   public final int tileSize = originalTileSize *scale; //48*48 tile
    public final int maxScreenCol = 16;
   public  final int getMaxScreenRow = 12;
   public final int  screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * getMaxScreenRow;
    // WORLD SETTINGS

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;



    //FPS
    int FPS = 60;

    //system
    TileManger tileM = new TileManger(this);
    KeyHandler keyH  = new KeyHandler(this);
    Sound se = new Sound();
    Sound music = new Sound();

   public CollisionChecker cChecker = new CollisionChecker(this);
   public AssetSetter aSetter = new AssetSetter(this);
   public UI ui = new UI(this);

    Thread gameThread;
   //Entity and object
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];

    // Game state

    public final int titleState = 0;
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setupGame() {
        aSetter.setObject();
        playMusic(0);
//        stopMusic();
        gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
//    public void run(){
//        while(gameThread != null){
//            double drawInterval = 1000000000/FPS; //0.0166 sec
//            double nextDrawTime = System.nanoTime() +drawInterval;
//
//
//            update();
//
//            repaint();
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime/1000000;
//
//                if(remainingTime <0){
//                    remainingTime = 0;
//                }
//
//                Thread.sleep((long) remainingTime);
//
//                nextDrawTime += drawInterval;
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//
//    }
    public void run(){

        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime -lastTime) / drawInterval;

            lastTime  = currentTime;

            if (delta >= 1){

                update();

                repaint();
                delta --;
                drawCount++;
            }
        }
    }

    public void update(){
        if(gameState == playState){
            player.update();
        }
        if(gameState == pauseState){
            // nothing
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Debug
        long drawStart = 0;
        if(keyH.checkDrawTime){
            drawStart = System.nanoTime();
        }

        drawStart = System.nanoTime();

        //TITLE SCREEN
        if (gameState == titleState){
            ui.draw(g2);
        }
        //OTHERS
        else {
            //DRAW TILE FIRST
            tileM.draw(g2);

            //Object
            for(int i = 0; i < obj.length; i ++){
                if(obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            //player
            player.draw(g2);

            //UI
            ui.draw(g2);

        }

        //debug
        if (keyH.checkDrawTime ){
            long drawend = System.nanoTime();
            long passed = drawend - drawStart;
            g2.setColor(Color.BLUE);
            g2.drawString("Draw TIme: " + passed, 10 ,400);
            System.out.println("Draw Time "+ passed);
        }


        g2.dispose();
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();

    }
    public void stopMusic(){
        music.stop();
    }
    public  void  playSE(int i){
        se.setFile(i);
        se.play();
    }
}
