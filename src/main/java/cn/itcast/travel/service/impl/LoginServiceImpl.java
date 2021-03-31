package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.LoginService;

public class LoginServiceImpl implements LoginService {
    @Override
    public User login(User user) {
        return new UserDaoImpl().findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
