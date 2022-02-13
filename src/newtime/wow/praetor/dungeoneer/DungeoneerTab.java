package newtime.wow.praetor.dungeoneer;

import newtime.wow.praetor.Tab;

import javax.swing.*;
import java.awt.event.*;

public class DungeoneerTab extends Tab {

    Dungeoneer dungeoneer;
    Map map;

    public DungeoneerTab(Dungeoneer dungeoneer){
        this.dungeoneer = dungeoneer;

        JLabel labelTargetX = new JLabel("Target X:");
        JLabel labelTargetY = new JLabel("Target Y:");
        JLabel labelTargetF = new JLabel("Target F:");

        JTextField targetX = new JTextField();
        JTextField targetY = new JTextField();
        JTextField targetF = new JTextField();

        JButton apply = new JButton("Apply");

        this.setLayout(null);

        labelTargetX.setBounds(5,5,100,20);
        targetX.setBounds(5,25,100,20);
        this.add(labelTargetX);
        this.add(targetX);

        labelTargetY.setBounds(5,45,100,20);
        targetY.setBounds(5,65,100,20);
        this.add(labelTargetY);
        this.add(targetY);

        labelTargetF.setBounds(5,85,100,20);
        targetF.setBounds(5,105,100,20);
        this.add(labelTargetF);
        this.add(targetF);

        apply.setBounds(5,135,100,20);
        this.add(apply);

        map = new Map("test.png");
        map.setBounds(125,0,500,500);
        this.add(map);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                map.setBounds(125,0,getWidth()-125,getHeight());
            }
        });

        map.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                double clickX = e.getX();
                double clickY = e.getY();
                double mapX = (clickX-5) / map.getWidth();
                double mapY = (clickY-5) / map.getHeight();
                map.targetLocation.x = mapX;
                map.targetLocation.y = mapY;
            }
        });

        apply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int x = Integer.parseInt(targetX.getText());
                    int y = Integer.parseInt(targetY.getText());
                    int f = Integer.parseInt(targetF.getText());
                    dungeoneer.targetPosition.x = x;
                    dungeoneer.targetPosition.y = y;
                    dungeoneer.targetPosition.face = f;
                    System.out.println("Manual position set to: " + dungeoneer.targetPosition.toString());
                }catch(Exception exception){
                    System.err.println("Wrong positional values!");
                }
            }
        });

    }

    public void onOpen() {
        dungeoneer.start(this);
    }

    public void onClose() {
        dungeoneer.stop();
    }
}
