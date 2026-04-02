package service;

import dao.BookingDAO;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();

    public boolean isPCBooked(int pcId) {
        return bookingDAO.isPCBooked(pcId);
    }

    public int createBooking(int userId, int pcId, int hours) {
        return bookingDAO.createBooking(userId, pcId, hours);
    }

//    public void cancelBooking(int bookingId, int pcId) {
//        try (Connection conn = DBConnection.getConnection()) {
//            PreparedStatement ps = conn.prepareStatement(
//                    "UPDATE bookings SET status='CANCELLED' WHERE id=?"
//            );
//            ps.setInt(1, bookingId);
//            ps.executeUpdate();
//
//            PreparedStatement ps2 = conn.prepareStatement(
//                    "UPDATE pcs SET status='AVAILABLE' WHERE id=?"
//            );
//            ps2.setInt(1, pcId);
//            ps2.executeUpdate();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}