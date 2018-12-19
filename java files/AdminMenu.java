package project;

import java.util.Scanner;

public class AdminMenu {

	public static void adminMenu(String loginname, int id, String checkrole) {

		System.out.println("Welcome to the admin menu " + loginname + "! Your user_id is: " + id
				+ "\nPress 1 to create new user.\nPress 2 to delete a user"
				+ "\nPress 3 to ban a user\nPress 4 to edit the role of a user"
				+ "\nPress 5 to view all the users in the system"
				+ "\nPress 6 to read the inbox\nPress 7 to send a message\nPress 8 to read the outbox"
				+ "\nPress 9 to change password\nPress 10 to print your chatlog\nPress 11 to go back to the login screen");
		Scanner scan = new Scanner(System.in);
		String input = scan.next();
		switch (input) {
		case "1":
			Methods.createUser(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "2":
			Methods.deleteUser(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "3":
			Methods.banUser(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "4":
			Methods.changeRole(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "5":
			Methods.viewUsers();
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "6":
			Methods.readInbox(id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "7":
			Methods.send(loginname, id, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "8":
			Methods.readOutbox(id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "9":
			Methods.changePassword(loginname, checkrole);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "10":
			Methods.print(loginname, id);
			Methods.returnMenu(loginname, id, checkrole);
			break;
		case "11":
			Menu.menu();
			break;
		default:
			Methods.wrongMenuInput(loginname, id, checkrole);
			break;
		}
		scan.close();

	}

}
