package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    public boolean active(String code);

    public boolean regist(User user);
}
