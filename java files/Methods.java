package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Methods {

	static String url = "jdbc:mysql://localhost:3306/project?useSSL=false";

	public static int checkUsername(String username) {

		Database.connect(url, "root", "12345");

		String query1 = "select count(*) as count\r\n" + "from project.user\r\n" + "where username = \"" + username
				+ "\"";
		PreparedStatement stm1;

		int checkUser = 0;
		try {
			stm1 = Database.connection.prepareStatement(query1);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				checkUser = rs.getInt("count");
				// System.out.println(checkUser);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return checkUser;
	}

	public static int checkPassword(String password, String username) {

		Database.connect(url, "root", "12345");

		String query2 = "select count(*) as count\r\n" + "from project.user\r\n" + "where password = aes_encrypt(\""
				+ password + "\", \"password\") and username = \"" + username + "\";";
		PreparedStatement stm2;
		int checkPass = 0;

		try {
			stm2 = Database.connection.prepareStatement(query2);
			ResultSet rs = stm2.executeQuery();
			while (rs.next()) {
				checkPass = rs.getInt("count");
				// System.out.println(checkPass);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return checkPass;
	}

	public static String checkRole(String username) {

		int userExists = checkSend(username);

		if (userExists == 1) {

			Database.connect(url, "root", "12345");

			String queryrole = "select role\r\n" + "from project.user\r\n" + "where username = \"" + username + "\";";
			PreparedStatement stmrole;
			String checkrole = null;
			try {
				stmrole = Database.connection.prepareStatement(queryrole);
				ResultSet rs = stmrole.executeQuery();
				while (rs.next()) {
					checkrole = rs.getString("role");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					Database.connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return checkrole;
		} else {
			return null;
		}
	}

	public static int checkUserID(String username) {

		Database.connect(url, "root", "12345");

		String user_id = "select user_id\r\n" + "from project.user\r\n" + "where username = \"" + username + "\";";
		PreparedStatement stmid;
		int id = 0;
		try {
			stmid = Database.connection.prepareStatement(user_id);
			ResultSet rsid = stmid.executeQuery();
			while (rsid.next()) {
				id = rsid.getShort("user_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return id;

	}

	public static void createUser(String loginname, int id, String checkrole) {

		Scanner scan = new Scanner(System.in);
		System.out.println("Type the username:");
		String username = scan.next();

		int exists = checkSend(username);

		if (exists == 0) {

			int role = chooseRole();

			System.out.println("Type the password of the new user:");
			String password = scan.next();

			String[] roles = { "admin", "mod", "user" };

			Database.connect(url, "root", "12345");

			String query = "insert into project.user values (?, ?, ?, aes_encrypt(?, 'password'));";
			PreparedStatement stm1;

			// String pass = "aes_encrypt(\"" + password + "\", \"password\")";

			try {
				stm1 = Database.connection.prepareStatement(query);
				stm1.setString(1, null);
				stm1.setString(2, username);
				stm1.setString(3, roles[role - 1]);
				stm1.setString(4, password);
				stm1.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					Database.connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("The user was created successfully!");
		} else {
			System.out.println("The username you entered already exists\nPress 1 to try again"
					+ "\nPress 2 to go back to the menu");

			String choose = scan.next();
			switch (choose) {
			case "1":
				createUser(loginname, id, checkrole);
				break;
			case "2":
				switch (checkrole) {
				case "admin":
					AdminMenu.adminMenu(loginname, id, checkrole);
					break;
				default:
					break;
				}
			default:
				wrongMenuInput(loginname, id, checkrole);
				break;
			}
		}
	}

	public static int chooseRole() {

		System.out.println("Choose the role:\nPress 1 for 'ADMIN'\nPress 2 for 'MOD'\nPress 3 for 'USER'");
		Scanner scan = new Scanner(System.in);
		String userrole = scan.next();

		int role = 0;
		switch (userrole) {
		case "1":
			role = 1;
			break;
		case "2":
			role = 2;
			break;
		case "3":
			role = 3;
			break;
		default:
			System.out.println("Wrong input. Try again.");
			chooseRole();
			break;
		}

		return role;

	}

	public static void deleteUser(String loginname, int id, String checkrole) {

		Scanner scan = new Scanner(System.in);
		System.out.println("type the username of the user you wish to delete:");
		String usertoDelete = scan.next();

		int checkDelete = checkSend(usertoDelete);

		// check if exists
		if (checkDelete == 1) {

			// check if you delete yourself
			if (!loginname.equals(usertoDelete)) {

				Database.connect(url, "root", "12345");

				String query = "delete from project.user\r\n" + "where username = \"" + usertoDelete + "\"";
				PreparedStatement stm;

				try {
					stm = Database.connection.prepareStatement(query);
					stm.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						Database.connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("The user" + usertoDelete + "was deleted successfully!");
			} else {
				System.out.println("You can't delete yourself\nPress 1 to try again\nPress 2 to go back to the menu");
				String choose = scan.next();
				switch (choose) {
				case "1":
					banUser(loginname, id, checkrole);
					break;
				case "2":
					switch (checkrole) {
					case "admin":
						AdminMenu.adminMenu(loginname, id, checkrole);
						break;
					case "mod":
						ModMenu.modMenu(loginname, id, checkrole);
						break;
					default:
						break;
					}
				default:
					wrongMenuInput(loginname, id, checkrole);
					break;
				}
			}
		} else {
			System.out.println("The username you entered does not exist\nPress 1 to try again"
					+ "\nPress 2 to go back to the menu");

			String choose = scan.next();
			switch (choose) {
			case "1":
				deleteUser(loginname, id, checkrole);
				break;
			case "2":
				switch (checkrole) {
				case "admin":
					AdminMenu.adminMenu(loginname, id, checkrole);
					break;
				case "mod":
					ModMenu.modMenu(loginname, id, checkrole);
					break;
				default:
					break;
				}
			default:
				wrongMenuInput(loginname, id, checkrole);
				break;
			}
		}

	}

	public static void banUser(String loginname, int id, String checkrole) {

		System.out.println("Type the username of the user you wish to ban:");
		Scanner scan = new Scanner(System.in);
		String banuser = scan.next();

		// check if user exists
		int exists = Methods.checkSend(banuser);

		if (exists == 1) {
			// check if you ban yourself
			if (!loginname.equals(banuser)) {

				Database.connect(url, "root", "12345");

				String query = "update project.user\r\n" + "set role = \"ban\"\r\n" + "where username = \"" + banuser
						+ "\";";
				PreparedStatement stm;

				try {
					stm = Database.connection.prepareStatement(query);
					stm.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						Database.connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("The user " + banuser + " is now banned.");
			} else {

				System.out.println("You can't ban yourself\nPress 1 to try again\nPress 2 to go back to the menu");
				String choose = scan.next();
				switch (choose) {
				case "1":
					banUser(loginname, id, checkrole);
					break;
				case "2":
					switch (checkrole) {
					case "admin":
						AdminMenu.adminMenu(loginname, id, checkrole);
						break;
					case "mod":
						ModMenu.modMenu(loginname, id, checkrole);
						break;
					default:
						break;
					}
				default:
					wrongMenuInput(loginname, id, checkrole);
					break;
				}
			}
		} else {
			System.out.println(
					"The user you are trying to ban does not exist\nPress 1 to try again\nPress 2 to go back to the menu");
			String choose = scan.next();
			switch (choose) {
			case "1":
				banUser(loginname, id, checkrole);
				break;
			case "2":
				switch (checkrole) {
				case "admin":
					AdminMenu.adminMenu(loginname, id, checkrole);
					break;
				case "mod":
					ModMenu.modMenu(loginname, id, checkrole);
					break;
				default:
					break;
				}
			default:
				wrongMenuInput(loginname, id, checkrole);
				break;
			}
		}

	}

	public static void changeRole(String loginname, int id, String checkrole) {

		Scanner scan = new Scanner(System.in);
		System.out.println("Type the username of the user you wish to change the role:");
		String username = scan.next();

		// check if user exists
		int exists = Methods.checkSend(username);

		if (exists == 1) {

			if (!username.equals(loginname)) {

				Database.connect(url, "root", "12345");

				String[] roles = { "admin", "mod", "user", "ban" };

				System.out.println(
						"Choose the role:\nPress 1 for 'ADMIN'\nPress 2 for 'MOD'\nPress 3 for 'USER'\nPress 4 for 'BAN'");
				String usernewrole = scan.next();

				int role = 0;
				switch (usernewrole) {
				case "1":
					role = 1;
					break;
				case "2":
					role = 2;
					break;
				case "3":
					role = 3;
					break;
				case "4":
					role = 4;
					break;
				default:
					break;
				}
				String newrole = roles[role - 1];

				String query = "update project.user\r\n" + "set role = \"" + newrole + "\"\r\n" + "where username = \""
						+ username + "\";";
				PreparedStatement stm;

				try {
					stm = Database.connection.prepareStatement(query);
					stm.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						Database.connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("The role of the user " + username + " has been successfully changed to " + newrole);
			} else {

				System.out.println(
						"You can't change the role of your admin account\nPress 1 to try again\nPress 2 to go back to the menu");
				String choose = scan.next();
				switch (choose) {
				case "1":
					banUser(loginname, id, checkrole);
					break;
				case "2":
					switch (checkrole) {
					case "admin":
						AdminMenu.adminMenu(loginname, id, checkrole);
						break;
					case "mod":
						ModMenu.modMenu(loginname, id, checkrole);
						break;
					default:
						break;
					}
				default:
					wrongMenuInput(loginname, id, checkrole);
					break;
				}

			}
		} else {
			System.out.println(
					"The user you are trying to edit does not exist\nPress 1 to try again\nPress 2 to go back to the menu");
			String choose = scan.next();
			switch (choose) {
			case "1":
				banUser(loginname, id, checkrole);
				break;
			case "2":
				switch (checkrole) {
				case "admin":
					AdminMenu.adminMenu(loginname, id, checkrole);
					break;
				case "mod":
					ModMenu.modMenu(loginname, id, checkrole);
					break;
				default:
					break;
				}
			default:
				wrongMenuInput(loginname, id, checkrole);
				break;
			}
		}

	}

	public static void changePassword(String username, String checkrole) {

		Scanner scan = new Scanner(System.in);
		System.out.println("Please type you old password:");
		String oldpassword = scan.next();

		if ((checkPassword(oldpassword, username)) == 1) {

			System.out.println("Please type the new password:");
			String newpassword = scan.next();

			Database.connect(url, "root", "12345");

			String query = "update project.user\r\n" + "set password = aes_encrypt(\"" + newpassword
					+ "\", \"password\")\r\n" + "where username = \"" + username + "\" and password = aes_encrypt(\""
					+ oldpassword + "\", \"password\");";
			PreparedStatement stm;

			try {
				stm = Database.connection.prepareStatement(query);
				stm.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					Database.connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			changePasswordWrong(username, checkrole);
		}
	}

	public static void changePasswordWrong(String username, String checkrole) {

		System.out.println(
				"The password you entered is not correct\nPress 1 to try again\nPress 2 to go back to the menu");
		Scanner scan = new Scanner(System.in);
		String input = scan.next();
		switch (input) {
		case "1":
			changePassword(username, checkrole);
			break;
		case "2":
			int userID = checkUserID(username);
			switch (checkrole) {
			case "admin":
				AdminMenu.adminMenu(username, userID, checkrole);
				break;
			case "mod":
				ModMenu.modMenu(username, userID, checkrole);
				break;
			case "user":
				UserMenu.userMenu(username, userID, checkrole);
				break;
			case "ban":
				BanMenu.banUser(username, userID, checkrole);
			default:
				break;
			}
		default:
			break;
		}

	}

	public static void viewUsers() {

		Database.connect(url, "root", "12345");

		String query = "select username, role\r\n" + "from project.user";
		PreparedStatement stm;
		String user = "";
		String role = "";

		try {
			stm = Database.connection.prepareStatement(query);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				user = rs.getString("username");
				role = rs.getString("role");
				System.out.printf("username: %s, role: %s", user, role);
				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void readInbox(int id) {

		Database.connect(url, "root", "12345");

		String query1 = "select count(*) as count\r\n" + "from project.messages\r\n" + "where receiver_id = " + id
				+ ";";
		PreparedStatement stm1;
		int inbox = 0;

		try {
			stm1 = Database.connection.prepareStatement(query1);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				inbox = rs.getShort("count");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (inbox == 0) {
			System.out.println("Sorry, you have no inbox messages");
		} else {

			Database.connect(url, "root", "12345");

			String query2 = "select username, msg, date\r\n" + "from project.user, project.messages\r\n"
					+ "where user.user_id=messages.sender_id and receiver_id = " + id;
			PreparedStatement stm2;
			String sender = "";
			String msg = "";
			Timestamp date;

			try {
				stm2 = Database.connection.prepareStatement(query2);
				ResultSet rs = stm2.executeQuery();
				while (rs.next()) {
					sender = rs.getString("username");
					msg = rs.getString("msg");
					date = rs.getTimestamp("date");
					System.out.printf("(%s) %s: %s", date, sender, msg);
					System.out.println();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					Database.connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void readOutbox(int id) {

		Database.connect(url, "root", "12345");

		String query1 = "select count(*) as count\r\n" + "from project.messages\r\n" + "where sender_id = " + id + ";";
		PreparedStatement stm1;
		int inbox = 0;

		try {
			stm1 = Database.connection.prepareStatement(query1);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				inbox = rs.getShort("count");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (inbox == 0) {
			System.out.println("Sorry, you have no outbox messages");
		} else {

			Database.connect(url, "root", "12345");

			String query2 = "select username, msg, date\r\n" + "from project.user, project.messages\r\n"
					+ "where user.user_id=messages.receiver_id and sender_id = " + id;
			PreparedStatement stm2;
			String sender = "";
			String msg = "";
			Timestamp date;

			try {
				stm2 = Database.connection.prepareStatement(query2);
				ResultSet rs = stm2.executeQuery();
				while (rs.next()) {
					sender = rs.getString("username");
					msg = rs.getString("msg");
					date = rs.getTimestamp("date");
					System.out.printf("(%s) %s: %s", date, sender, msg);
					System.out.println();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					Database.connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void send(String loginname, int id, String checkrole) {

		System.out.println("Type the name of the user you wish to send a message to:");
		Scanner scan = new Scanner(System.in);
		String receivername = scan.nextLine();

		int checkbeforesend = checkSend(receivername);

		if (checkbeforesend == 1) {

			Database.connect(url, "root", "12345");

			// get user_id for receivername
			String query1 = "select user_id\r\n" + "from project.user\r\n" + "where username = \"" + receivername
					+ "\"";
			PreparedStatement stm1;

			// type the message you wish to send
			System.out.println("Type the message you want to send:");
			String message = scan.nextLine();

			if (message.length() < 250) {

				String query2 = "insert into project.messages values (?, ?, ?, ?, ?)";
				PreparedStatement stm2;

				String timestamp = new SimpleDateFormat("yyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());

				int user_id = 0;
				try {
					stm1 = Database.connection.prepareStatement(query1);
					ResultSet rs = stm1.executeQuery();
					while (rs.next()) {
						user_id = rs.getInt("user_id");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					stm2 = Database.connection.prepareStatement(query2);
					stm2.setString(1, null);
					stm2.setInt(2, id);
					stm2.setInt(3, user_id);
					stm2.setString(4, message);
					stm2.setString(5, timestamp);
					stm2.executeUpdate();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						Database.connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Message sent successfully!");
			} else {
				System.out.println("Message too long\nPress 1 to try again" + "\nPress 2 to go back to the menu");
				String choose = scan.next();
				switch (choose) {
				case "1":
					send(loginname, id, checkrole);
					break;
				case "2":
					switch (checkrole) {
					case "admin":
						AdminMenu.adminMenu(loginname, id, checkrole);
						break;
					case "mod":
						ModMenu.modMenu(loginname, id, checkrole);
						break;
					case "user":
						UserMenu.userMenu(loginname, id, checkrole);
						break;
					default:
						break;
					}
				default:
					wrongMenuInput(loginname, id, checkrole);
					break;
				}
			}
		} else {
			System.out.println("The username you entered does not exist\nPress 1 to try again"
					+ "\nPress 2 to go back to the menu");
			String choose = scan.next();
			switch (choose) {
			case "1":
				send(loginname, id, checkrole);
				break;
			case "2":
				switch (checkrole) {
				case "admin":
					AdminMenu.adminMenu(loginname, id, checkrole);
					break;
				case "mod":
					ModMenu.modMenu(loginname, id, checkrole);
					break;
				case "user":
					UserMenu.userMenu(loginname, id, checkrole);
					break;
				default:
					break;
				}
			default:
				wrongMenuInput(loginname, id, checkrole);
				break;
			}
		}

	}

	public static int checkSend(String receivername) {

		Database.connect(url, "root", "12345");

		String query = "select count(*) as count\r\n" + "from project.user\r\n" + "where user.username = \""
				+ receivername + "\";";
		PreparedStatement stm;
		int checkusername = 0;

		try {
			stm = Database.connection.prepareStatement(query);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				checkusername = rs.getInt("count");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return checkusername;

	}

	public static void print(String loginname, int id) {

		// INBOX
		PrintWriter writer1 = null;
		try {
			writer1 = new PrintWriter(loginname + "_chatlog.txt");
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Database.connect(url, "root", "12345");

		String query1 = "select count(*) as count\r\n" + "from project.messages\r\n" + "where receiver_id = " + id
				+ ";";
		PreparedStatement stm1;
		int inbox = 0;

		try {
			stm1 = Database.connection.prepareStatement(query1);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				inbox = rs.getShort("count");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (inbox == 0) {
			writer1.println("Sorry, you have no inbox messages");
		} else {

			Database.connect(url, "root", "12345");

			String query2 = "select username, msg, date\r\n" + "from project.user, project.messages\r\n"
					+ "where user.user_id=messages.sender_id and receiver_id = " + id;
			PreparedStatement stm2;
			String sender = "";
			String msg = "";
			Timestamp date;

			writer1.print("****************" + "INBOX MESSAGES" + "****************");
			writer1.println();
			try {
				stm2 = Database.connection.prepareStatement(query2);
				ResultSet rs = stm2.executeQuery();
				while (rs.next()) {
					sender = rs.getString("username");
					msg = rs.getString("msg");
					date = rs.getTimestamp("date");
					writer1.printf("(%s) %s: %s", date, sender, msg);
					writer1.println();
				}
				writer1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					Database.connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("The chatlog file was created successfully!");

		}

		// OUTBOX
		PrintWriter writer2 = null;
		try {
			writer2 = new PrintWriter(new FileOutputStream(new File(loginname + "_chatlog.txt"), true));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Database.connect(url, "root", "12345");

		String query3 = "select count(*) as count\r\n" + "from project.messages\r\n" + "where sender_id = " + id + ";";
		PreparedStatement stm3;
		int inbox2 = 0;

		try {
			stm3 = Database.connection.prepareStatement(query3);
			ResultSet rs = stm3.executeQuery();
			while (rs.next()) {
				inbox2 = rs.getShort("count");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (inbox2 == 0) {
			System.out.println("Sorry, you have no outbox messages");
		} else {

			Database.connect(url, "root", "12345");

			String query4 = "select username, msg, date\r\n" + "from project.user, project.messages\r\n"
					+ "where user.user_id=messages.receiver_id and sender_id = " + id;
			PreparedStatement stm4;
			String sender = "";
			String msg = "";
			Timestamp date;

			writer2.print("****************" + "OUTBOX MESSAGES" + "****************");
			writer2.println();
			try {
				stm4 = Database.connection.prepareStatement(query4);
				ResultSet rs = stm4.executeQuery();
				while (rs.next()) {
					sender = rs.getString("username");
					msg = rs.getString("msg");
					date = rs.getTimestamp("date");
					writer2.printf("(%s) %s: %s", date, sender, msg);
					writer2.println();
				}
				writer2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					Database.connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void contactAdmin(int id) {

		// type the message you wish to send
		System.out.println("Type the message you want to send:");
		Scanner scan = new Scanner(System.in);
		String message = scan.nextLine();

		Database.connect(url, "root", "12345");

		String query2 = "insert into project.messages values (?, ?, ?, ?, ?)";
		PreparedStatement stm2;

		String timestamp = new SimpleDateFormat("yyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());

		try {
			stm2 = Database.connection.prepareStatement(query2);
			stm2.setString(1, null);
			stm2.setInt(2, id);
			stm2.setInt(3, 1);
			stm2.setString(4, message);
			stm2.setString(5, timestamp);
			stm2.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Database.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Message sent successfully!");

	}

	public static void wrongPass() {

		System.out.println("Looks like either the username or password is not correct.\n" + "Press 1 to try again.\n"
				+ "Press 2 to exit.");
		Scanner scan = new Scanner(System.in);
		String wrongpass = scan.next();

		if (wrongpass.equals("1")) {
			Menu.menu();
		} else if (wrongpass.equals("2")) {

		} else if (!wrongpass.equalsIgnoreCase("1") && !wrongpass.equalsIgnoreCase("2")) {
			wrongPass();
		}
		scan.close();
	}

	public static void returnMenu(String loginname, int id, String checkrole) {

		Scanner scan = new Scanner(System.in);
		System.out.println("Press 1 to return to the menu");
		String goBack = scan.next();
		if (goBack.equals("1")) {
			switch (checkrole) {
			case "admin":
				AdminMenu.adminMenu(loginname, id, checkrole);
				break;
			case "mod":
				ModMenu.modMenu(loginname, id, checkrole);
				break;
			case "user":
				UserMenu.userMenu(loginname, id, checkrole);
				break;
			case "ban":
				BanMenu.banUser(loginname, id, checkrole);
			default:
				break;
			}

		} else {
			System.out.println("Wrong command. Please press 1 to return to the main menu.");
			returnMenu(loginname, id, checkrole);
		}
	}

	public static void wrongMenuInput(String username, int userID, String checkrole) {

		System.out.println("Wrong input");

		switch (checkrole) {
		case "admin":
			AdminMenu.adminMenu(username, userID, checkrole);
			break;
		case "mod":
			ModMenu.modMenu(username, userID, checkrole);
			break;
		case "user":
			UserMenu.userMenu(username, userID, checkrole);
			break;
		case "ban":
			BanMenu.banUser(username, userID, checkrole);
		default:
			break;
		}

	}

	public static void cls() {

		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
