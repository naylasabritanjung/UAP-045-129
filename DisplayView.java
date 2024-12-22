
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DisplayView extends JPanel {

    private DisplayModel model;
    private Image image;
    private JButton button;

    public DisplayView(DisplayModel model) {
        this.model = model;

        try {
            image = ImageIO.read(getClass().getResource("/img/9098495.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gambar tidak ditemukan, coba periksa path/nama gambar!");
        }

        button = new JButton("Get Started!") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                GradientPaint gradient = new GradientPaint(0, 0, Color.ORANGE, width, height, Color.ORANGE.brighter());
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, width, height, 50, 50);

                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font("Verdana", Font.BOLD, 20));
                String text = getText();
                int textWidth = g2d.getFontMetrics().stringWidth(text);
                int textHeight = g2d.getFontMetrics().getAscent();
                int x = (width - textWidth) / 2;
                int y = (height + textHeight) / 2;

                g2d.drawString(text, x, y);
            }
        };

        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        this.setLayout(null);
        this.add(button);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButtonPosition();
            }
        });
        updateButtonPosition();
    }

    private void updateButtonPosition() {
        int panelWidth = getWidth();

        int buttonWidth = 350;
        int buttonHeight = 50;
        int x = (panelWidth - buttonWidth) / 2;
        int y = 500;

        button.setBounds(x, y, buttonWidth, buttonHeight);
    }

    public JButton getButton() {
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        frameGradasi(g2d);
        int pccGambar = ikonCuaca(g2d, image, 50);
        int teks1 = teksTengah(g2d, "Weather", pccGambar, Color.orange, 58);
        teksTengah(g2d, "Monitoring", pccGambar + 65, Color.white, 36);
    }

    private void frameGradasi(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        float angle = model.getAngle();

        float x1 = (float) (Math.cos(angle) * width / 2 + width / 2);
        float y1 = (float) (Math.sin(angle) * height / 2 + height / 2);
        float x2 = (float) (Math.cos(angle + Math.PI) * width / 2 + width / 2);
        float y2 = (float) (Math.sin(angle + Math.PI) * height / 2 + height / 2);

        GradientPaint gradient = new GradientPaint(x1, y1, model.getColor1(), x2, y2, model.getColor2());
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }

    private int ikonCuaca(Graphics2D g2d, Image image, int topY) {
        int lebar = getWidth();
        int lebarGambar = image.getWidth(null);
        int tinggiGambar = image.getHeight(null);

        int lebarGambarBaru = 300;
        int newHeight = (int) ((double) tinggiGambar / lebarGambar * lebarGambarBaru);

        int x = (lebar - lebarGambarBaru) / 2;
        int y = topY;

        g2d.drawImage(image, x, y, lebarGambarBaru, newHeight, null);
        return y + newHeight;
    }

    private int teksTengah(Graphics2D g2d, String text, int topY, Color color, int fontSize) {
        g2d.setColor(color);
        g2d.setFont(new Font("Verdana", Font.BOLD, fontSize));

        int lebar = getWidth();
        int lebarTeks = g2d.getFontMetrics().stringWidth(text);
        int ascent = g2d.getFontMetrics().getAscent();

        int x = (lebar - lebarTeks) / 2;
        int y = topY + ascent;

        g2d.drawString(text, x, y);
        return y;
    }
}