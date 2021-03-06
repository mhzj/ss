package cs.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 工作日管理
 * @author MCL
 *@date 2017年6月28日 上午10:08:10 
 */
@Entity
@Table(name="cs_workday")
public class Workday extends BaseEntity{
	
	@Id
	private String id;
	
	@Column(columnDefinition="date")
	private Date dates;	//日期
	
	@Column(columnDefinition="VARCHAR(2)")
	private String status;	//状态    1：将工作日改为休息日     2：将休息日改为工作日     0:默认值
	
	@Column(columnDefinition="VARCHAR(128)")
	private String remark;	//备注
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Date getDates() {
		return dates;
	}

	public void setDates(Date dates) {
		this.dates = dates;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
