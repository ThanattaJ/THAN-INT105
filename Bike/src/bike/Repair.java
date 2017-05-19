package bike;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Repair{
    private int num;
    private String askingUser="";
    private String repairingUser="";
    private String statusUser="";
    private String timerUser;
    private long peairId;
    private String problem;//ปัญหาของการซ่อมจักรยาน เช่น ยางแตก ช่างกรอก Asking
    private String detail;//รายละเอียด เช่น ต้องเปลี่ยนยาง ช่างกรอก Repairing
    private Timer t;//เก็บเวลาที่จะต้องเสร็จ
    private int hours;
    private int minute;
    private int secound;
    private String timeDetail;//เวลาที่ช่างซ่อมจะซ่อมเสร็จ
    private History historyRp = new History();
    private String bike="";//รับจาก GUI ให้ user กรอก
    private String whyRepair="";//ให้ user กรอกว่าทำไมถึงต้องส่งซ่อม
    private String color="";
    private long countTransId;
    private Date time = new Date();
    private String showTime="";
    private long repairStateId;
    private String status="";
    private boolean statusThread = false;
    private String askingAdminNotSuccess ;
    private String repairingAdminNotSuccess ;
    private String statusAdminNotSuccess ;
    private int itemIDAdminNotSuccess ;
    private Timestamp startTimeAdminNotSuccess ;
    private Timestamp endTimeAdminNotSuccess ;
    private long transIDAdminNotSuccess ;
    private long repairStateAdminNotSuccess ;
    private int userID;
    private int itemId;
    
    public Repair() {
        
    }
    
    public ArrayList<String> connectDBforListUserSentToRepair(){ //เอา prepairID,name,surname,userID เพื่อเอาไปใช้ใน listUserRepair
        ArrayList<String> list = new ArrayList<String>();
        String format="";
        try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            String temp = "SELECT Prepair_Desctiption.id,User.firstName,User.lastName,User.userID FROM Green_Society.Prepair_Desctiption " +
                            "JOIN Transaction ON Prepair_Desctiption.transID=Transaction.transID " +
                            "INNER JOIN User ON Transaction.userID=User.userID " +
                            "LEFT JOIN Green_Society.Repair_State ON Prepair_Desctiption.id = Repair_State.item_id " +
                            "WHERE Repair_State.item_id IS NULL";
            ResultSet rs = st.executeQuery(temp);
            while(rs.next()){
                int idPrepair = rs.getInt("id");
                String name = rs.getString("firstName");
                String surname = rs.getString("lastName");
                userID = rs.getInt("userID");
                format = idPrepair+"    |     Name: "+name+"    |   Surname: "+surname+"    |   ID: "+userID;
                list.add(format);
            }
            
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return list;
    }
    
    public void connectDBForChangeToSuccessFromPerpair(long perpairID){ //รับ perpairID เพื่อเปลี่ยนเป็น Repair_State.Recieving -> success
         try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            String temp = "UPDATE Repair_State JOIN Prepair_Desctiption ON Repair_State.item_id = Prepair_Desctiption.id "
                    + "SET Repair_State.Recieving = 'Success' WHERE Prepair_Desctiption.id = "+perpairID;
            st.executeUpdate(temp);
            
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }

    public void connectDBForChangeToSuccessFormRepairState(long idRepairState){ //รับ repairID เพื่อเปลี่ยนเป็น Repair_State.Recieving -> success
         try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            String temp = "UPDATE Repair_State SET Repair_State.Recieving = 'Success' WHERE id= "+idRepairState;
            
            st.executeUpdate(temp);
            
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    public ArrayList<String> connectDBForCheckRepairNotSucceess(){ //เอาไปใช้ในเพื่อดูว่าอันไหนที่ not success
        ArrayList<String> notSuccess = new ArrayList<String>();
        String name;
        String action;
        Timestamp remaining;
        String format;
        int id;
         try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            String temp = "SELECT Repair_State.id,User.firstName,User.userID,Repair_State.Repairing,Transaction.dateTime,Repair_State.item_id FROM Green_Society.User " +
                            "JOIN Repair_State ON User.userID = Repair_State.userID " +
                            "INNER JOIN Prepair_Desctiption ON Repair_State.item_id=Prepair_Desctiption.id " +
                            "INNER JOIN Transaction ON Prepair_Desctiption.transID=Transaction.transID " +
                            "WHERE Repair_State.Recieving = 'Not Success!'";
            ResultSet rs = st.executeQuery(temp);
            while(rs.next()){
                id = rs.getInt("id");
                name=rs.getString("firstName");
                userID = rs.getInt("UserID");
                action=rs.getString("Repairing");
                remaining=rs.getTimestamp("dateTime");
                itemId = rs.getInt("item_id");
                format=id+"   |   "+name+"   |   "+action+"       "+remaining;
                notSuccess.add(format);
            }
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
         return notSuccess;
    }

    public int getUserID() {
        return userID;
    }

    public int getItemId() {
        return itemId;
    }

    public long getCountTransId() {
        return countTransId;
    }
    
    public void connectDBFromAdminToUser(long itemId,int user){//กรอกรายละเอียดการซ่อมให้ user
         try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            //ดึงเอา id ที่มาที่สุดออกมา เพื่อให้มันสามารถ insert ลง table ให้ไม่ซ้ำกันได้
            String temp = "SELECT MAX(id) AS countId FROM Repair_State ";
            ResultSet rs = st.executeQuery(temp);
            int count=0;
            while(rs.next()){
                count = rs.getInt("countId");
            }
            repairStateId = ++count;
            //------------------------------------------------------------------
            //เอาข้อมูลลง DB Repair_State
            String sqlid = "\'"+repairStateId+"\'";
            String sqlAsking = "\'"+problem+"\'";
            String sulRepairing = "\'"+detail+"\'";
            String sqlRecieving = "\'"+Status(false)+"\'";
            String sqlItemId = "\'"+itemId+"\'";
            
            String temp2 ="INSERT INTO Repair_State "
                    + "VALUES"+" ("+sqlid+","
                    + sqlAsking +","
                    + sulRepairing + ","
                    + sqlRecieving + ","
                    + sqlItemId + ","
                    + user+")";
            
            st.executeUpdate(temp2);
            //---------------------------------------------------------------------------------------
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    public int connectDBFormAdminSelectTransID(long repairID){
        int transID=0;
        try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            //ดึงเอา id ที่มาที่สุดออกมา เพื่อให้มันสามารถ insert ลง table ให้ไม่ซ้ำกันได้
            String temp = "SELECT transID FROM Prepair_Desctiption JOIN Repair_State ON Prepair_Desctiption.id = Repair_State.item_id WHERE Repair_State.item_id =  "+repairID;
            ResultSet rs = st.executeQuery(temp);
            
            while(rs.next()){
                transID = rs.getInt("transID");
            }
            
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return transID;
    }
    
    public void connectDBForAdminUpdateTime(long transId,Timestamp endTime ){ //อัปเดตเฉพาะ returnDate ใน transaction
        try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            
            String temp = "update `Transaction` set return_dateTime = '"+endTime+"' WHERE transID = "+transId;
            st.executeUpdate(temp);
            //---------------------------------------------------------------------------------------
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    public void connectDBForAdminUpdateTime(long transId,Timestamp startTime,Timestamp endTime){
        try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            
            String temp = "update `Transaction` set return_dateTime = '"+endTime
                    + "' ,dateTime = '" + startTime
                    + "' WHERE transID ="+transId;
            st.executeUpdate(temp);
            //---------------------------------------------------------------------------------------
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
        
    public void connectDBFromUserToAdmin(String asking,String band,String color,long userID){ //รับจาก parameter จาก user เพื่อเก็บลง prepair_desctioption
        Timer t = new Timer();
        Timestamp startDate = new Timestamp(t.getBorrowTime().getTime());
        Timestamp returnDate = new Timestamp(t.getReturnTime().getTime());
        try{
            Connection connect = Database.connectDb("jan", "jan042");
            Statement st = connect.createStatement(); 
            //ดึงเอา id ที่มาที่สุดออกมา เพื่อให้มันสามารถ insert ลง table ให้ไม่ซ้ำกันได้
            String temp = "SELECT MAX(id) AS countId FROM Prepair_Desctiption ";
            ResultSet rs = st.executeQuery(temp);
            int count=0;
            while(rs.next()){
                count = rs.getInt("countId");
            }
            peairId = ++count;
            //------------------------------------------------------------------
            //ดึง transId ว่ามีมากที่สุดเท่าไร เพื่อให้มัน กรอกลง DB ได้ และไม่ซ้ำกับ transID
            String temp1 = "SELECT MAX(transId) AS countTransId FROM Transaction ";
            ResultSet rs1 = st.executeQuery(temp1);
            int countTrans=0;
            while(rs1.next()){
                countTrans = rs1.getInt("countTransId");
            }
            countTransId = ++countTrans;
            //------------------------------------------------------------------
            //เอาข้อมูลลง DB prapair_Desctiption
            String sqlid = "\'"+this.peairId+"\'";
            String brand = "\'"+band+"\'";
            String sqlcolor = "\'"+color+"\'";
            String sqlWhy = "\'"+asking+"\'";
            String sqlTransId = "\'"+countTransId+"\'";
            
            String temp2 ="INSERT INTO `Prepair_Desctiption` (`id`, `brand`, `color`, `other`, `transID`) "
                    + "VALUES"+" ("+sqlid+","
                    + brand +","
                    + sqlcolor + ","
                    + sqlWhy + ","
                    + sqlTransId +")";
            
            st.executeUpdate(temp2);
            //---------------------------------------------------------------------------------------
            //เอาข้อมูลลง transaction ด้วย 
            String userId = "\'"+userID+"\'";
            String action = "\'Repair\'";
            String officerId = "\'"+200+"\'";
            
            String temp3 ="INSERT INTO Transaction VALUES " //set ค่าให้กับ Database
                    + "("+this.countTransId+","
                    +"'"+startDate+"','"
                     +returnDate+"',"
                      +0+ ","
                      +0+" ,"
                      +userId+","
                      +action+"," 
                      +officerId+")";
             
            st.executeUpdate(temp3);
            //------------------------------------------------------------------
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }

    }
        
    public void connectDBShowRepairForUserFollowing(long userID){//เพื่อที่ User จะสามารถดูและติดตามดารซ่อมของตัวเองได้
        Timestamp startTime=null;
        Timestamp endTime=null;
        try{
            Connection connect = Database.connectDb("jan", "jan042");
            
            Statement st = connect.createStatement(); 
            //ดึงเอา id ที่มาที่สุดออกมา เพื่อให้มันสามารถ insert ลง table ให้ไม่ซ้ำกันได้
            String temp = "SELECT Asking,Repairing,Recieving FROM Repair_State where userID= "+userID;
            ResultSet rs = st.executeQuery(temp);
            int count=0;
            while(rs.next()){
                askingUser += "- " + rs.getString("Asking")+"<br>";
                repairingUser += "- "+rs.getString("Repairing") +"<br>";
                statusUser += "- "+rs.getString("Recieving") + "<br>";
            }
            
            String temp2 = "SELECT dateTime,return_dateTime FROM `Transaction` WHERE transID = "+this.countTransId;
            ResultSet rs2 = st.executeQuery(temp2);
            
            while(rs2.next()){
                startTime = rs2.getTimestamp("dateTime");
                endTime = rs2.getTimestamp("return_dateTime");
            }
            
            if(startTime.equals(null)){
                timerUser = "<html>Start: - </html>";
            }else{
                timerUser = "<html>Start: "+startTime+"</html>";
            }
            //---------------------------------------------------------------------------------------
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
       public String connectDBForRepairAdminDetail(long clickDetailUser){ //แสดงสิ่งที่ user ส่งมาซ่อม
        String detail="";
        try{
            Connection connect = Database.connectDb("jan", "jan042");
      
            Statement st = connect.createStatement(); 
            //show detail user ส่งซ่อมจักรยาน
            String temp = "SELECT * FROM `Prepair_Desctiption` where id ="+clickDetailUser;
            ResultSet rs = st.executeQuery(temp);
            
            while(rs.next()){
                detail+="Id: "+rs.getInt("id")+"\n";
                detail+="Brand: "+rs.getString("brand")+"\n";
                detail+="Color: "+rs.getString("color")+"\n";
                detail+="Why Repair: "+rs.getString("other");
                int transID = rs.getInt("transID");
            }
            
            //------------------------------------------------------------------
            if(connect != null){
                    connect.close();
		}
		}catch (SQLException e){
                    e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return detail;
    }
       
    public void connectDBForRepairAdmin(){ //for jPanelRepairingAdmin
        try{
            Connection connect = Database.connectDb("jan", "jan042");
            
            Statement st = connect.createStatement(); 
            //ดึงเอา id ที่มาที่สุดออกมา เพื่อให้มันสามารถ insert ลง table ให้ไม่ซ้ำกันได้
            String temp = "SELECT MAX(id) AS countId FROM Prepair_Desctiption ";
            ResultSet rs = st.executeQuery(temp);

            while(rs.next()){
                this.num = rs.getInt("countId");
            }
        }   
        catch(Exception ex){
            System.out.println(ex);
        }
    }

    public Date startTimeToRepair(){
        Date startDate = t.getBorrowTime();
        return startDate;
    }
    
    public Date endTimeToRepair(){
        Date endDate = t.getReturnTime();
        return endDate;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
    
   
    public void setStatusThread(boolean statusThread) {
        this.statusThread = statusThread;
    }

    public boolean isStatusThread() {
        return statusThread;
    }
    
    public void plusDay(int h,int m,int s){ //เมธอดคำนวณชั่วโมง นาที วินาที ไม่ให้มันเกินตามความเป็นจริง
        hours+=h;
        minute+=m;
        secound+=s;
    }
    
    public String Status(boolean tem){ // รับมาจากปุ่มกด ถ้าเสร็จเป็น true ไม่เสร็จเป็น false
        if(tem==false){
            status="Not success!";
        }else if(tem==true){
            status="Success!";
        }
        return status;  
    }
    
    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecound() {
        return secound;
    }

    public void setSecound(int secound) {
        this.secound = secound;
    }

    public String getBike() {
        return bike;
    }

    public void setBike(String bike) {
        this.bike = bike;
    }
    
    public Timer getTime() {
        return t;
    }

    public String getStatus() {
        return status;
    }

    public String getTimerUser() {
        return timerUser;
    }
    
    public Repair(String problem, String detail) {
        this.problem = problem;
        this.detail = detail;
    }

    public String getWhyRepair() {
        return whyRepair;
    }

    public void setWhyRepair(String whyRepair) {
        this.whyRepair = whyRepair;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTimeDetail() {
        return timeDetail;
    }
    
    public long getRepairStateId() {
        return repairStateId;
    }

    public String getAskingUser() {
        return askingUser;
    }

    public String getRepairingUser() {
        return repairingUser;
    }

    public String getStatusUser() {
        return statusUser;
    }
    public long getRepairStateAdminNotSuccess() {
        return repairStateAdminNotSuccess ;
    }

    public long getTransIDAdminNotSuccess() {
        return transIDAdminNotSuccess ;
    }

    public String getAskingAdminNotSuccess() {
        return askingAdminNotSuccess ;
    }

    public String getRepairingAdminNotSuccess() {
        return repairingAdminNotSuccess ;
    }

    public String getStatusAdminNotSuccess() {
        return statusAdminNotSuccess ;
    }

    public int getItemIDAdminNotSuccess() {
        return itemIDAdminNotSuccess ;
    }

    public Timestamp getStartTimeAdminNotSuccess() {
        return startTimeAdminNotSuccess ;
    }

    public Timestamp getEndTimeAdminNotSuccess() {
        return endTimeAdminNotSuccess ;
    }
    
    @Override
    public String toString() {
        return "Problem: " + problem 
                + "\nDetail: " + detail+"\n";
    }
    
}
