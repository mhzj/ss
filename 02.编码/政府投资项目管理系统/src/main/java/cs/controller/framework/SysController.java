package cs.controller.framework;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.common.BasicDataConfig;
import cs.common.Response;
import cs.common.sysResource.SysResourceDto;
import cs.model.DomainDto.SysConfigDto;
import cs.service.framework.SysService;

@Controller
@RequestMapping(name = "系统资源", path = "sys")
public class SysController {
	@Autowired
	private SysService sysService;
	
	private String ctrl="management/sysConfig";

	@RequiresPermissions("sys#resource#get")
	@RequestMapping(name = "获取系统资源数据", path = "resource", method = RequestMethod.GET)
	public @ResponseBody List<SysResourceDto> get(HttpServletRequest request) {
		List<SysResourceDto> ZTreeList = sysService.getSysResources();
		return ZTreeList;
	}
	
	@RequestMapping(name = "设置task签收人", path = "create", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public  void create(@RequestBody List<SysConfigDto> sysConfigDtos) {		
		sysConfigDtos.forEach(x->{
			SysConfigDto sysConfigDto=new SysConfigDto();
			sysConfigDto.setId(UUID.randomUUID().toString());
			sysConfigDto.setConfigType(BasicDataConfig.taskType);
			sysConfigDto.setConfigName(x.getConfigName());
			sysConfigDto.setConfigValue(x.getConfigValue());
			sysService.createTaskUser(sysConfigDto);
		});
		
	}
	
	@RequestMapping(name = "系统初始化签收人", path = "getSysConfigs", method = RequestMethod.GET)
	public @ResponseBody List<SysConfigDto> initUser() {
		List<SysConfigDto> list = sysService.getSysConfigs();
		return list;
				
	}
	
	@RequestMapping(name = "系统初始化", path = "init", method = RequestMethod.GET)
	public @ResponseBody String init(HttpServletRequest request) {
		Response response = sysService.SysInit();
		if(response.isSuccess()){
			return "Init system success";
		}else{
			return "Init system fail";
		}				
	}
	
	@RequestMapping(name = "基础数据初始化", path = "initBasicData", method = RequestMethod.GET)
	public @ResponseBody String initBasicData(HttpServletRequest request) {
		Response response = sysService.SysInitBasicData();
		if(response.isSuccess()){
			return "Init basicData success";
		}else{
			return "Init basicData fail";
		}	
	}
	//begin#html
	@RequestMapping(name = "系统配置主页", path = "html/index", method = RequestMethod.GET)
	public String index(){
		return ctrl+"/index";
	}
}
