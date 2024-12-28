import java.awt.Color;

/**
 * Kelas ini digunakan untuk memodelkan data yang diperlukan oleh aplikasi Weather Monitoring.
 * Kelas ini menyimpan dua warna dan sudut (angle) yang digunakan untuk menghasilkan
 * tampilan gradasi di antarmuka pengguna.
 *
 * <p>Kelas ini memiliki dua warna yang digunakan untuk membuat efek gradasi
 * pada tampilan aplikasi, serta sebuah sudut (angle) yang akan diperbarui secara berkala
 * untuk menciptakan animasi perubahan tampilan.</p>
 *
 * @version 1.0
 * @author Putri Nayla Sabri
 * @author Herdiana Dwi Maharani
 */
public class DisplayModel {
    private final Color color1;
    private final Color color2;
    private float angle;

    /**
     * Konstruktor kelas DisplayModel yang menginisialisasi warna dan sudut.
     *
     * <p>Warna pertama diatur ke biru tua (RGB: 0, 0, 139) dan warna kedua
     * diatur ke biru langit (RGB: 135, 206, 250). Sudut diinisialisasi ke nilai 0.</p>
     */
    public DisplayModel() {
        color1 = new Color(0, 0, 139); // Warna biru tua
        color2 = new Color(135, 206, 250); // Warna biru langit
        angle = 0;
    }

    /**
     * Mendapatkan warna pertama yang digunakan untuk gradasi.
     *
     * @return Warna pertama (biru tua)
     */
    public Color getColor1() {
        return color1;
    }

    /**
     * Mendapatkan warna kedua yang digunakan untuk gradasi.
     *
     * @return Warna kedua (biru langit)
     */
    public Color getColor2() {
        return color2;
    }

    /**
     * Mendapatkan nilai sudut yang digunakan untuk efek gradasi.
     *
     * @return Nilai sudut (angle) saat ini
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Memperbarui nilai sudut dengan penambahan increment.
     *
     * <p>Jika nilai sudut melebihi 2π, maka sudut akan direset kembali ke 0.</p>
     *
     * @param increment Penambahan nilai sudut
     */
    public void updateAngle(float increment) {
        angle += increment;
        if (angle > Math.PI * 2) {
            angle = 0;
        }
    }
}
