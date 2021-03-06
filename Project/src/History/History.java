package History;

import ConnectDB.ConnectDatabase;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
public class History {
    private long historyId;
    private long transID;
    
    public ArrayList<String> tableHistory(long id){
        ArrayList<String> table= new ArrayList<String>();
        String format;
        Timestamp dateTime;
        String action;
        String item;
        Timestamp returnTime;
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
            
            Statement st = connect.createStatement(); 
            
            String temp = "SELECT dateTime,action,itemName,return_dateTime FROM Green_Society.Items "
                    + "JOIN Transaction On Items.itemID = Transaction.itemID "
                    + "WHERE NOT Transaction.action = 'Return' AND Transaction.userID= "+id;
            ResultSet rs = st.executeQuery(temp);
            
            while(rs.next()){
                dateTime=rs.getTimestamp("dateTime");
                String newDateTime= new SimpleDateFormat("MM/dd/yyyy").format(dateTime);
                action = rs.getString("action");
                item = rs.getString("itemName");
                returnTime = rs.getTimestamp("return_dateTime");
                String newReturn =  new SimpleDateFormat("MM/dd/yyyy").format(returnTime);
                format = "      "+newDateTime+"                        "+action+"                            "+item+"                    "+newReturn;
                table.add(format);
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}                
        }
        
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return table;
    }
    
    
    public String showActionUser(long id){ //user ใส่ไอดีตัวเองที่ต้องการรู้ประวัติการใช้งานของตัวเอง
        String output="";
        int statUser=0;
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
            
            Statement st = connect.createStatement();
            String temp4 = "select * from `Transaction` where UserId LIKE "+id;
            ResultSet rs4 = st.executeQuery(temp4);
            while(rs4.next()){
                output+=("UserId: "+id+"\n");
                output+=("dateTime: " + rs4.getTimestamp("dateTime")+"\n");
                output+=("itemID: " + rs4.getString("itemID")+"\n");
                output+=("action: "+ rs4.getString("action")+"\n");
                statUser++;
                output+=("----------------------------------------------\n");
            }
            output+=("userID: "+id+"\n"+"The stat of user: "+statUser+"\n");
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return output;
    }
    
    public int statGreensocietyReturn(){//admin จะเห็นหน้าสถิติการใช้งานของ User แต่ล่ะคน เรียงลำดับการใช้งานมากไปน้อย
        int statOfReturn=0;
        String month;
        String date;
        java.util.Date now = new java.util.Date();
        if((now.getMonth()+1)<10){
            month = "0"+(now.getMonth()+1);
        }else{
            month = ""+now.getMonth()+1;
        }
        
        if(now.getDate()<10){
            date = "0"+ now.getDate();
        }else{
            date = ""+now.getDate();
        }
        
        String adminDate = (now.getYear()+1900)+"-"+month+"-"+date;
        
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
        
            Statement st = connect.createStatement();
            
            String temp5 = "SELECT COUNT(transID) AS statReturn FROM `Transaction` WHERE dateTime LIKE \'" +adminDate +"%\' AND action LIKE 'Return'";
            ResultSet rs5 = st.executeQuery(temp5);
            System.out.println("adminDate: "+adminDate);
            
            while(rs5.next()){
                statOfReturn = rs5.getInt("statReturn");
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return statOfReturn;
    }
    
    public int statGreensocietyRepair(){//admin จะเห็นหน้าสถิติการใช้งานของ User แต่ล่ะคน เรียงลำดับการใช้งานมากไปน้อย
        int statOfRepair=0;
        String month;
        String date;
        java.util.Date now = new java.util.Date();
        if((now.getMonth()+1)<10){
            month = "0"+(now.getMonth()+1);
        }else{
            month = ""+now.getMonth()+1;
        }
        
        if(now.getDate()<10){
            date = "0"+ now.getDate();
        }else{
            date = ""+now.getDate();
        }
        
        String adminDate = (now.getYear()+1900)+"-"+month+"-"+date;
        
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
        
            Statement st = connect.createStatement();
            
            String temp5 = "SELECT COUNT(transID) AS statRepair FROM `Transaction` WHERE dateTime LIKE \'" +adminDate +"%\' AND action LIKE 'Repair'";
            ResultSet rs5 = st.executeQuery(temp5);
            System.out.println("adminDate: "+adminDate);
            
            while(rs5.next()){
                statOfRepair = rs5.getInt("statRepair");
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return statOfRepair;
    }
    
    public int statGreensocietyBorrow(){//admin จะเห็นหน้าสถิติการใช้งานของ User แต่ล่ะคน เรียงลำดับการใช้งานมากไปน้อย
        int statOfBorrow=0;
        String month;
        String date;
        java.util.Date now = new java.util.Date();
        if((now.getMonth()+1)<10){
            month = "0"+(now.getMonth()+1);
        }else{
            month = ""+now.getMonth()+1;
        }
        
        if(now.getDate()<10){
            date = "0"+ now.getDate();
        }else{
            date = ""+now.getDate();
        }
        
        String adminDate = (now.getYear()+1900)+"-"+month+"-"+date;
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
        
            Statement st = connect.createStatement();
            
            String temp5 = "SELECT COUNT(transID) AS statBorrow FROM `Transaction` WHERE dateTime LIKE \'" +adminDate +"%\' AND action LIKE 'Borrow'";
            ResultSet rs5 = st.executeQuery(temp5);
            
            while(rs5.next()){
                statOfBorrow = rs5.getInt("statBorrow");
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return statOfBorrow;
    }
    
    public String showBorrowUser(long id){ //user ใส่ไอดีตัวเองที่ต้องการรู้ประวัติการใช้งานของตัวเอง ดึงข้อมูลเฉพาะวันที่ยืม
        String output="";
        String format="";
        Timestamp borrow;
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
            
            Statement st = connect.createStatement();
            String temp4 = "select dateTime from `Transaction` where UserId = "+ id +" and action LIKE 'Borrow' ";
            ResultSet rs4 = st.executeQuery(temp4);
            while(rs4.next()){
                borrow = rs4.getTimestamp("dateTime");
                format += new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(borrow);
                format +="\n";
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return format;
    }
    
     public String showHistoryUser(long id){ //user ใส่ไอดีตัวเองที่ต้องการรู้ประวัติการใช้งานของตัวเอง ดึงข้อมูลเฉพาะวันที่คืน
        String format="";
        Timestamp returnDate;
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
            
            Statement st = connect.createStatement();
            String temp4 = "SELECT itemName FROM Green_Society.Items JOIN Transaction On Items.itemID = Transaction.itemID WHERE Transaction.itemID LIKE \"%\" GROUP BY Items.itemName";
            ResultSet rs4 = st.executeQuery(temp4);
            while(rs4.next()){
//                returnDate = rs4.getTimestamp("return_dateTime");
//                format += new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(returnDate);
//                format +="\n";
                    format = rs4.getString("");
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return format;
    }

    public String showReturnUser(long id){ //user ใส่ไอดีตัวเองที่ต้องการรู้ประวัติการใช้งานของตัวเอง ดึงข้อมูลเฉพาะวันที่คืน
        String format="";
        Timestamp returnDate;
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
            
            Statement st = connect.createStatement();
            String temp4 = "select return_dateTime from `Transaction` where UserId = "+ id +" and action LIKE 'Return' ";
            ResultSet rs4 = st.executeQuery(temp4);
            while(rs4.next()){
                returnDate = rs4.getTimestamp("return_dateTime");
                format += new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(returnDate);
                format +="\n";
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return format;
    }
    
    public String showActionUserFormDatabase(long id){ //user ใส่ไอดีตัวเองที่ต้องการรู้ประวัติการใช้งานของตัวเอง ดึงข้อมูลเฉพาะวันที่คืน
        String format="";
        String temp;
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
            
            Statement st = connect.createStatement();
            String temp4 = "select action from `Transaction` where UserId = "+ id +" and action LIKE 'Borrow' ";
            ResultSet rs4 = st.executeQuery(temp4);
            while(rs4.next()){
                temp = rs4.getString("action");
                format += temp;
                format +="\n";
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return format;
    }
    
    public String showItemUserFormDatabase(long id){ //user ใส่ไอดีตัวเองที่ต้องการรู้ประวัติการใช้งานของตัวเอง ดึงข้อมูลเฉพาะวันที่คืน
        String format="";
        String temp;
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
            
            Statement st = connect.createStatement();
            String temp4 = "select itemID from `Transaction` where UserId = "+ id +" and action LIKE 'Borrow' ";
            ResultSet rs4 = st.executeQuery(temp4);
            while(rs4.next()){
                temp = rs4.getString("itemID");
                format += temp;
                format +="\n";
            }
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return format;
    }
    
    public String showRepairUser(long id){ //user ใส่ไอดีตัวเองที่ต้องการรู้ประวัติการใช้งานของตัวเอง ดึงข้อมูลเฉพาะวันที่คืน
        String output="";
        try{
            ConnectDatabase cndb = new ConnectDatabase();
            Connection connect = ConnectDatabase.connectDb("jan", "jan042");
            Class.forName("com.mysql.jdbc.Driver");
            
            Statement st = connect.createStatement();
            String temp = "select transId from `Transaction` where UserId = "+ id +" and action LIKE 'Repair' ";
            ResultSet rs = st.executeQuery(temp);
            while(rs.next()){
                transID = rs.getInt("transId");
            }
            
            String temp1 = "SELECT other FROM `Prepair_Desctiption` where transID = "+transID;
            ResultSet rs1 = st.executeQuery(temp1);
            while(rs1.next()){
                output+=rs1.getString("other");
            }
            
            
            try {
		if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
		}      
        }
        catch(ClassNotFoundException cfe){
            System.out.println(cfe);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return output;
    }
    
    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    
    public String toString(){
        String output = "";
        output+="historyId: "+historyId;
        return output;
    }
    
    
}