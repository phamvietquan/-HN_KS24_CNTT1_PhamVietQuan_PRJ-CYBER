package presentation;

import dao.BookingDAO;
import model.User;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    static long startTime;
    static boolean isPlaying = false;
    static User currentUser;

    public static void setUser(User user) {
        currentUser = user;
    }

    public static void showMenu() {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━ Customer ━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("|                            |                          |                            |");
        System.out.println("| 1. Đặt máy + Gọi món       | 2. Xem tai khoan         | 3. Xem trạng thái đơn      |");
        System.out.println("|                            |                          |                            |");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        System.out.println("|                            |");
        System.out.println("| 4. Đăng xuất               |");
        System.out.println("|                            |");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public static void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            if (isPlaying && currentUser != null) {
                long now = System.currentTimeMillis();
                long minutes = (now - startTime) / (1000 * 60);
                double cost = (minutes / 60.0) * 10000;

                if (cost >= currentUser.getBalance()) {
                    System.out.println("Hết tiền -> Đăng xuất...");
                    isPlaying = false;
                    return;
                }
            }
            showMenu();
            System.out.print("Chọn chức năng: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        CustomerHandler.datMayFlow(currentUser, sc);
                        break;
                    case 2:
                        CustomerHandler.xemTaiKhoan(currentUser);
                        break;
                    case 3:
                        xemTrangThai(currentUser.getId());
                        break;
                    case 4:
                        System.out.println("Đăng xuất");
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ");
                }

            } catch (Exception e) {
                System.out.println("Nhập sai định dạng!");
            }
        }
    }

    static void xemTrangThai(int userId) {
        BookingDAO dao = new BookingDAO();
        List<String> list = dao.getBookingsByUserId(userId);

        System.out.println("\n=== TRẠNG THÁI CỦA BẠN ===");

        if (list.isEmpty()) {
            System.out.println("Bạn chưa có đơn đặt máy nào.");
            return;
        }

        list.forEach(System.out::println);
    }
}