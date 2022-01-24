package ui;

import database.connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerStart extends JFrame implements ActionListener {

    public ServerStart() {

        //Image icon = Toolkit.getDefaultToolkit().getImage((getClass().getResource("logo.png")));
        //setIconImage(icon);
        setTitle("Restaurant Server");
        GUIServer();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void GUIServer() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Container cp = getContentPane();
        cp.setLayout(gb);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(gb);

        JButton btStart = new JButton("Nyalakan Server");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 4;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gb.setConstraints(btStart, gbc);

        registerPanel.add(btStart);

        btStart.addActionListener(this);

        registerPanel.setBorder(BorderFactory.createTitledBorder("Server"));

        JPanel panelNama = new JPanel();
        panelNama.setLayout(gb);
        panelNama.setBorder(BorderFactory.createEtchedBorder());

        JLabel Laplikasi =  new JLabel("2N Restaurant Server");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelNama.add(Laplikasi, gbc);

        cp.add(registerPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
        cp.add(panelNama, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 2,
                0, 0), 0, 0));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            ServerMenu serverMenu = new ServerMenu();
            serverMenu.startServer();
            dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        connection.setConnection();
        new ServerStart();
    }
}

