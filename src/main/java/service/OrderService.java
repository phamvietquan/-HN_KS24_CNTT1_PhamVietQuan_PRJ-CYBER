package service;

import dao.FoodDAO;
import model.Food;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class OrderService {

    public List<int[]> orderFood(FoodDAO dao, Scanner sc) {
        List<int[]> cart = new ArrayList<>();

        System.out.println("\n=== MENU ĐỒ ĂN ===");
        List<Food> foods = dao.findAll();
        foods.forEach(f ->
                System.out.printf("%-3d - %-20s - %,.0f VND (còn %d)%n",
                        f.getId(), f.getName(), f.getPrice(), f.getStock())
        );

        while (true) {
            System.out.print("Nhập ID món (0 để dừng): ");
            int id;
            try {
                id = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ID không hợp lệ, vui lòng nhập số!");
                continue;
            }
            if (id == 0) break;

            System.out.print("Số lượng: ");
            int qty;
            try {
                qty = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Số lượng không hợp lệ, vui lòng nhập số!");
                continue;
            }

            if (qty <= 0) {
                System.out.println("Số lượng phải > 0!");
                continue;
            }

            Food f = foods.stream()
                    .filter(x -> x.getId() == id)
                    .findFirst().orElse(null);

            if (f == null) {
                System.out.println("Món không tồn tại!");
                continue;
            }

            if (f.getStock() < qty) {
                System.out.println("Không đủ hàng! Hiện còn: " + f.getStock());
                continue;
            }

            cart.add(new int[]{id, qty});
            System.out.println("Đã thêm: " + f.getName() + " x" + qty);
        }

        return cart;
    }

    public void saveOrder(int bookingId, List<int[]> cart) {
        String sql = "INSERT INTO orders(booking_id, food_id, quantity) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                for (int[] item : cart) {
                    ps.setInt(1, bookingId);
                    ps.setInt(2, item[0]);
                    ps.setInt(3, item[1]);
                    ps.addBatch();
                }
                ps.executeBatch();

                PreparedStatement ps2 = conn.prepareStatement(
                        "UPDATE foods SET stock = stock - ? WHERE id = ?"
                );
                for (int[] item : cart) {
                    ps2.setInt(1, item[1]);
                    ps2.setInt(2, item[0]);
                    ps2.addBatch();
                }
                ps2.executeBatch();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}