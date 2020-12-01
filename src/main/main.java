package main;

import java.sql.*;
import java.util.Scanner;

public class main {
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/DataBaseClass";
	static final String USER = "dbclass";
	static final String PASS = "123456";
	private static int session = 0;
	
	@SuppressWarnings("resource")
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("(1) Login\n(2) Sign Up\n(3) Login as Administrator (Manager of Auction System)\n(4) Quit\n");
			System.out.println("Your choice: ");
			String command = scanner.nextLine();
			if(command.equals("1")) {
				login();
			}else if(command.equals("2")) {
				signup();
			}else if(command.equals("3")) {
				System.out.print("3");
			}
		}
	}
	
	@SuppressWarnings("resource")
	private static void login(){
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("enter your username: ");
		String username = scanner.nextLine();
		System.out.print("enter your password: ");
		String password = scanner.nextLine();
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "select id,password from user where username = "+username;
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				if(rs.getString("password")==password) {
					session = rs.getInt("id");
				}
			}
			
			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException se) {
			se.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
		}
		
		if(session != 1) {
			logined();
		}else {
			System.out.print("Invalid ID and/or password.");
		}
		
	}
	
	@SuppressWarnings("resource")
	private static void logined(){
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("(1) Sell Item\n(2) Check Stats of Your Item Listed on Auction\n(3) Buy Item\n(4) Check Status of Your Bid\n(5) Check Your Account\n(6) Quit");
			String command = scanner.nextLine();
		}
	}
	
	@SuppressWarnings("resource")
	private static void signup(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("enter your username: ");
		String username = scanner.nextLine();
		System.out.println("enter your password: ");
		String password = scanner.nextLine();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "insert into user(username,password) values ('" + username + "','" + password + "')";
			stmt.execute(sql);
			
			stmt.close();
			conn.close();
			
			System.out.println("Welcome "+username);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
		}
		
	}
}
