import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
public class TimerManager {
    public static void main(String[] args) throws Exception{
        new TimerManager();
    }

    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    public TimerManager() throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); //凌晨0点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date=calendar.getTime(); //第一次执行定时任务的时间
        Timer timer = new Timer();
        Task task = new Task();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(task,date,PERIOD_DAY);
    }

}
