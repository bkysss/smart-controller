import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SQLFunction {
    public static Map<String,Integer> GetActiveNum() throws ClassNotFoundException, SQLException {
        Map<String,Integer> map=new HashMap<>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql://localhost:3306/SmartController";
        Connection conn = DriverManager.getConnection(url,"root","123456");
        String date="",ip="";
        int actNum=0;
        try(PreparedStatement ps= conn.prepareStatement("select DDate,DIpAddr,ActiveNum from Daily")){
            try(ResultSet rs=ps.executeQuery()){
                while (rs.next()){
                    date=rs.getString("DDate");
                    ip=rs.getString("DIpAddr");
                    actNum=rs.getInt("ActiveNum");
                    map.put(date+ip,actNum);
                }
            }
        }
        conn.close();
        return map;
    }

    static int UpdateDaily(String srcIp,String dstIp) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/SmartController";
        Connection conn = DriverManager.getConnection(url, "root", "123456");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String time = timeFormat.format(calendar.getTime());
        String ddate = dateFormat.format(calendar.getTime());
        int n = -1;
        String clientIp = HandleCaptured.ClientIpStr(srcIp, ddate, dstIp);

        if (HandleCaptured.IsFirst(ddate, dstIp)) {
            try (PreparedStatement ps = conn.prepareStatement("update Daily set " +
                    "TOnFirst=?,TOffLast=?,ClientIPs=?,ActiveNum=ActiveNum+1 where DDate=? and DIpAddr=?")) {
                ps.setObject(1, time);
                ps.setObject(2, time);
                ps.setObject(3, clientIp);
                ps.setObject(4, ddate);
                ps.setObject(5, dstIp);
                n = ps.executeUpdate();
            }
        } else {
            try (PreparedStatement ps = conn.prepareStatement("update Daily set " +
                    "TOffLast=?,ClientIPs=?,ActiveNum=ActiveNum+1 where DDate=? and DIpAddr=?")) {
                ps.setObject(1, time);
                ps.setObject(2, clientIp);
                ps.setObject(3, ddate);
                ps.setObject(4, dstIp);
                n = ps.executeUpdate();
            }
        }
        return n;
    }

    static int HasInserted() throws ClassNotFoundException, SQLException { //判断当日是否已经插入初始数据
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/SmartController";
        Connection conn = DriverManager.getConnection(url, "root", "123456");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String ddate = dateFormat.format(calendar.getTime());
        int n=-1;
        try (PreparedStatement ps= conn.prepareStatement("select count(*) from Daily where DDate=?")){
            ps.setObject(1,ddate);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    n=rs.getInt("count(*)");
                }
            }
        }
        return n;
    }
}
