package project;

import java.util.Scanner;

public class BanMenu {

	public static void banUser(String loginname, int id, String checkrole) {

		System.out.println("Welcome to the user menu " + loginname + "! Your user_id is: " + id
				+ "\nIt seems like you account has been banned\nYou can login with another account or try to contact the admin"
				+ "\nPress 1 to go back to the login screen\nPress 2 to contact the admin");
		Scanner scan = new Scanner(System.in);
		String input = scan.next();
		switch (input) {
		case "1":
			Menu.menu();
			break;
		case "2":
			Methods.contactAdmin(id);
			break;
		default:
			Methods.wrongMenuInput(loginname, id, checkrole);
			break;
		}
		Methods.returnMenu(loginname, id, checkrole);
		scan.close();
	}

}
