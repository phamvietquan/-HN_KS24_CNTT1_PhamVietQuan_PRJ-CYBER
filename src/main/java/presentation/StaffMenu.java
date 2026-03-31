package presentation;

import dao.BookingDAO;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Scanner;

public class StaffMenu {

    public static void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== STAFF MENU =====");
            System.out.println("1. Xem yêu cầu đặt máy");
            System.out.println("2. Cập nhật trạng thái");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Sai định dạng!");
                continue;
            }

            switch (choice) {
                case 1:
                    xemBooking();
                    break;
                case 2:
                    capNhatTrangThai(sc);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Không hợp lệ!");
            }
        }
    }

    static void xemBooking() {
        try {
            BookingDAO dao = new BookingDAO();
            List<int[]> list = dao.getPendingBookings();

            System.out.println("\n=== DANH SÁCH CHỜ ===");
            if (list.isEmpty()) {
                System.out.println("Không có booking nào đang chờ.");
                return;
            }
            for (int[] row : list) {
                System.out.println(
                        "ID: " + row[0] +
                                " | User: " + row[1] +
                                " | PC: " + row[2]
                );
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải danh sách booking!");
            e.printStackTrace();
        }
    }

    static void capNhatTrangThai(Scanner sc) {
        System.out.print("Nhập booking ID: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        System.out.println("1. CONFIRMED");
        System.out.println("2. SERVING");
        System.out.println("3. DONE");
        System.out.print("Chọn: ");

        int c;
        try {
            c = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        String status;
        switch (c) {
            case 1: status = "CONFIRMED"; break;
            case 2: status = "SERVING"; break;
            case 3: status = "DONE"; break;
            default:
                System.out.println("Không hợp lệ");
                return;
        }

        String sql = "UPDATE bookings SET status=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            int row = ps.executeUpdate();

            if (row > 0) {
                if (status.equals("DONE")) {
                    PreparedStatement ps2 = conn.prepareStatement(
                            "UPDATE pcs SET status='AVAILABLE' WHERE id = (SELECT pc_id FROM bookings WHERE id=?)"
                    );
                    ps2.setInt(1, id);
                    ps2.executeUpdate();
                }
                System.out.println("Cập nhật thành công!");
            } else {
                System.out.println("ID không tồn tại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}