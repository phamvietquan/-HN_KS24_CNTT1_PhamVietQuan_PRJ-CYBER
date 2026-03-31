package dao;

import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public List<int[]> getPendingBookings() throws Exception {
        List<int[]> result = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status='PENDING'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new int[]{
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("pc_id")
                });
            }
        }
        return result;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE bookings SET status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}