import java.io.*;
import java.util.*;

// =================== Student Class =====================
class Student {
    int rollNo;
    String name;
    String email;
    String course;
    double marks;

    Student(int rollNo, String name, String email, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Roll No: " + rollNo +
               "\nName: " + name +
               "\nEmail: " + email +
               "\nCourse: " + course +
               "\nMarks: " + marks + "\n";
    }

    String toFileLine() {
        return rollNo + "|" + name + "|" + email + "|" + course + "|" + marks;
    }

    static Student fromFileLine(String line) {
        String[] p = line.split("\\|");
        return new Student(
                Integer.parseInt(p[0]),
                p[1],
                p[2],
                p[3],
                Double.parseDouble(p[4])
        );
    }
}


// =================== File Utility =====================
class FileUtil {

    static ArrayList<Student> loadStudents(String filename) {
        ArrayList<Student> list = new ArrayList<>();
        File file = new File(filename);

        try {
            if (!file.exists()) {
                file.createNewFile();
                return list;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(Student.fromFileLine(line));
                }
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }

        return list;
    }


    static void saveStudents(String filename, ArrayList<Student> list) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

            for (Student s : list) {
                bw.write(s.toFileLine());
                bw.newLine();
            }

            bw.close();

        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}


// =================== Student Manager =====================
class StudentManager {

    ArrayList<Student> students = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    String filename = "students.txt";

    StudentManager() {
        students = FileUtil.loadStudents(filename);
        System.out.println("Loaded students from file:\n");

        for (Student s : students) {
            System.out.println(s);
        }
    }

    void addStudent() {
        try {
            System.out.print("Enter Roll No: ");
            int roll = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Marks: ");
            double marks = Double.parseDouble(sc.nextLine());

            students.add(new Student(roll, name, email, course, marks));

            System.out.println("\nStudent Added Successfully!\n");

        } catch (Exception e) {
            System.out.println("Invalid Input!");
        }
    }

    void viewAllStudents() {
        System.out.println("\n----- Student List -----\n");

        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    void searchByName() {
        System.out.print("Enter Name to Search: ");
        String name = sc.nextLine();

        boolean found = false;

        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name)) {
                System.out.println(s);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No student found with that name.");
        }
    }

    void deleteByName() {
        System.out.print("Enter Name to Delete: ");
        String name = sc.nextLine();

        Iterator<Student> it = students.iterator();
        boolean removed = false;

        while (it.hasNext()) {
            if (it.next().name.equalsIgnoreCase(name)) {
                it.remove();
                removed = true;
            }
        }

        if (removed) System.out.println("Student removed successfully.");
        else System.out.println("No student found.");
    }

    void sortByMarks() {
        students.sort(Comparator.comparingDouble(s -> s.marks));

        System.out.println("\nSorted Student List by Marks:\n");
        viewAllStudents();
    }

    void displayFileAttributes() {
        File f = new File(filename);
        System.out.println("\nFile Attributes:");
        System.out.println("Name: " + f.getName());
        System.out.println("Path: " + f.getAbsolutePath());
        System.out.println("Size: " + f.length() + " bytes");
        System.out.println("Readable: " + f.canRead());
        System.out.println("Writable: " + f.canWrite());
    }

    void randomAccessRead() {
        try {
            RandomAccessFile raf = new RandomAccessFile(filename, "r");
            raf.seek(0);

            System.out.println("\nRandomAccessFile Output:\n");
            String line = raf.readLine();

            if (line != null)
                System.out.println("First Record: " + line);

            raf.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void saveAndExit() {
        FileUtil.saveStudents(filename, students);
        System.out.println("\nRecords Saved. Exiting Program.");
    }
}


// =================== MAIN CLASS =====================
public class StudentRecordSystem {
    public static void main(String[] args) {

        StudentManager sm = new StudentManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Show File Attributes");
            System.out.println("7. Read Using RandomAccessFile");
            System.out.println("8. Save and Exit");
            System.out.print("Enter choice: ");

            String ch = sc.nextLine();

            switch (ch) {
                case "1" -> sm.addStudent();
                case "2" -> sm.viewAllStudents();
                case "3" -> sm.searchByName();
                case "4" -> sm.deleteByName();
                case "5" -> sm.sortByMarks();
                case "6" -> sm.displayFileAttributes();
                case "7" -> sm.randomAccessRead();
                case "8" -> {
                    sm.saveAndExit();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
