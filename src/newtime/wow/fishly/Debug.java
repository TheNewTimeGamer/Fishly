package newtime.wow.fishly;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

public class Debug {

    private static JFrame frame;
    private static Canvas canvas;

    private static JPanel configPanel = new JPanel();

    private static JLabel minHueText = new JLabel("Min Hue: " + Fishly.minHue);
    private static JLabel maxHueText = new JLabel("Max Hue: " + Fishly.maxHue);

    private static JSlider minHueSlider = new JSlider();
    private static JSlider maxHueSlider = new JSlider();

    private static JLabel redText = new JLabel("Min Red: " + Fishly.minRed);
    private static JLabel greenText = new JLabel("Max Green: " + Fishly.maxGreen);
    private static JLabel blueText = new JLabel("Max Blue: " + Fishly.maxBlue);

    private static JSlider redSlider = new JSlider();
    private static JSlider greenSlider = new JSlider();
    private static JSlider blueSlider = new JSlider();

    private static JButton toggleButton = new JButton("Enable");

    private static boolean scanning = false;

    private static void createPreviewFrame(){
        Debug.canvas = new Canvas();

        Debug.frame = new JFrame();
        Debug.frame.setSize(800,600);
        Debug.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Debug.frame.setLocationRelativeTo(null);
        Debug.frame.setLayout(null);

        Debug.frame.add(configPanel);
        Debug.frame.add(Debug.canvas);

        Debug.configPanel.add(minHueText);
        Debug.configPanel.add(minHueSlider);
        minHueSlider.setMinimum(0);
        minHueSlider.setMaximum(360);
        minHueSlider.setValue(Fishly.minHue);
        minHueSlider.setPaintTicks(true);
        minHueSlider.setPaintTrack(true);
        minHueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Fishly.minHue = minHueSlider.getValue();
                Debug.minHueText.setText("Min Hue: " + Fishly.minHue);
            }
        });

        Debug.configPanel.add(maxHueText);
        Debug.configPanel.add(maxHueSlider);
        maxHueSlider.setMinimum(0);
        maxHueSlider.setMaximum(360);
        maxHueSlider.setValue(Fishly.maxHue);
        maxHueSlider.setPaintTicks(true);
        maxHueSlider.setPaintTrack(true);
        maxHueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Fishly.maxHue = maxHueSlider.getValue();
                Debug.maxHueText.setText("Max Hue: " + Fishly.maxHue);
            }
        });

        Debug.configPanel.add(redText);
        Debug.configPanel.add(redSlider);
        redSlider.setMinimum(0);
        redSlider.setMaximum(255);
        redSlider.setValue(Fishly.minRed);
        redSlider.setPaintTicks(true);
        redSlider.setPaintTrack(true);
        redSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Fishly.minRed = redSlider.getValue();
                Debug.redText.setText("Min Red: " + Fishly.minRed);
            }
        });

        Debug.configPanel.add(greenText);
        Debug.configPanel.add(greenSlider);
        greenSlider.setMinimum(0);
        greenSlider.setMaximum(255);
        greenSlider.setValue(Fishly.maxGreen);
        greenSlider.setPaintTicks(true);
        greenSlider.setPaintTrack(true);
        greenSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Fishly.maxGreen = greenSlider.getValue();
                Debug.greenText.setText("Max Green: " + Fishly.maxGreen);
            }
        });

        Debug.configPanel.add(blueText);
        Debug.configPanel.add(blueSlider);
        blueSlider.setMinimum(0);
        blueSlider.setMaximum(255);
        blueSlider.setValue(Fishly.maxBlue);
        blueSlider.setPaintTicks(true);
        blueSlider.setPaintTrack(true);
        blueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                blueText.setText("Max Blue: " + Fishly.maxBlue);
                Fishly.maxBlue = blueSlider.getValue();
            }
        });

        Debug.configPanel.add(toggleButton);

        Debug.configPanel.setBounds(0, 0, 200, 600);
        Debug.canvas.setBounds(200, 0, 600, 600);

        Debug.toggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Fishly.isDisabled = !Fishly.isDisabled;
                if(Fishly.isDisabled){
                    Debug.toggleButton.setText("Enable");
                }else{
                    Debug.toggleButton.setText("Disable");
                }
            }
        });

        Debug.canvas.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                Debug.scanning = true;
            }
            public void mouseExited(MouseEvent e) {
                super.mouseEntered(e);
                Debug.scanning = false;
            }
        });

        Debug.frame.setVisible(true);
    }

    public static void showPreview(BufferedImage screenshot, int highestRed, int foundX){
        if(frame == null || canvas == null){
            Debug.createPreviewFrame();
        }

        if(screenshot == null){
            return;
        }

        double focusX = 0;
        double focusY = 0;

        if(Debug.scanning){
            Point mousePos = canvas.getMousePosition();
            if(mousePos != null){
                double xMod = ((double)screenshot.getWidth()) / ((double)Debug.canvas.getWidth());
                double yMod = ((double)screenshot.getHeight()) / ((double)Debug.canvas.getHeight());
                focusX = mousePos.x * xMod;
                focusY = mousePos.y * yMod;
                Color color = new Color(screenshot.getRGB((int)focusX, (int)focusY));
                int hue = Fishly.getHue(color.getRed(), color.getGreen(), color.getBlue());
                Debug.frame.setTitle("m/M Hue: " + Fishly.minHue + " - " + Fishly.maxHue + " | RGB: " + Fishly.minRed + ", " + Fishly.maxGreen + "," + Fishly.maxBlue + " | " + " Hue: " + hue + " | " + color.toString());
            }
        }

        BufferStrategy bs = Debug.canvas.getBufferStrategy();
        if(bs == null){
            Debug.canvas.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0, Debug.canvas.getWidth(), Debug.canvas.getHeight());

        BufferedImage overlay = new BufferedImage(screenshot.getWidth(), screenshot.getHeight(), BufferedImage.TYPE_INT_ARGB);

        if(highestRed < Integer.MAX_VALUE) {
            Graphics overlayGraphics = overlay.getGraphics();
            overlayGraphics.setColor(Color.RED);
            overlayGraphics.fillRect(0, highestRed, canvas.getWidth(), 2);

            overlayGraphics.setColor(Color.BLUE);
            overlayGraphics.fillRect(foundX+25, highestRed+10, 2, 2);

            overlayGraphics.setColor(Color.GREEN);
            overlayGraphics.fillRect((int)focusX, (int)focusY, 2, 2);

            overlayGraphics.dispose();
        }

        g.drawImage(screenshot, 0, 0, Debug.canvas.getWidth(), Debug.canvas.getHeight(), null);
        g.drawImage(overlay, 0, 0, Debug.canvas.getWidth(), Debug.canvas.getHeight(), null);

        g.dispose();
        bs.show();
    }

}
