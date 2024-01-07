import java.lang.Thread;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.concurrent.ThreadLocalRandom;


public class Hotel {
    public static void main(String[] args) throws InterruptedException, IOException {
        Accounts.try_login();
    }
}

class Accounts {
    static Scanner sc = new Scanner(System.in);

    static void login() throws IOException {
        Scanner data_reader = new Scanner(new File("user_data.txt"));
        System.out.println("Enter your username: ");
        String user_try, password, line, username, pass_try;
        boolean found = false;
        user_try = sc.nextLine();
        while (data_reader.hasNextLine()) {
			line = data_reader.nextLine();
            username = line.split(" ", -1)[0];
            if (username.equals(user_try)) {
                found = true;
                password = line.split(" ", -1)[1];
                System.out.println("Enter your password: ");
                pass_try = sc.nextLine();
                while (pass_try.equals(password) == false) {
                    if (pass_try.toLowerCase().equals("q")) {
                        break;
                    } else {
                        System.out.println("Wrong password, try again!");
                        pass_try = sc.nextLine();
                    }
                }
                if (username.equals("admin")) {
                    admin();
                } else {
                    logged_in(user_try);
                }
            }       
		}
        if (found == false) {
            not_found();
        }
    }

    static void register() throws IOException {
        FileWriter data_writer = new FileWriter("user_data.txt", true);
        BufferedWriter bw = new BufferedWriter(data_writer);
        PrintWriter pw = new PrintWriter(data_writer);

        System.out.println("Let's make a new account!");
        String username = create_username();
        String password = create_password();
        pw.println(String.format("\n%s %s", username, password));
        data_writer.close(); 
        try_login();
    }

    static String create_username() throws FileNotFoundException {
        Scanner data_reader = new Scanner(new File("user_data.txt"));
        System.out.println("Enter your username:");
        String potential_username = sc.nextLine();
        String line, username;
        if (potential_username.lastIndexOf(" ") != -1) {
            System.out.println("Invalid username! Please try again.");
            potential_username = create_username();
        }
        while (data_reader.hasNextLine()) {
			line = data_reader.nextLine();
            username = line.split(" ", -1)[0];
            if (username.equals(potential_username)) {
                System.out.println("This username is taken! Try one more time.");
                potential_username = create_username();
            }
        }
        return potential_username;
    }

    static String create_password() {
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        System.out.println("Repeat your password: ");
        String repeated_password = sc.nextLine();
        if (password.equals(repeated_password) == false) {
            System.out.println("Your passwords don't match! Try again.");
            create_password();
        }
        return password;
    }

    static void not_found() throws IOException {
        System.out.println("Username not found! Would you like to create a new account?\n[Y] - yes\n[N] - no\nYour choice: ");
            String answer = sc.nextLine();
            if (answer.toLowerCase().equals("y")) {
                register();
            } else if (answer.toLowerCase().equals("n")) {
                try_login();
            } else {
                System.out.println("Invalid choice! Try again\n\n");
                not_found();
            }
    }

    static void try_login() throws IOException {
        System.out.println("Do you have an account in Paradise Resort? \n[Y] - yes\n[N] - no\nYour choice: ");
        String answer = sc.nextLine();
        if (answer.toLowerCase().equals("y")) {
            login();
        } else if (answer.toLowerCase().equals("n")) {
            register();
        } else {
            System.out.println("Invalid choice! Try again\n\n");
            try_login();
        }
    }

    static void logged_in(String user) throws IOException {
        System.out.println("Welcome to Paradise Resort Web!\nWhat would you like to do?\n[B] - My bookings\n[M] - Make a booking\n[L] - Log out");
        String answer = sc.nextLine();
        if (answer.toLowerCase().equals("b")) {
            my_bookings(2, user);
            logged_in(user);
        } else if (answer.toLowerCase().equals("m")) {
            choosing_type(user);
            logged_in(user);
        } else if (answer.toLowerCase().equals("l")) {
            log_out();
        } else {
            System.out.println("Invalid answer!");
            logged_in(user);
        }
    }

    static void log_out() throws IOException {
        System.out.println("Bye-bye!");
        try_login();
    }

    static void my_bookings(int type, String user) throws IOException {
        System.out.println("Bookings:\n");
        String line[]; int counter = 1;
        String str; boolean to_print = false;
        Scanner data_reader = new Scanner(new File("bookings.txt"));
        while (data_reader.hasNextLine()) {
            str = data_reader.nextLine();
            if (str.indexOf("blank") != 0) {
                line = str.split(" ", -1);
                if (type == 1) {
                    to_print = true;
                } else if (type == 2) {
                    if (line[2].equals(user)) {
                        to_print = true;
                    }
                } else if (type == 3) {
                    if (line[4].equals(user)) {
                        to_print = true;
                    }
                } else if (type == 4) {
                    if (line[0].equals(user)) {
                        to_print = true;
                    }
                }
                if (to_print) {
                    System.out.println(String.format("%d. Reservation number #%s:\nReservant's name: %s\nRoom #%s\nNumber of nights: %s\n\n", counter, line[0], line[4].replaceAll("_", " "), line[1], line[3]));
                    counter += 1;
                    to_print = false;
                }
            }
        }
        if (counter == 1) {
            System.out.println("No bookings found!");
        }
    }

