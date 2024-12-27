import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class History1 extends JFrame {

    public History1() {
        setTitle("History");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        HistoryPanel panel = new HistoryPanel();
        getContentPane().add(panel);
    }

    class HistoryPanel extends JPanel {

        private float angle = 0;
        private Timer timer;
        private Timer textTimer;
        private String fullText = "Tak perlu mengingat sendiri, kami simpan semuanya. Lihat riwayat cuaca di sini!";
        private StringBuilder currentText = new StringBuilder();
        private int textIndex = 0;
        private JButton backButton;  // Deklarasi tombol

        public HistoryPanel() {
            setLayout(null);

            // Membuat tombol "Back"
            backButton = new JButton("Back") {

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    int width = getWidth();
                    int height = getHeight();

                    // Gradient untuk tombol
                    GradientPaint gradient = new GradientPaint(0, 0, Color.ORANGE, width, height, Color.ORANGE.brighter());
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, width, height, 50, 50);

                    // Gambar di tombol
                    ImageIcon icon = new ImageIcon("C:\\Users\\Rani\\OneDrive\\文档\\Project.ProglanPraktikum\\UAP-045-129\\img\\home icon.png"); 
                    g2d.drawImage(icon.getImage(), 10, 10, 30, 30, this);  

                    // Teks pada tombol
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.setFont(new Font("Verdana", Font.BOLD, 20));
                    String text = getText();
                    int textWidth = g2d.getFontMetrics().stringWidth(text);
                    int textHeight = g2d.getFontMetrics().getAscent();
                    int x = (width - textWidth) / 2 + 5 ; 
                    int y = (height + textHeight) / 2;

                    g2d.drawString(text, x, y);
                }
            };

            //posisi button
            backButton.setBounds(50, 650, 130, 50);
            backButton.setContentAreaFilled(false);
            backButton.setFocusPainted(false);
            backButton.setBorderPainted(false);
            backButton.setVisible(false);  // sembunyikan tombol 

           
            backButton.addActionListener(e -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                new DisplayView(new DisplayModel()).setVisible(true);
            });

            add(backButton);

           
            timer = new Timer(20, e -> {
                angle += Math.PI / 180;
                repaint();
            });
            timer.start();

           
            textTimer = new Timer(150, e -> {
                if (textIndex < fullText.length()) {
                    currentText.append(fullText.charAt(textIndex));
                    textIndex++;
                    repaint();
                } else {
                    textTimer.stop();
                    backButton.setVisible(true);  // nampilkan tombol 
                }
            });
            textTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            frameGradasi(g2d);

            teksTengah(g2d, "History", 50, Color.orange, 30);

            teksTengah(g2d, currentText.toString(), 100, Color.white, 15);
        }

        private void frameGradasi(Graphics2D g2d) {
            int width = getWidth();
            int height = getHeight();

            float x1 = (float) (Math.cos(angle) * width / 2 + width / 2);
            float y1 = (float) (Math.sin(angle) * height / 2 + height / 2);
            float x2 = (float) (Math.cos(angle + Math.PI) * width / 2 + width / 2);
            float y2 = (float) (Math.sin(angle + Math.PI) * height / 2 + height / 2);

            Color color1 = new Color(0, 0, 139);
            Color color2 = new Color(135, 206, 250);
            GradientPaint gradient = new GradientPaint(x1, y1, color1, x2, y2, color2);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
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
}
