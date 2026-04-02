package presentation;

import dao.BookingDAO;

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
                // [FIX] Hiển thị đơn F&B kèm theo booking
                List<String> orders = dao.getOrdersByBookingId(row[0]);
                if (orders.isEmpty()) {
                    System.out.println("  (Không có đồ ăn/thức uống)");
                } else {
                    System.out.println("  Đơn F&B:");
                    orders.forEach(System.out::println);
                }
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
            case 2: status = "SERVING";   break;
            case 3: status = "DONE";      break;
            default:
                System.out.println("Không hợp lệ");
                return;
        }

        BookingDAO dao = new BookingDAO();
        boolean ok = dao.updateStatusWithPC(id, status);

        if (ok) {
            System.out.println("Cập nhật thành công! Trạng thái mới: " + status);
        } else {
            System.out.println("Cập nhật thất bại! Booking ID không tồn tại.");
        }
    }
}