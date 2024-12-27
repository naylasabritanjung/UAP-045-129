import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class DisplayController {
    private final DisplayModel model;
    private final DisplayView view;

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
