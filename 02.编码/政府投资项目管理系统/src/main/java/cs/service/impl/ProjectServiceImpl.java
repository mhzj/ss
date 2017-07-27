package cs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.BasicDataConfig;
import cs.common.ICurrentUser;
import cs.common.Util;
import cs.domain.Attachment;
import cs.domain.BasicData;
import cs.domain.MonthReport;
import cs.domain.Project;
import cs.domain.Project_;
import cs.domain.ReplyFile;
import cs.model.PageModelDto;
import cs.model.DomainDto.AttachmentDto;
import cs.model.DomainDto.MonthReportDto;
import cs.model.DomainDto.ProjectDto;
import cs.model.DomainDto.ReplyFileDto;
import cs.model.DtoMapper.IMapper;
import cs.repository.interfaces.IRepository;
import cs.repository.odata.ODataObj;
import cs.service.interfaces.ProjectService;
/**
 * @Description: 项目信息服务层
 * @author: cx
 * @Date：2017年7月10日
 * @version：0.1
 */
@Service
public class ProjectServiceImpl extends AbstractServiceImpl<ProjectDto, Project, String> implements ProjectService {
	private static Logger logger = Logger.getLogger(ProjectServiceImpl.class);
		
	@Autowired
	private IRepository<Attachment, String> attachmentRepo;
	@Autowired
	private IRepository<ReplyFile, String> replyFileRepo;
	@Autowired
	private IRepository<BasicData, String> basicDataRepo;
	@Autowired
	private IRepository<MonthReport, String> monthReportRepo;
	@Autowired
	private IMapper<AttachmentDto, Attachment> attachmentMapper;
	@Autowired
	private IMapper<MonthReportDto, MonthReport> monthReportMapper;
	@Autowired
	private IMapper<ProjectDto, Project> projectMapper;
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	@Transactional
	public PageModelDto<ProjectDto> get(ODataObj odataObj) {
		logger.info("查询项目数据");
		return super.get(odataObj);		
	}

	
	@Override
	@Transactional
	public Project update(ProjectDto projectDto,String id) {		
		Project project = super.update(projectDto,id);		
		//处理关联信息
		//附件
		project.getAttachments().forEach(x -> {//删除历史附件
			attachmentRepo.delete(x);
		});
		project.getAttachments().clear();
		projectDto.getAttachmentDtos().forEach(x -> {//添加新附件
			Attachment attachment = new Attachment();
			attachmentMapper.buildEntity(x, attachment);
			attachment.setCreatedBy(project.getCreatedBy());
			attachment.setModifiedBy(project.getModifiedBy());
			project.getAttachments().add(attachment);
		});
		//月报
		project.getMonthReports().forEach(x -> {//删除历史月报
			monthReportRepo.delete(x);
		});
		project.getMonthReports().clear();
		projectDto.getMonthReportDtos().forEach(x -> {//添加新月报
			MonthReport monthReport = new MonthReport();
			monthReportMapper.buildEntity(x, monthReport);
			monthReport.setCreatedBy(project.getCreatedBy());
			monthReport.setModifiedBy(project.getModifiedBy());
			project.getMonthReports().add(monthReport);
		});
		
		//保存数据
		super.repository.save(project);
		logger.info(String.format("编辑项目,项目名称 %s",projectDto.getProjectName()));
		return project;		
	}
	

	@Override
	@Transactional
	public void updateProjectByIsMonthReport(ProjectDto projectDto) {		
		Project project = super.repository.findById(projectDto.getId());
		project.setIsMonthReport(projectDto.getIsMonthReport());
		//设置修改人
		String longinName = currentUser.getLoginName();
		project.setModifiedBy(longinName);
		project.setModifiedDate(new Date());
		//保存数据
		super.repository.save(project);
		logger.info(String.format("修改项目是否月报,项目名称 %s",project.getProjectName()));
	}

