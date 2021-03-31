import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class test011 {
    @Test
    public void test01(){
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

        List<Category> cs = null;
        try {
            String sql = "select * from tab_category";
            List<Category> list = template.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
        }catch (DataAccessException e){
            //避免读取失败报错，错误不打印
        }
        for (Category c : cs) {
            System.out.println(c.toString());
        }
    }
    @Test
    public void test02(){
        List list = new ArrayList();
        list.add("123456");
        list.add("asdfghg");
        System.out.println(list.toArray()[0]);
    }
}
