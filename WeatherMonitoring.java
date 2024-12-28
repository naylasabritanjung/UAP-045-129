/**
 * Aplikasi untuk menampilkan histori cuaca dalam bentuk tabel
 * dan memberikan fungsionalitas untuk menghapus data berdasarkan indeks.
 *
 * <p> Aplikasi ini menggunakan <code>JTable</code> untuk menampilkan data cuaca yang meliputi lokasi, kondisi cuaca, suhu, 
 * dan waktu. Pengguna dapat menghapus data dari tabel dengan memasukkan nomor baris yang sesuai.
 * </p>
 *
 * <p>
 * Author: Putri Nayla Sabri, Herdiana Dwi Maharani
 * </p>
 *
 * @version 1.0
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * Kelas ini merupakan jendela utama dari aplikasi WeatherMonitoring yang mengatur tampilan dan interaksi pengguna.
 */
public class WeatherMonitoring extends JFrame {

    /**
     * Konstruktor untuk inisialisasi tampilan WeatherMonitoring.
     */
    public WeatherMonitoring() {
        setTitle("Histori");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MonitoringPanel panel = new MonitoringPanel();
        getContentPane().add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Panel utama yang menampilkan histori cuaca dan fungsionalitas untuk menghapus data.
     */
    static class MonitoringPanel extends JPanel {
        private float angle = 0;
        private Timer timer;
        private Timer textTimer;
        private String fullText = "Tak perlu mengingat sendiri, kami simpan semuanya. Lihat riwayat cuaca di sini!";
        private StringBuilder currentText = new StringBuilder();
        private int textIndex = 0;
        private JButton backButton, deleteButton;
        private JTextField deleteField;
        private static JTable weatherTable;
        public static DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"No", "Location", "Weather Condition", "Temperature", "Time"}, 0);

        /**
         * Konstruktor untuk inisialisasi elemen-elemen di dalam panel cuaca.
         */
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
            weatherTable = new JTable(tableModel);
            weatherTable.setFont(new Font("Verdana", Font.PLAIN, 12));
            weatherTable.setRowHeight(25);

            TableColumn locationColumn = weatherTable.getColumnModel().getColumn(1);
            locationColumn.setCellEditor(new DefaultCellEditor(new JTextField()));

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

            JScrollPane tableScrollPane = new JScrollPane(weatherTable);
            tableScrollPane.setBounds(50, 150, 800, 350);
            add(tableScrollPane);

            // Input field untuk menghapus data
            deleteField = new JTextField();
            deleteField.setFont(new Font("Verdana", Font.PLAIN, 14));
            deleteField.setBounds(630, 560, 50, 30);
            add(deleteField);

            // Button Hapus
            deleteButton = new JButton("Hapus") {
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
                    g2d.setFont(new Font("Verdana", Font.BOLD, 15));
                    String text = getText();
                    int textWidth = g2d.getFontMetrics().stringWidth(text);
                    int textHeight = g2d.getFontMetrics().getAscent();
                    int x = (width - textWidth) / 2;
                    int y = (height + textHeight) / 2;
                    g2d.drawString(text, x, y);
                }
            };

            deleteButton.setBounds(700, 550, 130, 50); // Posisi tombol di sebelah kanan
            deleteButton.setContentAreaFilled(false);
            deleteButton.setFocusPainted(false);
            deleteButton.setBorderPainted(false);
            deleteButton.addActionListener(e -> {
                try {
                    int index = Integer.parseInt(deleteField.getText()) - 1;
                    if (index >= 0 && index < tableModel.getRowCount()) {
                        tableModel.removeRow(index);
                        updateRowNumbers();
                        deleteField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Indeks tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            add(deleteButton);

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

        /**
         * Menambahkan data cuaca ke dalam tabel.
         *
         * @param location Lokasi cuaca
         * @param condition Kondisi cuaca
         * @param temperature Suhu cuaca
         * @param time Waktu pengamatan
         */
        public static void addDataToTable(String location, String condition, double temperature, String time) {
            int rowCount = tableModel.getRowCount();
            Object[] rowData = {rowCount + 1, location, temperature, condition, time};
            tableModel.addRow(rowData);
        }

        /**
         * Memperbarui nomor baris setelah penghapusan data.
         */
        private void updateRowNumbers() {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                tableModel.setValueAt(i + 1, i, 0);
            }
        }

        /**
         * Menggambar komponen pada panel cuaca, termasuk latar belakang dan teks.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            frameGradasi(g2d);
            teksTengah(g2d, "Histori", 50, Color.orange, 30);
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

    /**
     * Main method untuk menjalankan aplikasi WeatherMonitoring.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WeatherMonitoring::new);
    }
}