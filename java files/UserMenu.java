package project;

import java.util.Scanner;

public class UserMenu {

	public static void userMenu(String loginname, int id, String checkrole) {
		
		System.out.println(
				"Welcome to the user menu " + loginname + "! Your user_id is: " + id + "\nPress 1 to read the inbox"
						+ "\nPress 2 to send a message\nPress 3 to read the outbox\nPress 4 to change you password"
						+ "\nPress 5 to print your chatlog\nPress 6 to go back to the login screen");
		Scanner scan = new Scanner(System.in);
		String input = scan.next();
		switch (input) {
		case "1":
			Methods.readInbox(id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "2":
			Methods.send(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "3":
			Methods.readOutbox(id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "4":
			Methods.changePassword(loginname, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "5":
			Methods.print(loginname, id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "6":
			Menu.menu();
			break;
		default:
			Methods.wrongMenuInput(loginname, id, checkrole);
			break;
		}
		scan.close();

	}

}