	@Override
	@Transactional
	public Project create(ProjectDto projectDto) {
			//判断是否存在项目代码--生成项目代码
			if(projectDto.getProjectNumber() == null || projectDto.getProjectNumber().isEmpty()){
				//根据基础数据id查询出基础数据
				BasicData basicData = basicDataRepo.findById(projectDto.getProjectIndustry());
				String number = Util.getProjectNumber(projectDto.getProjectInvestmentType(), basicData);
				projectDto.setProjectNumber(number);
				//行业项目统计累加
				basicData.setCount(basicData.getCount()+1);
			}
			Project project = super.create(projectDto);	
			project.setModifiedDate(new Date());//设置修改时间
			//处理关联信息
			projectDto.getAttachmentDtos().forEach(x -> {//添加新附件
				Attachment attachment = new Attachment();
				attachmentMapper.buildEntity(x, attachment);
				attachment.setCreatedBy(project.getCreatedBy());
				attachment.setModifiedBy(project.getModifiedBy());
				project.getAttachments().add(attachment);
			});
			//月报
			projectDto.getMonthReportDtos().forEach(x -> {//添加新月报
				MonthReport monthReport = new MonthReport();
				monthReportMapper.buildEntity(x, monthReport);
				monthReport.setCreatedBy(project.getCreatedBy());
				monthReport.setModifiedBy(project.getModifiedBy());
				project.getMonthReports().add(monthReport);
			});			
			//保存数据
			super.repository.save(project);
			//将文件保存replyFile
			handlePiFuFile(project);
			logger.info(String.format("创建项目,项目名称 %s",projectDto.getProjectName()));
			return project;	
	}
	
	@Override
	@Transactional
	public List<ProjectDto> getProjectByNumber(String number) {
		//根据项目代码来获取项目信息
		Criterion criterion=Restrictions.eq(Project_.projectNumber.getName(), number);
		List<Project> findProjects = super.repository.findByCriteria(criterion);
		List<ProjectDto> projectDtos = new ArrayList<>();
		findProjects.stream().forEach(x->{
			ProjectDto dto = projectMapper.toDto(x);			
			projectDtos.add(dto);
		});
		return projectDtos;
	}

	@Override
	@Transactional
	public void updateVersion(String id, Boolean isLatestVersion) {
		Project project = super.repository.findById(id);
		project.setIsLatestVersion(isLatestVersion);
		project.setModifiedDate(new Date());//设置修改时间
		project.setModifiedBy(currentUser.getLoginName());//设置修改人
		super.repository.save(project);
		logger.info(String.format("修改项目版本,项目名称 %s",project.getProjectName()));
	}
	
	/**
	 * 批复文件库处理
	 */
	public void handlePiFuFile(Project project){
		//获取项目中批复文件以及文号
		Map<String,Attachment> pifus = new HashMap<>();
		project.getAttachments().stream().forEach(x->{
			if(x.getType().equals(BasicDataConfig.attachment_type_cbsjygs) ||
					x.getType().equals(BasicDataConfig.attachment_type_jys) ||
					x.getType().equals(BasicDataConfig.attachment_type_kxxyjbg)
					){
				if(x.getType().equals(BasicDataConfig.attachment_type_jys)){
					pifus.put(project.getPifuJYS_wenhao(), x);
				}
				else if(x.getType().equals(BasicDataConfig.attachment_type_kxxyjbg)){
					pifus.put(project.getPifuKXXYJBG_wenhao(), x);
				}
				else if(x.getType().equals(BasicDataConfig.attachment_type_cbsjygs)){
					pifus.put(project.getPifuCBSJYGS_wenhao(), x);
				}
			}
		});
		//判断项目中批复文件在文件库中是否存在
		//更新文件库
		Set<String> keSet=pifus.keySet();
		for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			ReplyFile replyfile = new ReplyFile();
			replyfile.setId(UUID.randomUUID().toString());
			replyfile.setCreatedBy(pifus.get(string).getCreatedBy());
			replyfile.setName(pifus.get(string).getName());
			replyfile.setFullName(pifus.get(string).getUrl());
			replyfile.setItemOrder(pifus.get(string).getItemOrder());
			replyfile.setModifiedBy(pifus.get(string).getModifiedBy());
			replyfile.setNumber(string);
			replyfile.setType(pifus.get(string).getType());
			replyFileRepo.save(replyfile);//更新文件库
		}
	} 
}
