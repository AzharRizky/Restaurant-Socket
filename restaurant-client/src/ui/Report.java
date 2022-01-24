package ui;

import model.Pesanan;
import model.Transaksi;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Report extends JFrame {
    private static Socket socket;
    private static Vector records;
    JTextArea struk = new JTextArea();

    public Report(Socket socket, Vector record) throws IOException {
        Report.socket = socket;
        Report.records = record;

        //Image icon = Toolkit.getDefaultToolkit().getImage((getClass().getResource("logo.png")));
        //setIconImage(icon);
        setTitle("Restaurant Client");
        GUIMenu();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void GUIMenu() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Container cp = getContentPane();
        cp.setLayout(gb);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(gb);

        struk = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gb.setConstraints(struk, gbc);
        menuPanel.add(struk);

        Calendar clr = new GregorianCalendar();
        int tanggal = clr.get(Calendar.DAY_OF_MONTH);
        int bulan = clr.get(Calendar.MONTH)+1;
        int tahun = clr.get(Calendar.YEAR);

        int jam = clr.get(Calendar.HOUR);
        int menit = clr.get(Calendar.MINUTE);
        int detik = clr.get(Calendar.SECOND);
        int am_pm = clr.get(Calendar.AM_PM);

        String siang_malam = "";
        if (am_pm == 1) {
            siang_malam = "PM";
        }
        else {
            siang_malam = "AM";
        }

        model.Transaksi transaksi = (Transaksi) records.elementAt(0);

        String noPesanan = tahun + "" + bulan + "" + tanggal + "" + transaksi.antrian;

        struk.append("\t\tTgl. " + tanggal + "/" + bulan + "/" + tahun +
                "   (" + jam + ":" + menit + ":" + detik + " " + siang_malam + ")");
        struk.append("\n=================================================\n"
                + "No Pesanan\t      :   " + noPesanan
                + "\nNama Pemesan     :   " + transaksi.pemesan
                + "\nNo Antrian\t      :   " + transaksi.antrian);
        struk.append("\n=================================================\n"
                + " Nama Menu\t      "
                + "|                 Jumlah"
                + " |\tHarga \n"
                + "=================================================\n");
        int i = 0;

        while(i < transaksi.pesanan.size()) {
            Pesanan pesanan = (Pesanan) transaksi.pesanan.elementAt(i);
            struk.append(" " + pesanan.menu + "\t      |\t      " + pesanan.jumlah + " |\t" + pesanan.harga + "\n");
            i++;
        }
        struk.append("=================================================\n"
                + "Total\t      :\t\t" + transaksi.total
                + "\n=================================================\n"
                + "\t               Terima Kasih"
                + "\n\tSelamat menikmati hidangan anda"
                + "\n Menu yang sudah dibeli tidak dapat ditukar atau dikembalikan");

        JButton btLogin = new JButton("Selesai Order");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 4;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gb.setConstraints(btLogin, gbc);
        menuPanel.add(btLogin);

        btLogin.addActionListener(e -> {
            new Register();
            dispose();
        });

        menuPanel.setBorder(BorderFactory.createTitledBorder("Struk Pembelianmu"));

        cp.add(panelNama, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 2,
                0, 0), 0, 0));
        cp.add(menuPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0,
                0, 0), 0, 0));
    }

    public void sendObject(Vector record) {
        try{
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(record);
            out.flush();
        } catch (IOException e) {
            closeEverything(socket);
        }

    }

    public void closeEverything(Socket socket) {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
