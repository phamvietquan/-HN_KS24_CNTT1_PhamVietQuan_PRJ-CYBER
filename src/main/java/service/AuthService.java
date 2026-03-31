package service;

import dao.UserDAO;
import model.User;
import util.HashUtil;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public void register(String username, String password, String fullName, String phone) {

        if (password.length() < 6) {
            System.out.println("Mật khẩu phải >= 6 ký tự");
            return;
        }

        if (userDAO.checkUsernameExists(username)) {
            System.out.println("Username đã tồn tại");
            return;
        }
        if (userDAO.checkPhoneExists(phone)) {
            System.out.println("Số điện thoại đã tồn tại");
            return;
        }

        String hashPass = HashUtil.hash(password);

        User user = new User(username, hashPass, fullName, phone, "CUSTOMER");

        userDAO.register(user);

        System.out.println("Đăng ký thành công!");
    }

    public User login(String username, String password) {

        String hashPass = HashUtil.hash(password);

        User user = userDAO.login(username, hashPass);

        if (user == null) {
            System.out.println("Sai tài khoản hoặc mật khẩu");
        }

        return user;
    }
}