
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
    } //end constructor

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

    //frame 
    public void openNewPanel(DisplayModel model) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame == null) return;
        
        JPanel newPanel = new JPanel() {
            private String fullText = "Langit punya cerita, dan kami akan menyampaikannya! ";
            private StringBuilder displayedText = new StringBuilder();
            private Timer typingTimer;
              
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                frameGradasi(g2d);
                int pccGambar = ikonCuaca(g2d, image, 30);
                
                teksTengah(g2d, displayedText.toString(), 330, Color.white, 15);
            }

            //Timer untuk teks frame 2
            {
                typingTimer = new Timer(40, e -> {
                    if (displayedText.length() < fullText.length()) {
                        displayedText.append(fullText.charAt(displayedText.length()));
                        repaint();
                    } else {
                        typingTimer.stop();
                        repaint();

                    }
                });
                typingTimer.start();
            }
        };
        

        newPanel.setLayout(null); //kustom posisi

        
        JButton button1 = createCustomButton("Cek Cuaca", new ImageIcon("img/awan2.png"));
        button1.setBounds(100, 400, 200, 60); 

        JButton button2 = createCustomButton("Monitoring", new ImageIcon("img/eye icon_4184857.png"));
        button2.setBounds(320, 400, 200, 60); 

        JButton button3 = createCustomButton("History", new ImageIcon("img/loop.png"));
        button3.setBounds(540, 400, 200, 60); 


        button1.addActionListener(e -> {
            WeatherCheckView.createAndShowFrame(model); // Menampilkan WeatherCheckView
        });

        button2.addActionListener(e -> {
            new WeatherMonitoring().setVisible(true);
        });

        button3.addActionListener(e -> {
            new History1().setVisible(true);  // open history1
        });



        newPanel.add(button1);
        newPanel.add(button2);
        newPanel.add(button3);

        frame.getContentPane().removeAll();
        frame.add(newPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private JButton createCustomButton(String text, ImageIcon icon) {

        Image image = icon.getImage(); 
        Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // ukuran gambar 
        icon = new ImageIcon(scaledImage);
        
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                
                GradientPaint gradient = new GradientPaint(0, 0, Color.YELLOW, getWidth(), 0, Color.ORANGE);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // bagian Oval 

                super.paintComponent(g);
            }
        };

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT); 
        button.setIcon(icon); 
        button.setIconTextGap(10); // Jarak teks & ikon
        button.setForeground(Color.BLACK); 
        button.setFont(new Font("Verdana", Font.BOLD, 15)); 

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
