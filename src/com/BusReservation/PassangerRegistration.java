package com.BusReservation;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class PassangerRegistration {
	static HashMap<String, String> userDetails = new HashMap<>();
	static Scanner scanner = new Scanner(System.in);
	static BusBooking busdetail = new BusBooking();
	
	public static void userRegistartions() {
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
		                System.err.println("Password must be exactly 8 characters long,\ninclude at least one number, and\none special character. Please try again.");
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

	public static void usersList() {
		 try {
			 System.out.println("\nRegistered Users:");
			 userDetails.forEach((username, password) -> System.out.println(" - " + username));
			 System.out.println();
	        } catch (Exception e) {
	            System.out.println("An error occurred while viewing user List: " + e.getMessage());
	        }
		
	}

	public static void deleteUser() {
		try {
			System.out.println("Please enter the username to delete:");
			String userName = scanner.nextLine();
			if (userDetails.containsKey(userName)) {
				userDetails.remove(userName);
				System.out.println("User deleted successfully.");
			} else {
				System.out.println("User not found.");
			}
	        } catch (Exception e) {
	            System.out.println("An error occurred while Delete user details: " + e.getMessage());
	        }
		
	}

	public static void editUser() {
		 try {
			 String userNewPassword;
			 System.out.println("Please enter your username:");
				String userName = scanner.nextLine();
				if (userDetails.containsKey(userName)) {
					   do {
				            System.out.println("Please enter your password:");
				            userNewPassword = scanner.nextLine();
				            if (!userNewPassword.matches("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8}$")) {
				                System.err.println("Password must be exactly 8 characters long,\ninclude at least one number, and\none special character. Please try again.");
				            }
				        } while (!userNewPassword.matches("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8}$"));
					storedDetails(userName, userNewPassword);
					System.out.println("Your registration has been updated successfully.");
				} else {
					System.out.println("User not found.");
				}
	        } catch (Exception e) {
	            System.out.println("An error occurred while update user details: " + e.getMessage());
	        }
		
	}

	public static void storedDetails(String userName, String userPassword) {
		 try {
				DbConnection dbConnection=new DbConnection();
				String final_response=	dbConnection.execute(userName, userPassword);
				
				
				System.out.println("final_response   :"+final_response);

			  userDetails.put(userName, userPassword);
	        } catch (Exception e) {
	            System.out.println("An error occurred while storing user details: " + e.getMessage());
	        }
		
	}

	public static void userLogin() {
		 try {
			 System.out.println("Please enter your username:");
				String userName = scanner.nextLine();
				System.out.println("Please enter your password:");
				String userPassword = scanner.nextLine();

				if (userDetails.containsKey(userName) && userDetails.get(userName).equals(userPassword)) {
					System.out.println("Welcome, " + userName + "!");
					System.out.println();
					busdetail.busBook();
				} else {
					System.out.println("Invalid username or password. Please try again.");
				}
	        } catch (Exception e) {
	            System.out.println("An error occurred while Login: " + e.getMessage());
	        }
		
	}

	public static void main(String[] args) {
		DbConnection db=  new DbConnection(); 
        
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
