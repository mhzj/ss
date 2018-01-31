package cs.model.Statistics.view;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import cs.model.Statistics.ProjectStatisticsBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: ApprovalStatisticsView 
 * @Description: 审批类导出Excel页面设计类
 * @author cx
 * @date 2018年1月26日 下午4:45:34 
 *
 */
public class ApprovalStatisticsView extends AbstractXlsView {
	private String type;
	private int approvalYear;
	public ApprovalStatisticsView(String type,int approvalYear){
		this.type=type;
		this.approvalYear=approvalYear; 
	}
	
	@SuppressWarnings("deprecation")
	@Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String typeDesc=type.equals("approval")?"审批阶段":type.equals("industry")?"项目行业":type.equals("unit")?"申报单位":"";
	
		String fileName = "光明新区政府投资审批类"+approvalYear+"年"+typeDesc+"分类汇总表.xls";
        response.setHeader("Content-Disposition", "attachment;filename=" +new String(fileName.getBytes("gb2312"), "iso8859-1"));
        Sheet sheet = workbook.createSheet("表1");
        
        CellStyle cellStyleTitle = workbook.createCellStyle();
        CellStyle cellStyleSubTitleLeft = workbook.createCellStyle();
        CellStyle cellStyleO = workbook.createCellStyle();

        //设置字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14); // 字体高度
        font.setFontName(" 黑体 "); // 字体
        cellStyleTitle.setFont(font);
        //设置表格边框
        cellStyleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleSubTitleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleSubTitleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleSubTitleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleSubTitleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleO.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleO.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleO.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleO.setBorderLeft(HSSFCellStyle.BORDER_THIN);
       
        //创建行
        Row title=sheet.createRow(0);
        Row subTitle=sheet.createRow(1);
        Row row_head = sheet.createRow(2);
        
        //设置行高
        title.setHeight((short)800);
        subTitle.setHeight((short)500);
        row_head.setHeight((short)360);
        
        //begin#标题
        //创建列
        createCellAlignCenter(workbook,title,0,"光明新区政府投资审批类"+approvalYear+"年"+typeDesc+"分类汇总表",cellStyleTitle);
        //合并标题
        //参数1：开始行、结束行、开始列、结束列
        CellRangeAddress cellRangeTitle = null;
        cellRangeTitle = type.equals("approval")?new CellRangeAddress(0,0,0,5):type.equals("industry")?new CellRangeAddress(0,0,0,3):new CellRangeAddress(0,0,0,6);
        setRegionStyle(sheet,cellRangeTitle,cellStyleTitle);
        sheet.addMergedRegion(cellRangeTitle);
        //end#标题
        
        //begin#子标题
        createCellAlignLeft(workbook,subTitle,0,"打印日期："+new SimpleDateFormat("yyyy年MM月dd日").format(new Date()),cellStyleSubTitleLeft);
        CellRangeAddress cellRangeSubTitleLeft = null;
        if(type.equals("approval")){
        	 //设置子标题列宽
            sheet.setColumnWidth(1, 256*18+184);
            sheet.setColumnWidth(2, 256*15+184);
            sheet.setColumnWidth(3, 256*15+184);
            sheet.setColumnWidth(4, 256*15+184);
            sheet.setColumnWidth(5, 256*15+184);
            
        	createCellAlignRight(workbook,subTitle,5,"资金：万   元\n面积：平方米",workbook.createCellStyle());
        	cellRangeSubTitleLeft = new CellRangeAddress(1,1,0,4);
        }else if(type.equals("industry")){
        	 //设置子标题列宽
            sheet.setColumnWidth(1, 256*26+184);
            sheet.setColumnWidth(2, 256*26+184);
            sheet.setColumnWidth(3, 256*26+184);

        	createCellAlignRight(workbook,subTitle,3,"资金：万   元\n面积：平方米",workbook.createCellStyle());
        	cellRangeSubTitleLeft = new CellRangeAddress(1,1,0,2);
        }else if(type.equals("unit")){
        	 //设置子标题列宽
        	sheet.setColumnWidth(0, 256*6+184);
            sheet.setColumnWidth(1, 256*19+184);
            sheet.setColumnWidth(2, 256*11+184);
            sheet.setColumnWidth(3, 256*14+184);
            sheet.setColumnWidth(4, 256*12+184);
            sheet.setColumnWidth(5, 256*12+184);
            sheet.setColumnWidth(6, 256*12+184);
            
            createCellAlignRight(workbook,subTitle,6,"资金：万   元\n面积：平方米",workbook.createCellStyle());
        	cellRangeSubTitleLeft = new CellRangeAddress(1,1,0,5);
        }
        setRegionStyle(sheet,cellRangeSubTitleLeft,cellStyleSubTitleLeft);
        sheet.addMergedRegion(cellRangeSubTitleLeft);
        //end#子标题

