import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class WeatherMonitoring extends JFrame {

    public WeatherMonitoring() {
        setTitle("Monitoring");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MonitoringPanel panel = new MonitoringPanel();
        getContentPane().add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static class MonitoringPanel extends JPanel {
        private float angle = 0;
        private Timer timer;
        private Timer textTimer;
        private String fullText = "Tak perlu mengingat sendiri, kami simpan semuanya. Lihat riwayat cuaca di sini!";
        private StringBuilder currentText = new StringBuilder();
        private int textIndex = 0;
        private JButton backButton;
        private static JTable weatherTable;
        public static DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"No", "Location", "Weather Condition", "Temperature", "Time"}, 0);

        public MonitoringPanel() {
            setLayout(null);

            // Button Kembali
            backButton = new JButton("Kembali") {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    int width = getWidth();
                    int height = getHeight();
                    GradientPaint gradient = new GradientPaint(0, 0, Color.ORANGE, width, height, Color.ORANGE.brighter());
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, width, height, 50, 50);
                    ImageIcon icon = new ImageIcon("img/home icon.png");
                    g2d.drawImage(icon.getImage(), 5, 10, 30, 30, this);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.setFont(new Font("Verdana", Font.BOLD, 15));
                    String text = getText();
                    int textWidth = g2d.getFontMetrics().stringWidth(text);
                    int textHeight = g2d.getFontMetrics().getAscent();
                    int x = (width - textWidth) / 2 + 5;
                    int y = (height + textHeight) / 2;
                    g2d.drawString(text, x, y);
                }
            };

            // Posisi button
            backButton.setBounds(50, 550, 130, 50);
            backButton.setContentAreaFilled(false);
            backButton.setFocusPainted(false);
            backButton.setBorderPainted(false);
            backButton.setVisible(false);

            backButton.addActionListener(e -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                new DisplayView(new DisplayModel()).setVisible(true);
            });

            add(backButton);

            // Tabel cuaca
            String[] columnNames = {"No", "Nama Lokasi", "Temperatur", "Cuaca", "Waktu"};
            // Gunakan tableModel yang sudah dideklarasikan
            weatherTable = new JTable(tableModel); // Inisialisasi tabel dengan model

// Pengaturan tampilan tabel
            weatherTable.setFont(new Font("Verdana", Font.PLAIN, 12));
            weatherTable.setRowHeight(25);

// Penyesuaian tampilan header tabel
            JTableHeader header = weatherTable.getTableHeader();
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel(value.toString());
                    label.setFont(new Font("Verdana", Font.BOLD, 14));
                    label.setBackground(new Color(135, 206, 250));
                    label.setForeground(Color.BLACK);
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    return label;
                }
            });

// Menambahkan tabel ke dalam JScrollPane
            JScrollPane tableScrollPane = new JScrollPane(weatherTable);
            tableScrollPane.setBounds(50, 150, 800, 350); // Atur ukuran dan posisi
            add(tableScrollPane); // Tambahkan JScrollPane ke panel

            timer = new Timer(20, e -> {
                angle += Math.PI / 180;
                repaint();
            });
            timer.start();

            textTimer = new Timer(40, e -> {
                if (textIndex < fullText.length()) {
                    backButton.setVisible(true);
                    currentText.append(fullText.charAt(textIndex));
                    textIndex++;
                    repaint();
                } else {
                    textTimer.stop();
                }
            });
            textTimer.start();
        }

        public static void addDataToTable(String location, String condition, double temperature, String time) {
            int rowCount = tableModel.getRowCount();
            Object[] rowData = {rowCount + 1, location, temperature, condition, time};
            tableModel.addRow(rowData);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            frameGradasi(g2d);
            teksTengah(g2d, "Monitoring", 50, Color.orange, 30);
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
