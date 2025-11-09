package gui;

import dao.TableDAO;
import dao.RentalDAO;
import concurrency.TimerMeja;
import model.Meja;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Dashboard extends JFrame {
  private final TableDAO tableDao = new TableDAO();
  private final RentalDAO rentalDao = new RentalDAO();
  private final Map<Integer, TimerMeja> timers = new HashMap<>();

  public Dashboard() {
    setTitle("Billiard Dashboard");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 600);
    setUndecorated(false);
    setBackground(Color.BLACK);

    java.util.List<Meja> data = tableDao.getAllTables();
    JPanel grid = new JPanel(new GridLayout(0, 4, 10, 10));

    for (Meja m : data) {
      TableCard card = new TableCard(m);
      card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      card.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override public void mouseClicked(java.awt.event.MouseEvent e) { onCardClicked(card); }
      });
      grid.add(card);

      timers.put(m.getId(), new TimerMeja());

      // mengecek sisa waktu di DB 
      int remaining = rentalDao.getRemainingSecondsForTable(m.getId());
      if (remaining > 0) {
        // jika 'OCCUPIED' maka waktu berlanjut
        try { 
          tableDao.updateStatus(m.getId(), "OCCUPIED"); 
        } catch (Exception ignore) {}

        card.setStatus("OCCUPIED");
        startTimerFor(card, remaining);

      } else {
        //  jika status di DB 'OCCUPIED' tapi tak ada sesi aktif, balikin hijau
        if ("OCCUPIED".equalsIgnoreCase(m.getStatus())) {
          try { 
            tableDao.updateStatus(m.getId(), "AVAILABLE"); 
          } catch (Exception ignore) {}
          card.setStatus("AVAILABLE");
        }
      }
    }
    add(new JScrollPane(grid));
  }

  private void onCardClicked(TableCard card) {
    Meja m = card.getMeja();
    if ("AVAILABLE".equalsIgnoreCase(m.getStatus())) {
      Integer[] choices = {1,2,3,4,5,6};
      Integer hours = (Integer) JOptionPane.showInputDialog(
        this, "Pilih durasi (jam):", "Sewa " + m.getCode(),
        JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

      if (hours == null) return; // batal

      int rate = m.getHourlyRate();
      int total = hours * rate;
      int ok = JOptionPane.showConfirmDialog(
        this, String.format("Total = %,d (%,d x %d jam)\nLanjut?", total, rate, hours),
        "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);

      if (ok == JOptionPane.OK_OPTION) {
        try {
          rentalDao.startRental(m.getId(), hours, rate);
          tableDao.updateStatus(m.getId(), "OCCUPIED");
          card.setStatus("OCCUPIED");

          startTimerFor(card, hours * 3600);

        } catch (Exception ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(this, "Gagal mulai sewa: " + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Meja " + m.getCode() + " sedang Occupied.");
    }
  }

  private void startTimerFor(TableCard card, int seconds) {
    timers.get(card.getMeja().getId()).start(seconds, remaining -> {
      card.setTimerText(format(remaining));
    }, () -> {
      // waktu habis, tandai selesai di DB dan meja hijau lagi
      try {
        rentalDao.finishActiveRental(card.getMeja().getId());
        tableDao.updateStatus(card.getMeja().getId(), "AVAILABLE");
      } catch (Exception ex) { ex.printStackTrace(); }
      JOptionPane.showMessageDialog(this, "Waktu meja " + card.getMeja().getCode() + " telah habis.");
      card.setStatus("AVAILABLE");
      card.setTimerText("0:00:00");
    });
  }

  private static String format(int sec) {
    int h = sec / 3600, m = (sec % 3600) / 60, s = sec % 60;
    return String.format("%02d:%02d:%02d", h, m, s);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
  }
}
