package newtime.wow.praetor;

import newtime.wow.praetor.fishly.FishingTab;
import newtime.wow.praetor.fishly.Fishly;

import javax.swing.*;

public class Debug {

    private Container container;

    private FishingTab fishingTab;
    private Fishly fishly;

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }
        new Debug();
    }

    public Debug(){
        this.createDebugFrame();
    }

    private void createDebugFrame(){
        container = new Container();

        this.fishly = new Fishly();
        this.fishingTab = new FishingTab(fishly, container);
        this.container.addTab("Fishly", this.fishingTab);
        container.setVisible(true);

        this.fishly.start(fishingTab);
    }

}
