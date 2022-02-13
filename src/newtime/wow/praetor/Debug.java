package newtime.wow.praetor;

import newtime.wow.praetor.dungeoneer.Dungeoneer;
import newtime.wow.praetor.dungeoneer.DungeoneerTab;
import newtime.wow.praetor.fishly.FishingTab;
import newtime.wow.praetor.fishly.Fishly;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Debug {

    private Container container;

    private Tab currentTab;

    private FishingTab fishingTab;
    private Fishly fishly;

    private DungeoneerTab dungeoneerTab;
    private Dungeoneer dungeoneer;

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
        this.fishingTab = new FishingTab(fishly);
        this.container.addTab("Fishly", this.fishingTab);

        this.currentTab = this.fishingTab;

        this.dungeoneer = new Dungeoneer();
        this.dungeoneerTab = new DungeoneerTab(this.dungeoneer);
        this.container.addTab("Dungeoneer", this.dungeoneerTab);

        container.setVisible(true);

        container.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                currentTab.onClose();
                currentTab = (Tab)container.getSelectedComponent();
                currentTab.onOpen();
            }
        });

        this.fishly.start(fishingTab);
    }

}
