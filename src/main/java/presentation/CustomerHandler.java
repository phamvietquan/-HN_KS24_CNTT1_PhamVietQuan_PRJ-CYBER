package presentation;

import dao.FoodDAO;
import dao.PCDAO;
import dao.UserDAO;
import model.Food;
import model.PC;
import model.User;
import service.BookingService;
import service.OrderService;

import java.util.*;
import java.util.stream.Collectors;

public class CustomerHandler {

    public static void datMayFlow(User user, Scanner sc) {

        PCDAO pcDAO = new PCDAO();
        BookingService bookingService = new BookingService();
        OrderService orderService = new OrderService();
        FoodDAO foodDAO = new FoodDAO();

        List<PC> list = pcDAO.findAll();

        // ===== 1. HIỂN THỊ THEO KHU =====
        System.out.println("\n===== Danh sách máy trống =====");

        Map<Integer, List<PC>> group = list.stream()
                .filter(pc -> "AVAILABLE".equalsIgnoreCase(pc.getStatus())
                        && !bookingService.isPCBooked(pc.getId()))
                .collect(Collectors.groupingBy(PC::getCategoryId));

        if (group.isEmpty()) {
            System.out.println("Hiện không có máy trống!");
            return;
        }

        for (Integer cat : group.keySet()) {
            System.out.println("\n--- KHU " + cat + " ---");
            group.get(cat).forEach(pc ->
                    System.out.println(pc.getId() + " - " + pc.getPcName())
            );
        }

        // ===== 2. CHỌN MÁY =====
        System.out.print("\nChọn ID Máy: ");
        int pcId;

        try {
            pcId = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("ID Không hợp lệ!");
            return;
        }

        PC selected = list.stream()
                .filter(p -> p.getId() == pcId)
                .findFirst()
                .orElse(null);

        if (selected == null) {
            System.out.println("Máy không tồn tại!");
            return;
        }

        // CHECK TRẠNG THÁI PC
        if (!selected.getStatus().equalsIgnoreCase("AVAILABLE")) {
            System.out.println("Máy đang được sử dụng!");
            return;
        }

        // CHECK ĐÃ ĐẶT
        if (bookingService.isPCBooked(pcId)) {
            System.out.println("Máy đã có người đặt! Không thể tiếp tục!");
            return;
        }

        // ===== 3. NHẬP GIỜ =====
        System.out.print("Nhập số giờ: ");
        int hours;

        try {
            hours = Integer.parseInt(sc.nextLine());
            if (hours <= 0) throw new Exception();
        } catch (Exception e) {
            System.out.println("Thời gian không hợp lệ!");
            return;
        }

        // ===== CHECK TIỀN =====
        double pcTotal = hours * selected.getPricePerHour();

        if (user.getBalance() < pcTotal) {
            System.out.println("Không đủ tiền để đặt máy!");
            return;
        }

        // ===== 4. TẠO BOOKING =====
        int bookingId = bookingService.createBooking(user.getId(), pcId, hours);

        if (bookingId == -1) {
            System.out.println("Đặt máy thất bại!");
            return;
        }

        // ===== 5. GỌI MÓN =====
        List<int[]> cart = orderService.orderFood(foodDAO, sc);

        if (!cart.isEmpty()) {
            orderService.saveOrder(bookingId, cart);
        }

        // ===== 6. TÍNH HÓA ĐƠN =====
        double foodTotal = 0;
        List<Food> allFoods = foodDAO.findAll();

        for (int[] item : cart) {
            Food f = allFoods.stream()
                    .filter(x -> x.getId() == item[0])
                    .findFirst().orElse(null);

            if (f != null) {
                foodTotal += f.getPrice() * item[1];
            }
        }

        double totalCost = pcTotal + foodTotal;

        // ===== 7. TRỪ TIỀN USER =====
        UserDAO userDAO = new UserDAO();
        userDAO.addBalance(user.getId(), -totalCost);
        user.setBalance(user.getBalance() - totalCost);

        // ===== 8. IN HÓA ĐƠN =====
        System.out.println("\n===== HOÁ ĐƠN =====");
        System.out.println("Máy: " + selected.getPcName());
        System.out.println("Khu: " + selected.getCategoryId());
        System.out.println("Số giờ: " + hours);
        System.out.printf("Tiền máy: %,.0f VND%n", pcTotal);
        System.out.printf("Tiền F&B: %,.0f VND%n", foodTotal);
        System.out.printf("Tổng: %,.0f VND%n", totalCost);
        System.out.printf("Số dư còn lại: %,.0f VND%n", user.getBalance());

        System.out.println("\nĐặt máy thành công!");
        System.out.println("Trạng thái: CHỜ XÁC NHẬN (Staff sẽ xác nhận và kích hoạt máy cho bạn)");
    }

    public static void xemTaiKhoan(User user) {
        double money = user.getBalance();
        double minutes = (money / 10000) * 60;

        System.out.printf("Số dư: %,.0f VND%n", money);
        System.out.println("Có thể chơi thêm: " + (int) minutes + " phút (tính theo 10,000 VND/giờ)");
    }
}