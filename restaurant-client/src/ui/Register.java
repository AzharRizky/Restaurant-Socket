package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import javax.swing.*;

class Register extends JFrame implements ActionListener {
    static JTextField name;

    public Register() {
        Image icon = Toolkit.getDefaultToolkit().getImage((getClass().getResource("/assets/logo.png")));
        setIconImage(icon);
        setTitle("Restaurant Client | Register");
        GUIRegister();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void GUIRegister() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Container cp = getContentPane();
        cp.setLayout(gb);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(gb);

        JLabel LNIP = new JLabel("Masukkan Nama:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gb.setConstraints(LNIP, gbc);
        registerPanel.add(LNIP);

        name = new JTextField(16);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gb.setConstraints(name, gbc);
        registerPanel.add(name);

        JButton btLogin = new JButton("Pesan Sekarang");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 4;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gb.setConstraints(btLogin, gbc);
        registerPanel.add(btLogin);

        btLogin.addActionListener(this);

        registerPanel.setBorder(BorderFactory.createTitledBorder("Register your name"));

        JPanel panelNama = new JPanel();
        panelNama.setLayout(gb);
        panelNama.setBorder(BorderFactory.createEtchedBorder());

        JLabel Laplikasi =  new JLabel("Selamat Datang di 2N Restaurant");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelNama.add(Laplikasi, gbc);

        JPanel gambar = new JPanel();
        gambar.setLayout(gb);
        gambar.setBorder(BorderFactory.createEtchedBorder());
        var image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/menu.jpg")));
        gambar.add(new JLabel("", image, SwingConstants.CENTER));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gambar.setBounds((int) ((screenSize.getWidth() - image.getIconWidth()) / 2),
                (int) ((screenSize.getHeight() - image.getIconHeight()) / 2),
                image.getIconWidth(), image.getIconHeight());

        cp.add(registerPanel, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
        cp.add(panelNama, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 2,
                0, 0), 0, 0));
        cp.add(gambar, new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 2,
                0, 0), 0, 0));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            String nama = name.getText();
            if(Objects.equals(nama, "")) {
                JOptionPane.showMessageDialog(null, "Harap Masukkan Nama!");
            } else {
                Socket socket = new Socket("127.0.0.1", 7896);
                Menus menu = new Menus(socket, nama);
                menu.sendMessage("Menu");
                dispose();
            }
        } catch (UnknownHostException e) {
            System.out.println("Sock: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Failed to connect, because Server was down or wrong IP/Port");
            System.out.println("IO: " + e.getMessage());
        }
    }

}
