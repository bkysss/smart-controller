import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimerTask;

public class Task extends TimerTask {


    String[] ServIpLIst={"192.168.154.133","127.0.0.1"};
    public void run() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/SmartController";
            Connection conn = DriverManager.getConnection(url,"root","123456");
            //Statement statement=conn.createStatement();
            Calendar calendar= Calendar.getInstance();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMdd");
            String ddate=dateFormat.format(calendar.getTime());
            for (String ipStr: Arrays.asList(ServIpLIst)) {
                try(PreparedStatement ps=conn.prepareStatement("insert into Daily(DDate,DIpAddr,SwitchID,IfNum," +
                        "ServCPU,TOnFirst,TOffLast,ActiveNum,ClientIps) values(?,?,?,?,?,?,?,?,?)")){
                    ps.setObject(1,ddate);
                    ps.setObject(2,ipStr);
                    ps.setObject(3,"1");
                    ps.setObject(4,"1");
                    ps.setObject(5,0);
                    ps.setObject(6,"000000");
                    ps.setObject(7,"000000");
                    ps.setObject(8,0);
                    ps.setObject(9,"");
                    int n=ps.executeUpdate();
                }
            }
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
