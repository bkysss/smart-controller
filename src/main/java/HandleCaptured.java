import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class HandleCaptured {

    static String url="jdbc:mysql://localhost:3306/SmartController";

    static boolean IsFirst(String ddate, String ip) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url,"root","123456");
        String sqlStr="select TOnFirst from Daily where DDate=? and DIpAddr=?";
        String time="";
        try(PreparedStatement ps=conn.prepareStatement("select TOnFirst from Daily where DDate=? AND DIpAddr=?")){
            ps.setObject(1,ddate);
            ps.setObject(2,ip);
            try(ResultSet rs=ps.executeQuery()){
                while (rs.next()){
                    time=rs.getString("TOnFirst");
                }
            }
        }
        conn.close();
        if(time.equals("000000")){
            return true;
        }
        else
            return false;

    }

    static String ClientIpStr(String clientIp, String ddate, String servIp) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url,"root","123456");
        String clientIps="";
        try(PreparedStatement ps=conn.prepareStatement("select ClientIps from Daily where DDate=? and DIpAddr=?")){
            ps.setObject(1,ddate);
            ps.setObject(2,servIp);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    clientIps=rs.getString("ClientIps");
                }
            }
        }
        conn.close();
        if(clientIp.equals("")){
            return clientIp;
        }
        else if(clientIps.contains(clientIp)){
            return clientIps;
        }
        else
            return clientIps+","+clientIp;
    }



}