        //begin表格头
        createCellAlignCenter(workbook,row_head,0,"序号",cellStyleO);
        if(type.equals("approval")){
        	createCellAlignCenter(workbook,row_head,1,"审批阶段",cellStyleO);
        	createCellAlignCenter(workbook,row_head,2,"项目数量",cellStyleO);
        	createCellAlignCenter(workbook,row_head,3,"申报投资",cellStyleO);
        	createCellAlignCenter(workbook,row_head,4,"总投资",cellStyleO);
        	createCellAlignCenter(workbook,row_head,5,"时限",cellStyleO);
        }else if(type.equals("industry")){
        	createCellAlignCenter(workbook,row_head,1,"项目行业",cellStyleO);
        	createCellAlignCenter(workbook,row_head,2,"项目数量",cellStyleO);
        	createCellAlignCenter(workbook,row_head,3,"总投资",cellStyleO);
        }else if(type.equals("unit")){
        	Row row_subHead = sheet.createRow(3);
        	row_subHead.setHeight((short)400);
        	createCellAlignCenter(workbook,row_head,1,"项目单位",cellStyleO);
        	createCellAlignCenter(workbook,row_head,2,"项目数量",cellStyleO);
        	createCellAlignCenter(workbook,row_subHead,2,"项目建议书",cellStyleO);
        	createCellAlignCenter(workbook,row_subHead,3,"可行性研究报告",cellStyleO);
        	createCellAlignCenter(workbook,row_subHead,4,"初步设计概算",cellStyleO);
        	createCellAlignCenter(workbook,row_subHead,5,"资金申请报告",cellStyleO);
        	createCellAlignCenter(workbook,row_head,6,"总投资",cellStyleO);
        	
        	CellRangeAddress cellRange0 = new CellRangeAddress(2,3,0,0);
        	CellRangeAddress cellRange1 = new CellRangeAddress(2,3,1,1);
        	CellRangeAddress cellRange2 = new CellRangeAddress(2,2,2,5);
        	CellRangeAddress cellRange3 = new CellRangeAddress(2,3,6,6);
        	
        	setRegionStyle(sheet,cellRange0,cellStyleO);
        	setRegionStyle(sheet,cellRange1,cellStyleO);
        	setRegionStyle(sheet,cellRange2,cellStyleO);
        	setRegionStyle(sheet,cellRange3,cellStyleO);
        	
        	sheet.addMergedRegion(cellRange0);
            sheet.addMergedRegion(cellRange1);
            sheet.addMergedRegion(cellRange2);
            sheet.addMergedRegion(cellRange3);
        }
        else{
        	createCellAlignCenter(workbook,row_head,1,typeDesc,cellStyleO);
        	createCellAlignCenter(workbook,row_head,2,"项目数量",cellStyleO);
        	createCellAlignCenter(workbook,row_head,3,"项目总投资",cellStyleO);
        }
        //end#表格头

