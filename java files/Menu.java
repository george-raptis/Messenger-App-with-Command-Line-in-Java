package project;

import java.util.Scanner;

public class Menu {

	Methods method = new Methods();

	public static void menu() {

		String url = "jdbc:mysql://localhost:3306/project?useSSL=false";
		Database.connect(url, "root", "12345");

		System.out.println("Welcome to the menu!");
		Scanner scan = new Scanner(System.in);

		// username
		System.out.println("Type the username:");
		String username = scan.next();
		int checkUser = Methods.checkUsername(username);

		// password
		System.out.println("Type the password:");
		String password = scan.next();
		//String password = PasswordField.readPassword("Enter password:\n");
		int checkPass = Methods.checkPassword(password, username);
		String checkrole;

		if (checkUser == 1) {
			if (checkPass == 1) {
				System.out.println("pass correct");
				checkrole = Methods.checkRole(username);
				int id = Methods.checkUserID(username);
				switch (checkrole) {
				case "admin":
					AdminMenu.adminMenu(username, id, checkrole);
					break;
				case "mod":
					ModMenu.modMenu(username, id, checkrole);
				case "user":
					UserMenu.userMenu(username, id, checkrole);
					break;
				case "ban":
					BanMenu.banUser(username, id, checkrole);
					break;

				default:
					break;
				}
			} else {
				Methods.wrongPass();
			}
		} else {
			Methods.wrongPass();
		}
		scan.close();

	}

}
