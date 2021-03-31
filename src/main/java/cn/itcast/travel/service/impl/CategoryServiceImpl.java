package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private static CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //分类的数据不会经常产生变化，所有可以使用redis来缓存这个数据
        Jedis jedis = JedisUtil.getJedis();
        //Set<String> categories = jedis.zrange("category", 0, -1);//从redis取
        //scores
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);
        List<Category> categoryList = null;
        //判断查询categories是否为空
        if (categories == null || categories.size() == 0) {
            //没有就要从关系数据库中取
            categoryList = categoryDao.findAll();
            //存入redis
            for (Category category : categoryList) {
                jedis.zadd("category", category.getCid(), category.getCname());
            }
        }else {
            //有就直接存入categorylist
            categoryList = new ArrayList<>();
            for (Tuple tuple : categories){
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                categoryList.add(category);
            }
        }
        return categoryList;
    }


}
