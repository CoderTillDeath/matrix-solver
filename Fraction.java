import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Fraction extends JPanel {
  public Fraction() {
    StringBuilder sb = new StringBuilder();
    sb.append("<html><body>");
    sb.append("1");
    sb.append("<sup>");
    sb.append("2");
    sb.append("</sup>");
    sb.append("<font size=+1>/<font size=-1>");
    sb.append("<sub>");
    sb.append("3");
    sb.append("</sub>");
    sb.append("</html></body>");
    JLabel label = new JLabel(sb.toString(), JLabel.CENTER);
    label.setBorder(BorderFactory.createLineBorder(Color.lightGray));
     add(label);
  }
  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(new Main());
    f.pack();
    f.setVisible(true);
  }
}
