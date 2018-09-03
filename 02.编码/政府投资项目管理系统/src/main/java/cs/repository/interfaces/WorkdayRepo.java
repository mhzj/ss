package cs.repository.interfaces;

import cs.domain.Workday;

import java.util.Date;
import java.util.List;

public interface WorkdayRepo extends IRepository<Workday, String>{

    boolean isExist(Date days);

    /**
     * 查询从当前日期开始，往前两年内的记录
     * @return
     */
    List<Workday> findWorkDay(String beginTime, String endTime);

    /**
     * 通过时间段获取
     * @param startDate
     * @return
     */
    List<Workday> getBetweenTimeDay(Date startDate, Date endDate);


    boolean isExistWorkDay(Date days, String temp);
}

