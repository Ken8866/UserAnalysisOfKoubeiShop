package org.aura.bigdata.utils;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerUtils {

    public static void main(String[] args) {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(new Date().toString());
            }
        };
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 27);
        calendar.set(Calendar.SECOND, 0);
        Date firstTime = calendar.getTime();

        if(firstTime.before(new Date())){
            Calendar nextTime = Calendar.getInstance();
            nextTime.setTime(firstTime);
            nextTime.add(Calendar.MINUTE,1);
            firstTime = nextTime.getTime();
        }

        long intervalPeriod = 60 * 1 * 1000 ;
        timer.schedule(task, firstTime,intervalPeriod);
    }

}
