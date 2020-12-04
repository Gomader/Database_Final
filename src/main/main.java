package main;

import java.sql.*;
import java.util.Scanner;

public class main {
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/DataBaseClass";//Please change here
	static final String USER = "dbclass";//Please change here
	static final String PASS = "123456";//Please change here
	private static int session = -1;
	private static String session_name;
	
	@SuppressWarnings("resource")
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("(1) Login\n(2) Sign Up\n(3) Login as Administrator (Manager of Auction System)\n(4) Quit\n\n");
			System.out.println("Your choice: ");
			String command = scanner.nextLine();
			if(command.equals("1")) {
				login();
			}else if(command.equals("2")) {
				signup();
			}else if(command.equals("3")) {
				adminlogin();
			}else if(command.equals("4")) {
				break;
			}
		}
	}
	
	@SuppressWarnings("resource")
	private static void login(){
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter your username: ");
		String username = scanner.nextLine();
		System.out.print("Enter your password: ");
		String password = scanner.nextLine();
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "select id,password from user where username = '"+username+"';";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				if(rs.getString("password").equals(password)) {
					session = rs.getInt("id");
					session_name = username;
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
		
		if(session != -1) {
			System.out.println("\nWelcome back "+session_name+"!\n");
			logined();
		}else {
			System.out.println("\nInvalid ID and/or password.\n");
		}
		
	}
	
	@SuppressWarnings("resource")
	private static void logined(){
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("(1) Sell Item\n(2) Check Stats of Your Item Listed on Auction\n(3) Buy Item\n(4) Check Status of Your Bid\n(5) Check Your Account\n(6) Quit\n\n");
			System.out.println("Your choice: ");
			String command = scanner.nextLine();
			if(command.equals("1")) {
				sell();
			}else if(command.equals("2")) {
				showmyitems();
			}else if(command.equals("3")) {
				buyitem();
			}else if(command.equals("4")) {
				showmybid();
			}else if(command.equals("5")) {
				showmypage();
			}else{
				session = -1;
				session_name = null;
				break;
			}
		}
	}
	
	@SuppressWarnings("resource")
	private static void signup(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("enter your username: ");
		String username = scanner.nextLine();
		System.out.println("enter your password: ");
		String password = scanner.nextLine();
		System.out.println("enter your email: ");
		String email = scanner.nextLine();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "insert into user(username,password,email) values ('" + username + "','" + password + "','"+email+"')";
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
	
	@SuppressWarnings("resource")
	private static void sell() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your good's name: ");
		String name = scanner.nextLine();
		System.out.println("Enter your good's category: ");
		String category = scanner.nextLine();
		System.out.println("Enter your good's description: ");
		String description = scanner.nextLine();
		System.out.println("Enter your starting price: ");
		String bidprice = scanner.nextLine();
		System.out.println("How long do you want to sale (num month/day/hour/second): ");
		String endtime = scanner.nextLine();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "insert into items(name,sellerid,category,description,bidprice,enddate) values ('" + name + "'," + session + ",'"+ category +"','"+ description +"',"+ bidprice +",date_add(now(),interval "+ endtime +"))";
			stmt.execute(sql);
			
			stmt.close();
			conn.close();
			
			System.out.println("Succeed! Your "+name + " is for sale!");
			
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
	
	private static void showmyitems() {
		System.out.println("\n");
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "select * from items where sellerid = " + session;
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String name = rs.getString("name");
				String category = rs.getString("category");
				String description = rs.getString("description");
				int bidprice = rs.getInt("bidprice");
				int buyprice = rs.getInt("buyprice");
				String state;
				int st = rs.getInt("state");
				if(st == 0) {
					state = "On Sale";
				}else if(st == 1 || st == 2) {
					state = "Sold";
				}else {
					state = "Disabled";
				}
				String postdate = rs.getString("postdate");
				String enddate = rs.getString("enddate");
				System.out.println(name +"\t"+ category +"\t" + description +"\t" + bidprice +"\t" + buyprice +"\t" + state +"\t" + postdate +"\t" + enddate);
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
	}
	
	@SuppressWarnings("resource")
	private static void buyitem() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("(1) Search by name\n(2) Search by category\n(3) Search by keyword\n(4) Search by seller\n(5) Search by postdate\n(6) Quit\n\n");
		System.out.println("Your choice: ");
		String command = scanner.nextLine();
		if(command.equals("6")) {
			return;
		}
		String sql = null;
		System.out.println("Input: ");
		String word = scanner.nextLine();
		if(command.equals("1")) {
			sql = "select * from items where name like '%" + word + "%'";
		}else if(command.equals("2")) {
			sql = "select * from items where category like '%" + word + "%'";
		}else if(command.equals("3")) {
			sql = "select * from items where description like '%" + word + "%'";
		}else if(command.equals("4")) {
			sql = "select user.username,items.* from items join user on items.sellerid = user.id where user.username like '%" + word + "%'";
		}else if(command.equals("5")) {
			sql = "select * from items where postdate like '%" + word + "%'";
		}
		
		Connection conn = null;
		Statement stmt = null;
		System.out.println();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String description = rs.getString("description");
				int bidprice = rs.getInt("bidprice");
				int buyprice = rs.getInt("buyprice");
				String state;
				int st = rs.getInt("state");
				if(st == 0) {
					state = "On Sale";
				}else if(st == 1 || st == 2) {
					state = "Sold";
				}else {
					state = "Disabled";
				}
				String postdate = rs.getString("postdate");
				String enddate = rs.getString("enddate");
				System.out.println(id + "\t"+name +"\t"+ category +"\t" + description + "\t" + bidprice + "\t" + buyprice + "\t" + state +"\t" + postdate +"\t" + enddate);
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
		
		System.out.println("Which one do you want to buy?('q' to quit)Please enter the id of your main good.");
		String choice = scanner.nextLine();
		
		if(choice.equals("q")) {
			return;
		}
		
		conn = null;
		stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "select bidprice from items where id = "+choice+";";
			stmt.execute(sql);
			ResultSet rs = stmt.executeQuery(sql);
			int bidprice = 0;
			while(rs.next()) {
				bidprice = rs.getInt("bidprice");
			}
			stmt.close();
			conn.close();
			
			int bid;
			while(true) {
				System.out.println("Enter your price: ");
				bid = scanner.nextInt();
				if(bid > bidprice) {
					break;
				}
				System.out.println("Please let your price higher than bid price now!");
			}
			
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "insert into bidinformation(itemid,bider,price) values("+choice+","+session+","+bid+");";
			stmt.execute(sql);
			
			System.out.println("\nSuccessful bidding!\n");
			
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
	}
	
	private static void showmybid() {
		System.out.println("\n");
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "select * from bidinformation join items on bidinformation.itemid = items.id where bider = " + session;
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String name = rs.getString("name");
				String category = rs.getString("category");
				String description = rs.getString("description");
				int bidprice = rs.getInt("bidprice");
				int buyprice = rs.getInt("buyprice");
				int myprice = rs.getInt("price");
				String state;
				if(buyprice == 0) {
					if(myprice < bidprice) {
						state = "Biding but someone higher than you!";
					}else {
						state = "Biding and you're the highest!";
					}
				}else if(myprice < buyprice) {
					state = "Bidding Failed!";
				}else {
					state = "Successful biding!";
				}
				String postdate = rs.getString("postdate");
				String enddate = rs.getString("enddate");
				System.out.println(name +"\t"+ category +"\t" + description +"\t" + bidprice +"\t" + buyprice +"\t" + state +"\t" + postdate +"\t" + enddate);
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
	}
	
	private static void showmypage() {
		System.out.println("\n");
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "select * from user where id = " + session;
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String email = rs.getString("email");
				int numberOfItemsSold = rs.getInt("numberOfItemsSold");
				int numberOfItemsPurchased = rs.getInt("numberOfItemsPurchased");
				int balance = rs.getInt("balance");

				System.out.println("id: "+id+"\tusername: "+ username +"\t" + email +"\tNumber Of Items Sold: "+numberOfItemsSold+"\tNumber Of Items Bought: "+numberOfItemsPurchased+"\tbalance: " +balance+"\n");
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
	}
	
	@SuppressWarnings("resource")
	private static void adminlogin() {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter admin username: ");
		String username = scanner.nextLine();
		System.out.print("Enter admin password: ");
		String password = scanner.nextLine();
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "select password from admin where username = '"+username+"';";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				if(rs.getString("password").equals(password)) {
					session = -2;
					session_name = username;
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
		
		if(session != -1) {
			System.out.println("\nWelcome "+session_name+"!\n");
			adminlogined();
		}else {
			System.out.println("\nInvalid ID and/or password.\n");
		}
	}
	
	private static void adminlogined() {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql = "select balance from admin where username = '"+session_name+"';";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int balance = rs.getInt("balance");
				System.out.println("Company Balance: "+balance);
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
			conn = null;
			stmt = null;
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "select user.username,items.* from items join user on user.id = items.sellerid;";
			rs = stmt.executeQuery(sql);
			
			System.out.println("\nAll Items' Information:\n");
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String username = rs.getString("username");
				String category = rs.getString("category");
				String description = rs.getString("description");
				int bidprice = rs.getInt("bidprice");
				int buyprice = rs.getInt("buyprice");
				int state = rs.getInt("state");
				int bids = rs.getInt("bids");
				String postdate = rs.getString("postdate");
				String enddate = rs.getString("enddate");
				
				System.out.println(id+"\t"+name+"\t"+username+"\t"+category+"\t"+description+"\t"+bidprice+"\t"+buyprice+"\t"+state+"\t"+bids+"\t"+postdate+"\t"+enddate+"\t");
			}
			
			conn = null;
			stmt = null;
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "select user.username,bidinformation.* from bidinformation join user on user.id = bidinformation.bider order by bidinformation.itemid;";
			rs = stmt.executeQuery(sql);
			
			System.out.println("\nAll Bids' Information:\n");
			
			while(rs.next()) {
				int id = rs.getInt("id");
				int itemid = rs.getInt("itemid");
				String bider = rs.getString("username");
				int price = rs.getInt("price");
				System.out.println(id+"\t"+itemid+"\t"+bider+"\t"+price);
			}
			
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
