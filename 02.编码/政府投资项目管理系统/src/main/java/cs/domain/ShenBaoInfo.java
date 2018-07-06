package cs.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cs.common.BasicDataConfig;
/**
 * @Description: 申报信息表
 * @author: cx
 * @Date：2017年7月10日
 * @version：0.1
 */
@Entity
@Table(name="cs_shenBaoInfo")
public class ShenBaoInfo extends BaseProject{
	@Id
	private String id;
	
	//begin#与项目相关
	@Column(columnDefinition="varchar(255) NULL COMMENT '项目ID'")
	private String projectId;
	
	@Column(columnDefinition="bit(1) DEFAULT b'0' COMMENT '项目是否纳入项目库'")
	private Boolean isIncludLibrary=false;
	
	@Column(columnDefinition="bit(1) DEFAULT b'0' COMMENT '项目是否纳入打包计划'")
	private Boolean isIncludPack=false;

	@Column(columnDefinition="varchar(125) NULL COMMENT '申报阶段'")
	private String projectShenBaoStage;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="datetime COMMENT '申报时间'")
	private Date shenbaoDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="datetime COMMENT '签收时间'")
	private Date qianshouDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="datetime COMMENT '批复时间'")
	private Date pifuDate;
	
	@Column(columnDefinition="int NULL COMMENT '计划年度'")
	private Integer planYear;
	
	//begin#年度计划相关
	@Column(columnDefinition="varchar(500) NULL COMMENT '建设单位'")
	private String constructionUnit;
	@Column(columnDefinition="varchar(500) NULL COMMENT '年度建设任务'")
	private String yearConstructionTask;
	@Column(columnDefinition="varchar(255) NULL COMMENT '安排年度投资Id'")
	private String yearPlanCapitalId;
	@Column(columnDefinition="varchar(125) NULL COMMENT '打包类型'")
	private String packageType=BasicDataConfig.packageType_danLie;//默认为单列项目
	@Column(columnDefinition="bit(1) DEFAULT b'0' COMMENT '是否需要申请指标外资金'")
	private Boolean isApplyOutsideCapital=false;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '申请指标外资金'")
	private Double applyOutsideCapital=0.0;
	
	//下一年度计划 Begin（三年滚动计划）
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第一年申请年度投资累计'")
	private Double applyYearInvest=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第一年资金筹措方案(申请)-公共预算'")
	private Double capitalSCZ_ggys_TheYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第一年资金筹措方案(申请)-国土资金'")
	private Double capitalSCZ_gtzj_TheYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第一年资金筹措方案(申请)-其他资金'")
	private Double capitalSCZ_qita=0.0;
	@Column(columnDefinition="varchar(500) NULL COMMENT '第一年其他资金来源'")
	private String capitalOtherDescriptionShenBao;
	
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第二年申请年度投资累计'")
	private Double applyYearInvest_LastYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第二年度资金筹措方案(申请)-公共预算'")
	private Double capitalSCZ_ggys_LastYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第二年度资金筹措方案(申请)-国土资金'")
	private Double capitalSCZ_gtzj_LastYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第二年资金筹措方案(申请)-其他资金'")
	private Double capitalSCZ_qita_LastYear=0.0;
	@Column(columnDefinition="varchar(500) NULL COMMENT '第二年其他资金来源'")
	private String capitalOtherDescriptionShenBao_LastYear;
	
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第三年申请年度投资累计'")
	private Double applyYearInvest_LastTwoYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第三年度资金筹措方案(申请)-公共预算'")
	private Double capitalSCZ_ggys_LastTwoYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第三年度资金筹措方案(申请)-国土资金'")
	private Double capitalSCZ_gtzj_LastTwoYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第三年度资金筹措方案(申请)-其他资金'")
	private Double capitalSCZ_qita_LastTwoYear=0.0;
	@Column(columnDefinition="varchar(500) NULL COMMENT '第三年其他资金来源'")
	private String capitalOtherDescriptionShenBao_LastTwoYear;
	
	@Column(columnDefinition="varchar(500) NULL COMMENT '第一年度建设内容'")
	private String yearConstructionContent;
	@Column(columnDefinition="varchar(500) NULL COMMENT '第二年度建设内容'")
	private String yearConstructionContentLastYear;
	@Column(columnDefinition="varchar(500) NULL COMMENT '第三年度建设内容'")
	private String yearConstructionContentLastTwoYear;
	
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第一年安排年度投资累计'")
	private Double yearInvestApproval=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第一年安排年度资金筹措方案-公共预算'")
	private Double capitalAP_ggys_TheYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第一年安排年度资金筹措方案-国土基金'")
	private Double capitalAP_gtzj_TheYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第一年安排年度资金筹措方案-其他'")
	private Double capitalAP_qita=0.0;
	
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第二年安排年度投资累计'")
	private Double yearInvestApproval_lastYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第二年度安排年度资金筹措方案-公共预算'")
	private Double capitalAP_ggys_LastYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第二年度安排年度资金筹措方案-国土基金'")
	private Double capitalAP_gtzj_LastYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第二年度安排年度资金筹措方案-其他'")
	private Double capitalAP_qita_LastYear=0.0;
	
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第三年安排年度投资累计'")
	private Double yearInvestApproval_lastTwoYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第三年度安排年度资金筹措方案-公共预算'")
	private Double capitalAP_ggys_LastTwoYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第三年度安排年度资金筹措方案-国土基金'")
	private Double capitalAP_gtzj_LastTwoYear=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '第三年度安排年度资金筹措方案-其他'")
	private Double capitalAP_qita_LastTwoYear=0.0;
	
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '累计安排投资'")
	private Double apInvestSum=0.0;
	//begin#财政相关
	@Column(columnDefinition="varchar(125) NULL COMMENT '功能科目'")
	private String functionSubjects;
	@Column(columnDefinition="varchar(125) NULL COMMENT '经济分类科目'")
	private String econClassSubjects;
	//end#财政相关
	@Column(columnDefinition="varchar(500) NULL COMMENT '申报信息备注'")
	private String yearConstructionContentShenBao;
	//下一年度计划 End（三年滚动计划）
	
	//计划下达 Begin
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '计划下达申请--公共预算'")
	private Double sqPlanReach_ggys=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '计划下达申请--国土基金'")
	private Double sqPlanReach_gtzj=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '计划下达安排--公共预算'")
	private Double apPlanReach_ggys=0.0;
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '计划下达安排--国土基金'")
	private Double apPlanReach_gtzj=0.0;
	@Column(columnDefinition="varchar(125) NULL COMMENT '项目建设性质分类'")
	private String projectConstrChar;
	//计划下达 End
	
	//begin#审批相关
	@Column(columnDefinition="varchar(125) NULL COMMENT '审批阶段'")
	private String processStage;
	@Column(columnDefinition="int(1) NULL COMMENT '审批状态'")
	private Integer processState;
	@Column(columnDefinition="bit(1) DEFAULT b'0' COMMENT '审批是否完结'")
	private boolean complate;
	@Column(columnDefinition="varchar(125) NULL COMMENT '审批流程ID'")
	private String zong_processId;
	@Column(columnDefinition="varchar(125) NULL COMMENT '审批任务ID'")
	private String thisTaskId;
	@Column(columnDefinition="varchar(125) NULL COMMENT '审批任务名称'")
	private String thisTaskName;
	//end
	
	//begin#在线监管
	@Column(columnDefinition="varchar(125) NULL COMMENT '在线监管流程ID'")
	private String monitor_processId;
	@Column(columnDefinition="bit(1) DEFAULT b'0' COMMENT '是否提交申报附件'")
	private Boolean isSubShenBaoAtt;
	//end
	
	//begin#建议书相关
	@Column(columnDefinition="varchar(500) NULL COMMENT '项目建设必要性和依据'")
	private String projectConstrBasis;
	//end
	
	//begin#可行性研究报告相关
	@Column(columnDefinition="varchar(500) NULL COMMENT '推荐方案介绍'")
	private String recomProgram;
	@Column(columnDefinition="varchar(500) NULL COMMENT '社会及经济效益评价'")
	private String socialAndEconomic;
	//end
	
	//begin#前期计划相关
	@Column(columnDefinition="bit(1) DEFAULT b'0' COMMENT '是否申请前期工作经费'")
	private Boolean isApplyQianQiFei = false;//默认为不申请
	@Column(columnDefinition="double(11,4) DEFAULT 0 COMMENT '工作经费申请金额'")
	private Double qianQiFeiApply=0.0;
	//end
	
	//begin#续建计划相关
	@Column(columnDefinition="varchar(500) NULL COMMENT '上一年形象进度'")
	private String lastYearImageSchedule;
	//end
	
	//begin#竣工决算相关
	@Column(columnDefinition="varchar(500) NULL COMMENT '形象进度'")
	private String yearImageSchedule;

	//begin#审核相关
	@Column(columnDefinition="varchar(125) NULL COMMENT '审核状态'")
	private String auditState;
	@Column(columnDefinition="varchar(255) NULL COMMENT '签收人'")
	private String receiver;
	//end
	
	//begin#社投申报字段
	@Column(columnDefinition="varchar(500) NULL COMMENT '存在的问题'")
	private String existingProblem;
	@Column(columnDefinition="varchar(500) NULL COMMENT '推进建议'")
	private String moveSuggestion;
	//end
	
	//begin#关联信息
	@OneToOne(cascade=CascadeType.ALL)
	private ShenBaoUnitInfo bianZhiUnitInfo=new ShenBaoUnitInfo();
	
	public Double getYearInvestApproval() {
		return yearInvestApproval;
	}
	public void setYearInvestApproval(Double yearInvestApproval) {
		this.yearInvestApproval = yearInvestApproval;
	}
	@OneToOne(cascade=CascadeType.ALL)
	private ShenBaoUnitInfo shenBaoUnitInfo=new ShenBaoUnitInfo();
	@OneToMany(cascade=CascadeType.ALL)
	private List<Attachment> attachments=new ArrayList<>();
	
	
	public Boolean getIsSubShenBaoAtt() {
		return isSubShenBaoAtt;
	}
	public void setIsSubShenBaoAtt(Boolean isSubShenBaoAtt) {
		this.isSubShenBaoAtt = isSubShenBaoAtt;
	}
	public String getMonitor_processId() {
		return monitor_processId;
	}
	public void setMonitor_processId(String monitor_processId) {
		this.monitor_processId = monitor_processId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Boolean getIsIncludLibrary() {
		return isIncludLibrary;
	}
	public void setIsIncludLibrary(Boolean isIncludLibrary) {
		this.isIncludLibrary = isIncludLibrary;
	}
	public String getProjectShenBaoStage() {
		return projectShenBaoStage;
	}
	public void setProjectShenBaoStage(String projectShenBaoStage) {
		this.projectShenBaoStage = projectShenBaoStage;
	}
	public String getProjectConstrChar() {
		return projectConstrChar;
	}
	public void setProjectConstrChar(String projectConstrChar) {
		this.projectConstrChar = projectConstrChar;
	}
	public Integer getPlanYear() {
		return planYear;
	}
	public void setPlanYear(Integer planYear) {
		this.planYear = planYear;
	}
	public String getConstructionUnit() {
		return constructionUnit;
	}
	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}
	public String getYearConstructionTask() {
		return yearConstructionTask;
	}
	public void setYearConstructionTask(String yearConstructionTask) {
		this.yearConstructionTask = yearConstructionTask;
	}
	public String getYearPlanCapitalId() {
		return yearPlanCapitalId;
	}
	public void setYearPlanCapitalId(String yearPlanCapitalId) {
		this.yearPlanCapitalId = yearPlanCapitalId;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public Boolean getIsApplyOutsideCapital() {
		return isApplyOutsideCapital;
	}
	public void setIsApplyOutsideCapital(Boolean isApplyOutsideCapital) {
		this.isApplyOutsideCapital = isApplyOutsideCapital;
	}
	public Double getApplyOutsideCapital() {
		return applyOutsideCapital;
	}
	public void setApplyOutsideCapital(Double applyOutsideCapital) {
		this.applyOutsideCapital = applyOutsideCapital;
	}
	public Double getApplyYearInvest() {
		return applyYearInvest;
	}
	public void setApplyYearInvest(Double applyYearInvest) {
		this.applyYearInvest = applyYearInvest;
	}
	public Double getApplyYearInvest_LastYear() {
		return applyYearInvest_LastYear;
	}
	public void setApplyYearInvest_LastYear(Double applyYearInvest_LastYear) {
		this.applyYearInvest_LastYear = applyYearInvest_LastYear;
	}
	public Double getApplyYearInvest_LastTwoYear() {
		return applyYearInvest_LastTwoYear;
	}
	public void setApplyYearInvest_LastTwoYear(Double applyYearInvest_LastTwoYear) {
		this.applyYearInvest_LastTwoYear = applyYearInvest_LastTwoYear;
	}
	public Double getYearInvestApproval_lastYear() {
		return yearInvestApproval_lastYear;
	}
	public void setYearInvestApproval_lastYear(Double yearInvestApproval_lastYear) {
		this.yearInvestApproval_lastYear = yearInvestApproval_lastYear;
	}
	public Double getYearInvestApproval_lastTwoYear() {
		return yearInvestApproval_lastTwoYear;
	}
	public void setYearInvestApproval_lastTwoYear(Double yearInvestApproval_lastTwoYear) {
		this.yearInvestApproval_lastTwoYear = yearInvestApproval_lastTwoYear;
	}
	public String getYearConstructionContent() {
		return yearConstructionContent;
	}
	public void setYearConstructionContent(String yearConstructionContent) {
		this.yearConstructionContent = yearConstructionContent;
	}
	public String getYearConstructionContentLastYear() {
		return yearConstructionContentLastYear;
	}
	public void setYearConstructionContentLastYear(String yearConstructionContentLastYear) {
		this.yearConstructionContentLastYear = yearConstructionContentLastYear;
	}
	public String getYearConstructionContentLastTwoYear() {
		return yearConstructionContentLastTwoYear;
	}
	public void setYearConstructionContentLastTwoYear(String yearConstructionContentLastTwoYear) {
		this.yearConstructionContentLastTwoYear = yearConstructionContentLastTwoYear;
	}
	public String getYearConstructionContentShenBao() {
		return yearConstructionContentShenBao;
	}
	public void setYearConstructionContentShenBao(String yearConstructionContentShenBao) {
		this.yearConstructionContentShenBao = yearConstructionContentShenBao;
	}
	public Double getCapitalSCZ_ggys_TheYear() {
		return capitalSCZ_ggys_TheYear;
	}
	public void setCapitalSCZ_ggys_TheYear(Double capitalSCZ_ggys_TheYear) {
		this.capitalSCZ_ggys_TheYear = capitalSCZ_ggys_TheYear;
	}
	public Double getCapitalSCZ_gtzj_TheYear() {
		return capitalSCZ_gtzj_TheYear;
	}
	public void setCapitalSCZ_gtzj_TheYear(Double capitalSCZ_gtzj_TheYear) {
		this.capitalSCZ_gtzj_TheYear = capitalSCZ_gtzj_TheYear;
	}
	public Double getCapitalSCZ_qita() {
		return capitalSCZ_qita;
	}
	public void setCapitalSCZ_qita(Double capitalSCZ_qita) {
		this.capitalSCZ_qita = capitalSCZ_qita;
	}
	public String getCapitalOtherDescriptionShenBao() {
		return capitalOtherDescriptionShenBao;
	}
	public void setCapitalOtherDescriptionShenBao(String capitalOtherDescriptionShenBao) {
		this.capitalOtherDescriptionShenBao = capitalOtherDescriptionShenBao;
	}
	public Double getCapitalSCZ_ggys_LastYear() {
		return capitalSCZ_ggys_LastYear;
	}
	public void setCapitalSCZ_ggys_LastYear(Double capitalSCZ_ggys_LastYear) {
		this.capitalSCZ_ggys_LastYear = capitalSCZ_ggys_LastYear;
	}
	public Double getCapitalSCZ_gtzj_LastYear() {
		return capitalSCZ_gtzj_LastYear;
	}
	public void setCapitalSCZ_gtzj_LastYear(Double capitalSCZ_gtzj_LastYear) {
		this.capitalSCZ_gtzj_LastYear = capitalSCZ_gtzj_LastYear;
	}
	public Double getCapitalSCZ_qita_LastYear() {
		return capitalSCZ_qita_LastYear;
	}
	public void setCapitalSCZ_qita_LastYear(Double capitalSCZ_qita_LastYear) {
		this.capitalSCZ_qita_LastYear = capitalSCZ_qita_LastYear;
	}
	public String getCapitalOtherDescriptionShenBao_LastYear() {
		return capitalOtherDescriptionShenBao_LastYear;
	}
	public void setCapitalOtherDescriptionShenBao_LastYear(String capitalOtherDescriptionShenBao_LastYear) {
		this.capitalOtherDescriptionShenBao_LastYear = capitalOtherDescriptionShenBao_LastYear;
	}
	public Double getCapitalSCZ_ggys_LastTwoYear() {
		return capitalSCZ_ggys_LastTwoYear;
	}
	public void setCapitalSCZ_ggys_LastTwoYear(Double capitalSCZ_ggys_LastTwoYear) {
		this.capitalSCZ_ggys_LastTwoYear = capitalSCZ_ggys_LastTwoYear;
	}
	public Double getCapitalSCZ_gtzj_LastTwoYear() {
		return capitalSCZ_gtzj_LastTwoYear;
	}
	public void setCapitalSCZ_gtzj_LastTwoYear(Double capitalSCZ_gtzj_LastTwoYear) {
		this.capitalSCZ_gtzj_LastTwoYear = capitalSCZ_gtzj_LastTwoYear;
	}
	public Double getCapitalSCZ_qita_LastTwoYear() {
		return capitalSCZ_qita_LastTwoYear;
	}
	public void setCapitalSCZ_qita_LastTwoYear(Double capitalSCZ_qita_LastTwoYear) {
		this.capitalSCZ_qita_LastTwoYear = capitalSCZ_qita_LastTwoYear;
	}
	public String getCapitalOtherDescriptionShenBao_LastTwoYear() {
		return capitalOtherDescriptionShenBao_LastTwoYear;
	}
	public void setCapitalOtherDescriptionShenBao_LastTwoYear(String capitalOtherDescriptionShenBao_LastTwoYear) {
		this.capitalOtherDescriptionShenBao_LastTwoYear = capitalOtherDescriptionShenBao_LastTwoYear;
	}
	public Double getApInvestSum() {
		return apInvestSum;
	}
	public void setApInvestSum(Double apInvestSum) {
		this.apInvestSum = apInvestSum;
	}
	public Double getCapitalAP_ggys_TheYear() {
		return capitalAP_ggys_TheYear;
	}
	public void setCapitalAP_ggys_TheYear(Double capitalAP_ggys_TheYear) {
		this.capitalAP_ggys_TheYear = capitalAP_ggys_TheYear;
	}
	public Double getCapitalAP_gtzj_TheYear() {
		return capitalAP_gtzj_TheYear;
	}
	public void setCapitalAP_gtzj_TheYear(Double capitalAP_gtzj_TheYear) {
		this.capitalAP_gtzj_TheYear = capitalAP_gtzj_TheYear;
	}
	public Double getCapitalAP_qita() {
		return capitalAP_qita;
	}
	public void setCapitalAP_qita(Double capitalAP_qita) {
		this.capitalAP_qita = capitalAP_qita;
	}
	public Double getCapitalAP_ggys_LastYear() {
		return capitalAP_ggys_LastYear;
	}
	public void setCapitalAP_ggys_LastYear(Double capitalAP_ggys_LastYear) {
		this.capitalAP_ggys_LastYear = capitalAP_ggys_LastYear;
	}
	public Double getCapitalAP_gtzj_LastYear() {
		return capitalAP_gtzj_LastYear;
	}
	public void setCapitalAP_gtzj_LastYear(Double capitalAP_gtzj_LastYear) {
		this.capitalAP_gtzj_LastYear = capitalAP_gtzj_LastYear;
	}
	public Double getCapitalAP_qita_LastYear() {
		return capitalAP_qita_LastYear;
	}
	public void setCapitalAP_qita_LastYear(Double capitalAP_qita_LastYear) {
		this.capitalAP_qita_LastYear = capitalAP_qita_LastYear;
	}
	public Double getCapitalAP_ggys_LastTwoYear() {
		return capitalAP_ggys_LastTwoYear;
	}
	public void setCapitalAP_ggys_LastTwoYear(Double capitalAP_ggys_LastTwoYear) {
		this.capitalAP_ggys_LastTwoYear = capitalAP_ggys_LastTwoYear;
	}
	public Double getCapitalAP_gtzj_LastTwoYear() {
		return capitalAP_gtzj_LastTwoYear;
	}
	public void setCapitalAP_gtzj_LastTwoYear(Double capitalAP_gtzj_LastTwoYear) {
		this.capitalAP_gtzj_LastTwoYear = capitalAP_gtzj_LastTwoYear;
	}
	public Double getCapitalAP_qita_LastTwoYear() {
		return capitalAP_qita_LastTwoYear;
	}
	public void setCapitalAP_qita_LastTwoYear(Double capitalAP_qita_LastTwoYear) {
		this.capitalAP_qita_LastTwoYear = capitalAP_qita_LastTwoYear;
	}
	
	public Double getSqPlanReach_ggys() {
		return sqPlanReach_ggys;
	}
	public void setSqPlanReach_ggys(Double sqPlanReach_ggys) {
		this.sqPlanReach_ggys = sqPlanReach_ggys;
	}
	public Double getSqPlanReach_gtzj() {
		return sqPlanReach_gtzj;
	}
	public void setSqPlanReach_gtzj(Double sqPlanReach_gtzj) {
		this.sqPlanReach_gtzj = sqPlanReach_gtzj;
	}
	public String getFunctionSubjects() {
		return functionSubjects;
	}
	public void setFunctionSubjects(String functionSubjects) {
		this.functionSubjects = functionSubjects;
	}
	public String getEconClassSubjects() {
		return econClassSubjects;
	}
	public void setEconClassSubjects(String econClassSubjects) {
		this.econClassSubjects = econClassSubjects;
	}
	public String getProcessStage() {
		return processStage;
	}
	public void setProcessStage(String processStage) {
		this.processStage = processStage;
	}
	public Integer getProcessState() {
		return processState;
	}
	public void setProcessState(Integer processState) {
		this.processState = processState;
	}
	public String getProjectConstrBasis() {
		return projectConstrBasis;
	}
	public void setProjectConstrBasis(String projectConstrBasis) {
		this.projectConstrBasis = projectConstrBasis;
	}
	public String getRecomProgram() {
		return recomProgram;
	}
	public void setRecomProgram(String recomProgram) {
		this.recomProgram = recomProgram;
	}
	public String getSocialAndEconomic() {
		return socialAndEconomic;
	}
	public void setSocialAndEconomic(String socialAndEconomic) {
		this.socialAndEconomic = socialAndEconomic;
	}
	public Boolean getIsApplyQianQiFei() {
		return isApplyQianQiFei;
	}
	public void setIsApplyQianQiFei(Boolean isApplyQianQiFei) {
		this.isApplyQianQiFei = isApplyQianQiFei;
	}
	public Double getQianQiFeiApply() {
		return qianQiFeiApply;
	}
	public void setQianQiFeiApply(Double qianQiFeiApply) {
		this.qianQiFeiApply = qianQiFeiApply;
	}
	public String getLastYearImageSchedule() {
		return lastYearImageSchedule;
	}
	public void setLastYearImageSchedule(String lastYearImageSchedule) {
		this.lastYearImageSchedule = lastYearImageSchedule;
	}
	public String getYearImageSchedule() {
		return yearImageSchedule;
	}
	public void setYearImageSchedule(String yearImageSchedule) {
		this.yearImageSchedule = yearImageSchedule;
	}
	public String getAuditState() {
		return auditState;
	}
	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getExistingProblem() {
		return existingProblem;
	}
	public void setExistingProblem(String existingProblem) {
		this.existingProblem = existingProblem;
	}
	public String getMoveSuggestion() {
		return moveSuggestion;
	}
	public void setMoveSuggestion(String moveSuggestion) {
		this.moveSuggestion = moveSuggestion;
	}
	public ShenBaoUnitInfo getBianZhiUnitInfo() {
		return bianZhiUnitInfo;
	}
	public void setBianZhiUnitInfo(ShenBaoUnitInfo bianZhiUnitInfo) {
		this.bianZhiUnitInfo = bianZhiUnitInfo;
	}
	public ShenBaoUnitInfo getShenBaoUnitInfo() {
		return shenBaoUnitInfo;
	}
	public void setShenBaoUnitInfo(ShenBaoUnitInfo shenBaoUnitInfo) {
		this.shenBaoUnitInfo = shenBaoUnitInfo;
	}
	public List<Attachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	public Date getShenbaoDate() {
		return shenbaoDate;
	}
	public void setShenbaoDate(Date shenbaoDate) {
		this.shenbaoDate = shenbaoDate;
	}
	public Date getQianshouDate() {
		return qianshouDate;
	}
	public void setQianshouDate(Date qianshouDate) {
		this.qianshouDate = qianshouDate;
	}
	public Date getPifuDate() {
		return pifuDate;
	}
	public void setPifuDate(Date pifuDate) {
		this.pifuDate = pifuDate;
	}
	public Double getApPlanReach_ggys() {
		return apPlanReach_ggys;
	}
	public void setApPlanReach_ggys(Double apPlanReach_ggys) {
		this.apPlanReach_ggys = apPlanReach_ggys;
	}
	public Double getApPlanReach_gtzj() {
		return apPlanReach_gtzj;
	}
	public void setApPlanReach_gtzj(Double apPlanReach_gtzj) {
		this.apPlanReach_gtzj = apPlanReach_gtzj;
	}
	public String getZong_processId() {
		return zong_processId;
	}
	public void setZong_processId(String zong_processId) {
		this.zong_processId = zong_processId;
	}
	public String getThisTaskId() {
		return thisTaskId;
	}
	public void setThisTaskId(String thisTaskId) {
		this.thisTaskId = thisTaskId;
	}
	public String getThisTaskName() {
		return thisTaskName;
	}
	public void setThisTaskName(String thisTaskName) {
		this.thisTaskName = thisTaskName;
	}
	public boolean getComplate() {
		return complate;
	}
	public void setComplate(boolean complate) {
		this.complate = complate;
	}
	public Boolean getIsIncludPack() {
		return isIncludPack;
	}
	public void setIsIncludPack(Boolean isIncludPack) {
		this.isIncludPack = isIncludPack;
	}

	
}
