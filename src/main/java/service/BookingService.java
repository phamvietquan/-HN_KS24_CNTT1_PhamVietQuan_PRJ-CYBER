package service;

import dao.PCDAO;
import util.DBConnection;

import java.sql.*;

public class BookingService {

    public boolean isPCBooked(int pcId) {
        String sql = "SELECT * FROM bookings WHERE pc_id=? AND status IN ('PENDING','CONFIRMED','SERVING')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pcId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int createBooking(int userId, int pcId, int hours) {
        String sql = "INSERT INTO bookings(user_id, pc_id, start_time, end_time, status) " +
                "VALUES(?,?,NOW(), DATE_ADD(NOW(), INTERVAL ? HOUR), 'SERVING')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, pcId);
            ps.setInt(3, hours);

            ps.executeUpdate();

            // UPDATE PC → BUSY
            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE pcs SET status = 'SERVING' WHERE id = ?"
            );
            ps2.setInt(1, pcId);
            ps2.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public void cancelBooking(int bookingId, int pcId) {
        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE bookings SET status='CANCELLED' WHERE id=?"
            );
            ps.setInt(1, bookingId);
            ps.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE pcs SET status='AVAILABLE' WHERE id=?"
            );
            ps2.setInt(1, pcId);
            ps2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}