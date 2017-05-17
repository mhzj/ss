package cs.service.common;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.domain.BasicData;
import cs.domain.BasicData_;
import cs.model.management.BasicDataDto;
import cs.repository.common.BasicDataRepo;

@Service
public class BasicDataServiceImpl implements BasicDataService {
	// 依赖注入持久层
	@Autowired
	private BasicDataRepo basicDataRepo;

	@Override
	@Transactional
	public String queryValueById(String id) {
		String value = "";
		if (id != null) {
			BasicData basicData = basicDataRepo.findById(id);
			value = basicData.getDescription();
		} else {
			value = "未知";
		}

		return value;
	}

	/**
	 * @deprecated 根据标识查询出类型集合
	 * @param identity
	 *            标识编号
	 * @return list 类型集合
	 * @author cx
	 * @date 2017-05-09
	 */
	@Override
	@Transactional
	public List<BasicDataDto> queryByIdentity(String identity) {
		List<BasicDataDto> basicDataDtos = new ArrayList<>();

		if (identity != null && !identity.equals("")) {
			Criterion criterion = Restrictions.eq(BasicData_.identity.getName(), identity);
			List<BasicData> basicDatas = basicDataRepo.findByCriteria(criterion);

			basicDatas.forEach(item -> {
				BasicDataDto basicDataDto = new BasicDataDto();
				basicDataDto.setComment(item.getComment());
				basicDataDto.setDescription(item.getDescription());
				basicDataDto.setCreatedBy(item.getCreatedBy());
				basicDataDto.setCreatedDate(item.getCreatedDate());
				basicDataDto.setpId(item.getpId());
				basicDataDto.setId(item.getId());
				basicDataDto.setIdentity(item.getIdentity());
				basicDataDto.setModifiedDate(item.getModifiedDate());
				basicDataDto.setModifiedBy(item.getModifiedBy());
				//查找孩子
				List<BasicDataDto> children=queryByParentId(item.getId());
				basicDataDto.setChildren(children);
				basicDataDtos.add(basicDataDto);
			});

		}

		return basicDataDtos;
	}

	@Override
	@Transactional
	public List<BasicDataDto> queryByParentId(String pId) {

		Criterion criterion = Restrictions.eq(BasicData_.pId.getName(), pId);
		List<BasicData> basicDataList = basicDataRepo.findByCriteria(criterion);
		List<BasicDataDto> basicDataDtoList = new ArrayList<>();
		basicDataList.forEach(x -> {
			BasicDataDto basicDataDto = new BasicDataDto();

			basicDataDto.setComment(x.getComment());
			basicDataDto.setDescription(x.getDescription());
			basicDataDto.setCreatedBy(x.getCreatedBy());
			basicDataDto.setCreatedDate(x.getCreatedDate());
			basicDataDto.setpId(x.getpId());
			basicDataDto.setId(x.getId());
			basicDataDto.setIdentity(x.getIdentity());
			basicDataDto.setModifiedDate(x.getModifiedDate());
			basicDataDto.setModifiedBy(x.getModifiedBy());

			//查找孩子
			List<BasicDataDto> children=queryByParentId(x.getId());
			basicDataDto.setChildren(children);			
			basicDataDtoList.add(basicDataDto);

		});

		return basicDataDtoList;
	}

}
