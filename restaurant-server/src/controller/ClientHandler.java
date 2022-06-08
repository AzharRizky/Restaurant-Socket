package controller;

import database.connection;
import model.Menu;
import model.Pesanan;
import model.Transaksi;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

public class ClientHandler extends Thread {
    private Socket socket;
    int increment;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    static Connection koneksi;
    static Vector records = new Vector(10,10);

    public ClientHandler(Socket socket, int angka) {
        try {
            this.socket = socket;
            increment = angka;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.start();
        } catch (IOException e) {
            closeEverything(socket, in, out);
        }
    }

    @Override
    public void run() {
        try {
            var x = in.readObject();
            if (x.equals("Menu")) {
                sendMenu();
            } else {
                receivedPesanan((Vector) x);
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            closeEverything(socket, in, out);
        }
    }

    public void receivedPesanan(Vector x) {
        try {
            koneksi = connection.connection;

            Statement state = koneksi.createStatement();

            Vector records = x;

            Calendar clr = new GregorianCalendar();
            int tanggal = clr.get(Calendar.DAY_OF_MONTH);
            int bulan = clr.get(Calendar.MONTH) + 1;
            int tahun = clr.get(Calendar.YEAR);

            Transaksi transaksi = (Transaksi) records.elementAt(0);
            String noPesanan = tahun + "" + bulan + "" + tanggal + "" + transaksi.antrian;
            state.executeUpdate("INSERT INTO transaksi (id, pemesan, antrian, total, status) VALUES ('" + noPesanan + "', '" + transaksi.pemesan + "', '" + transaksi.antrian + "', '" + transaksi.total + "', 'Belum Selesai');");

            int i = 0;

            while (i < transaksi.pesanan.size()) {
                Pesanan pesanan = (Pesanan) transaksi.pesanan.elementAt(i);
                state.executeUpdate("INSERT INTO penjualan (id_transaksi, menu, jumlah, harga) VALUES ('" + noPesanan + "', '" + pesanan.menu + "', '" + pesanan.jumlah + "', '" + pesanan.harga + "');");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendMenu() throws IOException, SQLException {
        //setting connection to databse
        koneksi = connection.connection;

        //create stament untuk query
        Statement state = koneksi.createStatement();

        //query buku tergantung pada publisher yg di tentukan
        ResultSet rs =  state.executeQuery("SELECT * FROM menu;");

        records.removeAllElements();

        while(rs.next()){
            Menu menu = new Menu();
            menu.antrian = increment;
            menu.nama = rs.getString("nama");
            menu.harga = rs.getString("harga");
            records.addElement(menu);
        }

        out.writeObject(records);

    }

    public void closeEverything(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
