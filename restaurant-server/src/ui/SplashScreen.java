package ui;

import database.connection;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SplashScreen {
    private final JWindow window;
    private long startTime;
    private int minimumMilliseconds;

    public SplashScreen() {
        window = new JWindow();

        var image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/splash.jpg")));
        window.getContentPane().add(new JLabel("", image, SwingConstants.CENTER));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setBounds((int) ((screenSize.getWidth() - image.getIconWidth()) / 2),
                (int) ((screenSize.getHeight() - image.getIconHeight()) / 2),
                image.getIconWidth(), image.getIconHeight());
    }

    public void show(int minimumMilliseconds) {
        this.minimumMilliseconds = minimumMilliseconds;

        connection.setConnection();

        window.setVisible(true);
        startTime = System.currentTimeMillis();
    }

    public void hide() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        try {
            Thread.sleep(Math.max(minimumMilliseconds - elapsedTime, 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();
    }

    public static void main(String[] args) {
        var splash = new SplashScreen();
        splash.show(7000);
        splash.hide();
        new ServerStart();
    }
}