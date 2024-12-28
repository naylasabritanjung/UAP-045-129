import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Kelas ini merupakan tampilan dari aplikasi pengecekan cuaca yang menampilkan antarmuka pengguna.
 * Kelas ini berfungsi untuk menampilkan komponen seperti kolom input teks untuk nama lokasi,
 * tombol untuk mencari cuaca berdasarkan lokasi, serta tombol untuk kembali ke tampilan utama.
 *
 * Selain itu, kelas ini juga menangani animasi gradasi latar belakang dan pemrosesan event terkait
 * dengan tombol yang ada.
 *
 * @author Putri Nayla Sabri dan Herdiana Dwi Maharani
 * @version 1.0
 */
public class WeatherCheckView extends JPanel {

    private final DisplayModel model;
    private final JTextField textField;
    private JButton cari;
    private JButton back;

    /**
     * Konstruktor untuk menginisialisasi tampilan cuaca.
     * Mengatur komponen-komponen tampilan seperti kolom input, tombol cari, dan tombol kembali.
     *
     * @param model Model yang terkait dengan tampilan ini.
     */
    public WeatherCheckView(DisplayModel model) {
        this.model = model;
        this.textField = new JTextField(20);
        setLayout(null);
        textField.setBounds(150, 280, 580, 50);
        textField.setFont(new Font("Verdana", Font.PLAIN, 15));
        textField.setBorder(BorderFactory.createEmptyBorder());
        add(textField);

        cari = new JButton("Cari");
        cari.setBounds(650, 350, 80, 40);
        cari.setFont(new Font("Verdana", Font.BOLD, 15));
        cari.setContentAreaFilled(false);
        cari.setFocusable(false);
        cari.setBorder(BorderFactory.createEmptyBorder());
        add(cari);

        back = new JButton("Kembali");
        back.setBounds(150, 350, 130, 40);
        back.setFont(new Font("Verdana", Font.BOLD, 15));
        back.setContentAreaFilled(false);
        back.setFocusable(false);

        back.addActionListener(e -> {
            // Menutup jendela saat ini
            SwingUtilities.getWindowAncestor(this).dispose();

            // Membuat dan menampilkan DisplayView
            DisplayView displayView = new DisplayView(new DisplayModel());
            displayView.openNewPanel(new DisplayModel());
            displayView.setVisible(true);
        });

        add(back);

        // Timer untuk animasi gradasi
        Timer timer = new Timer(50, e -> {
            model.updateAngle(0.03f);
            repaint();
        });
        timer.start();
    }

    /**
     * Mengembalikan kolom input teks untuk nama lokasi.
     *
     * @return Kolom input teks (JTextField).
     */
    public JTextField getTextField() {
        return textField;
    }

    /**
     * Mengembalikan tombol untuk mencari cuaca.
     *
     * @return Tombol cari (JButton).
     */
    public JButton getCari() {
        return cari;
    }

    /**
     * Mengembalikan tombol untuk kembali ke tampilan utama.
     *
     * @return Tombol kembali (JButton).
     */
    public JButton getBack() {
        return back;
    }

    /**
     * Menggambar komponen-komponen tampilan cuaca, termasuk gradasi latar belakang dan teks.
     *
     * @param g Objek grafis untuk menggambar komponen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        frameGradasi(g2d);

        int initialY = 150;
        int nextY = teksTengah(g2d, "Rencanakan perjalanan Anda!", initialY, Color.ORANGE, 30);
        teksTengah(g2d, "Cari tahu apakah matahari bersinar di kota Anda.", nextY + 10, Color.WHITE, 20);

        g2d.setColor(new Color(255, 255, 255, 150));
        RoundRectangle2D.Float roundTextField = new RoundRectangle2D.Float(textField.getX(), textField.getY(), textField.getWidth(), textField.getHeight(), 15, 15);
        g2d.fill(roundTextField);
    }

    /**
     * Menggambar latar belakang dengan gradasi berdasarkan sudut yang dihitung dari model.
     *
     * @param g2d Objek Graphics2D untuk menggambar.
     */
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

    /**
     * Menampilkan teks secara horizontal di tengah panel dengan posisi Y yang ditentukan.
     *
     * @param g2d Objek Graphics2D untuk menggambar.
     * @param text Teks yang akan ditampilkan.
     * @param topY Posisi Y awal dari teks.
     * @param color Warna teks.
     * @param fontSize Ukuran font teks.
     * @return Posisi Y setelah teks digambar.
     */
    private int teksTengah(Graphics2D g2d, String text, int topY, Color color, int fontSize) {
        g2d.setColor(color);
        g2d.setFont(new Font("Verdana", Font.BOLD, fontSize));

        int panelWidth = getWidth();
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int ascent = g2d.getFontMetrics().getAscent();

        int x = (panelWidth - textWidth) / 2;
        int y = topY + ascent;

        g2d.drawString(text, x, y);
        return y;
    }

    /**
     * Menggambar border dengan gradasi warna untuk tombol cari dan kembali.
     *
     * @param g Objek grafis untuk menggambar border.
     */
    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, Color.ORANGE, 1, getHeight(), Color.ORANGE.brighter());
        g2d.setPaint(gradient);

        RoundRectangle2D.Float roundButtonCari = new RoundRectangle2D.Float(cari.getX(), cari.getY(), cari.getWidth(), cari.getHeight(), 20, 20);
        g2d.fill(roundButtonCari);

        RoundRectangle2D.Float roundButtonBack = new RoundRectangle2D.Float(back.getX(), back.getY(), back.getWidth(), back.getHeight(), 20, 20);
        g2d.fill(roundButtonBack);
    }

    /**
     * Membuat dan menampilkan frame utama dari aplikasi pengecekan cuaca.
     *
     * @param model Model tampilan yang digunakan.
     */
    public static void createAndShowFrame(DisplayModel model) {
        JFrame frame = new JFrame("Weather Check");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);

        WeatherCheckView weatherCheckView = new WeatherCheckView(model);
        frame.add(weatherCheckView);

        new WeatherCheckController(weatherCheckView);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}