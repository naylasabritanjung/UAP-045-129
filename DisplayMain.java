import javax.swing.JFrame;

/**
 * Kelas utama untuk menjalankan aplikasi Weather Monitoring.
 *
 * Kelas ini berfungsi sebagai entry point untuk aplikasi, yang menginisialisasi
 * model, tampilan, dan controller, serta menampilkan jendela utama aplikasi
 * dengan ukuran dan komponen yang telah ditentukan.
 *
 * @author Putri Nayla Sabri
 * @author Herdiana Dwi Maharani
 * @version 1.0
 */
public class DisplayMain {

    /**
     * Metode utama yang digunakan untuk menjalankan aplikasi.
     *
     * Menginisialisasi objek model, tampilan, dan controller, kemudian membuat
     * dan menampilkan jendela utama aplikasi dengan ukuran yang telah ditentukan.
     *
     * @param args Argumen baris perintah (tidak digunakan dalam aplikasi ini)
     */
    public static void main(String[] args) {
        DisplayModel model = new DisplayModel();
        DisplayView view = new DisplayView(model);

        new DisplayController(model, view);

        JFrame frame = new JFrame("Weather Monitoring App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.add(view);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
