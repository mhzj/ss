package cs.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import cs.common.Response;
import cs.common.sysResource.ClassFinder;
import cs.common.sysResource.SysResourceDto;
import cs.domain.Resource;
import cs.domain.Role;
import cs.domain.SysConfig;
import cs.domain.User;
import cs.repository.repositoryImpl.RoleRepoImpl;
import cs.repository.repositoryImpl.SysConfigRepoImpl;
import cs.repository.repositoryImpl.UserRepoImpl;

@Service
public class SysServiceImpl implements SysService {
	private static Logger logger = Logger.getLogger(SysServiceImpl.class);

	@Autowired
	private RoleRepoImpl roleRepo;
	@Autowired
	private UserRepoImpl userRepo;
	@Autowired
	private SysConfigRepoImpl sysConfigRepo;

	@Override
	public List<SysResourceDto> get() {

		List<SysResourceDto> resources = new ArrayList<SysResourceDto>();
		List<Class<?>> classes = ClassFinder.find("cs.controller");
		for (Class<?> obj : classes) {

			if (obj.isAnnotationPresent(RequestMapping.class)) {
				SysResourceDto resource = new SysResourceDto();

				Annotation classAnnotation = obj.getAnnotation(RequestMapping.class);
				RequestMapping classAnnotationInfo = (RequestMapping) classAnnotation;

				resource.setName(classAnnotationInfo.name());
				resource.setPath(classAnnotationInfo.path()[0]);

				List<SysResourceDto> operations = new ArrayList<SysResourceDto>();

				for (Method method : obj.getDeclaredMethods()) {

					if (method.isAnnotationPresent(RequestMapping.class)) {
						SysResourceDto operation = new SysResourceDto();

						Annotation methodAnnotation = method.getAnnotation(RequestMapping.class);
						RequestMapping methodAnnotationInfo = (RequestMapping) methodAnnotation;

						String httpMethod = methodAnnotationInfo.method().length == 0 ? "GET"
								: methodAnnotationInfo.method()[0].name();
						operation.setPath(String.format("%s#%s#%s", resource.getPath(), methodAnnotationInfo.path()[0].replace("{", "").replace("}", ""),
								httpMethod));
						operation.setName(String.format("%s(%s)", methodAnnotationInfo.name(), operation.getPath()));
						operation.setMethod(httpMethod);
						operations.add(operation);

					}
				}
				resource.setChildren(operations);
				resources.add(resource);
			}

		}
		return resources;
	}

	@Override
	@Transactional
	public Response SysInit() {
		Response response = new Response();
		List<SysConfig> sysConfigs = sysConfigRepo.findAll();
		SysConfig sysConfig;
		// 更新sysConfig

		if (sysConfigs.size() > 0) {// 已经被初始化
			response.setMessage("已经存在初始化数据，此次操作无效");
			logger.warn("已经存在初始化数据，此次操作无效");

		} else {// 未被初始化

			// 初始化角色
			Role role = new Role();
			role.setRoleName("超级管理员");
			role.setId(UUID.randomUUID().toString());
			role.setComment("系统初始化创建,不可删除");

			List<SysResourceDto> resourceDtos = this.get();
			List<Resource> resources = new ArrayList<>();
			resourceDtos.forEach(x -> {
				x.getChildren().forEach(y -> {
					Resource resource = new Resource();
					resource.setMethod(y.getMethod());
					resource.setName(y.getName());
					resource.setPath(y.getPath());
					resources.add(resource);

				});

			});
			role.setResources(resources);

			roleRepo.save(role);

			// 初始化用户
			User user = new User();
			user.setLoginName("admin");
			user.setId(UUID.randomUUID().toString());
			user.setPassword("admin");
			user.setComment("系统初始化创建,不可删除");
			user.setDisplayName("超级管理员");
			user.getRoles().add(role);
			userRepo.save(user);

			// 更新sysConfig
			sysConfig = new SysConfig();
			sysConfig.setInit(true);
			sysConfig.setId(UUID.randomUUID().toString());
			sysConfigRepo.save(sysConfig);

			response.setMessage("初始化成功");
			response.setSuccess(true);
			
			logger.info("系统初始化成功!");

		}
		return response;

	}
}
