import javax.swing.JFrame;

public class DisplayMain {
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
