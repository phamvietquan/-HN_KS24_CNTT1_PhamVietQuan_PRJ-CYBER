package presentation;

import model.User;
import service.AuthService;

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

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Nhập sai định dạng!");
                continue;
            }

            switch (choice) {

                case 1:
                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();
                    if (u.isEmpty()) {
                        System.out.println("Username không được để trống!");
                        break;
                    }

                    System.out.print("Password: ");
                    String p = sc.nextLine();

                    System.out.print("Full name: ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Full name không được để trống!");
                        break;
                    }

                    System.out.print("Phone: ");
                    String phone = sc.nextLine().trim();
                    if (phone.isEmpty()) {
                        System.out.println("Phone không được để trống!");
                        break;
                    }

                    auth.register(u, p, name, phone);
                    break;

                case 2:
                    while (true) {
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
                                break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.print("Nhập lại? (Y/N): ");
                        String retry = sc.nextLine();
                        if (!retry.equalsIgnoreCase("Y")) break;
                    }
                    break;

                case 0:
                    System.exit(0);
            }
        }
    }
}