package cs.common.utils;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 工作日工具类
 * @author Administrator
 *
 */
public class WorkDayUtil {
    private static Logger log = Logger.getLogger(WorkDayUtil.class);

    public static void main(String[] args) throws Exception {
        /*SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        //加班集合
        Map holidayMapEnable = new HashMap();
        holidayMapEnable.put("2018-10-13",null);
        holidayMapEnable.put("2018-10-14",null);

        //节假日集合
        Map holidayMapDisEnable = new HashMap();
        holidayMapDisEnable.put("2018-10-01", null);
        holidayMapDisEnable.put("2018-10-02", null);
        holidayMapDisEnable.put("2018-10-03", null);
        holidayMapDisEnable.put("2018-10-04", null);
        holidayMapDisEnable.put("2018-10-05", null);
        holidayMapDisEnable.put("2018-10-06", null);
        holidayMapDisEnable.put("2018-10-07", null);

        Date start = sdf1.parse("2018-09-24 10:20:00");
        Date end = sdf1.parse("2018-10-15 15:30:00");
        int[] a = getUseWorkDayTime(start,end,holidayMapEnable,holidayMapDisEnable,9,18);

        System.out.println("已用工作日:"+a[0]+"天,"+a[1]+"小时,"+a[2]+"分钟,"+a[3]+"秒");*/

        int[] a = {5,5,39,55};
        int[] b = {4,3,20,32};
        getAddWorkDay(a,b,9);

    }

    /**
     * @param source
     * @param type
     * @return
     */
    public static String getStringByIntList(int[] source,int type){
        int day = source[0];
        int hour = source[1];
        int minutes = source[2];
        int second = source[3];
        String result = null;

        switch (type){
            case 0:
                if(hour>0) day = day + 1;
                result = day + "天";
                break;
            case 1:
                result = day +"天"+hour+"时"+minutes+"分"+second+"秒";
                break;
        }
        return result;
    }

    /**
     * 计算工作日
     * @param start   起始日期
     * @param end     结束日期
     * @param holidayMapEnable     特殊日期加班时间
     * @param holidayMapDisEnable  节假日
     * @param upHour               上班日期
     * @param downHour             下班日期
     * @return
     * @throws ParseException
     */
    public static int[] getUseWorkDayTime (
            Date start
            ,Date end
            ,Map holidayMapEnable
            ,Map holidayMapDisEnable
            ,int upHour
            ,int downHour) throws ParseException{

        if(start == null){
            throw new NullPointerException("开始日期为空！方法名：getUseWorkDayTime");
        }
        if(end == null){
            throw new NullPointerException("结束日期为空！方法名：getUseWorkDayTime");
        }

        //获取起始日期和结束日期之间的工作日总量
        Map workdayResult = getworkDay(start,end,holidayMapEnable,holidayMapDisEnable);

        //工作日总量,不算起始和结束日期
        int workday = workdayResult.get("workDay") != null ? Integer.parseInt(String.valueOf(workdayResult.get("workDay"))) : 0;
        //起始日期是否为工作日
        boolean isStartWorkDay = workdayResult.get("isStartWorkDay")!= null ? Boolean.parseBoolean(String.valueOf(workdayResult.get("isStartWorkDay"))) : false;
        //结束日期是否为工作日
        boolean isEndWorkDay = workdayResult.get("isEndWorkDay")!= null ? Boolean.parseBoolean(String.valueOf(workdayResult.get("isEndWorkDay"))) : false;
        //获取起始日期和结束日期是否为同一天
        boolean isSameStartAndEnd = workdayResult.get("isSameStartAndEnd")!= null ? Boolean.parseBoolean(String.valueOf(workdayResult.get("isSameStartAndEnd"))) : false;

        //接收计算结果
        int[] allMills = {0,0,0,0};

        //起始日期和结束日期为同一天,并且为工作日isEndWorkDay
        if(isStartWorkDay && isEndWorkDay && isSameStartAndEnd){
            //workday = workday + 1;   //测试使用
        //结束日期比起始日期多一天以上
        }else if(!isSameStartAndEnd){
            long startMills = 0l;
            long endMills = 0l;

            //为工作日，则去结算毫秒数
            if(isStartWorkDay){
                startMills = getDatePoor(start,upHour,downHour,1);
            }
            if(isEndWorkDay){
                endMills = getDatePoor(end,upHour,downHour,2);
            }
            allMills = formatDuring(startMills+endMills,downHour-upHour);
        }
        //起始日期和结束日期共用的工作时间和其他时间段的时期相加
        int temp = allMills[0];
        allMills[0] = temp + workday;
        return allMills;
    }

