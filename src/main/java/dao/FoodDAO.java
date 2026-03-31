package dao;

import model.Food;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {

    public void insert(Food f) {
        String sql = "INSERT INTO foods(name,price,stock) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, f.getName());
            ps.setDouble(2, f.getPrice());
            ps.setInt(3, f.getStock());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Food> findAll() {
        List<Food> list = new ArrayList<>();
        String sql = "SELECT * FROM foods";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Food f = new Food();
                f.setId(rs.getInt("id"));
                f.setName(rs.getString("name"));
                f.setPrice(rs.getDouble("price"));
                f.setStock(rs.getInt("stock"));
                list.add(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public void update(Food f) {
        String sql = "UPDATE foods SET name=?, price=?, stock=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, f.getName());
            ps.setDouble(2, f.getPrice());
            ps.setInt(3, f.getStock());
            ps.setInt(4, f.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        String sql = "DELETE FROM foods WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}