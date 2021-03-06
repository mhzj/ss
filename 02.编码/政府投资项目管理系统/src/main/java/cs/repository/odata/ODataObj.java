package cs.repository.odata;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.sn.framework.common.StringUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class ODataObj {
    private String orderby;
    private boolean orderbyDesc;
    private String[] select;
    private int skip;
    private int top;
    private boolean isCount;
    private int count;


    @SuppressWarnings("rawtypes")
    private List<ODataFilterItem> filter = new ArrayList<>();

    public String getOrderby() {
        return orderby;
    }

    public boolean isOrderbyDesc() {
        return orderbyDesc;
    }

    public String[] getSelect() {
        return select;
    }

    public int getSkip() {
        return skip;
    }

    public int getTop() {
        return top;
    }

    public boolean getIsCount() {
        return isCount;
    }

    public int getCount() {
        return count;
    }

    @SuppressWarnings("rawtypes")
    public List<ODataFilterItem> getFilter() {
        return filter;
    }

    private static Logger logger = Logger.getLogger(ODataObj.class);

    // method
    public ODataObj(HttpServletRequest request) throws ParseException {
        // get parameter
        String filter = request.getParameter("$filter");
        //去除filter的前后括号
        if (StringUtil.isNotBlank(filter)) {
            try {
//                // 解码，避免中文乱码
                filter = URLDecoder.decode(filter, StringUtil.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (filter.startsWith("(")) {
                filter = filter.substring(1, filter.length() - 1);
            }
        }
        String orderby = request.getParameter("$orderby");
        String select = request.getParameter("$select");
        String skip = request.getParameter("$skip");
        String top = request.getParameter("$top");
        String inlinecount = request.getParameter("$inlinecount");

        BuildObj(filter, orderby, select, skip, top, inlinecount);

    }

    public ODataObj() {

    }

    @SuppressWarnings("rawtypes")
    public void BuildObj(String filter, String orderby, String select, String skip, String top, String inlinecount)
            throws ParseException {
        // build odata object
        // build orderby
        if (orderby != null && !orderby.isEmpty()) {
            String[] orderbyItems = orderby.trim().split(" ");
            this.orderby = orderbyItems[0];
            if (orderbyItems.length == 2 && orderbyItems[1].toLowerCase().equals("desc")) {
                this.orderbyDesc = true;
            }
        }
        // build select
        if (select != null && !select.isEmpty()) {
            this.select = select.split(",");
        }
        // build skip,top,inlinecount
        if (skip != null && !skip.isEmpty()) {
            this.skip = Integer.parseInt(skip);
        }
        if (top != null && !top.isEmpty()) {
            this.top = Integer.parseInt(top);
        }
        if (inlinecount != null && inlinecount.toLowerCase().equals("allpages")) {
            this.isCount = true;
        }

        // build filter
        if (filter != null && !filter.isEmpty()) {
            List<ODataFilterItem> filterItemsList = new ArrayList<ODataFilterItem>();
            String[] filters = filter.split("and");
            for (String filterItem : filters) {
                filterItem = filterItem.trim();
                // handle like
                if (filterItem.contains("substringof")) {
                    ODataFilterItem<String> oDataFilterItem = new ODataFilterItem<String>();
                    Pattern patternField = Pattern.compile(",(.*?)\\)");
                    Pattern patternValue = Pattern.compile("'(.*?)'");
                    Matcher matcherField = patternField.matcher(filterItem);
                    Matcher matcherValue = patternValue.matcher(filterItem);
                    if (matcherField.find() && matcherValue.find()) {
                        oDataFilterItem.setField(matcherField.group(1));
                        oDataFilterItem.setValue(matcherValue.group(1));
                        oDataFilterItem.setOperator("like");
                        filterItemsList.add(oDataFilterItem);
                    }

                }
                // handle eq,ne,gt,ge,lt,le
                else {

                    String[] filterItems = filterItem.split(" ");
                    if (filterItems.length == 3) {
                        if (filterItems[2].toLowerCase().contains("datetime")) {// datetime
                            ODataFilterItem<Date> oDataFilterItem = new ODataFilterItem<Date>();

                            oDataFilterItem.setField(filterItems[0]);
                            oDataFilterItem.setOperator(filterItems[1]);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String date = filterItems[2].toLowerCase().replaceAll("datetime", "").replaceAll("'", "");
                            oDataFilterItem.setValue(dateFormat.parse(date));
                            filterItemsList.add(oDataFilterItem);
                        } else if (filterItems[2].toLowerCase().contains("guid")) {//guid
                            ODataFilterItem<UUID> oDataFilterItem = new ODataFilterItem<UUID>();

                            oDataFilterItem.setField(filterItems[0]);
                            oDataFilterItem.setOperator(filterItems[1]);

                            UUID id = UUID.fromString(filterItems[2].toLowerCase().replaceAll("guid", "").replaceAll("'", ""));
                            oDataFilterItem.setValue(id);
                            filterItemsList.add(oDataFilterItem);
                        } else if (filterItems[2].contains("'")) {// string
                            ODataFilterItem<String> oDataFilterItem = new ODataFilterItem<String>();

                            oDataFilterItem.setField(filterItems[0]);
                            oDataFilterItem.setOperator(filterItems[1]);
                            oDataFilterItem.setValue(filterItems[2].replaceAll("'", ""));
                            filterItemsList.add(oDataFilterItem);
                        } else if (filterItems[2].equalsIgnoreCase("false") || filterItems[2].equalsIgnoreCase("true")) {//boolean
                            ODataFilterItem<Boolean> oDataFilterItem = new ODataFilterItem<Boolean>();

                            oDataFilterItem.setField(filterItems[0]);
                            oDataFilterItem.setOperator(filterItems[1]);
                            oDataFilterItem.setValue(Boolean.parseBoolean(filterItems[2]));
                            filterItemsList.add(oDataFilterItem);
                        } else if(filterItems[1].toLowerCase().contains("ge")
                                ||filterItems[1].toLowerCase().contains("le")){
                            ODataFilterItem<Double> oDataFilterItem = new ODataFilterItem<Double>();

                            oDataFilterItem.setField(filterItems[0]);
                            oDataFilterItem.setOperator(filterItems[1]);
                            oDataFilterItem.setValue(Double.valueOf(filterItems[2].toString()));
                            filterItemsList.add(oDataFilterItem);
                        }else {// int
                            ODataFilterItem<Integer> oDataFilterItem = new ODataFilterItem<Integer>();

                            oDataFilterItem.setField(filterItems[0]);
                            oDataFilterItem.setOperator(filterItems[1]);
                            oDataFilterItem.setValue(Integer.parseInt(filterItems[2]));
                            filterItemsList.add(oDataFilterItem);
                        }

                    }

                }

            } // for
            this.filter = filterItemsList;
        } // if
    }

    @SuppressWarnings("rawtypes")
    public Criteria buildQuery(Criteria criteria) {
        logger.debug("begin:buildQuery");

        // ����filter
        if (filter != null) {
            for (ODataFilterItem oDataFilterItem : filter) {
                String field = oDataFilterItem.getField();
                String operator = oDataFilterItem.getOperator();
                Object value = oDataFilterItem.getValue();
                logger.debug(String.format("fitler:field:%s,operator:%s,value:%s", field, operator, value));
                switch (operator) {
                    case "like":
                        criteria.add(Restrictions.like(field, "%" + value + "%"));
                        break;
                    case "eq":
                        criteria.add(Restrictions.eq(field, value));
                        break;
                    case "ne":
                        criteria.add(Restrictions.ne(field, value));
                        break;
                    case "gt":
                        criteria.add(Restrictions.gt(field, value));
                        break;
                    case "ge":
                        criteria.add(Restrictions.ge(field, value));
                        break;
                    case "lt":
                        criteria.add(Restrictions.lt(field, value));
                        break;
                    case "le":
                        criteria.add(Restrictions.le(field, value));
                        break;
                    case "isNull":
                        criteria.add(Restrictions.isNull(field));
                        break;
                    case "notIn":
                        criteria.add(Restrictions.not(Restrictions.in(field,value)));
                    default:
                        break;
                }
            }
        }
        //ͳ������
        if (this.isCount) {

            Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
            this.count = totalResult;
            criteria.setProjection(null);
            logger.debug("count:" + totalResult);
        }
        // �����ҳ
        if (this.top != 0) {
            criteria.setFirstResult(this.skip);
            criteria.setMaxResults(this.top);
        }
        // ����orderby
        if (this.orderby != null && !this.orderby.isEmpty()) {
            if (this.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(this.orderby).desc());
            } else {
                criteria.addOrder(Property.forName(this.orderby).asc());
            }
        }
        logger.debug("end:buildQuery");
        return criteria;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setOrderbyDesc(boolean orderbyDesc) {
        this.orderbyDesc = orderbyDesc;
    }

    public void setSelect(String[] select) {
        this.select = select;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setCount(boolean isCount) {
        this.isCount = isCount;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @SuppressWarnings("rawtypes")
    public void setFilter(List<ODataFilterItem> filter) {
        this.filter = filter;
    }

}
