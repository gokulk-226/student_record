import java.sql.*;
import java.util.Scanner;

public class StudentRecordManagement {
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";     
    static final String PASS = "GokulMysql@2004"; 
    

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            int choice;
            do {
                System.out.println("\n--- Student Record Management ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        addStudent(conn, sc);
                        break;
                    case 2:
                        viewStudents(conn);
                        break;
                    case 3:
                        updateStudent(conn, sc);
                        break;
                    case 4:
                        deleteStudent(conn, sc);
                        break;
                    case 5:
                        System.out.println("Exiting... Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } while (choice != 5);
        } catch (SQLException e) {
            System.out.println("Database connection error!");
            e.printStackTrace();
        }
    }

    static void addStudent(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter course: ");
        String course = sc.nextLine();

        String sql = "INSERT INTO students (name, email, course) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student successfully added.");
            } else {
                System.out.println("Failed to add student.");
            }
        }
    }

    static void viewStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nID\tName\t\tEmail\t\t\tCourse");
            System.out.println("-------------------------------------------------------------");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%d\t%s\t\t%s\t\t%s\n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("course"));
            }
            if (!hasData) {
                System.out.println("No student records found.");
            }
        }
    }

    static void updateStudent(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter student ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new email: ");
        String email = sc.nextLine();
        System.out.print("Enter new course: ");
        String course = sc.nextLine();

        String sql = "UPDATE students SET name = ?, email = ?, course = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            pstmt.setInt(4, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student record successfully updated.");
            } else {
                System.out.println("No matching student found to update.");
            }
        }
    }

    static void deleteStudent(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter student ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student record successfully deleted.");
            } else {
                System.out.println("No matching student found to delete.");
            }
        }
    }
}
