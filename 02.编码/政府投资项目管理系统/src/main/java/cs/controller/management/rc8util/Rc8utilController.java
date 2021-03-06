package cs.controller.management.rc8util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.huasisoft.h1.api.org.DepartmentManager;
import com.huasisoft.h1.api.org.PersonManager;
import com.huasisoft.h1.model.ORGDepartment;
import com.huasisoft.h1.model.ORGPerson;
import com.huasisoft.h1.util.HuasisoftUtil;
import com.huasisoft.portal.model.Backlog;
import sun.misc.BASE64Encoder;

import cs.domain.framework.User;
import cs.domain.framework.User_;
import cs.model.framework.UserDto;
import cs.repository.framework.UserRepo;
import cs.service.framework.UserService;
import cs.service.interfaces.ProcessService;

/**
 * @author Administrator
 * @Description RC8账户管理控制器
 */
@Controller
@RequestMapping(name = "RC8账户管理", path = "risesoft")
public class Rc8utilController {
	@Autowired
	private UserService userService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private UserRepo userRepo;
	// private static String[] tzk_id_list =
	// {"4853bb99c50b413f89658af304e18698","{261F89FE-F016-4ADA-A5A0-B43EA7D9FFF0}","{BFA7B3F4-FFFF-FFFF-B32B-A23800000002}","{BFA7B3F4-FFFF-FFFF-B32B-D0B500000003}","{AC18B3FB-FFFF-FFFF-FB69-97BD00000015}","{BFA7B3F4-FFFF-FFFF-B32E-58DB00000008}","{AC18B3FB-0000-0000-6629-02E600000002}"};

	private final static String PARENTID = "{00000000-0000-0000-0000-000000000000}";

	private void findChildDepartments(Set<ORGDepartment> departHashSet, String departmentId, DepartmentManager dm)
			throws Exception {
		List<String> ids = new ArrayList<String>();
		ids.add(departmentId);
		// 根据id查询部门信息
		List<ORGDepartment> deptments = dm.getByIds(ids);
		/** * 加入当前单位 */
		departHashSet.addAll(deptments.stream().collect(Collectors.toList()));
		/** * 获取子单位 */
		List<ORGDepartment> departments = dm.listByParentID(departmentId);
		for (ORGDepartment d : departments) {
			/** * 递归子单位 */
			findChildDepartments(departHashSet, d.getId(), dm);
		}
	}

	// 每七天执行一次
	// @RequiresPermissions("risesoft#tzkUsers#get")
	@Scheduled(cron = "0 0 0 1/7 * ? ")
	@RequestMapping(name = "查询RC8投资科人员", path = "tzkUsers", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void getTZKUser() throws Exception {

		PersonManager pm = HuasisoftUtil.getPersonManager();
		DepartmentManager dm = HuasisoftUtil.getDepartmentManager();

		Set<ORGDepartment> departHashSet = new HashSet<ORGDepartment>();
		// 获取所有 PARENTID下的部门
		findChildDepartments(departHashSet, PARENTID, dm);

		// 根据部门获取所有部门下的用户
		for (ORGDepartment dept : departHashSet) {
			List<ORGPerson> persons = pm.listByPath(dept.getId());
			for (ORGPerson person : persons) {
				UserDto userDto = new UserDto();
				userDto.setDisplayName(person.getName());
				userDto.setLoginName(person.getLoginName());
				userDto.setPassword(person.getPlainText());
				userDto.setEmail(person.getEmail());
				userDto.setMobilePhone(person.getMobile());
				userDto.setOaId(person.getId());
				userService.createSYSUser(userDto);
			}
		}

		/*
		 * int a = 1; //区下所有部门对象 List<ORGDepartment> orgDeptList =
		 * dm.listByParentID(PARENTID); if(!orgDeptList.isEmpty()){ for (int i =
		 * 0; i < orgDeptList.size(); i++) { ORGDepartment orgDept =
		 * orgDeptList.get(i);
		 * 
		 * if(orgDept != null){
		 * 
		 * //部门对象下所有部门集合 List<ORGDepartment> depts =
		 * dm.listAllDept(orgDept.getId()); List<ORGDepartment> depts2 =
		 * dm.listAllDept(orgDept.getParentID()); depts.addAll(depts2);
		 * if(!depts.isEmpty()){ for (int j = 0; j < depts.size(); j++) {
		 * //部门下面所有人员 List<ORGPerson> persons =
		 * pm.listByParentID(depts.get(j).getId()); for (ORGPerson person :
		 * persons) { UserDto userDto = new UserDto();
		 * userDto.setDisplayName(person.getName());
		 * userDto.setLoginName(person.getLoginName());
		 * userDto.setPassword(person.getPlainText());
		 * userDto.setEmail(person.getEmail());
		 * userDto.setMobilePhone(person.getMobile());
		 * userDto.setOaId(person.getId()); userService.createSYSUser(userDto);
		 * a++; System.out.println(a); } } } } } }
		 */

	}

	@RequestMapping(name = "返回待办数字", path = "todoNumber")
	@ResponseBody
	public int getTodoNumber(@RequestParam(name = "userID", required = false) String userID) throws UnsupportedEncodingException {
		int num = 0;
		if(userID == null){
			return num;
		}
		String id =URLDecoder.decode(userID, "UTF-8");
		System.out.println("待办数字--人员ID："+id);
//		 id = new String(Base64.getDecoder().decode(userID), "UTF-8");
		try {
//			PersonManager pm = HuasisoftUtil.getPersonManager();
//			ORGPerson person = null;
//
//			person = pm.get(id);
//			if (person != null) {
//				System.out.println("待办数字--OA名称："+person.getLoginName());
				Criterion criterion = Restrictions.eq(User_.oaId.getName(), id);
				List<User> localUser = userRepo.findByCriteria(criterion);
				if (!CollectionUtils.isEmpty(localUser)) {
					System.out.println("待办数字--人员名称："+localUser.get(0).getLoginName());
					num = processService.findAllTodoTaskNumber(localUser.get(0).getId());
				} else {
					return num;
				}
//			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("rc8人员查询失败");
		}
		return num;
	}
	
	@RequestMapping(name = "查询RC8投资科人员", path = "getnum", method = RequestMethod.GET)
	@ResponseBody
	public int getnum(){
		int num = processService.findAllTodoTaskNumber("2c30f464-8e99-4cae-8985-238b06b798a4");
		System.out.println(num);
		return num;
	}

}
