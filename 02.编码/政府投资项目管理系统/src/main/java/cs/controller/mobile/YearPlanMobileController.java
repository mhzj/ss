package cs.controller.mobile;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cs.model.PageModelDto;
import cs.model.DomainDto.ShenBaoInfoDto;
import cs.model.DomainDto.YearPlanDto;
import cs.repository.odata.ODataObj;
import cs.service.interfaces.YearPlanService;

@Controller
@RequestMapping(name="手机端--年度计划管理", path="mobile/management/yearPlan")
public class YearPlanMobileController {
	
	@Autowired
	private YearPlanService yearPlanService;
	
	@RequestMapping(name = "获取年度计划列表数据", path = "",method=RequestMethod.GET)
	public @ResponseBody PageModelDto<YearPlanDto> get(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<YearPlanDto>  yearPlanDtos= yearPlanService.get(odataObj);		
		return yearPlanDtos;
	}
	
	@RequestMapping(name = "获取年度计划项目列表数据", path = "{id}/projectList",method=RequestMethod.GET)
	public @ResponseBody PageModelDto<ShenBaoInfoDto> getShenBaoInfo(HttpServletRequest request,@PathVariable String id) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<ShenBaoInfoDto> shenBaoInfoDtos=new PageModelDto<ShenBaoInfoDto>();
		shenBaoInfoDtos = yearPlanService.getYearPlanShenBaoInfo(id,odataObj);
		return shenBaoInfoDtos;
	}
	
}
