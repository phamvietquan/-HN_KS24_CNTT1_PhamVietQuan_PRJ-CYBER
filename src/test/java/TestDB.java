import util.DBConnection;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Kết nối thành công!");
        } catch (Exception e) {
            System.out.println("Fail!");
            e.printStackTrace();
        }
    }
}