package newtime.wow.fishly;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Debug {

    private static JFrame frame;
    private static Canvas canvas;

    private static void createPreviewFrame(){
        Debug.canvas = new Canvas();

        Debug.frame = new JFrame();
        Debug.frame.setSize(800,600);
        Debug.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Debug.frame.setLocationRelativeTo(null);
        Debug.frame.add(Debug.canvas);

        Debug.frame.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                Fishly.isDisabled = true;
            }

            public void mouseExited(MouseEvent e) {
                super.mouseEntered(e);
                Fishly.isDisabled = false;
            }
        });

        Debug.frame.setVisible(true);
    }

    public static void showPreview(BufferedImage screenshot, int highestRed, int foundX){
        if(frame == null || canvas == null){
            Debug.createPreviewFrame();
        }

        BufferStrategy bs = Debug.canvas.getBufferStrategy();
        if(bs == null){
            Debug.canvas.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0, Debug.canvas.getWidth(), Debug.canvas.getHeight());

        if(highestRed < Integer.MAX_VALUE) {
            Graphics screenshotGraphics = screenshot.getGraphics();
            screenshotGraphics.setColor(Color.RED);
            screenshotGraphics.fillRect(0, highestRed, canvas.getWidth(), 2);

            screenshotGraphics.setColor(Color.BLUE);
            screenshotGraphics.fillRect(foundX+25, highestRed+10, 2, 2);

            screenshotGraphics.dispose();
        }

        if(Fishly.isDisabled){
            Point mousePos = Debug.frame.getMousePosition();
            if(mousePos != null){
                Color color = new Color(screenshot.getRGB(mousePos.x, mousePos.y));
                int hue = Fishly.getHue(color.getRed(), color.getGreen(), color.getBlue());
                System.out.println(hue + ", " + color.toString());
            }
        }

        g.drawImage(screenshot, 0, 0, Debug.canvas.getWidth(), Debug.canvas.getHeight(), null);

        g.dispose();
        bs.show();
    }

}
