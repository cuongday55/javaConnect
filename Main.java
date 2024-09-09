
import java.sql.*;
import java.util.Scanner;
import static javax.swing.text.html.HTML.Attribute.ID;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author cuongtc
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            Connection connect = ketNoi();
//            check connect
            System.out.println(connect != null);
            menu(connect);
            
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Connection ketNoi() throws ClassNotFoundException, SQLException {
        
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433 ; databaseName=Bai1";
        String user = "sa2";
        String pass = "123";
        Connection connect = DriverManager.getConnection(url, user, pass);
        return connect;
    }
    
    private static void menu(Connection connect) {
        while (true) {
            try {
                System.out.print("1. hieu thi thong tin\n"
                        + "2. cap nhat tuoi\n"
                        + "3. xoa nv\n"
                        + "4. them moi\n"
                        + "5. tim kiem\n"
                        + "6. cap nhat thong tin\n"
                        + "0 thoat chuong trinh\n"
                        + "chon chuc nang: ");
                byte chon = new Scanner(System.in).nextByte();
                if (chon == 1) {
                    System.out.println("ban da chon chuc nang 1!");
                    hienThi(connect);
                } else if (chon == 2) {
                    System.out.println("ban da chon chuc nang 2!");
                    capNhatTuoi(connect);
                } else if (chon == 3) {
                    System.out.println("ban da chon chuc nang 3!");
                    xoaNV(connect);
                } else if (chon == 4) {
                    System.out.println("ban da chon chuc nang 4!");
                    themNV(connect);
                } else if (chon == 5) {
                    System.out.println("ban da chon chuc nang 5!");
                    timKiemSV(connect);
                } else if (chon == 6) {
                    System.out.println("ban da chon chuc nang 6!");
                    capNhatTT(connect);
                } else if (chon == 0) {
                    System.out.println("ban da chon chuc nang 0!");
                    System.exit(0);
                } else {
                    System.out.println("ban chon sai! moi chon lai");
                }
            } catch (Exception e) {
                System.out.println("yeu cua nhap dung dinh dang");
            }
        }
    }
    
    private static void hienThi(Connection connect) throws SQLException {
        Statement sttm = connect.createStatement();
        String sql = "select * from Employees";
        
        ResultSet rs = sttm.executeQuery(sql);
        
        while (rs.next()) {
            System.out.print("ID: \t" + rs.getInt(1) + "\t");
            System.out.print("FirstName: \t" + rs.getString(2) + "\t");
            System.out.print("LastName: \t" + rs.getString(3) + "\t");
            System.out.print("age: \t" + rs.getInt(4) + "\t");
            System.out.println("");
        }
        rs.close();
        sttm.close();
    }
    
    private static void capNhatTuoi(Connection connect) throws SQLException {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("nhap id can cap nhat tuoi: ");
        int ID = Integer.parseInt(sc.nextLine());
        if (checkID(ID, connect)) {
            System.out.print("nhap tuoi: ");
            int age = Integer.parseInt(sc.nextLine());
            if (age < 0) {
                age = 18;
            }
            connect.setAutoCommit(false);
            String sql = "UPDATE Employees SET age = ? WHERE ID = ?";
            PreparedStatement sttm = connect.prepareStatement(sql);
            sttm.setInt(1, age);
            sttm.setInt(2, ID);
            int sl = sttm.executeUpdate();
            System.out.print("nhap 1 de xac dinh cap nhat va khac 1 de huy: ");
            String tam = sc.nextLine();
            if (tam.equals("1")) {
                connect.commit();
                System.out.println("da co " + sl + " nhan vien duoc cap nhat!");
            } else {
                connect.rollback();
                System.out.println("da huy bo cap nhat tuoi!");
            }
            connect.setAutoCommit(true);
            sttm.close();
        }
        else {
            System.out.println("ma khong ton tai!");
        }
    }
    
    private static void xoaNV(Connection connect) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("nhap id can cap xoa: ");
        int ID = Integer.parseInt(sc.nextLine());
        if (checkID(ID, connect)) {
            String sql = "DELETE Employees WHERE ID = " + ID;
            connect.setAutoCommit(false);
            PreparedStatement sttm = connect.prepareStatement(sql);
            int sl = sttm.executeUpdate();
            System.out.println("1 de xac nhan khac 1 de huy: ");
            String chon = sc.nextLine();
            if (chon.equals("1")) {
                connect.commit();
                System.out.println("da co " + sl + " nhan vien duoc xoa!");
            } else {
                System.out.println("da huy!");
                connect.rollback();
            }
            connect.setAutoCommit(true);
            sttm.close();
        } else {
            System.out.println("khong ton tai id");
        }
    }
    
    private static void themNV(Connection connect) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("nhap id: ");
        int ID = Integer.parseInt(sc.nextLine());
        System.out.print("nhap FirstName: ");
        String FirstName = sc.nextLine();
        System.out.print("nhap LastName: ");
        String LastName = sc.nextLine();
        System.out.print("nhap tuoi: ");
        int age = Integer.parseInt(sc.nextLine());
        if (!checkID(ID, connect)) {
            String sql = "INSERT INTO Employees (ID , FirstName , LastName , age) "
                    + "values(" + ID + ",'" + FirstName + "','" + LastName + "','" + age + "')";
            Statement sttm = connect.createStatement();
            int sl = sttm.executeUpdate(sql);
            System.out.println("da co " + sl + " nhan vien duoc them!");
            sttm.close();
        } else {
            System.out.println("da ton tai ma nhan vien");
        }
        
    }
    
    private static void timKiemSV(Connection connect) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("nhap id can tim kiem: ");
        int ID = Integer.parseInt(sc.nextLine());
        
        if (checkID(ID, connect)) {
            String sql = "SELECT * FROM Employees WHERE ID = " + ID;
            Statement sttm = connect.createStatement();
            ResultSet rs = sttm.executeQuery(sql);
            while (rs.next()) {
                System.out.print("ID: " + rs.getInt(1) + " ");
                System.out.print("FirstName: " + rs.getString(2) + " ");
                System.out.print("LastName: " + rs.getString(3) + " ");
                System.out.print("age: " + rs.getInt(4) + " ");
                System.out.println("");
            }
            rs.close();
            sttm.close();
        } else {
            System.out.println("ma khong ton tai!");
        }
    }
    
    private static boolean checkID(int id, Connection connect) throws SQLException {
        Statement sttm = connect.createStatement();
        String sql = "select ID from Employees";
        
        ResultSet rs = sttm.executeQuery(sql);
        
        while (rs.next()) {
            if (rs.getInt(1) == id) {
                rs.close();
                sttm.close();
                return true;
            }
        }
        rs.close();
        sttm.close();
        return false;
    }
    
    private static void capNhatTT(Connection connect) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("nhap id can cap nhat: ");
        int ID = Integer.parseInt(sc.nextLine());
        String sql;
        PreparedStatement sttm;
        if (checkID(ID, connect)) {
            System.out.print("nhap ho: ");
            String firstName = sc.nextLine();
            if (!firstName.equals("")) {
                sql = "UPDATE Employees SET FirstName=? WHERE ID = ?";
                sttm = connect.prepareStatement(sql);
                sttm.setString(1, firstName);
                sttm.setInt(2, ID);
                sttm.executeUpdate();
            }
            System.out.print("nhap ten: ");
            String lastName = sc.nextLine();
            if (!lastName.equals("")) {
                sql = "UPDATE Employees SET LastName=? WHERE ID = ?";
                sttm = connect.prepareStatement(sql);
                sttm.setString(1, lastName);
                sttm.setInt(2, ID);
                sttm.executeUpdate();
            }
            System.out.print("nhap tuoi: ");
            int tuoi = Integer.parseInt(sc.nextLine());
            if (tuoi < 0) {
                tuoi = 0;
            }
            sql = "UPDATE Employees SET age = ? WHERE ID = ?";
            sttm = connect.prepareStatement(sql);
            sttm.setInt(1, tuoi);
            sttm.setInt(2, ID);
            int r = sttm.executeUpdate();
            System.out.println("da co " + r + " nhan vien duoc cap nhat!");
            sttm.close();
        } else {
            System.out.println("ma khong ton tai!");
        }
    }
}
