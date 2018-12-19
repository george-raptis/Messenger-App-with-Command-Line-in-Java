package project;

import java.util.Scanner;

public class ModMenu {

	public static void modMenu(String loginname, int id, String checkrole) {

		System.out.println("Welcome to the mod menu " + loginname + "! Your user_id is: " + id + "\n"
				+ "Press 1 to delete a user\nPress 2 to ban a user" + "\nPress 3 to read the inbox"
				+ "\nPress 4 to send a message\nPress 5 to read the outbox"
				+ "\nPress 6 to change you password\nPress 7 to print your chatlog"
				+ "\nPress 8 to go back to the login screen");
		Scanner scan = new Scanner(System.in);
		String input = scan.next();
		switch (input) {
		case "1":
			Methods.deleteUser(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "2":
			Methods.banUser(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "3":
			Methods.readInbox(id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "4":
			Methods.send(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "5":
			Methods.readOutbox(id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "6":
			Methods.changePassword(loginname, checkrole);
			break;
		case "7":
			Methods.print(loginname, id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "8":
			Menu.menu();
			break;
		default:
			Methods.wrongMenuInput(loginname, id, checkrole);
			break;
		}
		scan.close();

	}

}
