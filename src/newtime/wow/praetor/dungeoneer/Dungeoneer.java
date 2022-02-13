package newtime.wow.praetor.dungeoneer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Dungeoneer implements Runnable {

    public DungeoneerTab dungeoneerTab;

    public Dimension screenSize;
    public Robot robot;

    public PositionData currentPosition = new PositionData(0,0,0);
    public PositionData targetPosition = new PositionData(0,0,0);

    public Thread thread;

    public Dungeoneer(){
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public boolean running = true;

    public void run(){
        while(running) {
            loop();
        }
    }

    public void loop(){
        BufferedImage image = this.robot.createScreenCapture(new Rectangle(0,0,screenSize.width,screenSize.height));
        currentPosition = getPositionData(image);

        PositionData delta = getDeltaPosition();
        dungeoneerTab.map.render();
    }

    public PositionData getDeltaPosition(){
        return new PositionData(targetPosition.x-currentPosition.x, targetPosition.y-currentPosition.y, targetPosition.face-currentPosition.face);
    }

    public PositionData getPositionData(BufferedImage image){
        Color positionColor = new Color(image.getRGB(0,0));
        PositionData position = new PositionData(positionColor.getRed(), positionColor.getGreen(), positionColor.getBlue());
        return position;
    }

    public void start(DungeoneerTab dungeoneerTab){
        this.dungeoneerTab = dungeoneerTab;
        if(this.thread != null){
            System.err.println("Thread is non null");
        }
        this.thread = new Thread(this);
        this.running = true;
        this.thread.start();
    }

    public void stop(){
        this.running = false;
        this.thread.interrupt();
    }

}

class PositionData {

    public double x,y,face;

    public PositionData(double x, double y, double face){
        this.x = x;
        this.y = y;
        this.face = face;
    }

    public String toString(){
        return x + ", " + y + " : " + face;
    }

}
