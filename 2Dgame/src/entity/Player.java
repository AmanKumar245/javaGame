package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
     public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8,16, 32,32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();


    }


    public void setDefaultValues(){
        worldX = gp.tileSize *23;
        worldY = gp.tileSize *21;
        Speed = 4;
        direction = "down";

    }
    public void getPlayerImage(){
        try{
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_2.png")));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void update(){
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed|| keyH.rightPressed){

            if (keyH.upPressed){
                direction = "up";


            } else if (keyH.downPressed) {
                direction = "down";

            } else if (keyH.leftPressed) {
                direction = "left";


            } else if (keyH.rightPressed) {
                direction = "right";

            }
            //cheack tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //object collion
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);


            if(collisionOn== false){
                switch (direction){
                    case "up":
                        worldY -= Speed;
                        break;
                    case "down":
                        worldY += Speed;
                        break;
                    case "left":
                        worldX -= Speed;
                        break;
                    case "right":
                        worldX +=Speed;
                        break;
                }
            }

            spriteCounter++;
            if(spriteCounter >12){
                if(spriteNum ==1){
                    spriteNum=2;
                } else if (spriteNum ==2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int i){
        if(i != 999){

            String objectName = gp.obj[i].name;

            switch (objectName) {
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a Key!");

                    break;
                case "Door":
                    if(hasKey>0){
                        gp.playSE(3);
                        gp.obj[i]= null;
                        hasKey--;
                        gp.ui.showMessage("You opened the door!");
                    }else {
                        gp.ui.showMessage("You need a Key!");

                    }
                    break;
                case "Boot":
                    gp.playSE(2);
                    Speed +=3;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed Up!");
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;

            }


        }

    }

    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //g2.fillRect(X, Y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;

        switch(direction){
            case "up":
                if(spriteNum ==1){
                    image = up1;
                }
                if(spriteNum ==2){
                    image = up2;
                }
                break;
            case "down":
                if(spriteNum ==1){
                    image = down1;
                }
                if(spriteNum ==2){
                    image = down2;
                }
                break;
            case "right":
                if(spriteNum ==1){
                    image = right1;
                }
                if(spriteNum ==2){
                    image = right2;
                }
                break;
            case "left":
                if(spriteNum ==1){
                    image = left1;
                }
                if(spriteNum ==2){
                    image = left2;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        g2.drawImage(image , screenX, screenY, gp.tileSize, gp.tileSize, null);

    }
}
