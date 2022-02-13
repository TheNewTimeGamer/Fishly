package newtime.wow.praetor.fishly;

import newtime.wow.praetor.Tab;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class FishingTab extends Tab {

    private Fishly fishly;

    private boolean scanning = false;

    private Canvas canvas;

    private JPanel configPanel = new JPanel();

    private JLabel minHueText = new JLabel("Min Hue: ");
    private JLabel maxHueText = new JLabel("Max Hue: ");

    private JSlider minHueSlider = new JSlider();
    private JSlider maxHueSlider = new JSlider();

    private JLabel redText = new JLabel("Min Red: ");
    private JLabel greenText = new JLabel("Max Green: ");
    private JLabel blueText = new JLabel("Max Blue: ");

    private JSlider redSlider = new JSlider();
    private JSlider greenSlider = new JSlider();
    private JSlider blueSlider = new JSlider();

    private static JButton toggleButton = new JButton("Enable");

    public FishingTab(Fishly fishly){
        this.fishly = fishly;
        canvas = new Canvas();

        this.setLayout(null);

        this.add(configPanel);
        this.add(canvas);

        final FishingTab currentTab = this;
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                canvas.setBounds(configPanel.getWidth(), 0, currentTab.getWidth()-configPanel.getWidth(), currentTab.getHeight());
            }
        });

        configPanel.add(minHueText);
        configPanel.add(minHueSlider);
        minHueSlider.setMinimum(0);
        minHueSlider.setMaximum(360);
        minHueSlider.setValue(fishly.minHue);
        minHueSlider.setPaintTicks(true);
        minHueSlider.setPaintTrack(true);
        minHueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                fishly.minHue = minHueSlider.getValue();
                minHueText.setText("Min Hue: " + fishly.minHue);
            }
        });

        configPanel.add(maxHueText);
        configPanel.add(maxHueSlider);
        maxHueSlider.setMinimum(0);
        maxHueSlider.setMaximum(360);
        maxHueSlider.setValue(fishly.maxHue);
        maxHueSlider.setPaintTicks(true);
        maxHueSlider.setPaintTrack(true);
        maxHueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                fishly.maxHue = maxHueSlider.getValue();
                maxHueText.setText("Max Hue: " + fishly.maxHue);
            }
        });

        configPanel.add(redText);
        configPanel.add(redSlider);
        redSlider.setMinimum(0);
        redSlider.setMaximum(255);
        redSlider.setValue(fishly.minRed);
        redSlider.setPaintTicks(true);
        redSlider.setPaintTrack(true);
        redSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                fishly.minRed = redSlider.getValue();
                redText.setText("Min Red: " + fishly.minRed);
            }
        });

        configPanel.add(greenText);
        configPanel.add(greenSlider);
        greenSlider.setMinimum(0);
        greenSlider.setMaximum(255);
        greenSlider.setValue(fishly.maxGreen);
        greenSlider.setPaintTicks(true);
        greenSlider.setPaintTrack(true);
        greenSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                fishly.maxGreen = greenSlider.getValue();
                greenText.setText("Max Green: " + fishly.maxGreen);
            }
        });

        configPanel.add(blueText);
        configPanel.add(blueSlider);
        blueSlider.setMinimum(0);
        blueSlider.setMaximum(255);
        blueSlider.setValue(fishly.maxBlue);
        blueSlider.setPaintTicks(true);
        blueSlider.setPaintTrack(true);
        blueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                blueText.setText("Max Blue: " + fishly.maxBlue);
                fishly.maxBlue = blueSlider.getValue();
            }
        });

        configPanel.add(toggleButton);

        configPanel.setBounds(0, 0, 200, 600);
        canvas.setBounds(200, 0, 600, 600);

        toggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Fishly.isDisabled = !Fishly.isDisabled;
                if(Fishly.isDisabled){
                    toggleButton.setText("Enable");
                }else{
                    toggleButton.setText("Disable");
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                scanning = true;
            }
            public void mouseExited(MouseEvent e) {
                super.mouseEntered(e);
                scanning = false;
            }
        });
    }

    public void showPreview(BufferedImage screenshot, int highestRed, int foundX){
        if(screenshot == null){
            return;
        }

        double focusX = 0;
        double focusY = 0;

        if(scanning){
            Point mousePos = canvas.getMousePosition();
            if(mousePos != null){
                double xMod = ((double)screenshot.getWidth()) / ((double)canvas.getWidth());
                double yMod = ((double)screenshot.getHeight()) / ((double)canvas.getHeight());
                focusX = mousePos.x * xMod;
                focusY = mousePos.y * yMod;
                Color color = new Color(screenshot.getRGB((int)focusX, (int)focusY));
            }
        }

        BufferStrategy bs = canvas.getBufferStrategy();
        if(bs == null){
            canvas.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0, canvas.getWidth(), canvas.getHeight());

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

        g.drawImage(screenshot, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        g.drawImage(overlay, 0, 0, canvas.getWidth(), canvas.getHeight(), null);

        g.dispose();
        bs.show();
    }

    public void onOpen() {
        fishly.start(this);
    }

    public void onClose() {
        fishly.stop();
    }
}