    /**
     * 获取目标时间在 9点-18点用的时间
     * @param target   计算目标时间
     * @param upHour   上班时间
     * @param downHour 下班时间
     * @param flag 1 用于标识下班时间减去目标小时(18点-目标小时),2 用于标识目标小时减去上班时间(目标小时-9点)
     * @return
     */
    public static long getDatePoor(Date target
            ,int upHour
            ,int downHour
            ,int flag) {
        //一天工作时间
        int workTime = downHour - upHour;

        Calendar targetCl = Calendar.getInstance();
        targetCl.setTime(target);

        //初始化上班时间为早上9点
        Calendar upCl = Calendar.getInstance();
        upCl.setTime(target);
        upCl.set(Calendar.HOUR_OF_DAY, upHour);
        upCl.set(Calendar.MINUTE, 0);
        upCl.set(Calendar.SECOND, 0);
        //初始化上班时间下午18点
        Calendar downCl = Calendar.getInstance();
        downCl.setTime(target);
        downCl.set(Calendar.HOUR_OF_DAY, downHour);
        downCl.set(Calendar.MINUTE, 0);
        downCl.set(Calendar.SECOND, 0);

        // 1 为计算起始日期时间, 用下班时间 - 起始时间
        if(flag  == 1){
            //起始日期在9点上班之前，算一天工作日
            if(targetCl.before(upCl)){
                return 1000 * 60 * 60 * workTime;
            }
            //起始日期在18点下班以后，不算时间
            if(targetCl.after(downCl)){
                return 0;
            }
            return getDiffTimeInMillisByStartAndEnd(downCl.getTime(),targetCl.getTime());
        // 2 为计算结束日期时间,用结束时间 - 上班时间
        }else{
            //结束日期在9点上班之前，不算时间
            if(targetCl.before(upCl)){
                return 0;
            }
            //结束日期在18点下班以后，算一天工作日
            if(targetCl.after(downCl)){
                return 1000 * 60 * 60 * workTime;
            }
            return getDiffTimeInMillisByStartAndEnd(upCl.getTime(),targetCl.getTime());
        }
    }



    public static Map getworkDay(Date start,Date end,Map holidayMapEnable,Map holidayMapDisEnable){
        //开始日期是否为工作日
        boolean isStartWorkDay = false;
        //结束日期是否为工作日
        boolean isEndWorkDay = false;
        //开始日期和结束日期是否为同一天
        boolean isSameStartAndEnd = false;
        //起始日期和结束日期之间的工作日
        int workDay = 0;

        //返回结果:工作日总数/起始日期是否为工作日/结束日期是否为工作日
        Map map = new HashMap();

        //获取两个日期之间的天数
        int minusDay = 0;
        //初始化日期的天数,为计算方便
        Calendar cl1 = init(start);
        Calendar cl2 = init(end);

        int cl1_day = cl1.get(Calendar.DATE);
        int cl2_day = cl2.get(Calendar.DATE);
        try {
            //起始日期是否为工作日
            isStartWorkDay = checkHoliday(cl1,holidayMapEnable,holidayMapDisEnable);
            //结束日期是否为工作日
            isEndWorkDay = checkHoliday(cl2,holidayMapEnable,holidayMapDisEnable);

            if(cl2_day == cl1_day ){
                isSameStartAndEnd = true;
            }

            //如果起始日期和结束日期为同一天或两者就差一天
            if((cl2_day - cl1_day) <= 1){
                workDay = 0;
            }
            //结束日期 > 起始日期1天以上
            else {
                minusDay = daysBetween(cl1.getTime(),cl2.getTime()) - 1; //因为日期差不算开始和结束两天，所以减1
                Calendar cl = Calendar.getInstance();
                cl.setTime(start);
                //遍历在起始日期和结束日期中间的天数
                for (int i = 0; i < minusDay; i++) {
                    cl.add(Calendar.DATE,1);
                    boolean isWorkDay = checkHoliday(cl,holidayMapEnable,holidayMapDisEnable);
                    //计算起始日期和结束日期之间的工作日
                    if(isWorkDay){
                        workDay++;
                    }
                }
            }
        } catch (ParseException e) {
            log.debug("计算工作日出错，方法名：getworkDay ===>>>>>> "+e.getMessage());
        }
        map.put("workDay",workDay);
        map.put("isSameStartAndEnd",isSameStartAndEnd);
        map.put("isStartWorkDay",isStartWorkDay);
        map.put("isEndWorkDay",isEndWorkDay);
        return map;
    }



