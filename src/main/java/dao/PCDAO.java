package dao;

import model.PC;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PCDAO {

    public void insert(PC pc) {
        String sql = "INSERT INTO pcs(pc_name, category_id, price_per_hour) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pc.getPcName());
            ps.setInt(2, pc.getCategoryId());
            ps.setDouble(3, pc.getPricePerHour());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PC> findAll() {
        List<PC> list = new ArrayList<>();
        String sql = "SELECT * FROM pcs";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                PC pc = new PC();
                pc.setId(rs.getInt("id"));
                pc.setPcName(rs.getString("pc_name"));
                pc.setPricePerHour(rs.getDouble("price_per_hour"));
                pc.setStatus(rs.getString("status"));
                pc.setCategoryId(rs.getInt("category_id"));
                list.add(pc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public void update(PC pc) {
        String sql = "UPDATE pcs SET pc_name=?, category_id=?, price_per_hour=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pc.getPcName());
            ps.setInt(2, pc.getCategoryId());
            ps.setDouble(3, pc.getPricePerHour());
            ps.setInt(4, pc.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        String sql = "DELETE FROM pcs WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // check máy đã đặt
    static void showAvailablePC(PCDAO dao) {
        System.out.println("\n=== DANH SACH MAY TRONG ===");

        dao.findAll().stream()
                .filter(pc -> "AVAILABLE".equalsIgnoreCase(pc.getStatus()))
                .forEach(pc ->
                        System.out.println(pc.getId() + " - " + pc.getPcName())
                );
    }
    public boolean existsByName(String name) {
        String sql = "SELECT id FROM pcs WHERE pc_name=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}