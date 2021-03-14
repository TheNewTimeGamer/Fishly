package newtime.wow.praetor;

import javax.swing.*;

public class Container extends JTabbedPane {

    public JFrame frame;

    public Container(){
        frame = new JFrame();
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);
    }


}
