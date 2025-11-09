// dao/RentalDAO.java
package dao;

import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class RentalDAO {

  public int startRental(int tableId, int hours, int rate) throws SQLException {
    String sql = "INSERT INTO rental_sessions(table_id,start_time,end_time,total_price,status) " +
                 "VALUES (?,?,?,?,'ACTIVE')";
    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end   = start.plusHours(hours);
    int total = hours * rate;

    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setInt(1, tableId);
      ps.setTimestamp(2, Timestamp.valueOf(start));
      ps.setTimestamp(3, Timestamp.valueOf(end));
      ps.setInt(4, total);
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        return rs.next() ? rs.getInt(1) : -1;
      }
    }
  }

  // Ambil sisa detik untuk meja tertentu, jika ada sesi yang active
  public int getRemainingSecondsForTable(int tableId) {
    String sql = "SELECT end_time FROM rental_sessions " +
                 "WHERE table_id=? AND status='ACTIVE' ORDER BY id DESC LIMIT 1";
    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, tableId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          LocalDateTime end = rs.getTimestamp(1).toLocalDateTime();
          long sec = ChronoUnit.SECONDS.between(LocalDateTime.now(), end);
          return (int) Math.max(0, sec);
        }
      }
    } catch (Exception e) { e.printStackTrace(); }
    return 0;
  }

  // Selesaikan sesi 
  public void finishActiveRental(int tableId) throws SQLException {
    String sql = "UPDATE rental_sessions SET status='FINISHED' " +
                 "WHERE table_id=? AND status='ACTIVE'";
    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, tableId);
      ps.executeUpdate();
    }
  }
}