        //begin#数据列
        int rowNum=type.equals("unit")?4:3;//数据加载开始行
        int index=1;
  
        
        @SuppressWarnings("unchecked")
		List<ProjectStatisticsBean> data = (List<ProjectStatisticsBean>) model.get("data");
        for (ProjectStatisticsBean obj:data) {
            Row row = sheet.createRow(rowNum);
            //创建数据
            createCellAlignCenter(workbook,row,0, index,cellStyleO);//序号
            createCellAlignCenter(workbook,row,1, obj.getClassDesc(),cellStyleO);//项目统计分类
            if(type.equals("approval")){
            	createCellAlignCenter(workbook,row,2, obj.getProjectNumbers(),cellStyleO);//项目数量
            	createCellAlignCenter(workbook,row,3, "-",cellStyleO);//申报投资
            	createCellAlignCenter(workbook,row,4, obj.getProjectInvestSum(),cellStyleO);//总投资
            	createCellAlignCenter(workbook,row,5, "-",cellStyleO);//时限
            }else if(type.equals("industry")){
            	createCellAlignCenter(workbook,row,2, obj.getProjectNumbers(),cellStyleO);//项目数量
            	createCellAlignCenter(workbook,row,3, obj.getProjectInvestSum(),cellStyleO);//总投资
            }else if(type.equals("unit")){
            	createCellAlignCenter(workbook,row,2, obj.getApprovalStageXMJYSNumbers(),cellStyleO);//项目建议书数
            	createCellAlignCenter(workbook,row,3, obj.getApprovalStageKXXYJBGNumbers(),cellStyleO);//可行性研究报告数
            	createCellAlignCenter(workbook,row,4, obj.getApprovalStageCBSJGSNumbers(),cellStyleO);//初步设计概算数
            	createCellAlignCenter(workbook,row,5, obj.getApprovalStageZJSQBGNumbers(),cellStyleO);//资金申请报告数
            	createCellAlignCenter(workbook,row,6, obj.getProjectInvestSum(),cellStyleO);//总投资数
            }
            rowNum++;index++;
        }
        //end#数据列
    }
//创建值为string字体居中的单元格	
    @SuppressWarnings("deprecation")
	private void createCellAlignCenter(Workbook workbook,Row row, int cellNumber,String value,CellStyle cellStyle){
    	//设置边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        createCell(workbook,row,cellNumber,value,CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_CENTER,cellStyle);
    }
    @SuppressWarnings("deprecation")
//创建值为double字体居中的单元格	
    private void createCellAlignCenter(Workbook workbook,Row row, int cellNumber,double value,CellStyle cellStyle){
    	//设置边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        createCell(workbook,row,cellNumber,value,CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_CENTER,cellStyle);
    }
    @SuppressWarnings("deprecation")
//创建值为string字体居左的单元格
    private void createCellAlignLeft(Workbook workbook,Row row, int cellNumber,String value,CellStyle cellStyle){
    	//设置边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        createCell(workbook,row,cellNumber,value,CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER,cellStyle);
    }
    @SuppressWarnings("deprecation")
//创建值为string字体居右的单元格    
    private void createCellAlignRight(Workbook workbook,Row row, int cellNumber,String value,CellStyle cellStyle){
    	//设置边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        createCell(workbook,row,cellNumber,value,CellStyle.ALIGN_RIGHT,CellStyle.VERTICAL_CENTER,cellStyle);
    }
    @SuppressWarnings("deprecation")
//重写创建列
    private void createCell(Workbook workbook,Row row, int cellNumber,String value, short halign, short valign,CellStyle cellStyle){
        Cell cell=row.createCell(cellNumber);
        cell.setCellValue(value);

        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setWrapText(true);
      //设置边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cell.setCellStyle(cellStyle);
    }
    @SuppressWarnings("deprecation")
  //重写创建列
    private void createCell(Workbook workbook,Row row, int cellNumber,double value, short halign, short valign,CellStyle cellStyle){
        Cell cell=row.createCell(cellNumber);// 创建单元格
        cell.setCellValue(value);// 设置值

        cellStyle.setAlignment(halign);// 设置单元格水平方向对齐方式
        cellStyle.setVerticalAlignment(valign);// 设置单元格垂直方向对齐方式
        cellStyle.setWrapText(true);
        //设置边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cell.setCellStyle(cellStyle);
    }

    
    /**
     * 
     * @Title: setRegionStyle 
     * @Description: 设置单元格样式
     * @param sheet 当前表
     * @param region 合并列
     * @param cs  样式
     */
    public static void setRegionStyle(Sheet sheet, CellRangeAddress region, CellStyle cs) {
    	for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            HSSFRow row = (HSSFRow) sheet.getRow(i);
            if (row == null)
                row = (HSSFRow) sheet.createRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                }
                cell.setCellStyle(cs);
            }
    	}
    }
}
