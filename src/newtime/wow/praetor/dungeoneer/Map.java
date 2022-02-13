package newtime.wow.praetor.dungeoneer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Map extends Canvas {

    BufferedImage map;

    PositionData currentLocation = new PositionData(0,0,0);
    PositionData targetLocation = new PositionData(0,0,0);

    public Map(String mapName) {
        try {
            this.map = ImageIO.read(new File(mapName));
        } catch (IOException e) {
            this.map = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB);
            Graphics g = this.map.getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0,0,this.map.getWidth(),this.map.getHeight());
            g.dispose();
        }
    }

    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,this.getWidth(),this.getHeight());

        g.drawImage(map, 0, 0, this.getWidth(), this.getHeight(), null);

        g.setColor(Color.BLUE);

        double xTarget = (this.targetLocation.x*this.getWidth());
        double yTarget = (this.targetLocation.y*this.getHeight());

        g.fillOval((int)xTarget,(int)yTarget,10,10);

        g.setColor(Color.GREEN);

        double xCurrent = (this.currentLocation.x*this.getWidth());
        double yCurrent = (this.currentLocation.y*this.getHeight());

        g.fillOval((int)xCurrent,(int)yCurrent,10,10);

        g.setColor(Color.BLUE);
        g.drawLine((int)xTarget+5,(int)yTarget+5,(int)xCurrent+5,(int)yCurrent+5);

        g.dispose();
        bs.show();
    }

}
