package cs.controller.shenbaoAdmin;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.DomainDto.ProjectDto;
import cs.model.DomainDto.ShenBaoInfoDto;
import cs.repository.odata.ODataObj;
import cs.service.interfaces.ShenBaoInfoService;

@Controller
@RequestMapping(name="项目申报",path="shenbaoAdmin/shenbao")
public class ShenBaoAdminShenBaoController {
	private String ctrlName = "shenbaoAdmin/shenbao";
	
	@Autowired ShenBaoInfoService shenBaoInfoService;
	
	//@RequiresPermissions("shenbaoAdmin/shenbao##get")
	@RequestMapping(name = "创建申报信息", path = "",method=RequestMethod.GET)
	public @ResponseBody PageModelDto<ShenBaoInfoDto> get(HttpServletRequest request) throws ParseException{
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<ShenBaoInfoDto> shenBaoInfoDtos = shenBaoInfoService.get(odataObj);		
		return shenBaoInfoDtos;	
	}
	
	//@RequiresPermissions("shenbaoAdmin/shenbao##post")
	@RequestMapping(name = "创建申报信息", path = "",method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void  createShenBaoInfo(@RequestBody ShenBaoInfoDto shenBaoInfoDto){
		shenBaoInfoService.createShenBaoInfo(shenBaoInfoDto);	
	}
		
	//begin#html
	@RequestMapping(name = "列表页", path = "html/list")
	public String list() {
		return this.ctrlName + "/list";
	}
	
	@RequestMapping(name = "申报页", path = "html/edit")
	public String create() {
		return this.ctrlName + "/edit";
	}
	
	@RequestMapping(name = "申报记录列表页", path = "html/records")
	public String records() {
		return this.ctrlName + "/records";
	}
	
	@RequestMapping(name = "申报记录详情页", path = "html/shenBaoInfo")
	public String shenBaoInfo() {
		return this.ctrlName + "/shenBaoInfo";
	}
}
