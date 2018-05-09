package cs.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import cs.common.BasicDataConfig;
import cs.domain.ShenBaoInfo;
import cs.domain.ShenBaoInfo_;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObjNew;
import cs.repository.odata.OdataFilter;
import cs.repository.odata.OdataFilter.Operate;
/**
 * @Description: 申报信息持久层
 * @author: cx
 * @Date：2017年7月10日
 * @version：0.1
 */
@Repository
public class ShenBaoInfoRepoImpl extends AbstractRepository<ShenBaoInfo	, String> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ShenBaoInfo> findByOdata2(ODataObjNew odataObj, List<String> taskIds, String str) {
	logger.debug("findByOdata2");	
		
		List<OdataFilter> idsFilter = new ArrayList<OdataFilter>(taskIds.size());
		List<OdataFilter> idsFilter2 = new ArrayList<OdataFilter>(taskIds.size());
		List<OdataFilter> idsFilter3 = new ArrayList<OdataFilter>(taskIds.size());
		taskIds.forEach(x -> {
			idsFilter.add(new OdataFilter(ShenBaoInfo_.zong_processId.getName(), Operate.EQ, x));
		});
		OdataFilter orFilter = new OdataFilter(null, Operate.OR, idsFilter);
		if(str.equals("plan")){
			idsFilter2.add(new OdataFilter(ShenBaoInfo_.projectShenBaoStage.getName(), Operate.EQ, BasicDataConfig.projectShenBaoStage_planReach));
			
			idsFilter2.add(orFilter);
			OdataFilter orFilter2 =new OdataFilter(null, Operate.AND, idsFilter2);
			odataObj.addFilter(orFilter2);
			
		}else if(str.equals("all")){
			
		}else{
			idsFilter2.add(new OdataFilter(ShenBaoInfo_.projectShenBaoStage.getName(), Operate.EQ, BasicDataConfig.projectShenBaoStage_XMJYS));
			idsFilter2.add(new OdataFilter(ShenBaoInfo_.projectShenBaoStage.getName(), Operate.EQ, BasicDataConfig.projectShenBaoStage_KXXYJBG));
			idsFilter2.add(new OdataFilter(ShenBaoInfo_.projectShenBaoStage.getName(), Operate.EQ, BasicDataConfig.projectShenBaoStage_ZJSQBG));
			idsFilter2.add(new OdataFilter(ShenBaoInfo_.projectShenBaoStage.getName(), Operate.EQ, BasicDataConfig.projectShenBaoStage_CBSJGS));
			
			OdataFilter orFilter2 = new OdataFilter(null, Operate.OR, idsFilter2);
			idsFilter3.add(orFilter);
			idsFilter3.add(orFilter2);
			OdataFilter orFilter3 =new OdataFilter(null, Operate.AND, idsFilter3);
			odataObj.addFilter(orFilter3);
		}
		
		return odataObj.createQuery(getSession(), ShenBaoInfo.class).list();
	}

}
