package cs.model.exportExcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
/**
* @ClassName: ExcelReportLBTJView 
* @Description: 年度计划编制导出项目类别统计Excel设计 
* @author cx
* @date 2017年9月6日 下午4:22:06 
*
 */
public class ExcelReportLBTJView extends AbstractXlsView {
	private int year;
	public ExcelReportLBTJView(int year){
		this.year=year;
	}
	
	@Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment;filename=\"yearPlanByCategory.xls\"");
        Sheet sheet = workbook.createSheet("表1");
        //创建行
        Row title=sheet.createRow(0);
        Row row_head = sheet.createRow(1);
      
        //begin#标题
        //创建列
        createCellAlignCenter(workbook,title,0,"光明新区"+year+"年区级政府投资项目计划汇总表");
        //合并标题
        //参数1：开始行、结束行、开始列、结束列
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
        //end#标题

        //begin表格头
        createCellAlignCenter(workbook,row_head,0,"序号");
        createCellAlignCenter(workbook,row_head,1,"类别");
        createCellAlignCenter(workbook,row_head,2,"建设项目数量");
        createCellAlignCenter(workbook,row_head,3,"总投资金额");
        createCellAlignCenter(workbook,row_head,4,"累计拨付资金");
        createCellAlignCenter(workbook,row_head,5,"累计下达计划");
        createCellAlignCenter(workbook,row_head,6,"年度预算安排资金（单位：万元）");
        //end#表格头

        //begin#数据列
        int rowNum=2;//从第三行开始
        int index=1;
        Integer projectSum=0;
        Double investSum=0.0;
        Double investAccuSum=0.0;
        Double apInvestSum=0.0;
        Double yearInvestApprovalSum=0.0;
        
        List<ExcelDataLBTJ> excelDataLBTJList = (List<ExcelDataLBTJ>) model.get("excelDataLBTJList");
        for (ExcelDataLBTJ data:excelDataLBTJList) {
            Row row = sheet.createRow(rowNum);
            //创建数据
            createCellAlignCenter(workbook,row,0, index);//序号
            createCellAlignCenter(workbook,row,1, data.getProjectCategory());//项目类别
            createCellAlignCenter(workbook,row,2, data.getProjectSum());//项目总数
            createCellAlignCenter(workbook,row,3, data.getInvestSum());//总投资
            createCellAlignCenter(workbook,row,4, data.getInvestAccuSum());//累计拨付
            createCellAlignCenter(workbook,row,5, data.getApInvestSum());//累计下达
            createCellAlignCenter(workbook,row,6, data.getYearInvestApprovalSum());//年度预安排
            
            projectSum +=data.getProjectSum();
            investSum += data.getInvestSum();
            investAccuSum += data.getInvestAccuSum();
            apInvestSum += data.getApInvestSum();
            yearInvestApprovalSum += data.getYearInvestApprovalSum();
            
            rowNum++;index++;
        }
        //end#数据列
        
        //数据合计列
        Row row = sheet.createRow(rowNum);
        createCellAlignCenter(workbook,row,0, "合计");
        createCellAlignCenter(workbook,row,2, projectSum);//项目总数
        createCellAlignCenter(workbook,row,3, investSum);//总投资
        createCellAlignCenter(workbook,row,4, investAccuSum);//累计下达
        createCellAlignCenter(workbook,row,5, apInvestSum);//累计拨付
        createCellAlignCenter(workbook,row,6, yearInvestApprovalSum);//年度预安排
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,1));

    }
    private void createCellAlignCenter(Workbook workbook,Row row, int cellNumber,String value){
        createCell(workbook,row,cellNumber,value,CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_CENTER);
    }
    private void createCellAlignCenter(Workbook workbook,Row row, int cellNumber,double value){
        createCell(workbook,row,cellNumber,value,CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_CENTER);
    }
    private void createCellAlignLeft(Workbook workbook,Row row, int cellNumber,String value){
        createCell(workbook,row,cellNumber,value,CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER);
    }
    private void createCellAlignRight(Workbook workbook,Row row, int cellNumber,String value){
        createCell(workbook,row,cellNumber,value,CellStyle.ALIGN_RIGHT,CellStyle.VERTICAL_CENTER);
    }
    private void createCell(Workbook workbook,Row row, int cellNumber,String value, short halign, short valign){
        Cell cell=row.createCell(cellNumber);
        cell.setCellValue(value);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setWrapText(true);
        cell.setCellStyle(cellStyle);
    }
  //重写创建列
    private void createCell(Workbook workbook,Row row, int cellNumber,double value, short halign, short valign){
        Cell cell=row.createCell(cellNumber);
        cell.setCellValue(value);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setWrapText(true);
        cell.setCellStyle(cellStyle);
    }
}
