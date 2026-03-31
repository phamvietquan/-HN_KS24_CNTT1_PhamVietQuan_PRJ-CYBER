package presentation;

import model.User;
import service.AuthService;
import util.InputUtil;

import java.util.Scanner;

public class MainApp {
    public static void showMainMenu() {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("|                          |                          |                          |");
        System.out.println("| 1. Đăng ký               | 2. Đăng nhập             | 0. Thoát                 |");
        System.out.println("|                          |                          |                          |");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();
        while (true) {
            showMainMenu();
            System.out.print("Chọn chức năng: ");

            int choice = InputUtil.getInt(sc);

            switch (choice) {

                case 1:
                    System.out.print("Username: ");
                    String u = sc.nextLine();

                    System.out.print("Password: ");
                    String p = sc.nextLine();

                    System.out.print("Full name: ");
                    String name = sc.nextLine();

                    System.out.print("Phone: ");
                    String phone = sc.nextLine();

                    auth.register(u, p, name, phone);
                    break;

                case 2:
                    System.out.print("Username: ");
                    String u2 = sc.nextLine();

                    System.out.print("Password: ");
                    String p2 = sc.nextLine();

                    try {
                        User user = auth.login(u2, p2);

                        if (user != null) {
                            String role = user.getRole();

                            if ("ADMIN".equals(role)) {
                                System.out.println("Đăng nhập quyền ADMIN");
                                AdminMenu.start();

                            } else if ("STAFF".equals(role)) {
                                System.out.println("Đăng nhập quyền STAFF");
                                StaffMenu.start();

                            } else {
                                System.out.println("Đăng nhập quyền USER");
                                CustomerMenu.setUser(user);
                                CustomerMenu.start();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case 0:
                    System.exit(0);
            }
        }
    }
}