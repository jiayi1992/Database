package TrainTickets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class traintickets {

	
	public static void main(String[] args) throws SQLException, ParseException {
    	String id;    
    	String password;
    	int state;
    	Connection conn = getConn();
    	System.out.println("Welcome to Train Ticket Management System"+"\n");
    	while(true){
	    	Scanner sc=new Scanner(System.in);
			System.out.println("Enter your id: ");
			id=sc.nextLine();
			System.out.println("Enter your password: ");
			password=sc.nextLine();
			state = signin(id,password,conn);
			switch (state){
				case 0:
					System.out.println("Your id or password is worry, try again"+"\n");
					break;
				case 1:
					while(true){
						int n = 0;
						menuCus ();
						sc=new Scanner(System.in);
						n = sc.nextInt();
						sc.nextLine();
						System.out.println("");
			    		switch (n){
			    			case 1:
			    				searchByTno(conn);
			    				break;
			    			case 2:
			    				searchByDate(conn);
			    				break;
			    			case 3:
			    				searchByPlace(conn);
			    				break;
			    			case 4:
			    				searchOrder(id,conn);
			    				break;
			    			case 5:
			    				buyTickets(id,conn);
			    				break;
			    			case 6:
			    				returnTickets(id,conn);
			    				break;
			    			case 7:			
			    				break;
			    			default:
			    				System.out.println("Wrong number, please enter the option number again"+"\n");
			    				break;
			    		}
			    		if (n==7) {
			    			System.out.println("Exit Successful");
			    			break;
			    		}
					}
					break;
				case 2:
					while(true){
						int n = 0;
						menuSel ();
						sc=new Scanner(System.in);
						n = sc.nextInt();
						sc.nextLine();
						System.out.println("");
			    		switch (n){
			    			case 1:
			    				searchTrainInformation(conn);
			    				break;
			    			case 2:
			    				insertTrainInformation(conn);
			    				break;
			    			case 3:
			    				alterTrainInformation(conn);
			    				break;
			    			case 4:
			    				deleteTrainInformation(conn);
			    				break;
			    			case 7:			
			    				break;
			    			default:
			    				System.out.println("Wrong number, please enter the option number again");
			    				break;
			    		}
			    		if (n==7) {
			    			System.out.println("Exit Successful");
			    			break;
			    		}
					}
					break;
			}
    	}
    }
	private static Connection getConn() {
	    String driver = "oracle.jdbc.driver.OracleDriver";
	    String url = "jdbc:oracle:thin:@dataserv.mscs.mu.edu:1521:orcl";
    	String username="Xin";    
    	String password="005961571";
	    Connection conn = null;
	    while(true){
		    try {
		        Class.forName(driver); 
		        conn = (Connection) DriverManager.getConnection(url, username, password);
		        break;
		    } catch (ClassNotFoundException e) {
		    	System.out.println("Your username or password is worry, try again");
		    } catch (SQLException e) {
		    	System.out.println("Your username or password is worry, try again");
		    }
	    }
	    return conn;
	}
	
	static void menuCus (){
		System.out.print("****************************************************"+"\n"
							+"(1) Search Train Information By Train Number"+"\n"
							+"(2) Search Train Information By Date"+"\n"
							+"(3) Search Train Information By Place"+"\n"
							+"(4) Search Order"+"\n"
							+"(5) Buy Tickets"+"\n"
							+"(6) return Tickets"+"\n"
							+"(7) Exit"+"\n"
							+"****************************************************"+"\n"
							+"\n"+"Please make your choice : ");
	}
	static void menuSel (){
		System.out.print("****************************************************"+"\n"
							+"(1) Search Train Information"+"\n"
							+"(2) Insert Train Information"+"\n"
							+"(3) Alter Train Information"+"\n"
							+"(4) Delete Train Information"+"\n"
							+"(7) Exit"+"\n"
							+"****************************************************"+"\n"
							+"\n"+"Please make your choice : ");
	}
	
	static int signin(String id, String password,Connection conn) throws SQLException {
		int state = 0;
		ResultSet rs = null;  
		String queryStr1 = "select password from person where id=?";
		String queryStr2 = "select id from manager where id=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
	        pstmt.setString(1, id);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			if(rs.getString("password").equals(password)){
				try{
					PreparedStatement pstmt = conn.prepareStatement(queryStr2);
			        pstmt.setString(1, id);
			        rs = pstmt.executeQuery();
		        }catch(SQLException e){
		        	e.printStackTrace();
		        }
				if(rs!=null&&rs.next()){ 
					state = 2;
				}else{
					state = 1;
				}
			}else{
				state = 0;
			}
        }else{ 
        	System.out.println("no result"+"\n"); 
        	state = 0;
        } 
		return state;
	}
	
	static void searchByTno(Connection conn) throws SQLException{
		String tno;
		ResultSet rs = null; 
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter Tno : ");
		tno = sc.nextLine();
		System.out.println(tno+"\n");
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace,r.re_number from Train_information t,remain_tickets r where t.tno=r.tno and t.tno=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
	        pstmt.setString(1, tno);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION  REMAIN TICKETS "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")
        						+"     "+rs.getInt("re_number")); 
        	while(rs.next()) {  
    			System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
				+"  "+ rs.getString("startplace")+"   "+rs.getString("destination")
				+"   "+rs.getInt("re_number"));  
            	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	} 
	}
	
	static void searchByDate(Connection conn) throws ParseException, SQLException{
		String date1;
		java.util.Date curDate;
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
		ResultSet rs = null; 
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter Date (YYYY-MM-DD): ");
		date1 = sc.nextLine();
		curDate = format1.parse(date1);
//		System.out.println(curDate);
		java.sql.Date date=new java.sql.Date(curDate.getTime());
//		System.out.println(date);
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace,r.re_number from Train_information t,remain_tickets r where t.tno=r.tno and t.tstart=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
	        pstmt.setDate(1, date);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION  REMAIN TICKETS "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")
        						+"     "+rs.getInt("re_number")); 
        	while(rs.next()) {  
    			System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
				+"  "+ rs.getString("startplace")+"   "+rs.getString("destination")
				+"   "+rs.getInt("re_number"));  
            	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	} 
	}
	static void searchByPlace(Connection conn) throws SQLException{
		String startplace;
		String destination;
		ResultSet rs = null; 
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter startplace : ");
		startplace = sc.nextLine();
		System.out.print("Please enter destination : ");
		destination = sc.nextLine();
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace,r.re_number from Train_information t,remain_tickets r where t.tno=r.tno and t.startplace=? and t.destination=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
			pstmt.setString(1, startplace);
	        pstmt.setString(2, destination);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION  REMAIN TICKETS "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")
        						+"     "+rs.getInt("re_number")); 
        	while(rs.next()) {  
    			System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
				+"  "+ rs.getString("startplace")+"   "+rs.getString("destination")
				+"   "+rs.getInt("re_number"));  
            	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	} 
	}
	static void searchOrder(String id,Connection conn) throws SQLException{
		ResultSet rs = null; 
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace from Train_information t,Ticket_order ti where t.tno=ti.tno and ti.id='james.borg@conf.com'";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
			//pstmt.setString(1, id);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION   "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")); 
        	while(rs.next()) {  
            	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
            					+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")); 	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	} 
	}
	static void buyTickets(String id,Connection conn) throws SQLException{
		String tno;
		int number;
		int Rows = 0;
		ResultSet rs = null; 
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace,r.re_number from Train_information t,remain_tickets r where t.tno=r.tno";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION  REMAIN TICKETS "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")
        						+"     "+rs.getInt("re_number")); 
        	while(rs.next()) {  
    			System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
				+"  "+ rs.getString("startplace")+"   "+rs.getString("destination")
				+"   "+rs.getInt("re_number"));  
            	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	} 
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter Tno : ");
		tno = sc.nextLine();
		String queryStr2 = "select re_number from remain_tickets where tno=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr2);
	        pstmt.setString(1, tno);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			if(rs.getInt("re_number")>0)
			{
				String queryStr3 = "insert into ticket_order (tno,id) values(?,?)";
				String queryStr4 = "UPDATE REMAIN_TICKETS SET re_number=re_number-1 where tno=?";
				try{
					PreparedStatement pstmt = conn.prepareStatement(queryStr3);
					pstmt.setString(1, tno);
					pstmt.setString(2, id);
					Rows = pstmt.executeUpdate();
					System.out.println("\n"+"You buy the ticket successful"+"\n"); 
					try{
						PreparedStatement pstmt1 = conn.prepareStatement(queryStr4);
						pstmt1.setString(1, tno);
						Rows = pstmt1.executeUpdate();
			        }catch(SQLException e){
			        	System.out.println("error"); 
			        }
		        }catch(SQLException e){
		        	System.out.println("\n"+"You can buy only one ticket for each train"+"\n"); 
		        }
			}else{
				System.out.println("\n"+"There is no ticket left"+"\n"); 
			}
		}else{
			System.out.println("\n"+"Not found the train"+"\n"); 
		}
	}
	static void returnTickets(String id,Connection conn) throws SQLException{
		ResultSet rs = null; 
		int Rows=0;
		String tno;
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace from Train_information t,Ticket_order ti where t.tno=ti.tno and ti.id='james.borg@conf.com'";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION  REMAIN TICKETS "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")); 
        	while(rs.next()) {  
            	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
            					+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")); 	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	} 
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter Tno : ");
		tno = sc.nextLine();
		String queryStr2="DELETE FROM ticket_order WHERE tno=? and id=?";
		String queryStr3 = "UPDATE REMAIN_TICKETS SET re_number=re_number+1 where tno=?";
		String queryStr4 = "Select * from ticket_order where tno=? and id=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr4);
	        pstmt.setString(1, tno);
			pstmt.setString(2, id);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			try{
				PreparedStatement pstmt = conn.prepareStatement(queryStr2);
				pstmt.setString(1, tno);
				pstmt.setString(2, id);
				Rows = pstmt.executeUpdate();
				System.out.println("\n"+"You return the ticket successful"+"\n"); 
				try{
					PreparedStatement pstmt1 = conn.prepareStatement(queryStr3);
					pstmt1.setString(1, tno);
					Rows = pstmt1.executeUpdate();
		        }catch(SQLException e){
		        	System.out.println("error"); 
		        }
	        }catch(SQLException e){
	        	System.out.println("\n"+"You don't have ticket for this train"+"\n"); 
	        }
		}else{
			System.out.println("\n"+"You don't have ticket for this train"+"\n"); 
		}
		
	}
	
	static void searchTrainInformation(Connection conn) throws SQLException{
		ResultSet rs = null; 
		Scanner sc = new Scanner(System.in);
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace,r.re_number from Train_information t,remain_tickets r where t.tno=r.tno";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION  REMAIN TICKETS "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")
        						+"     "+rs.getInt("re_number")); 
        	while(rs.next()) {  
    			System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
				+"  "+ rs.getString("startplace")+"   "+rs.getString("destination")
				+"   "+rs.getInt("re_number"));  
            	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	} 
	}
	
	static void insertTrainInformation(Connection conn) throws SQLException, ParseException{
		String tno,date1,startplace,destination;
		int price,re_number;
		java.util.Date curDate;
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
		ResultSet rs = null; 
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter Train Number : ");
		tno = sc.nextLine();
		System.out.print("Please enter Date : ");
		date1 = sc.nextLine();
		System.out.print("Please enter Price : ");
		price = sc.nextInt();
		sc.nextLine();
		System.out.print("Please enter Startplace : ");
		startplace = sc.nextLine();
		System.out.print("Please enter Destination : ");
		destination = sc.nextLine();
		System.out.print("Please enter remian ticket number : ");
		re_number = sc.nextInt();
		sc.nextLine();
		curDate = format1.parse(date1);
		java.sql.Date date=new java.sql.Date(curDate.getTime());
		int Rows = 0;
		String queryStr1 = "insert into Train_information (tno,tstart,price,startplace,destination) values(?,?,?,?,?)";
		String queryStr2 = "insert into remain_tickets (tno,re_number) values(?,?)";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
			pstmt.setString(1, tno);
	        pstmt.setDate(2, date);
	        pstmt.setInt(3, price);
	        pstmt.setString(4, startplace);
	        pstmt.setString(5, destination);
			Rows = pstmt.executeUpdate();
			System.out.println("Train Information Record Updated Successfully"+"\n");
			try{
				PreparedStatement pstmt1 = conn.prepareStatement(queryStr2);
				pstmt1.setString(1, tno);
		        pstmt1.setInt(2, re_number);
				Rows = pstmt1.executeUpdate();
		    }catch(SQLException e){
		    	System.out.println("error"+"\n");
			}
	    }catch(SQLException e){
	    	System.out.println("Participator Record Updated Fail"+"\n");
		}
	}
	
	static void alterTrainInformation(Connection conn) throws SQLException, ParseException{	
		String tno,date1,startplace,destination;
		int price,re_number;
		ResultSet rs = null; 
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter Train Number Which You Want To ALter : ");
		tno = sc.nextLine();
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace,r.re_number from Train_information t,remain_tickets r where t.tno=r.tno and t.tno=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
	        pstmt.setString(1, tno);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION  REMAIN TICKETS "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")
        						+"     "+rs.getInt("re_number")); 
        	while(rs.next()) {  
    			System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
				+"  "+ rs.getString("startplace")+"   "+rs.getString("destination")
				+"   "+rs.getInt("re_number"));  
            	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	} 

		java.util.Date curDate;
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
		System.out.print("Please enter Date : ");
		date1 = sc.nextLine();
		System.out.print("Please enter Price : ");
		price = sc.nextInt();
		sc.nextLine();
		System.out.print("Please enter Startplace : ");
		startplace = sc.nextLine();
		System.out.print("Please enter Destination : ");
		destination = sc.nextLine();
		System.out.print("Please enter remian ticket number : ");
		re_number = sc.nextInt();
		sc.nextLine();
		curDate = format1.parse(date1);
		java.sql.Date date=new java.sql.Date(curDate.getTime());
		int Rows = 0;
		String queryStr2 = "update Train_information set tstart=?,price=?,startplace=?,destination=? where tno=?";
		String queryStr3 = "update remain_tickets set re_number=? where tno=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr2);
	        pstmt.setDate(1, date);
	        pstmt.setInt(2, price);
	        pstmt.setString(3, startplace);
	        pstmt.setString(4, destination);
	        pstmt.setString(5, tno);
			Rows = pstmt.executeUpdate();
			
			try{
				PreparedStatement pstmt1 = conn.prepareStatement(queryStr3);
		        pstmt1.setInt(1, re_number);
		        pstmt1.setString(2, tno);
				Rows = pstmt1.executeUpdate();
				System.out.println("Train Information Record Updated Successfully"+"\n");
		    }catch(SQLException e){
		    	System.out.println("error"+"\n");
			}
	    }catch(SQLException e){
	    	System.out.println("Participator Record Updated Fail"+"\n");
		}
	}
	
	static void deleteTrainInformation(Connection conn) throws SQLException, ParseException{
		ResultSet rs = null; 
		Scanner sc = new Scanner(System.in);
		String queryStr1 = "select t.tno,t.tstart,t.price,t.destination,t.startplace,r.re_number from Train_information t,remain_tickets r where t.tno=r.tno";
		try{
			PreparedStatement pstmt = conn.prepareStatement(queryStr1);
	        rs = pstmt.executeQuery();
        }catch(SQLException e){
        	e.printStackTrace();
        }
		if(rs!=null&&rs.next()){ 
			System.out.println("Train Information"+"\n");
			System.out.println("TNO  DATE      PRICE  STARTPLACR  DESTINATION  REMAIN TICKETS "+"\n");
        	System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
        						+"     "+ rs.getString("startplace")+"     "+rs.getString("destination")
        						+"     "+rs.getInt("re_number")); 
        	while(rs.next()) {  
    			System.out.println(rs.getString("tno")+" "+rs.getDate("tstart")+" "+rs.getInt("price")
				+"  "+ rs.getString("startplace")+"   "+rs.getString("destination")
				+"   "+rs.getInt("re_number"));  
            	
        		}  
        	}else{ 
        		System.out.println("no result"); 
        	}
		System.out.println("Enter Train Number");
		String tno;
		tno = sc.nextLine();
		String queryStr2 = "DELETE FROM ticket_order WHERE tno =?";
		String queryStr3 = "DELETE FROM remain_tickets WHERE tno =?";
		String queryStr4 = "DELETE FROM Train_information WHERE tno =?";
		try{
			PreparedStatement pstmt2 = conn.prepareStatement(queryStr2);
			pstmt2.setString(1, tno);
	        rs = pstmt2.executeQuery();
	        try{
				PreparedStatement pstmt3 = conn.prepareStatement(queryStr3);
				pstmt3.setString(1, tno);
		        rs = pstmt3.executeQuery();
		        try{
					PreparedStatement pstmt4 = conn.prepareStatement(queryStr4);
					pstmt4.setString(1, tno);
			        rs = pstmt4.executeQuery();
			        System.out.println("Delete Train Information Successful");
		        }catch(SQLException e){
		        	e.printStackTrace();
		        }
	        }catch(SQLException e){
	        	e.printStackTrace();
	        }
        }catch(SQLException e){
        	e.printStackTrace();
        }	
	}
}
