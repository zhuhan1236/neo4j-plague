package view;

/**
 * Created by zhuhan on 14/12/18.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

import plagueWorld.*;
import model.*;

public class PlagueWorldGUI extends JFrame{
    public PlagueWorldGUI gui;
    public PlagueWorld plagueWorld;
    public JFrame frame;
    public JButton loadB;

    public JButton infectB;
    public String infectSourceData;
    public JLabel infectionSourceL;
    public JTextField infectionSourceTF;

    public JButton timeB;
    public String timeData;
    public JLabel timeL;
    public JTextField timeTF;

    public JLabel infectListL;
    public String[] infectListData;
    public JList infectLi;
    public JScrollPane infectSP;

    public JLabel connectionListL;
    public String[] connectionListData;
    public JList connectionLi;
    public JScrollPane connectionSP;

    public PlagueWorldGUI(){
        gui = this;
    }

    public void init(PlagueWorld plagueWorld){
        this.plagueWorld = plagueWorld;
        frame = new JFrame("PlagueWorld");

        infectionSourceL = new JLabel("infect from");
        timeL = new JLabel("skip days");
        infectListL = new JLabel("plague infection");
        connectionListL = new JLabel("world connection");

        infectionSourceTF = new JTextField("Japan", 20);
        timeTF = new JTextField("1", 10);

        loadB = new JButton("load map");
        infectB = new JButton("infect");
        timeB = new JButton("go");

        infectLi = new JList();
        connectionLi = new JList();
        infectLi.setBorder(BorderFactory.createEtchedBorder());
        connectionLi.setBorder(BorderFactory.createEtchedBorder());
        infectSP = new JScrollPane(infectLi);
        infectSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        infectSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        connectionSP = new JScrollPane(connectionLi);
        connectionSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        connectionSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel operateP = new JPanel();
        JPanel displayP = new JPanel();

        frame.add(operateP, BorderLayout.NORTH);
        frame.add(displayP, BorderLayout.CENTER);

        operateP.setLayout(new GridBagLayout());
        operateP.add(loadB,
                    new GridBagConstraints(
                        1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 20, 0, 0), 0, 0));

        operateP.add(infectionSourceL,
                new GridBagConstraints(
                        0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 0, 0, 20), 0, 0));
        operateP.add(infectionSourceTF,
                new GridBagConstraints(
                        1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 0, 0, 20), 0, 0));
        operateP.add(infectB,
                new GridBagConstraints(
                        2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 20, 0, 0), 0, 0));

        operateP.add(timeL,
                new GridBagConstraints(
                        0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 0, 0, 20), 0, 0));
        operateP.add(timeTF,
                new GridBagConstraints(
                        1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 0, 0, 20), 0, 0));
        operateP.add(timeB,
                new GridBagConstraints(
                        2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 20, 0, 0), 0, 0));

        displayP.setLayout(new GridBagLayout());
        displayP.add(infectListL,
                new GridBagConstraints(
                        0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 200, 0, 100), 0, 0));
        displayP.add(connectionListL,
                new GridBagConstraints(
                        1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(5, 100, 0, 50), 0, 0));
        displayP.add(infectSP,
                new GridBagConstraints
                        (0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(5, 20, 0, 450), 0, 0));
        displayP.add(connectionSP,
                new GridBagConstraints
                        (1, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(5, 100, 0, 20), 0, 0));

        frame.setSize(900, 700);
        frame.setVisible(true);

        loadBRegister();
        infectBRegister();
        timeBRegister();
    }

    public void loadBRegister(){
        loadB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                plagueWorld.loadDatabase();
                loadB.setEnabled(false);
            }
        });
    }

    public void infectBRegister(){
        infectB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plagueWorld.infectCountry(infectionSourceTF.getText());
                infectB.setEnabled(false);
                infectionSourceTF.setEnabled(false);
            }
        });
    }

    public void timeBRegister(){
        timeB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plagueWorld.go(Integer.parseInt(timeTF.getText()));
            }
        });
    }
}
