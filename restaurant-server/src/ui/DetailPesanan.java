package ui;

import database.connection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DetailPesanan extends JFrame {
    static Connection koneksi;
    static JTable tablePesanan;
    DefaultTableModel modelPesanan = new DefaultTableModel(new Object[]{"ID", "ID Transaksi", "Nama Menu", "Jumlah", "Harga Satuan", "Total Harga"}, 0);
    private static String id, pemesan, antrian, total, status;

    public DetailPesanan(String id) {
        DetailPesanan.id = id;
        dataDetail();

        Image icon = Toolkit.getDefaultToolkit().getImage((getClass().getResource("/assets/logo.png")));
        setIconImage(icon);
        setTitle("Detail Pesanan");
        GUIDetailPesanan();
        pesananTable(modelPesanan);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void GUIDetailPesanan() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Container cp = getContentPane();
        cp.setLayout(gb);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        JPanel transaksiPanel = new JPanel();
        transaksiPanel.setLayout(gb);
        transaksiPanel.setBorder(BorderFactory.createTitledBorder("Detail Transaksi"));

        JLabel Lid = new JLabel("ID Transaksi: ");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gb.setConstraints(Lid, gbc);
        transaksiPanel.add(Lid);

        Label idLabel = new Label(id);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gb.setConstraints(idLabel, gbc);
        transaksiPanel.add(idLabel);

        JLabel LPemesan = new JLabel("Nama Pemesan: ");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(LPemesan, gbc);
        transaksiPanel.add(LPemesan);

        Label pemesanLabel = new Label(pemesan);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gb.setConstraints(pemesanLabel, gbc);
        transaksiPanel.add(pemesanLabel);

        JLabel LAntrian = new JLabel("No Antrian: ");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gb.setConstraints(LAntrian, gbc);
        transaksiPanel.add(LAntrian);

        Label antrianLabel = new Label(antrian);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gb.setConstraints(antrianLabel, gbc);
        transaksiPanel.add(antrianLabel);

        JLabel LTotal = new JLabel("Total: ");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gb.setConstraints(LTotal, gbc);
        transaksiPanel.add(LTotal);

        Label totalLabel = new Label(total);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gb.setConstraints(totalLabel, gbc);
        transaksiPanel.add(totalLabel);

        JLabel LStatus = new JLabel("Status: ");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gb.setConstraints(LStatus, gbc);
        transaksiPanel.add(LStatus);

        Label statusLabel = new Label(status);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 4;
        gb.setConstraints(statusLabel, gbc);
        transaksiPanel.add(statusLabel);

        if (statusLabel.getText().equals("Belum Selesai")) {
            JButton btStatus = new JButton("Selesai Dimasak");
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridwidth = 2;
            gbc.ipady = 2;
            gbc.gridx = 0;
            gbc.gridy = 5;
            gb.setConstraints(btStatus, gbc);

            transaksiPanel.add(btStatus);

            btStatus.addActionListener(e -> {
                updateStatus("Selesai");
                dispose();
            });
        }

        JButton btBack = new JButton("Tutup dan Kembali");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gb.setConstraints(btBack, gbc);
        transaksiPanel.add(btBack);
        btBack.addActionListener(e -> {
            DefaultTableModel modelPesanan = new DefaultTableModel(new String[]{"ID", "Pemesan", "Antrian", "Total", "Status", "Aksi"}, 0 );
            ServerMenu.pesananTable(modelPesanan);
            ServerMenu.tablePesanan.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            ServerMenu.tablePesanan.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField()));
            dispose();
        });

        JPanel pesananPanel = new JPanel();
        pesananPanel.setLayout(gb);

        tablePesanan = new JTable(modelPesanan);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gb.setConstraints(tablePesanan, gbc);
        pesananPanel.add(tablePesanan);

        pesananPanel.add(new JScrollPane(tablePesanan));

        cp.add(panelNama, new GridBagConstraints(0, 0, 3, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 2,
                0, 0), 0, 0));
        cp.add(transaksiPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
        cp.add(pesananPanel, new GridBagConstraints(1, 1, 1, 2, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
    }

    static public void pesananTable(DefaultTableModel modelPesanan) {
        try {
            koneksi = connection.connection;

            //create stament untuk query
            Statement state = koneksi.createStatement();

            //query buku tergantung pada publisher yg di tentukan
            ResultSet rs = state.executeQuery("SELECT * FROM penjualan WHERE id_transaksi = '" + id + "';");

            //overwrite model tabel
            modelPesanan.setRowCount(0);

            while (rs.next()) {
                String id = rs.getString("id");
                String idTransaksi = rs.getString("id_transaksi");
                String menu = rs.getString("menu");
                String jumlah = rs.getString("jumlah");
                String harga = rs.getString("harga");
                double hargaSatuan = Double.parseDouble(harga)/Integer.parseInt(jumlah);
                modelPesanan.addRow(new Object[]{id, idTransaksi, menu, jumlah, String.valueOf(hargaSatuan), harga});
            }

            tablePesanan.setModel(modelPesanan);

        } catch (SQLException s) {
            s.printStackTrace();
        }

    }

    public void updateStatus(String status) {
        try {
            koneksi = connection.connection;

            Statement state = koneksi.createStatement();

            state.executeUpdate("UPDATE transaksi SET status = '" + status + "' WHERE id ='" + id + "';");

            DefaultTableModel modelPesanan = new DefaultTableModel(new String[]{"ID", "Pemesan", "Antrian", "Total", "Status", "Aksi"}, 0 );
            ServerMenu.pesananTable(modelPesanan);
            ServerMenu.tablePesanan.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            ServerMenu.tablePesanan.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dataDetail(){
        try {
            koneksi = connection.connection;

            Statement state = koneksi.createStatement();

            ResultSet rs = state.executeQuery("SELECT * FROM transaksi WHERE id ='" + id + "';");

            while (rs.next()) {
                DetailPesanan.pemesan = rs.getString("pemesan");
                DetailPesanan.antrian = rs.getString("antrian");
                DetailPesanan.total = rs.getString("total");
                DetailPesanan.status = rs.getString("status");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
