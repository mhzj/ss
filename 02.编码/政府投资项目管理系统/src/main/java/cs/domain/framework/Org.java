package cs.domain.framework;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import cs.domain.BaseEntity;

/**
 * @Description: 部门表
 * @author: cx
 * @Date：2017年7月10日
 * @version：0.1
 */
@Entity
@Table(name = "cs_org")
public class Org extends BaseEntity {
    @Id
    private String id;
    @Column(columnDefinition = "varchar(255)  COMMENT '名字'")
    private String name;
    @Column(columnDefinition = "varchar(255)  COMMENT '备注'")
    private String comment;

    @Column(columnDefinition = "varchar(255)  COMMENT '部门标识'")
    private String orgIdentity;


    @ManyToMany(mappedBy = "orgs")
    private List<User> users = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrgIdentity() {
        return orgIdentity;
    }

    public void setOrgIdentity(String orgIdentity) {
        this.orgIdentity = orgIdentity;
    }


}
