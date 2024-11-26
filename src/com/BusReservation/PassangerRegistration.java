package com.BusReservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class PassangerRegistration {
	static HashMap<String, String> userDetailsDb = new HashMap<>();
	static Scanner scanner = new Scanner(System.in);
	static BusBooking busdetail = new BusBooking();
	static DbConnection dbConnection = new DbConnection();

	private static void userRegistartions() {
		String userName;
		String userPassword;
		String userRePassword;
		try {
			do {
				System.out.println("Please enter your username:");
				userName = scanner.nextLine();
				if (!userName.matches("[a-zA-Z]+")) {
					System.err.println("Username must contain only alphabets. Please try again.");
				}
			} while (!userName.matches("[a-zA-Z]+"));

			do {
				System.out.println("Please enter your password:");
				userPassword = scanner.nextLine();
				if (!userPassword.matches("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8}$")) {
					System.err.println(
							"Password must be exactly 8 characters long,\ninclude at least one number, and\none special character. Please try again.");
				}
			} while (!userPassword.matches("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8}$"));

			do {
				System.out.println("Please confirm your password:");
				userRePassword = scanner.nextLine();
			} while (!userRePassword.equals(userPassword));

			storedDetails(userName, userPassword);
			System.out.println("Registration completed successfully! Please log in.");
		} catch (Exception e) {
			System.out.println("An error occurred while registration: " + e.getMessage());
		}

	}

	private static String execute(String username, String password) {
		Connection conn = dbConnection.creatConnection();
		String jdbc_template = "INSERT INTO users (user_name, password) VALUES (?, ?)";

		try {
			PreparedStatement pst = conn.prepareStatement(jdbc_template);
			pst.setString(1, username);
			pst.setString(2, password);
			int rowsAffected = pst.executeUpdate();
			Boolean finals = rowsAffected > 0;
			return finals.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return "false";
		} finally {
			dbConnection.closeConnection(conn);
		}

	}

	private static void usersList() {
		Connection conn = dbConnection.creatConnection();
		try {
			String final_response = executeView(conn);
			System.out.println("\nRegistered Users:");
			System.out.println(final_response);

			System.out.println();
		} catch (Exception e) {
			System.out.println("An error occurred while viewing user List: " + e.getMessage());
		} finally {
			dbConnection.closeConnection(conn);
		}

	}

	private static String executeView(Connection conn) {
		String jdbc_template = "SELECT * FROM users";

		try {
			PreparedStatement pst = conn.prepareStatement(jdbc_template);
			ResultSet rs = pst.executeQuery();

			StringBuilder result = new StringBuilder();
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("user_name");
				String password = rs.getString("password");

				result.append("Id: ").append(id).append(", Username: ").append(username).append(", Password: ")
						.append(password).append("\n");
			}

			return result.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return "false";
		}

	}

	private static void deleteUser() {

		Connection conn = dbConnection.creatConnection();
		String jdbc_template = "DELETE  FROM users WHERE user_name = ?";

		try {
			PreparedStatement pst = conn.prepareStatement(jdbc_template);

			System.out.println("Please enter the username to delete:");
			String userName = scanner.nextLine();

			pst.setString(1, userName);

			int rowsAffected = pst.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("User '" + userName + "' deleted successfully.");
			} else {
				System.out.println("User '" + userName + "' not found.");
			}
		} catch (Exception e) {
			System.out.println("An error occurred while Delete user details: " + e.getMessage());
		} finally {
			dbConnection.closeConnection(conn);
		}


	}

	private static void editUser() {
	    try {
	        String userNewPassword;
	        System.out.println("Please enter your username:");
	        String userName = scanner.nextLine();

	        if (userExistsInDb(userName)) {
	            do {
	                System.out.println("Please enter your new password:");
	                userNewPassword = scanner.nextLine();

	                if (!userNewPassword.matches("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")) {
	                    System.err.println(
	                            "Password must be at least 8 characters long, include at least one number, and one special character. Please try again.");
	                }
	            } while (!userNewPassword.matches("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$"));

	            if (updateUserPassword(userName, userNewPassword)) {
	                System.out.println("Your registration has been updated successfully.");
	            } else {
	                System.out.println("An error occurred while updating the password in the database.");
	            }
	        } else {
	            System.out.println("User not found.");
	        }
	    } catch (Exception e) {
	        System.out.println("An error occurred while updating user details: " + e.getMessage());
	    }
	}

	private static boolean userExistsInDb(String userName) {
	    String jdbc_template = "SELECT COUNT(*) FROM users WHERE user_name = ?";
	    try  {
	    	Connection conn = dbConnection.creatConnection();
	        PreparedStatement pst = conn.prepareStatement(jdbc_template);
	        pst.setString(1, userName);
	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0; // If user count > 0, user exists
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error checking user existence: " + e.getMessage());
	    }
	    return false;
	}

	private static boolean updateUserPassword(String userName, String newPassword) {
	    String jdbc_template = "UPDATE users SET password = ? WHERE user_name = ?";
	    try (Connection conn = dbConnection.creatConnection();
	         PreparedStatement pst = conn.prepareStatement(jdbc_template)) {

	        pst.setString(1, newPassword); 
	        pst.setString(2, userName);     
	        int rowsAffected = pst.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        System.out.println("Error updating password: " + e.getMessage());
	    }
	    return false;
	}


	private static void storedDetails(String userName, String userPassword) {
		try {
          execute(userName, userPassword);
		} catch (Exception e) {
			System.out.println("An error occurred while storing user details: " + e.getMessage());
		}

	}

	private static void userLogin() {
		try {
			System.out.println("Please enter your username:");
			String userName = scanner.nextLine();
			System.out.println("Please enter your password:");
			String userPassword = scanner.nextLine();
			HashMap<String, String> userDetails = checkUsers(userName);

			if (userDetails.containsKey(userName) && userDetails.get(userName).equals(userPassword)) {
				System.out.println("Welcome, " + userName + "!");
				System.out.println();
				busdetail.busBook();
			} else {
				System.out.println("Invalid username or password. Please try again.");
			}
		} catch (Exception e) {
			System.out.println("An error occurred during login: " + e.getMessage());
		}

	}

	private static HashMap<String, String> checkUsers(String userName) {
		String jdbc_template = "SELECT user_name, password FROM users WHERE user_name = ?";
		HashMap<String, String> userDetailsDb = new HashMap<>();
		Connection conn = dbConnection.creatConnection();
		try (PreparedStatement pst = conn.prepareStatement(jdbc_template)) {

			pst.setString(1, userName);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					String username = rs.getString("user_name");
					String password = rs.getString("password");
					userDetailsDb.put(username, password);
				}
			}
		} catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
		} finally {
			dbConnection.closeConnection(conn);
		}

		return userDetailsDb;
	}

	public static void main(String[] args) {
		boolean flag = true;
		try {
			do {
				System.err.println("\n--- Bus Reservation System ---");
				System.err.println("1. Login");
				System.err.println("2. Registration");
				System.err.println("3. Users List");
				System.err.println("4. Edit User");
				System.err.println("5. Delete User");
				System.err.println("6. Exit");
				System.err.print("Enter your input: ");

				String choice = scanner.nextLine();
				switch (choice) {
				case "1":
					userLogin();
					break;
				case "2":
					userRegistartions();
					break;
				case "3":
					usersList();
					break;
				case "4":
					editUser();
					break;
				case "5":
					deleteUser();
					break;
				case "6":
					System.out.println("Goodbye!");
					flag = false;
					break;
				default:
					System.out.println("Enter the valid input");
				}
			} while (flag);

		} catch (Exception e) {
			System.out.println("An unexpected error occurred: " + e.getMessage());
		}

	}

}