    /**
     * 校验日期是否为工作日
     * 1.先校验是否为加班日期
     * 2.校验是否为节假日
     * 3.校验是否为周六日
     * @param calendar  校验日期
     * @param holidayMapEnable    加班日期集合
     * @param holidayMapDisEnable 节假日集合
     * @return
     * @throws Exception
     */
    public static boolean checkHoliday(Calendar calendar,Map holidayMapEnable,Map holidayMapDisEnable) throws ParseException{
        String targetDay = getFormatDateTime("yyyy-MM-dd",calendar.getTime());
        //优先判断是否为加班日期
        boolean isaddworkday = holidayMapEnable.containsKey(targetDay);
        if(isaddworkday){
            return true;
        }
        //再判断是否为节假日
        boolean isholiday =  holidayMapDisEnable.containsKey(targetDay);
        if(isholiday){
            return false;
        }
        //最后判断日期是否是周六周日
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return false;
        }
        return true;
    }

    /**
     * 计算两个数组时间的和
     * @param source
     * @param targetDay
     * @return
     */
    public static int[] getAddWorkDay(int[] source,int[] targetDay,int workDay){
        int d0 = source[0] + targetDay[0];
        int h0 = source[1] + targetDay[1];
        int m0 = source[2] + targetDay[2];
        int s0 = source[3] + targetDay[3];

        if(s0 >= 60){
            m0 = m0+1;
            s0 = s0 - 60;
        }
        if(m0 >= 60){
            h0 = h0+1;
            m0 = m0-60;
        }
        if(h0 >= workDay){
            d0 = d0+1;
            h0 = h0-workDay;
        }

        int[] result = {d0,h0,m0,s0};
        log.debug("天："+d0+"小时："+h0+"分钟："+m0+"秒："+s0);
        // TODO: 2018/9/14 部署生产环境时，删除
        System.out.println("天："+d0+"小时："+h0+"分钟："+m0+"秒："+s0);
        return result;
    }

    /**
     * 总时间-已用时间=剩余时间
     * @param source     已用时间
     * @param targetDay  总时间
     * @return
     */
    public static int[] getDiffWorkDay(int[] source,int[] targetDay,int workDay){
        int d0 = source[0];
        int h0 = source[1];
        int m0 = source[2];
        int s0 = source[3];
        //如果有source 为0，则直接返回targetDay
        if(d0 == 0 && h0 ==0 && m0 == 0 && s0 ==0){
            return targetDay;
        }
        int[] result = new int[4];
        Calendar clSource = init(d0,h0,m0,s0);
        Calendar clTarget = init(targetDay[0],targetDay[1],targetDay[2],targetDay[3]);
        //如果source > targetDay,则返回0
        if(clTarget.before(clSource)){
            return result;
        }
        long l =  getDiffTimeInMillisByStartAndEnd(clSource.getTime(),clTarget.getTime());
        return formatMills(l,workDay);
    }

    public  static  Calendar  init(int day,int hour,int minutes,int second){
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.DATE,day);
        cl.set(Calendar.HOUR,hour);
        cl.set(Calendar.MINUTE,minutes);
        cl.set(Calendar.SECOND,second);
        return cl;
    }

    public  static Calendar init(Date sourceDate){
        Calendar cl1 = Calendar.getInstance();
        cl1.setTime(sourceDate);
        cl1.set(Calendar.HOUR,0);
        cl1.set(Calendar.MINUTE,0);
        cl1.set(Calendar.SECOND,0);
        return cl1;
    }


    /**
     * 计算两个日期之间相差的天数
     * @param startDate 较小的时间
     * @param endDate   较大的时间
     * @return 相差天数
     * @throws ParseException
     * calendar 对日期进行时间操作
     * getTimeInMillis() 获取日期的毫秒显示形式
     */
    public static int daysBetween(Date startDate,Date endDate) throws ParseException    {
        long between_days=getDiffTimeInMillisByStartAndEnd(startDate,endDate)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 计算两个日期之间相差的毫秒数
     * @param startDate 较小的时间
     * @param endDate   较大的时间
     * @return long  毫秒数
     * @throws ParseException
     * calendar 对日期进行时间操作
     * getTimeInMillis() 获取日期的毫秒显示形式
     */
    public static long  getDiffTimeInMillisByStartAndEnd(Date startDate,Date endDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endDate);
        long time2 = cal.getTimeInMillis();
        if(time1 > time2){
            return time1-time2;
        }
        return time2-time1;
    }

    /**
     * 计算两个日期之间相加的毫秒数
     * @param startDate 较小的时间
     * @param endDate   较大的时间
     * @return long  毫秒数
     * @throws ParseException
     * calendar 对日期进行时间操作
     * getTimeInMillis() 获取日期的毫秒显示形式
     */
    public static long getAddTimeInMillisByStartAndEnd(Date startDate,Date endDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endDate);
        long time2 = cal.getTimeInMillis();
        return time2+time1;
    }


    /**
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static int[] formatDuring(long mss,int workTime) {
        int[] time = new int[4];
        long days = mss / (1000 * 60 * 60 * workTime);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

        //9小时算一天工作日,获取装换为工作日后剩余的小时
        hours = (hours - (days* workTime));

        time[0] = Integer.parseInt(String.valueOf(days));
        time[1] = Integer.parseInt(String.valueOf(hours));
        time[2] = Integer.parseInt(String.valueOf(minutes));
        time[3] = Integer.parseInt(String.valueOf(seconds));

        log.debug( time[0] + " days " + time[1] + " hours " + time[2] + " minutes "
                + time[3] + " seconds ");
        // TODO: 2018/9/14 部署生产环境时，删除
        System.out.println( time[0] + " days " + time[1] + " hours " + time[2] + " minutes "
        		+ time[3] + " seconds ");
        return time;
    }

    /**
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static int[] formatMills(long mss,int workDay) {
        int[] time = new int[4];
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

        //9小时算一天工作日,所以用24-15获得工作日时间。
        int diffDay = 24 - workDay;
        if(hours > diffDay){
            hours = hours - diffDay;
        }else{
            hours = diffDay - hours;
        }

        time[0] = Integer.parseInt(String.valueOf(days));
        time[1] = Integer.parseInt(String.valueOf(hours));
        time[2] = Integer.parseInt(String.valueOf(minutes));
        time[3] = Integer.parseInt(String.valueOf(seconds));


        log.debug( time[0] + " days " + time[1] + " hours " + time[2] + " minutes "
                + time[3] + " seconds ");
        // TODO: 2018/9/14 部署生产环境时，删除
        System.out.println( time[0] + " days " + time[1] + " hours " + time[2] + " minutes "
        		+ time[3] + " seconds ");
        return time;
    }

    /**
     * 将时间格式化为yyyy-MM-dd HH:mm:ss.S
     *
     * @param aMask
     * @param aDate
     * @return
     */
    public static String getFormatDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate == null) {
            return returnValue;
        } else {
            try {
                df = new SimpleDateFormat(aMask);
                returnValue = df.format(aDate);
            } catch (Exception e) {
                log.error("时间格式转换出错 " + e.getMessage());
            }
        }
        return returnValue;
    }
}
