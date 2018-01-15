import java.awt.Graphics;
import javax.swing.JPanel;

public class Render extends JPanel{
  /*
   * this method is used from HappyBirdie to paint simultaneously as HappyBirdie runs.
   * @param g is the variable of Graphics which is used to pass as arguments in method callings
   */
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    HappyBirdie.happyBirdie.repaint(g);
  }
}
