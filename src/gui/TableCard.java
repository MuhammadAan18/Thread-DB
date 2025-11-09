package gui;

import model.Meja;
import javax.swing.*;
import java.awt.*;

public class TableCard extends JPanel {
  private final JLabel lblCode = new JLabel();
  private final JLabel lblStatus = new JLabel();
  private final JLabel lblTimer = new JLabel("0:00:00");
  private Meja meja;

  public TableCard(Meja m) {
    this.meja = m;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
    setBackground(Color.BLACK);

    lblCode.setAlignmentX(CENTER_ALIGNMENT);
    lblStatus.setAlignmentX(CENTER_ALIGNMENT);
    lblTimer.setAlignmentX(CENTER_ALIGNMENT);
    lblCode.setFont(lblCode.getFont().deriveFont(Font.BOLD, 14f));
    lblTimer.setFont(lblTimer.getFont().deriveFont(Font.BOLD, 16f));

    add(Box.createVerticalStrut(45));
    add(lblCode);
    add(Box.createVerticalStrut(10));
    add(lblStatus);
    add(Box.createVerticalStrut(10));
    add(lblTimer);

    refreshView();
  }

  public void refreshView() {
    lblCode.setText(meja.getCode());
    lblStatus.setText(meja.getStatus());
    if ("AVAILABLE".equalsIgnoreCase(meja.getStatus())) {
      setBackground(Color.GREEN);
    } else {
      setBackground(Color.RED);
    }
    setOpaque(true);
  }

  public void setTimerText(String text){ 
    lblTimer.setText(text); 
  }

  public Meja getMeja(){ 
    return meja; 
  }
  
  public void setStatus(String s){
    lblStatus.setText(s);
    if ("AVAILABLE".equalsIgnoreCase(s)) {
      setBackground(Color.GREEN);
    } else {
      setBackground(Color.RED);
    }
    setOpaque(true);
  }
}
