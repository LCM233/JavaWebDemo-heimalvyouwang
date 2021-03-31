package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean active(String code) {
        User user = userDao.findByCode(code);
        if (user!=null){
            userDao.updateStatus(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean regist(User user) {
        /*
            1、根据用户名查找user是否存在
            2、存在，返回false
            3、不存在，返回true，并保存
            4、保存后，还得设置激活状态,设置激活码（使用唯一字符串工具UnidUtil）
            5、发送激活邮件
         */
        User byUsername = userDao.findByUsername(user.getUsername());
        if (byUsername !=null){
            return false;
        }else {
            user.setCode(UuidUtil.getUuid());
            user.setStatus("N");
            userDao.save(user);
            //邮件正文，是一条超链接
            String content = "<a href='http://localhost/travel/user/activeUser?code='"+ user.getCode() +">";
            //发送邮件
            MailUtils.sendMail(user.getEmail(),content,"激活邮件");
            return true;
        }
    }
}
