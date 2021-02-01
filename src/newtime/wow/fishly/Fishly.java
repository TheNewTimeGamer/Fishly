package newtime.wow.fishly;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Fishly implements Runnable {

	public static final boolean DEBUG_PREVIEW = true;

	public static volatile int maxHue = 345;
	public static volatile int minHue = 17;

	public static volatile int minRed = 100;

	public static volatile int maxBlue = 100;
	public static volatile int maxGreen = 100;

	public static volatile int deltaThreshold = 4;

	public static final int X_OFFSET = 700;
	public static final int Y_OFFSET = 260;

	public static final int MAX_FISHING_COUNTER = 60*20;

	public static volatile BufferedImage screen;
	public static volatile int foundX = 0;
	public static volatile int highestRed = Integer.MAX_VALUE;

	public static Robot robot;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception ignored){}
		try {
			Fishly.robot = new Robot();
		}catch(Exception e){
			e.printStackTrace();
		}
		new Fishly();
	}
	
	private Thread spottingThread;

	public static volatile boolean isDisabled = true;

	public Fishly() {
		this.start();
	}
	
	private void start() {
		if(spottingThread != null) {
			System.err.println("Spotting thread already active!");
		}
		this.spottingThread = new Thread(this);
		this.spottingThread.start();

		System.out.println("Loading preview frame..");
		while(true) {
			Fishly.screen = this.robot.createScreenCapture(new Rectangle(X_OFFSET, Y_OFFSET, 500, 400));
			Debug.showPreview(screen, highestRed, foundX);
		}
	}
	
	private void stop() {
		if(spottingThread == null) {
			System.err.println("Spotting thread already dead!");
		}
	}
	
	
	private boolean running = true;

	public void run() {
		long last = 0;

		while(running) {
			if(System.currentTimeMillis() - last > (1000/60)){
				screenCap();
				last = System.currentTimeMillis();
			}
		}
		
	}

	int deltaCounter = 60;
	int fishingCounter = MAX_FISHING_COUNTER;

	int previousHighestRed = Integer.MAX_VALUE;

	boolean fishing = false;

	private void performDelay(int miliseconds){
	    try{
	        Thread.sleep(miliseconds);
        }catch(Exception e){
	        e.printStackTrace();
        }
    }

	private void screenCap() {
		if(!fishing){
            int variation = (int)(Math.random()*200);
            performDelay(1000+variation);
			startFishing();
            performDelay(1350+variation);
		}

		Fishly.foundX = 0;
		Fishly.highestRed = Integer.MAX_VALUE;

		int r, g, b;
		Color color;

		for(int y = 0; y < screen.getHeight(); y++){
			for(int x = 0; x < screen.getWidth(); x++){
				color = new Color(screen.getRGB(x,y));
				r = color.getRed();
				g = color.getGreen();
				b = color.getBlue();

				if(isValidFeatherColor(r, g, b) && r > Fishly.minRed && g < Fishly.maxGreen && b < Fishly.maxBlue && isValidArea(x,y)) {
					highestRed = y;
					foundX = x;
				}

			}
		}

		if(fishingCounter-- <= 0){
		    System.out.println("Fishing took too long, resetting..");
		    fishing = false;
        }

		if(deltaCounter-- <= 0) {
			int delta = previousHighestRed - highestRed;
			if (Math.abs(delta) > Fishly.deltaThreshold && Math.abs(delta) < 1000) {
			    System.out.println("Delta:" + delta);
				catchFish(foundX, highestRed);
			}
			previousHighestRed = highestRed;
            deltaCounter = 5;
		}
	}

	private boolean isValidArea(int x, int y){
		return true;
	}

	// If Red is max, then Hue = (G-B)/(max-min)
	public static int getHue(int red, int green, int blue) {
		float min = Math.min(Math.min(red, green), blue);
		float max = Math.max(Math.max(red, green), blue);

		if (min == max) {
			return 0;
		}

		float hue = 0f;
		if (max == red) {
			hue = (green - blue) / (max - min);

		} else if (max == green) {
			hue = 2f + (blue - red) / (max - min);

		} else {
			hue = 4f + (red - green) / (max - min);
		}

		hue = hue * 60;
		if (hue < 0) hue = hue + 360;

		return Math.round(hue);
	}

	public boolean isValidFeatherColor(int red, int green, int blue){
		int hue = getHue(red, green, blue);
	    if(hue < Fishly.minHue || hue > Fishly.maxHue){
	        return true;
        }
	    return false;
    }

    public boolean isValidBobber(BufferedImage screen, int x, int y){
	    if(x+25 >= screen.getWidth() || y+10 >= screen.getHeight()){
	        return false;
        }
	    Color color = new Color(screen.getRGB(x+25,y+10));
	    if(!isMajorityBlue(color.getRed(), color.getGreen(), color.getBlue())) {
	        return true;
        }
	    return false;
    }

    public boolean isMajorityBlue(int red, int green, int blue){
        if(blue < red) {
            return false;
        }
	    return true;
    }

	public void catchFish(int x, int y){
		if(!fishing || isDisabled){return;}

		robot.mouseMove(x+X_OFFSET, y+Y_OFFSET);
		robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);

		int variation = (int)(Math.random()*75);

		performDelay(325 + variation);

		robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);

        performDelay(300 + variation);

		fishing = false;
	}

	public void startFishing(){
		if(isDisabled){return;}

        fishingCounter = MAX_FISHING_COUNTER;
		previousHighestRed = Integer.MAX_VALUE;

		robot.keyPress(KeyEvent.VK_8);
		int variation = (int)(Math.random()*200);

		performDelay(25 + variation);

		robot.keyRelease(KeyEvent.VK_8);
		fishing = true;
	}
	
}
