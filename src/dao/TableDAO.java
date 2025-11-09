package dao;

import model.Meja;
import java.sql.*;
import java.util.*;

public class TableDAO {
  public List<Meja> getAllTables() {
    List<Meja> list = new ArrayList<>();
    String sql = "SELECT id, code, status, hourly_rate FROM billiardaan.billiard_tables";
    try (Connection c = DBUtil.getConnection();
         Statement st = c.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
      while (rs.next()) {
        list.add(new Meja(
          rs.getInt("id"),
          rs.getString("code"),
          rs.getString("status"),
          rs.getInt("hourly_rate")
        ));
      }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
  }

  public void updateStatus(int tableId, String status) throws SQLException {
    String sql = "UPDATE billiardaan.billiard_tables SET status=? WHERE id=?";
    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, status);
      ps.setInt(2, tableId);
      ps.executeUpdate();
    }
  }
}
