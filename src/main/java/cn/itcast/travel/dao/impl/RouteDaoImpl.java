package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据cid获取总页数
     *
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        String sql = "select count(*) from tab_route where 1=1";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList(); //存放拼接sql的?对应值

        //判断
        if (cid != 0) {
            sb.append("and cid = ?");
            params.add(cid);
        }
        if (rname != null && rname.length() > 0) {
            sb.append("and rname like ?");
            params.add("%" + rname + "%"); //%rname%包含的意思
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, params.toArray()); //返回integer类型
    }

    /**
     * 根据cid 开始条目 页面容纳大小返回route列表
     *
     * @param cid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        String sql = "select * from tab_route where 1=1";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();

        if (cid != 0) {
            sb.append("and cid = ?");
            params.add(cid);
        }
        if (rname != null && rname.length() > 0) {
            sb.append("and rname like ?");
            params.add("%" + rname + "%");
        }
        sb.append("limit ?,?"); //分页条件
        sql = sb.toString();
        params.add(start);
        params.add(pageSize);


        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }

    /**
     * 根据rid查找路线route
     *
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
    }


}
