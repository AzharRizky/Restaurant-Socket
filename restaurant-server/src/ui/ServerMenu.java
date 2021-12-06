package ui;

import controller.ClientHandler;
import database.connection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerMenu extends JFrame {
    static Connection koneksi;
    private final ServerSocket serverSocket;
    String[] menuComboBox = listMenu();
    static JTable tableMenu, tablePesanan;
    public static JComboBox menuComboEdit, menuComboDel;
    DefaultTableModel modelMenu = new DefaultTableModel(new String[]{"ID", "Nama Menu", "Harga"}, 0 );
    DefaultTableModel modelPesanan = new DefaultTableModel(new String[]{"ID", "Pemesan", "Antrian", "Total", "Status", "Aksi"}, 0 );

    public ServerMenu() throws IOException {
        this.serverSocket = new ServerSocket(7896);
        Image icon = Toolkit.getDefaultToolkit().getImage((getClass().getResource("/assets/logo.png")));
        setIconImage(icon);
        setTitle("Restaurant Server");
        GUIServerMenu();
        menuTable(modelMenu);
        pesananTable(modelPesanan);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void GUIServerMenu() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Container cp = getContentPane();
        cp.setLayout(gb);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelNama = new JPanel();
        panelNama.setLayout(gb);
        panelNama.setBorder(BorderFactory.createEtchedBorder());

        JLabel Laplikasi =  new JLabel("2N Restaurant Server Menu");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelNama.add(Laplikasi, gbc);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(gb);

        JButton btOff = new JButton("Matikan Server");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gb.setConstraints(btOff, gbc);

        registerPanel.add(btOff);

        btOff.addActionListener(e -> {
            closeServerSocket();
            new ServerStart();
            dispose();
        });

        registerPanel.setBorder(BorderFactory.createTitledBorder("Selamat Datang Admin"));

        JPanel crudMenuPanel = new JPanel();
        crudMenuPanel.setLayout(gb);

        JLabel labelTambah = new JLabel("Tambah Menu");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gb.setConstraints(labelTambah, gbc);
        crudMenuPanel.add(labelTambah);

        Label labelMenu = new Label("Nama Menu:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(labelMenu, gbc);
        crudMenuPanel.add(labelMenu);

        JTextField namaMenu = new JTextField(8);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gb.setConstraints(namaMenu, gbc);
        crudMenuPanel.add(namaMenu);

        Label labelHarga = new Label("Harga Menu:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gb.setConstraints(labelHarga, gbc);
        crudMenuPanel.add(labelHarga);

        JTextField hargaMenu = new JTextField(8);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gb.setConstraints(hargaMenu, gbc);
        crudMenuPanel.add(hargaMenu);

        JButton btTambah = new JButton("Tambah Menu");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gb.setConstraints(btTambah, gbc);
        crudMenuPanel.add(btTambah);

        btTambah.addActionListener(e -> {
            if (namaMenu.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Harap isi Nama Menu!");
            } else if (hargaMenu.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Harap isi Harga Menu!");
            } else {
                addMenu(namaMenu.getText(), hargaMenu.getText());
                JOptionPane.showMessageDialog(null, "Menu Berhasil Ditambahkan");
                namaMenu.setText("");
                hargaMenu.setText("");
            }
        });

        JLabel labelEdit = new JLabel("Edit Menu");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gb.setConstraints(labelEdit, gbc);
        crudMenuPanel.add(labelEdit);

        Label labelEditPilih = new Label("Pilih Menu:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gb.setConstraints(labelEditPilih, gbc);
        crudMenuPanel.add(labelEditPilih);

        menuComboEdit = new JComboBox(menuComboBox);
        menuComboEdit.setSize(15,3);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 5;
        gb.setConstraints(menuComboEdit, gbc);
        crudMenuPanel.add(menuComboEdit);

        Label labelMenuEdit = new Label("Nama Menu:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gb.setConstraints(labelMenuEdit, gbc);
        crudMenuPanel.add(labelMenuEdit);

        JTextField editNamaMenu = new JTextField(8);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 6;
        gb.setConstraints(editNamaMenu, gbc);
        crudMenuPanel.add(editNamaMenu);

        Label labelEditHarga = new Label("Harga Menu:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gb.setConstraints(labelEditHarga, gbc);
        crudMenuPanel.add(labelEditHarga);

        JTextField editHargaMenu = new JTextField(8);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 7;
        gb.setConstraints(editHargaMenu, gbc);
        crudMenuPanel.add(editHargaMenu);

        JButton btEdit = new JButton("Edit Menu");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 8;
        gb.setConstraints(btEdit, gbc);
        crudMenuPanel.add(btEdit);

        btEdit.addActionListener(e -> {
            if (editNamaMenu.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Harap isi Nama Menu!");
            } else if (editHargaMenu.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Harap isi Harga Menu!");
            } else {
                updateMenu((String) menuComboEdit.getSelectedItem(), editNamaMenu.getText(), editHargaMenu.getText());
                JOptionPane.showMessageDialog(null, "Menu Berhasil Diupdate");
                editNamaMenu.setText("");
                editHargaMenu.setText("");
                menuComboEdit.setSelectedIndex(0);
            }
        });

        JLabel labelDelete = new JLabel("Delete Menu");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 9;
        gb.setConstraints(labelDelete, gbc);
        crudMenuPanel.add(labelDelete);

        Label labelDeletePilih = new Label("Pilih Menu:");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 10;
        gb.setConstraints(labelDeletePilih, gbc);
        crudMenuPanel.add(labelDeletePilih);

        menuComboDel = new JComboBox(menuComboBox);
        menuComboDel.setSize(15,3);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 10;
        gb.setConstraints(menuComboDel, gbc);
        crudMenuPanel.add(menuComboDel);

        JButton btDelete = new JButton("Hapus Menu");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 11;
        gb.setConstraints(btDelete, gbc);
        crudMenuPanel.add(btDelete);

        btDelete.addActionListener(e -> {
            deleteMenu((String) menuComboDel.getSelectedItem());
            JOptionPane.showMessageDialog(null, "Menu Berhasil Dihapus");
            menuComboDel.setSelectedIndex(0);
        });

        crudMenuPanel.setBorder(BorderFactory.createTitledBorder("Edit Menu Restaurant"));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(gb);

        JButton btRefreshMenu = new JButton("Refresh Menu");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(btRefreshMenu, gbc);
        menuPanel.add(btRefreshMenu);
        btRefreshMenu.addActionListener(e -> {
            menuTable(modelMenu);
        });

        tableMenu = new JTable(modelMenu);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(tableMenu, gbc);
        menuPanel.add(tableMenu);
        menuPanel.add(new JScrollPane(tableMenu));

        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu Restaurant"));

        JPanel pesananPanel = new JPanel();
        pesananPanel.setLayout(gb);

        JButton btRefreshPesanan = new JButton("Refresh Pesanan");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(btRefreshPesanan, gbc);
        pesananPanel.add(btRefreshPesanan);
        btRefreshPesanan.addActionListener(e -> {
            pesananTable(modelPesanan);
            tablePesanan.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            tablePesanan.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField()));
        });

        tablePesanan = new JTable(modelPesanan);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(tablePesanan, gbc);
        pesananPanel.add(tablePesanan);

        pesananPanel.add(new JScrollPane(tablePesanan));

        pesananPanel.setBorder(BorderFactory.createTitledBorder("Pesanan Restaurant"));

        cp.add(panelNama, new GridBagConstraints(0, 0, 3, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 2,
                0, 0), 0, 0));
        cp.add(registerPanel, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
        cp.add(crudMenuPanel, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
        cp.add(menuPanel, new GridBagConstraints(0, 1, 1, 2, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
        cp.add(pesananPanel, new GridBagConstraints(2, 1, 1, 2, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
    }

    public void addMenu(String nama, String harga) {
        try {
            koneksi = connection.connection;

            Statement state = koneksi.createStatement();

            state.executeUpdate("INSERT INTO menu (nama, harga) VALUES ('" + nama + "', '" + harga + "')");

            menuTable(modelMenu);
            menuComboEdit.addItem(nama);
            menuComboDel.addItem(nama);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMenu(String nama, String namaUpdate, String harga) {
        try {
            koneksi = connection.connection;

            Statement state = koneksi.createStatement();

            state.executeUpdate("UPDATE menu SET nama = '" + namaUpdate + "', harga = '" + harga + "' WHERE nama ='" + nama + "';");

            menuTable(modelMenu);
            menuComboEdit.removeItem(nama);
            menuComboEdit.addItem(namaUpdate);
            menuComboDel.removeItem(nama);
            menuComboDel.addItem(namaUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenu(String nama) {
        try {
            koneksi = connection.connection;

            Statement state = koneksi.createStatement();

            state.executeUpdate("DELETE FROM menu WHERE nama = '" + nama + "';");

            menuTable(modelMenu);
            menuComboEdit.removeItem(nama);
            menuComboDel.removeItem(nama);
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    static public void menuTable(DefaultTableModel modelMenu) {
        try {
            koneksi = connection.connection;

            //create stament untuk query
            Statement state = koneksi.createStatement();

            //query buku tergantung pada publisher yg di tentukan
            ResultSet rs = state.executeQuery("SELECT * FROM menu;");

            //overwrite model tabel
            modelMenu.setRowCount(0);

            while (rs.next()) {
                String id = rs.getString("id");
                String nama = rs.getString("nama");
                String harga = rs.getString("harga");
                modelMenu.addRow(new Object[]{id, nama, harga});
            }

            tableMenu.setModel(modelMenu);

        } catch (SQLException s) {
            s.printStackTrace();
        }

    }

    public static void pesananTable(DefaultTableModel modelPesanan) {
        try {
            koneksi = connection.connection;

            //create stament untuk query
            Statement state = koneksi.createStatement();

            ResultSet rs = state.executeQuery("SELECT * FROM transaksi;");

            //overwrite model tabel
            modelPesanan.setRowCount(0);

            while (rs.next()) {
                String id = rs.getString("id");
                String pemesan = rs.getString("pemesan");
                String antrian = rs.getString("antrian");
                String total = rs.getString("total");
                String status = rs.getString("status");
                modelPesanan.addRow(new Object[]{id, pemesan, antrian, total, status, id});
            }
            tablePesanan.setModel(modelPesanan);
            tablePesanan.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            tablePesanan.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField()));
        } catch (SQLException s) {
            s.printStackTrace();
        }

    }

    static String[] listMenu() {
        try {

            koneksi = connection.connection;

            //create stament untuk query
            Statement state = koneksi.createStatement();
            ResultSet rs = state.executeQuery("SELECT COUNT(nama) AS jumlah FROM menu;");
            rs.next();
            String[] data = new String[rs.getInt("jumlah")];

            rs = state.executeQuery("SELECT * FROM menu");
            int count = 0;
            while (rs.next()){
                data[count] = rs.getString("nama") ;
                count += 1;
            }
            return data;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        new Thread(() -> {
            try {
                //Get Date & Time
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                System.out.println("Server ON (" + formatter.format(date) + ")");

                //Initialize Port & Increment Message
                int angka = 1;

                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket, angka);
                    angka++;
                }
            } catch (UnknownHostException e) {
                System.out.println("Sock: " + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }
        }).start();
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object object, boolean selected, boolean focused, int row, int col) {
        setText("Details");

        return this;
    }

}

class ButtonEditor extends DefaultCellEditor {
    protected JButton detail = new JButton();
    private String id;
    private Boolean click;

    public ButtonEditor(JTextField text) {
        super(text);
        detail.setOpaque(true);

        detail.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object object, boolean selected, int row, int col) {
        detail.setText("Details");
        id = object.toString();
        click = true;

        return detail;
    }

    @Override
    public Object getCellEditorValue() {
        if(click) {
            new DetailPesanan(id);
        }
        click = false;
        return "Details";
    }

    @Override
    public boolean stopCellEditing() {
        click = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
