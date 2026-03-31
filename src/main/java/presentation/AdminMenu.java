package presentation;

import dao.CategoryDAO;
import dao.FoodDAO;
import dao.PCDAO;
import dao.UserDAO;
import model.Category;
import model.Food;
import model.PC;
import model.User;
import service.AuthService;
import service.BookingService;
import util.DBConnection;
import util.InputUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    public static void showAdminMenu() {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━ ADMIN ━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("|                          |                          |                          |");
        System.out.println("| 1. Quản lý PC            | 2. Quản lý Food          | 3. Quản lý Category      |");
        System.out.println("|                          |                          |                          |");
        System.out.println("|━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        System.out.println("|                          |                          |");
        System.out.println("| 4. Quản lý User          | 5. Đăng xuất             |");
        System.out.println("|                          |                          |");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public static void start() {
        Scanner sc = new Scanner(System.in);
        PCDAO pcDAO = new PCDAO();
        FoodDAO foodDAO = new FoodDAO();

        while (true) {
            showAdminMenu();
            System.out.print("Chọn chức năng: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        pcMenu(pcDAO, sc);
                        break;
                    case 2:
                        foodMenu(foodDAO, sc);
                        break;
                    case 3:
                        categoryMenu(new CategoryDAO(), sc);
                        break;
                    case 4:
                        userMenu(sc);
                        break;
                    case 5:
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

    public static void showPcMenu() {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━QUẢN LÝ MÁY TRẠM━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("|                            |                          |                          |");
        System.out.println("| 1. Hiển thị danh sách máy  | 2. Thêm máy              | 3. Sửa thông tin máy     |");
        System.out.println("|                            |                          |                          |");
        System.out.println("|━━━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        System.out.println("|                            |                          |");
        System.out.println("| 4. Xoá máy                 | 0. Quay lại              |");
        System.out.println("|                            |                          |");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    static void pcMenu(PCDAO dao, Scanner sc) {
        while (true) {
            showPcMenu();
            System.out.print("Chọn chức năng: ");
            try {
                int c = Integer.parseInt(sc.nextLine());
                switch (c) {
                    case 1:
                        hienThiMay(dao);
                        break;
                    case 2:
                        themMay(dao, sc);
                        break;
                    case 3:
                        suaMay(dao, sc);
                        break;
                    case 4:
                        xoaMay(dao, sc);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ");
                }
            } catch (Exception e) {
                System.out.println("Nhập sai định dạng!");
            }
        }
    }

    public static void showFoodMenu() {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("|                          |                          |                          |");
        System.out.println("| 1. Hiển thị danh sách    | 2. Thêm dịch vụ          | 3. Sửa dịch vụ           |");
        System.out.println("|                          |                          |                          |");
        System.out.println("|━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        System.out.println("|                          |                          |");
        System.out.println("| 4. Xóa dịch vụ           | 0. Quay lại              |");
        System.out.println("|                          |                          |");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    static void foodMenu(FoodDAO dao, Scanner sc) {
        while (true) {
            showFoodMenu();
            System.out.print("Chọn chức năng: ");
            try {
                int c = Integer.parseInt(sc.nextLine());
                switch (c) {
                    case 1:
                        hienThiFood(dao);
                        break;
                    case 2:
                        themFood(dao, sc);
                        break;
                    case 3:
                        suaFood(dao, sc);
                        break;
                    case 4:
                        xoaFood(dao, sc);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ");
                }
            } catch (Exception e) {
                System.out.println("Nhập sai định dạng!");
            }
        }
    }

    static void napTien(Scanner sc) {
        UserDAO dao = new UserDAO();

        System.out.print("Nhập user id: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        User user = dao.findAll().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        if (user == null) {
            System.out.println("User không tồn tại!");
            return;
        }

        System.out.print("Nhập số tiền: ");
        double money;
        try {
            money = Double.parseDouble(sc.nextLine());
            if (money <= 0) {
                System.out.println("Số tiền phải > 0!");
                return;
            }
        } catch (Exception e) {
            System.out.println("Số tiền không hợp lệ!");
            return;
        }

        dao.addBalance(id, money);
        System.out.println("Nạp tiền thành công");
    }

    static void hienThiMay(PCDAO dao) {
        var list = dao.findAll();

        if (list.isEmpty()) {
            System.out.println("\nDanh sách máy trống!");
            return;
        }

        BookingService bookingService = new BookingService(); // [FIX] Tạo 1 lần, không tạo trong forEach

        System.out.println("Danh sách máy");
        System.out.println("+-----+----------------------+----------+--------------+--------------+");
        System.out.printf("| %-3s | %-20s | %-8s | %-12s | %-12s |\n", "ID", "Tên máy", "Khu", "Giá", "Trạng thái");
        System.out.println("+-----+----------------------+----------+--------------+--------------+");

        list.forEach(pc -> {
            String status = bookingService.isPCBooked(pc.getId()) ? "Đang dùng" : "Trống";
            System.out.printf("| %-3d | %-20s | %-8d | %-12s | %-12s |\n",
                    pc.getId(),
                    pc.getPcName(),
                    pc.getCategoryId(),
                    String.format("%,.0f VND", pc.getPricePerHour()),
                    status);
        });

        System.out.println("+-----+----------------------+----------+--------------+--------------+");
    }

    static void themMay(PCDAO dao, Scanner sc) {
        System.out.print("Nhập tên máy: ");
        String name = sc.nextLine();

        // [FIX] Kiểm tra rỗng TRƯỚC, sau đó mới check trùng — và chỉ check 1 lần
        if (name.isEmpty()) {
            System.out.println("Không được để trống tên máy");
            return;
        }

        if (dao.existsByName(name)) {
            System.out.println("Tên máy đã tồn tại!");
            return;
        }

        CategoryDAO categoryDAO = new CategoryDAO();

        System.out.println("Danh sách khu:");
        categoryDAO.findAll().forEach(c -> System.out.println(c.getId() + " - " + c.getName()));

        System.out.print("Nhập category id: ");
        int cat;
        try {
            cat = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Category ID không hợp lệ!");
            return;
        }

        boolean catExists = categoryDAO.findAll().stream()
                .anyMatch(c -> c.getId() == cat);

        if (!catExists) {
            System.out.println("Category không tồn tại!");
            return;
        }

        System.out.print("Nhập giá/giờ: ");
        double price;
        try {
            price = Double.parseDouble(sc.nextLine());
            if (price <= 0)
                throw new Exception();
        } catch (Exception e) {
            System.out.println("Giá không hợp lệ!");
            return;
        }

        PC pc = new PC();
        pc.setPcName(name);
        pc.setCategoryId(cat);
        pc.setPricePerHour(price);
        pc.setStatus("AVAILABLE");

        dao.insert(pc);
        System.out.println("Thêm máy thành công");
    }

    static void suaMay(PCDAO dao, Scanner sc) {
        System.out.println("\n=== SỬA MÁY ===");

        System.out.print("Nhập ID máy: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        PC old = dao.findAll().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (old == null) {
            System.out.println("Không tìm thấy máy!");
            return;
        }

        System.out.println("\n--- Thông tin hiện tại ---");
        System.out.println("Tên: " + old.getPcName());
        System.out.println("Khu: " + old.getCategoryId());
        System.out.println("Giá: " + old.getPricePerHour());

        System.out.print("\nTên mới (Enter để giữ nguyên): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) {
            old.setPcName(name);
        }

        CategoryDAO categoryDAO = new CategoryDAO();
        System.out.println("\nDanh sách khu:");
        categoryDAO.findAll().forEach(c -> System.out.println(c.getId() + " - " + c.getName()));

        System.out.print("Category mới (Enter để giữ nguyên): ");
        String catInput = sc.nextLine();
        if (!catInput.isEmpty()) {
            try {
                int cat = Integer.parseInt(catInput);
                boolean exists = categoryDAO.findAll().stream()
                        .anyMatch(c -> c.getId() == cat);
                if (!exists) {
                    System.out.println("Category không tồn tại!");
                    return;
                }
                old.setCategoryId(cat);
            } catch (Exception e) {
                System.out.println("Category không hợp lệ!");
                return;
            }
        }

        System.out.print("Giá mới (Enter để giữ nguyên): ");
        String priceInput = sc.nextLine();
        if (!priceInput.isEmpty()) {
            try {
                double price = Double.parseDouble(priceInput);
                if (price <= 0) {
                    System.out.println("Giá phải > 0");
                    return;
                }
                old.setPricePerHour(price);
            } catch (Exception e) {
                System.out.println("Giá không hợp lệ!");
                return;
            }
        }

        dao.update(old);
        System.out.println("\nCập nhật thành công!");
    }

    static void xoaMay(PCDAO dao, Scanner sc) {
        System.out.print("Nhập ID máy cần xóa: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        PC pc = dao.findAll().stream()
                .filter(p -> p.getId() == id)
                .findFirst().orElse(null);

        if (pc == null) {
            System.out.println("Máy không tồn tại!");
            return;
        }

        System.out.print("Bạn có chắc muốn xóa? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            dao.delete(id);
            System.out.println("Xóa thành công");
        } else {
            System.out.println("Đã hủy");
        }
    }

    static void hienThiFood(FoodDAO dao) {
        // [FIX] Gọi findAll() 1 lần duy nhất, dùng cho cả kiểm tra empty và in ra
        List<Food> list = dao.findAll();

        if (list.isEmpty()) {
            System.out.println("\nDanh sách đồ ăn trống!");
            return;
        }

        System.out.println("+-----+-----------------+------------+-----------+");
        System.out.printf("| %-3s | %-15s | %-10s | %-9s |\n", "ID", "Tên", "Giá", "Tồn kho");
        System.out.println("+-----+-----------------+------------+-----------+");

        list.forEach(f -> System.out.printf("| %-3d | %-15s | %-10s | %-9d |\n",
                f.getId(),
                f.getName(),
                String.format("%,.0f VND", f.getPrice()),
                f.getStock()));

        System.out.println("+-----+-----------------+------------+-----------+");
    }

    static void themFood(FoodDAO dao, Scanner sc) {
        System.out.print("Nhập tên: ");
        String name = sc.nextLine();

        if (name.isEmpty()) {
            System.out.println("Tên không được để trống");
            return;
        }

        boolean exists = dao.findAll().stream()
                .anyMatch(f -> f.getName().equalsIgnoreCase(name));

        if (exists) {
            System.out.println("Tên món đã tồn tại!");
            return;
        }

        System.out.print("Nhập giá: ");
        double price;
        try {
            price = Double.parseDouble(sc.nextLine());
            if (price <= 0)
                throw new Exception();
        } catch (Exception e) {
            System.out.println("Giá không hợp lệ");
            return;
        }

        System.out.print("Nhập tồn kho: ");
        int stock;
        try {
            stock = Integer.parseInt(sc.nextLine());
            if (stock < 0)
                throw new Exception();
        } catch (Exception e) {
            System.out.println("Tồn kho không hợp lệ");
            return;
        }

        Food f = new Food();
        f.setName(name);
        f.setPrice(price);
        f.setStock(stock);

        dao.insert(f);
        System.out.println("Thêm dịch vụ thành công");
    }

    static void suaFood(FoodDAO dao, Scanner sc) {
        System.out.print("Nhập ID cần sửa: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        Food old = dao.findAll().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);

        if (old == null) {
            System.out.println("Không tìm thấy dịch vụ");
            return;
        }

        System.out.println("Thông tin cũ:");
        System.out.println(old.getId() + " - " + old.getName() + " - " + old.getPrice());

        System.out.print("Giá mới: ");
        double price;
        try {
            price = Double.parseDouble(sc.nextLine());
            if (price <= 0)
                throw new Exception();
        } catch (Exception e) {
            System.out.println("Giá không hợp lệ!");
            return;
        }

        System.out.print("Tồn kho mới: ");
        int stock;
        try {
            stock = Integer.parseInt(sc.nextLine());
            if (stock < 0)
                throw new Exception();
        } catch (Exception e) {
            System.out.println("Tồn kho không hợp lệ!");
            return;
        }

        old.setPrice(price);
        old.setStock(stock);

        dao.update(old);
        System.out.println("Cập nhật thành công");
    }

    static void xoaFood(FoodDAO dao, Scanner sc) {
        System.out.print("Nhập ID cần xóa: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        Food old = dao.findAll().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);

        if (old == null) {
            System.out.println("ID không tồn tại");
            return;
        }

        System.out.print("Bạn có chắc muốn xóa? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            dao.delete(id);
            System.out.println("Xóa thành công");
        } else {
            System.out.println("Đã hủy");
        }
    }

    static void categoryMenu(CategoryDAO dao, Scanner sc) {
        while (true) {
            System.out
                    .println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━ CATEGORY ━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out
                    .println("|                            |                          |                            |");
            System.out
                    .println("| 1. Hiển thị danh sách      | 2. Thêm category         | 3. Xoá category            |");
            System.out
                    .println("|                            |                          |                            |");
            System.out
                    .println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
            System.out.println("|                            |");
            System.out.println("| 0. Quay lại                |");
            System.out.println("|                            |");
            System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

            int c;
            try {
                c = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Nhập sai định dạng!");
                continue;
            }

            switch (c) {
                case 1:
                    dao.findAll().forEach(x -> System.out.println(x.getId() + " - " + x.getName()));
                    break;

                case 2:
                    System.out.print("Tên category: ");
                    String name = sc.nextLine();
                    if (name.isEmpty()) {
                        System.out.println("Không được để trống!");
                        break;
                    }
                    Category cat = new Category();
                    cat.setName(name);
                    dao.insert(cat);
                    break;

                case 3:
                    System.out.print("ID cần xóa: ");
                    try {
                        int id = Integer.parseInt(sc.nextLine());
                        if (dao.findById(id) == null) {
                            System.out.println("Không tồn tại!");
                            break;
                        }
                        dao.delete(id);
                    } catch (Exception e) {
                        System.out.println("ID không hợp lệ!");
                    }
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    static void userMenu(Scanner sc) {
        UserDAO dao = new UserDAO();

        while (true) {
            System.out
                    .println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━ Customer ━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out
                    .println("|                            |                          |                            |");
            System.out
                    .println("| 1. Xem danh sách user      | 2. Nạp tiền              | 0. Quay lại                |");
            System.out
                    .println("|                            |                          |                            |");
            System.out
                    .println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

            int c;
            try {
                c = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Nhập sai định dạng!");
                continue;
            }

            switch (c) {
                case 1:
                    System.out.print("Nhập role cần xem (ADMIN/STAFF/CUSTOMER): ");
                    String role = sc.nextLine();

                    System.out.println("\n=== DANH SÁCH " + role + " ===");
                    System.out.println("+-----+-----------------+-----------+--------------+-----------+");
                    System.out.printf("| %-3s | %-15s | %-9s | %-12s | %-9s |\n",
                            "ID", "Username", "Role", "Balance", "PC");
                    System.out.println("+-----+-----------------+-----------+--------------+-----------+");

                    String sql = "SELECT pc_id FROM bookings WHERE user_id=? AND status='SERVING'";

                    dao.findAll().stream()
                            .filter(u -> role.equalsIgnoreCase(u.getRole()))
                            .forEach(u -> {
                                String pc = "Không";
                                try (Connection conn = DBConnection.getConnection();
                                        PreparedStatement ps = conn.prepareStatement(sql)) {
                                    ps.setInt(1, u.getId());
                                    ResultSet rs = ps.executeQuery();
                                    if (rs.next()) {
                                        pc = "PC " + rs.getInt("pc_id");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.printf("| %-3d | %-15s | %-9s | %12s | %-9s |\n",
                                        u.getId(),
                                        u.getUsername(),
                                        u.getRole(),
                                        String.format("%,.0f", u.getBalance()),
                                        pc);
                            });

                    System.out.println("+-----+-----------------+-----------+--------------+-----------+");
                    break;

                case 2:
                    napTien(sc);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}