package cs.service.interfaces;

import cs.domain.MonthReport;
import cs.model.DomainDto.MonthReportDto;

public interface MonthReportService extends IService<MonthReportDto, MonthReport, String> {	
	void saveMonthReport(MonthReportDto monthReportDto);
	
	void changeMonthReport(MonthReportDto monthReportDto);
}
