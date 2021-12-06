package ui;

import model.Menu;
import model.Pesanan;
import model.Transaksi;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Menus extends JFrame {
    public static JComboBox menuCombo, hargaCombo;
    public Label nama, antrian;
    static Vector record = new Vector(10,10);
    DefaultComboBoxModel menuComboBox = new DefaultComboBoxModel(new String[]{"Pilih Menu"});
    DefaultComboBoxModel hargaComboBox = new DefaultComboBoxModel(new String[]{""});
    private static Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String Snama;
    static public  JTable table;
    DefaultTableModel model = new DefaultTableModel(new String[]{"Nama Menu", "Harga Satuan", "Jumlah", "Harga per Pesanan"}, 0 );
    private double total;

    public Menus(Socket socket, String name) {
        try {
            Menus.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.Snama = name;

            Image icon = Toolkit.getDefaultToolkit().getImage((getClass().getResource("/assets/logo.png")));
            setIconImage(icon);
            receivedObject(menuComboBox, hargaComboBox);
            setTitle("Restaurant Client");
            GUIMenu();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (IOException e) {
            closeEverything(socket, out, in);
        }
    }

    public void GUIMenu(){
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Container cp = getContentPane();
        cp.setLayout(gb);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(gb);

        JLabel LNama = new JLabel("Nama:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(LNama, gbc);
        menuPanel.add(LNama);

        nama = new Label(this.Snama);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gb.setConstraints(nama, gbc);
        menuPanel.add(nama);

        JLabel LTotal = new JLabel("Total:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gb.setConstraints(LTotal, gbc);
        menuPanel.add(LTotal);

        Label totalLabel = new Label(String.valueOf(total));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gb.setConstraints(totalLabel, gbc);
        menuPanel.add(totalLabel);

        JLabel LAntrian = new JLabel("Antrian:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gb.setConstraints(LAntrian, gbc);
        menuPanel.add(LAntrian);

        antrian = new Label("");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gb.setConstraints(antrian, gbc);
        menuPanel.add(antrian);

        JButton btShift = new JButton("Batal Pesan");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gb.setConstraints(btShift, gbc);

        btShift.addActionListener(e -> {
            new Register();
            dispose();
        });

        menuPanel.add(btShift);

        JButton btGaji = new JButton("Bayar");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.ipady = 2;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gb.setConstraints(btGaji, gbc);

        btGaji.addActionListener(e -> {
            if (totalLabel.getText().equals("0.0")) {
                JOptionPane.showMessageDialog(null, "Pesanan Masih Kosong!");
            } else {
                record.removeAllElements();

                Transaksi transaksi = new Transaksi();
                transaksi.pesanan = new Vector(10, 10);
                transaksi.pesanan.removeAllElements();

                transaksi.pemesan = nama.getText();
                transaksi.antrian = antrian.getText();
                transaksi.total = totalLabel.getText();

                for (int i = 0; i < table.getRowCount(); i++) {
                    Pesanan pesanan = new Pesanan();
                    pesanan.menu = (String) table.getValueAt(i, 0);
                    pesanan.jumlah = (String) table.getValueAt(i, 2);
                    pesanan.harga = (double) table.getValueAt(i, 3);
                    transaksi.pesanan.addElement(pesanan);
                }
                record.addElement(transaksi);

                try {
                    Socket socket = new Socket("127.0.0.1", 7896);
                    Report report = new Report(socket, record);
                    report.sendObject(record);
                    dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        menuPanel.add(btGaji);

        JPanel panelTabelMenu = new JPanel();
        panelTabelMenu.setLayout(gb);

        table = new JTable(model);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gb.setConstraints(table, gbc);
        panelTabelMenu.add(table);

        panelTabelMenu.add(new JScrollPane(table));

        panelTabelMenu.setBorder(BorderFactory.createTitledBorder("Daftar Pesanan"));

        JPanel shiftPanelKanan = new JPanel();
        shiftPanelKanan.setLayout(gb);

        menuPanel.setBorder(BorderFactory.createTitledBorder("Selamat Datang"));

        JPanel panelNama = new JPanel();
        panelNama.setLayout(gb);
        panelNama.setBorder(BorderFactory.createEtchedBorder());

        JLabel Laplikasi =  new JLabel("2N Restaurant");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelNama.add(Laplikasi, gbc);

        JPanel pesanPanel = new JPanel();
        pesanPanel.setLayout(gb);

        JLabel LPesan1 = new JLabel("Menu:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gb.setConstraints(LPesan1, gbc);
        pesanPanel.add(LPesan1);

        menuCombo = new JComboBox(menuComboBox);
        menuCombo.setSize(15,3);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gb.setConstraints(menuCombo, gbc);
        pesanPanel.add(menuCombo);

        menuCombo.addActionListener(e -> {
            if(menuCombo.getSelectedIndex() >= 0) {
                hargaCombo.setSelectedIndex(menuCombo.getSelectedIndex());
            }
        });

        JLabel LPesan2 = new JLabel("Harga:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(LPesan2, gbc);
        pesanPanel.add(LPesan2);

        hargaCombo = new JComboBox(hargaComboBox);
        hargaCombo.setSize(15,3);
        hargaCombo.setSelectedItem(null);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gb.setConstraints(hargaCombo, gbc);
        pesanPanel.add(hargaCombo);

        hargaCombo.addActionListener(e -> {
            hargaCombo.setEditable(false);
            if(hargaCombo.getSelectedIndex() >= 0) {
                menuCombo.setSelectedIndex(hargaCombo.getSelectedIndex());
            }
        });

        JLabel LJml1 = new JLabel("Jumlah: ");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gb.setConstraints(LJml1, gbc);
        pesanPanel.add(LJml1);

        JTextField jml1 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gb.setConstraints(jml1, gbc);
        pesanPanel.add(jml1);

        JButton btLogin = new JButton("Tambahkan Pesanan");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 4;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gb.setConstraints(btLogin, gbc);
        pesanPanel.add(btLogin);

        btLogin.addActionListener(e -> {
            if(Objects.equals(menuCombo.getSelectedItem(), "Pilih Menu")) {
                JOptionPane.showMessageDialog(null, "Tentukan Menu!");
            } else if (Objects.equals(hargaCombo.getSelectedItem(), "")){
                JOptionPane.showMessageDialog(null, "Tentukan Menu!");
            } else if (Objects.equals(jml1.getText(), "")) {
                JOptionPane.showMessageDialog(null, "Tentukan Jumlah Pembelian!");
            } else {
                DefaultTableModel model_table = (DefaultTableModel) table.getModel();
                model_table.addRow(new Object[] {
                        menuCombo.getSelectedItem(),
                        hargaCombo.getSelectedItem(),
                        jml1.getText(),
                        Double.parseDouble((String) Objects.requireNonNull(hargaCombo.getSelectedItem())) * Integer.parseInt(jml1.getText()),
                });
                total = total + Double.parseDouble((String) Objects.requireNonNull(hargaCombo.getSelectedItem())) * Integer.parseInt(jml1.getText());
                totalLabel.setText(String.valueOf(total));

                menuCombo.setSelectedIndex(0);
                hargaCombo.setSelectedIndex(0);
                jml1.setText("");
            }
        });

        pesanPanel.setBorder(BorderFactory.createTitledBorder("Pesanan"));

        cp.add(pesanPanel, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
        cp.add(menuPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
        cp.add(panelNama, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 2,
                0, 0), 0, 0));
        cp.add(panelTabelMenu,  new GridBagConstraints(1, 1, 1, 2, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
    }

    public void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();

            while(socket.isConnected()) {
                out.writeUTF(msg);
                out.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, out, in);
        }
    }


    public void receivedObject(DefaultComboBoxModel mCBmodel, DefaultComboBoxModel hCBmodel) {
        new Thread(() -> {
            try {
                Vector records = (Vector) in.readObject();

                out.writeUTF("Done");

                int i = 0;

                while(i < records.size()) {
                    model.Menu menu = (Menu) records.elementAt(i);
                    mCBmodel.addElement(menu.nama);
                    hCBmodel.addElement(menu.harga);
                    antrian.setText(String.valueOf(menu.antrian));
                    i++;
                }

                Menus.menuCombo.setModel(mCBmodel);
                Menus.hargaCombo.setModel(hCBmodel);
                closeEverything(socket, out, in);
            } catch (IOException | ClassNotFoundException e) {
                closeEverything(socket, out, in);
            }
        }).start();
    }

    public void closeEverything(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Koneksi Ditutup");
        }
    }

}