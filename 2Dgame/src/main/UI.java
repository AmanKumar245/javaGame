package main;

import object.OBJ_Heart;
import object.OBJ_Key;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40 , arial_80B;

    BufferedImage heart_full,heart_half,heart_blank;

    public boolean messageOn = false;
    public String message = "";
    int messagecounter = 0;
    public boolean  gameFinished = false;
    public String currentDialogue = "";
    double playTime;
    DecimalFormat dformat = new DecimalFormat("#0.00");

    public int commandNum = 0;

    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial",Font.PLAIN,40);
        arial_80B = new Font("Arial",Font.BOLD,80);

//        OBJ_Key key = new OBJ_Key(gp);
//        keyImage = key.image;

        // create HUD object
        SuperObject heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;

    }

    public void draw(Graphics2D g2){
//        if (gameFinished){
//            g2.setFont(arial_40);
//            g2.setColor(Color.white);
//            String text;
//            int textLength;
//            int x;
//            int y;
//
//            text = "You found the Treasure!";
//            textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
//             x = gp.screenWidth/2 - textLength/2;
//             y = gp.screenWidth/2 - (gp.tileSize*3);
//             g2.drawString(text, x , y);
//
//            text = "Your Time is " + dformat.format(playTime) + "!";
//            textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
//            x = gp.screenWidth/2 - textLength/2;
//            y = gp.screenWidth/2 + (gp.tileSize*3);
//            g2.drawString(text, x , y);
//
//
//             g2.setFont(arial_80B);
//             g2.setColor(Color.yellow);
//             text = "Congratulations";
//             textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
//            x = gp.screenWidth/2 - textLength/2;
//            y = gp.screenWidth/2 + (gp.tileSize*2);
//            g2.drawString(text, x , y);
//
//            gp.gameThread = null;
//
//        }else{
//            g2.setFont(arial_40);
//            g2.setColor(Color.white);
//            //g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize,null);
//            g2.drawString("x " + gp.player.hasKey,74,65);
//
//            //Time
//            playTime +=(double) 1/60;
//            g2.drawString("Time:" + dformat.format(playTime), gp.tileSize*11, 65);
//
//            //MEssage
//            if(messageOn){
//                g2.setFont(g2.getFont().deriveFont(30F));
//                g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
//
//                messagecounter++;
//
//                if(messagecounter>120){
//                    messagecounter = 0;
//                    messageOn = false;
//
//                }
//            }

    //}
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        //TITLE STATE
        if (gp.gameState == gp.titleState){
           drawTitleScreen();
        }

        //PLAY STATE
        if(gp.gameState == gp.playState){
            drawPlayerLife();
        }
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }
        // dailouge
        if (gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }
    }

    public void drawPlayerLife(){

//        gp.player.life = 5;

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // Draw Max Life
        while(i < gp.player.maxLife/2){
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x += gp.tileSize;
        }

        // Reset
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        // Draw Current Life
        while(i < gp.player.life){
            g2.drawImage(heart_half,x,y,null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heart_full,x,y,null);
            }
            i++;
            x += gp.tileSize;

        }
    }

    public void drawTitleScreen(){
        g2.setColor(new Color(0,0,0));
        g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 85F));
        String text = "Jungle Mai Mangal";
        int x = getXforCenteredText(text) ;
        int y = gp.tileSize*3;
        //SHADOW COLOR
        g2.setColor(Color.gray);
        g2.drawString(text,x+5,y+5);

        //MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x , y);

        //BLUE BOY IMAGE
        x = gp.screenWidth/2 -(gp.tileSize*2)/2;
        y += gp.tileSize*2;
        g2.drawImage(gp.player.down1, x , y , gp.tileSize*2,gp.tileSize*2,null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize*3.5;
        g2.drawString(text,x ,y );
        if (commandNum == 0){
            g2.drawString(">",x-gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text,x ,y );
        if (commandNum == 1){
            g2.drawString(">",x-gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text,x ,y );
        if (commandNum == 2){
            g2.drawString(">",x-gp.tileSize, y);
        }



    }
    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);

        int y = gp.screenHeight/2;
        g2.drawString(text,x,y);
    }

    public void drawDialogueScreen(){
        // window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow(x,y,width,height);


        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        y += gp.tileSize;
        x+= gp.tileSize;

        for(String line : currentDialogue.split("\n")) {
            g2.setColor(Color.white);
            g2.drawString(line,x,y);
            y += 40;
        }

    }

    public void drawSubWindow(int x,int y,int width, int height){
        g2.setColor(new Color(0,0,0,200));
        g2.fillRoundRect(x,y,width,height,35,35);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5,y +5,width - 10,height - 10,25,25);
    }

    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}
