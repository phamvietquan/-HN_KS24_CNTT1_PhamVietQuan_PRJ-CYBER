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

    public boolean updateStatusWithPC(int bookingId, String status) {
        String sqlBooking   = "UPDATE bookings SET status=? WHERE id=?";
        String sqlPcDone    = "UPDATE pcs SET status='AVAILABLE' WHERE id = (SELECT pc_id FROM bookings WHERE id=?)";
        String sqlPcServing = "UPDATE pcs SET status='SERVING'   WHERE id = (SELECT pc_id FROM bookings WHERE id=?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement ps = conn.prepareStatement(sqlBooking);
                ps.setString(1, status);
                ps.setInt(2, bookingId);
                int rows = ps.executeUpdate();

                if (rows == 0) {
                    conn.rollback();
                    return false;
                }

                if ("DONE".equals(status)) {
                    PreparedStatement ps2 = conn.prepareStatement(sqlPcDone);
                    ps2.setInt(1, bookingId);
                    ps2.executeUpdate();
                } else if ("SERVING".equals(status)) {
                    PreparedStatement ps2 = conn.prepareStatement(sqlPcServing);
                    ps2.setInt(1, bookingId);
                    ps2.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getBookingsByUserId(int userId) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT id, pc_id, status FROM bookings WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(
                        "Booking ID: " + rs.getInt("id") +
                                " | PC: "      + rs.getInt("pc_id") +
                                " | Status: "  + rs.getString("status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isPCBooked(int pcId) {
        String sql = "SELECT id FROM bookings WHERE pc_id=? AND status IN ('PENDING','CONFIRMED','SERVING') LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pcId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int createBooking(int userId, int pcId, int hours) {
        String sqlInsert = "INSERT INTO bookings(user_id, pc_id, start_time, end_time, status) " +
                "VALUES(?,?,NOW(), DATE_ADD(NOW(), INTERVAL ? HOUR), 'PENDING')";
        String sqlPc     = "UPDATE pcs SET status='PENDING' WHERE id=?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setInt(2, pcId);
                ps.setInt(3, hours);
                ps.executeUpdate();

                PreparedStatement ps2 = conn.prepareStatement(sqlPc);
                ps2.setInt(1, pcId);
                ps2.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int generatedId = rs.next() ? rs.getInt(1) : -1;

                conn.commit();
                return generatedId;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> getOrdersByBookingId(int bookingId) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT f.name, o.quantity, f.price " +
                "FROM orders o JOIN foods f ON o.food_id = f.id " +
                "WHERE o.booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(String.format("  - %-20s x%-3d  %,.0f VND",
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price") * rs.getInt("quantity")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}