    static void choosing_type(String user) throws IOException {
        System.out.println("What kind of room do you want?\n[1] - Single room\n[2] - Double room");
        String roomType = sc.nextLine();
        if (roomType.equals("1")) {
            booking(user, 1);
        } else if (roomType.equals("2")) {
            booking(user, 2);
        } else {
            System.out.println("Invalid input! Try again.");
            choosing_type(user);
        }
    }

    static void booking(String user, int type) throws IOException {
        int overall_books = number_of_bookings(user);
        if (overall_books >= 6) {
            System.out.println("You can't have more than 6 bookings!\n");
            return;
        }
        Scanner data_reader = new Scanner(new File("rooms.txt"));
        int counter = 0; int price;
        if (type == 1) {
            counter = 0;
            price = 100;
        } else {
            counter = 30;
            price = 150;
        }        
        String str;
        
        for (int i = 0; i < counter; i++) {
            data_reader.nextLine();
        }

        if (overall_books >= 4) {
            price *= 0.8;
        } else if (overall_books >= 2) {
            price *= 0.9;
        }
        for (int i = counter; i < counter + 30; i++) {
            str = data_reader.nextLine();
            if (str.contains("-") == false) {
                System.out.println("Name of the reservant?");
                String name = sc.nextLine();
                System.out.println("There is a room for you!\nRoom #" + str + "\nFor how many nights?");
                int nights = sc.nextInt();
                System.out.println("---------------");
                System.out.println("Price for 1 night: " + price + " $");
                System.out.println("Number of nights: " + nights);
                System.out.println("---------------");
                System.out.println("Total: " + nights * price + " $");
                System.out.println("---------------\n");
                System.out.println("Confirm?\n\n[Y] - yes\n[N] - no");
                String answer = sc.nextLine();
                answer = sc.nextLine();
                if (answer.toLowerCase().equals("y")) {
                    String line[]; 
                    String numbers[] = new String[60]; 
                    counter = 0;
                    Scanner reader = new Scanner(new File("bookings.txt"));
                    while (reader.hasNextLine()) {
                        line = reader.nextLine().split(" ", -1);
                        numbers[counter] = line[0];
                        counter += 1;
                    }
                    String number = Integer.toString(ThreadLocalRandom.current().nextInt(10000, 100000 + 1));
                    boolean check = true;
                    while (check) {
                        boolean small_check = true;
                        for (i = 0; i < counter; i++) {
                            if (numbers[i].equals(number)) {
                                small_check = false;
                            }
                        }
                        if (!small_check) {
                            number = Integer.toString(ThreadLocalRandom.current().nextInt(10000, 100000 + 1));
                        } else {
                            check = false;
                        }
                    }
                    System.out.println("\nBooking confirmed!\nYour reservation number is #" + number);
                    String to_over = number + " " + str + " " + user + " " + nights + " " + name.replaceAll(" ", "_");
                    overwrite("rooms.txt", str);
                    overwrite("bookings.txt", to_over);
                    return;
                } else if (answer.toLowerCase().equals("n")){
                    return;
                } else {
                    System.out.println("Invalid choice!");
                    booking(user, type);
                }
            }
        }
    }

    static void overwrite(String filename, String content) throws IOException {
        Scanner sc = new Scanner(new File(filename));
        StringBuffer buffer = new StringBuffer();
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()+System.lineSeparator());
        }
        String fileContents = buffer.toString();
        sc.close();
        if (filename.equals("bookings.txt")) {
            String oldLine = "blank";
            fileContents = fileContents.replaceFirst(oldLine, content + "\nblank");
        } else {
            String oldLine = content;
            fileContents = fileContents.replaceFirst(oldLine, content + "-");
        }
        FileWriter writer = new FileWriter(filename);
        writer.append(fileContents);
        writer.flush();
        writer.close();
    }

    static int number_of_bookings(String user) throws FileNotFoundException {
        String line[]; int counter = 0;
        String str;
        Scanner data_reader = new Scanner(new File("bookings.txt"));
        while (data_reader.hasNextLine()) {
            str = data_reader.nextLine();
            if (str.indexOf("blank") != 0) {
                line = str.split(" ", -1);
                if (line[2].equals(user)) {
                    counter += 1;
                }
            }
        }
        return counter;
    }

    static void admin() throws IOException {
        System.out.println("Hi, admin!\nWhat do you want to do?\n\n[S] - see all bookings\n[R] - find a booking by reservation number\n[U] - find bookings by username\n[N] - find bookings by name\n[L] - log out");
        String answer = sc.nextLine();
        if (answer.toLowerCase().equals("s")) {
            my_bookings(1, "");
        } else if (answer.toLowerCase().equals("r")) {
            System.out.println("Enter the reservation number: ");
            String number = sc.nextLine();
            my_bookings(4, number);
        } else if (answer.toLowerCase().equals("u")) {
            System.out.println("Following usernames found: ");
            Scanner data_reader = new Scanner(new File("user_data.txt"));
            String line;
            while (data_reader.hasNextLine()) {
			    line = data_reader.nextLine();
                System.out.println(line.split(" ", -1)[0]);
            }
            System.out.println("\nYour choice: ");
            String choice = sc.nextLine();
            my_bookings(2, choice);
        } else if (answer.toLowerCase().equals("n")) {
            System.out.println("Enter thr name: ");
            String choice = sc.nextLine();
            my_bookings(3, choice.replaceAll(" ", "_"));
        } else if (answer.toLowerCase().equals("l")) {
            log_out();
        } else {
            System.out.println("Invalid choice!");
        }
        admin();
    }
}