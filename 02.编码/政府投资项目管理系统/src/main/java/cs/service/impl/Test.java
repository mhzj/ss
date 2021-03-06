package cs.service.impl;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/5/14 0014.
 */
public class Test {
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();

    /**回退指定时间*/
    public Date regressesDay(Date date,int day){
            //设置回退日期
            calendar.setTime(date);
            //回退天数
            calendar.add(Calendar.DATE,day);
            return calendar.getTime();
    }
    public Date  formatDateByString(String date){
        try{
            return simpleDateFormat.parse(date);
        }catch (Exception e){
        }
        return null;
    }

    // 排除周末计算 date之后dayNum 时间
    public Date getLimitTime(Date date,int dayNum){
        calendar.setTime(date);
        int mod = dayNum % 5;
        int other = dayNum / 5 * 7;
        for (int i = 0; i < mod;) {
            calendar.add(Calendar.DATE, 1);
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.FRIDAY:
                case Calendar.SATURDAY:
                    break;
                default:
                    i++;
                    break;
            }
        }
        if (other > 0)
            calendar.add(Calendar.DATE, other);
        return calendar.getTime();
    }
    /**
     * 是否超期时间
     * **/
    public int overdueTime(Date date,String shenpi,String type,int dayNum){
        String regressesDayTime = null;
        Date shenpiEnd;
        int week ,weekDay,days=0;
        long dayTime =0L;
        try {
            //当前时间
            Date nowTime = simpleDateFormat.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
            //审批项目开始时间
            Date shenpiTime = simpleDateFormat.parse(shenpi);
            String  tt  = new SimpleDateFormat("yyyy-MM-dd").format(shenpiTime);
            //如果当前时间是周末需要往前移动1到2天  当前时间周末不参与计算
            week=getWeek(nowTime);
            if (week==6||week==0) {//周六，周日
                if(week ==6){
                    nowTime  = regressesDay(nowTime,-1);
                }
                if(week ==0){
                    nowTime = regressesDay(nowTime,-2);
                }
            }
            //审批项目开始时间大于当前时间:尚未开始
            if (shenpiTime.getTime()> nowTime.getTime()) {
                //退出
				return 0;
            }
            //审批结束时间
            shenpiEnd = getLimitTime(shenpiTime,dayNum);
            // 当前时间与审批结束时间之间的周末 间隔天数

            if(nowTime.getTime()>shenpiEnd.getTime()){
                week = getSundayNum(nowTime,shenpiEnd,"yyyy-MM-dd");
                dayTime = (long)((shenpiEnd.getTime()+((24 * 60 * 60 * 1000)*week))-nowTime.getTime());
            }
            if(nowTime.getTime()<shenpiEnd.getTime()){
                week = getSundayNum(nowTime,shenpiEnd,"yyyy-MM-dd");
                dayTime = (long)((shenpiEnd.getTime()-((24 * 60 * 60 * 1000)*week))-nowTime.getTime());
            }
            days = (int)dayTime/(24 * 60 * 60 * 1000);

        } catch (Exception e) {
        }
        return days;
    }

    /**
     * 获取2个日期之间周六，周日的天数
     * @param startDate
     * @param endDate
     * @param format
     * @return
     * @author zhaigx
     * @date 2013-3-13
     */
    public static int getSundayNum(Date start, Date stop, String format) {
        List<Date> yearMonthDayList = new ArrayList<Date>();

        if (start.after(stop)) {
            Date tmp = start;
            start = stop;
            stop = tmp;
        }
        //将起止时间中的所有时间加到List中
        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.setTime(start);
        while (calendarTemp.getTime().getTime() <= stop.getTime()) {
            yearMonthDayList.add(calendarTemp.getTime());
            calendarTemp.add(Calendar.DAY_OF_YEAR, 1);
        }
        Collections.sort(yearMonthDayList);
        int num=0;//周六，周日的总天数
        int size=yearMonthDayList.size();
        int week=0;
        for (int i = 0; i < size; i++) {
            Date day=yearMonthDayList.get(i);
            week=getWeek(day);
            if (week==6||week==0) {//周六，周日
                num++;
            }
        }
        return num;
    }
    /**
     * 获取某个日期是星期几
     * @param date
     * @param format
     * @return 0-星期日
     * @author zhaigx
     * @date 2013-3-13
     */
    public static int getWeek(Date date) {
        Calendar calendarTemp = Calendar.getInstance();
        try {
            calendarTemp.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = calendarTemp.get(Calendar.DAY_OF_WEEK);
        int value=i-1;//0-星期日
        //        System.out.println(value);
        return value;
    }
    public static void main(String[] args) throws Exception{
        Test test = new  Test();
        //测试当前日期
//        SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd");
//        Date now = simpleDateFormats.parse("2018-05-13");
//        int week=getWeek(now);
//        if (week==6||week==0) {//周六，周日
//            if(week ==6){
//                now  = test.regressesDay(now,-1);
//            }
//            if(week ==0){
//                now = test.regressesDay(now,-2);
//            }
//        }
//        String  nowString  = new SimpleDateFormat("yyyy-MM-dd").format(now);
//        System.out.println( nowString+" 回退后时间");
         long dayTime = 4320000000L;
         int day = (int)(dayTime/(24 * 60 * 60 * 1000));
        System.out.println("day "+day);
//        System.out.println( test.overdueTime(new Date(),"2018-05-21",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-10",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-11",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-12",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-13",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-16",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-17",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-18",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-19",null,10));
//        System.out.println( test.overdueTime(new Date(),"2018-04-20",null,10));
    }
}
