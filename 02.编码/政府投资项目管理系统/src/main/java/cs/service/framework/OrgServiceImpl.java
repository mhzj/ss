package cs.service.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.domain.framework.Org;
import cs.domain.framework.Role;
import cs.domain.framework.User;
import cs.model.PageModelDto;
import cs.model.framework.OrgDto;
import cs.model.framework.RoleDto;
import cs.model.framework.UserDto;
import cs.repository.framework.OrgRepo;
import cs.repository.framework.UserRepo;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;

/**
 * @deprecated 部门服务实现类
 * @author Administrator
 *
 */
@Service
public class OrgServiceImpl implements OrgService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private OrgRepo orgRepo;
	@Autowired
	private ICurrentUser currentUser;


	/**
	 * 
	 * @param odataObj
	 * @return 查询部门
	 */
	@Override
	@Transactional
	public PageModelDto<OrgDto> get(ODataObj odataObj) {
		List<Org> orgList = orgRepo.findByOdata(odataObj);
		List<OrgDto> orgDtoList = new ArrayList<>();
		for (Org item : orgList) {
			OrgDto orgDto = new OrgDto();
			orgDto.setId(item.getId());
			orgDto.setName(item.getName());
			orgDto.setCreatedDate(item.getCreatedDate());
			orgDto.setComment(item.getComment());
			orgDto.setOrgIdentity(item.getOrgIdentity());
			
			List<User> users= item.getUsers();
			List<UserDto> usersDto= new ArrayList<>();
			users.forEach(x->{
				UserDto userDto = new UserDto();
				userDto.setDisplayName(x.getDisplayName());
				userDto.setId(x.getId());
				List<Role> roles = x.getRoles();
				List<RoleDto> rolesDto = new ArrayList<>();
				roles.forEach(y ->{
					
					RoleDto roleDto = new RoleDto();
					roleDto.setId(y.getId());
					roleDto.setCreatedBy(y.getCreatedBy());
					roleDto.setCreatedDate(y.getCreatedDate());
					roleDto.setItemOrder(y.getItemOrder());
					roleDto.setModifiedBy(y.getModifiedBy());
					roleDto.setModifiedDate(y.getModifiedDate());
					roleDto.setComment(y.getComment());
					roleDto.setRoleName(y.getRoleName());
					
					rolesDto.add(roleDto);
				});
				userDto.setRoles(rolesDto);
				usersDto.add(userDto);
			});
			orgDto.setUserDtos(usersDto);
			orgDtoList.add(orgDto);
		}
		PageModelDto<OrgDto> pageModelDto = new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(orgDtoList);

		logger.info("查询部门数据");		
		return pageModelDto;
	}
	/**
	 * 创建部门
	 * @param orgDto 部门实体
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	@Transactional
	public void createOrg(OrgDto orgDto) {
		// 判断部门是否已经存在
		Criteria criteria = orgRepo.getSession().createCriteria(Org.class);
		criteria.add(Restrictions.eq("orgIdentity", orgDto.getOrgIdentity()));
		List<Org> orgs = criteria.list();
		if (orgs.isEmpty()) {// 部门不存在
			Org org = new Org();
			org.setComment(orgDto.getComment());
			org.setName(orgDto.getName());
			org.setId(UUID.randomUUID().toString());
			org.setCreatedBy(currentUser.getUserId());
			org.setOrgIdentity(orgDto.getOrgIdentity());

			orgRepo.save(org);

			logger.info(String.format("创建部门,部门名:%s", orgDto.getOrgIdentity()));
		} else

		{
			throw new IllegalArgumentException(String.format("部门标识：%s 已经存在,请重新输入！", orgDto.getOrgIdentity()));
		}
	}
	/**
	 * @deprecated 更新部门
	 * @param orgDto
	 */
	@Override
	@Transactional
	public void updateOrg(OrgDto orgDto) {
		Org org = orgRepo.findById(orgDto.getId());
		org.setComment(orgDto.getComment());
		org.setName(orgDto.getName());
		org.setModifiedBy(currentUser.getUserId());

		orgRepo.save(org);
		logger.info(String.format("更新部门,部门名:%s", orgDto.getName()));
	}
	/**
	 * @deprecated 删除部门
	 * @param id
	 */
	@Override
	@Transactional
	public void deleteOrg(String id) {
		Org org = orgRepo.findById(id);
		if (org != null) {
			
			List<User> users=org.getUsers();
			for (User user : users) {//把部门里的用户移出才能删除
				user.getOrgs().remove(org);
			}
			orgRepo.delete(org);
			logger.info(String.format("删除部门,部门identity:%s", org.getOrgIdentity()));
		}

	}

	@Override
	@Transactional
	public void deleteOrgs(String[] ids) {
		for (String id : ids) {
			this.deleteOrg(id);
		}
		logger.info("批量删除部门");
	}
	/**
	 * 查询部门
	 * @param id 主键
	 * @return
	 */
	@Override
	@Transactional
	public PageModelDto<UserDto> getOrgUsers(String id) {
		PageModelDto<UserDto> pageModelDto = new PageModelDto<>();
		List<UserDto> userDtos = new ArrayList<>();
		Org org = orgRepo.findById(id);
		if (org != null) {
			org.getUsers().forEach(x -> {
				UserDto userDto = new UserDto();
				userDto.setId(x.getId());
				userDto.setComment(x.getComment());
				userDto.setLoginName(x.getLoginName());
				userDto.setDisplayName(x.getDisplayName());
				
				List<Role> roles = x.getRoles();
				List<RoleDto> rolesDto = new ArrayList<>();
				roles.forEach(y ->{
					
					RoleDto roleDto = new RoleDto();
					roleDto.setId(y.getId());
					roleDto.setCreatedBy(y.getCreatedBy());
					roleDto.setCreatedDate(y.getCreatedDate());
					roleDto.setItemOrder(y.getItemOrder());
					roleDto.setModifiedBy(y.getModifiedBy());
					roleDto.setModifiedDate(y.getModifiedDate());
					roleDto.setComment(y.getComment());
					roleDto.setRoleName(y.getRoleName());
					
					
					rolesDto.add(roleDto);
				});
				
				
				userDto.setRoles(rolesDto);
				
				userDtos.add(userDto);

			});
			pageModelDto.setValue(userDtos);
			pageModelDto.setCount(userDtos.size());
			logger.info(String.format("查找部门用户，部门%s", org.getOrgIdentity()));
		}

		return pageModelDto;
	}
	/**
	 * @deprecated 查找非部门用户
	 * @param id 部门ID
	 * @param oDataObj
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional
	public PageModelDto<UserDto> getUsersNotInOrg(String id, ODataObj oDataObj) {
		PageModelDto<UserDto> pageModelDto = new PageModelDto<>();
		List<UserDto> userDtos = new ArrayList<>();
		Org org = orgRepo.findById(id);
		List<String> userIds = new ArrayList<>();
		if (org != null) {

			org.getUsers().forEach(x -> {
				userIds.add(x.getId());
			});

			//List<User> users = userRepo.getUsersNotIn(userIds, oDataObj);
			userIds.forEach(x -> {
				ODataFilterItem<String> filterItem = new ODataFilterItem();
				filterItem.setField("id");
				filterItem.setOperator("ne");
				filterItem.setValue(x);
				oDataObj.getFilter().add(filterItem);
			});
			List<User> users = userRepo.findByOdata(oDataObj);
			users.forEach(x -> {
				UserDto userDto = new UserDto();
				userDto.setId(x.getId());
				userDto.setComment(x.getComment());
				userDto.setLoginName(x.getLoginName());
				userDto.setDisplayName(x.getDisplayName());
				userDtos.add(userDto);

			});
			pageModelDto.setValue(userDtos);
			pageModelDto.setCount(oDataObj.getCount());

			logger.info(String.format("查找非部门用户,部门%s", org.getOrgIdentity()));
		}

		return pageModelDto;
	}
	/**
	 * @deprecated 部门添加用户
	 * @param userId
	 * @param orgId
	 */
	@Override
	@Transactional
	public void addUserToOrg(String userId, String orgId) {
		Org org = orgRepo.findById(orgId);
		if (org != null) {
			User user = userRepo.findById(userId);
			if (user != null) {
				user.getOrgs().add(org);
			}
			userRepo.save(user);
			logger.info(String.format("添加用户到部门,部门%s,用户:%s", org.getOrgIdentity(), user.getLoginName()
					));
			
		}

	}
	/**
	 * @deprecated 删除部门用户
	 * @param userId 用户ID
	 * @param orgId部门ID
	 */
	@Override
	@Transactional
	public void removeOrgUser(String userId, String orgId) {
		Org org = orgRepo.findById(orgId);
		if (org != null) {
			User user = userRepo.findById(userId);
			if (user != null) {
				user.getOrgs().remove(org);
			}
			userRepo.save(user);
			logger.info(String.format("从部门移除用户,部门%s,用户:%s", org.getOrgIdentity(), user.getLoginName()));
		}

	}

	@Override
	@Transactional
	public void removeOrgUsers(String[] userIds, String orgId) {
		Org org = orgRepo.findById(orgId);
		if (org != null) {
			for (String id : userIds) {
				this.removeOrgUser(id,orgId);
			}
			logger.info(String.format("批量删除部门用户,部门%s", org.getOrgIdentity()));
		}
	}

}
