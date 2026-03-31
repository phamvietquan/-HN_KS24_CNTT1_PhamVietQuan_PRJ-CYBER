package util;

import java.util.Scanner;

public class InputUtil {

    public static int getInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Nhập sai định dạng, vui lòng nhập lại (số nguyên): ");
            }
        }
    }

    public static double getDouble(Scanner sc) {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Nhập sai định dạng, vui lòng nhập lại (số thực): ");
            }
        }
    }
}
