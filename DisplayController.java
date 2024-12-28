import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Controller untuk menangani logika interaksi antara model dan tampilan
 * pada aplikasi Display.
 *
 * Kelas ini berfungsi untuk mengatur pembaruan sudut pada model secara
 * periodik menggunakan timer, serta menangani event yang terjadi pada
 * tampilan, seperti menekan tombol untuk memperbarui tampilan.
 *
 * @author Putri Nayla Sabri
 * @author Herdiana Dwi Maharani
 * @version 1.0
 */
public class DisplayController {
    private final DisplayModel model;
    private final DisplayView view;

    /**
     * Konstruktor untuk menginisialisasi objek controller, model, dan view,
     * serta mengatur logika interaksi antara keduanya.
     *
     * @param model Objek model yang menyimpan data dan logika aplikasi
     * @param view Objek tampilan yang menampilkan UI aplikasi
     */
    public DisplayController(DisplayModel model, DisplayView view) {
        this.model = model;
        this.view = view;


        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.updateAngle(0.03f);
                view.repaint();
            }
        });
        timer.start();

        view.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.updateAngle(0.1f);
                view.openNewPanel(model);
                view.repaint();
            }
        });
    }
}
