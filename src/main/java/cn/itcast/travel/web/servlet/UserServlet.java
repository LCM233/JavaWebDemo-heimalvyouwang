package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.LoginServiceImpl;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 对应tab_user表的servlet
 */
@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();

    /**
     * 登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service查询
        User loginUser = new LoginServiceImpl().login(user);
        //创建resultinfo对象
        ResultInfo resultInfo = new ResultInfo();
        //判断是否有用户,用户是否激活
        if (loginUser == null) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("账号或密码错误");
        } else if (!"Y".equals(loginUser.getStatus())) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("账号未完成邮箱验证");
        } else {
            resultInfo.setFlag(true);
            request.getSession().setAttribute("user", loginUser);
        }
        //设置响应编码
        response.setContentType("application/json;charset=utf-8");
        //将resultinfo响应回浏览器
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), resultInfo);
    }

    /**
     * 激活用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String msg = ""; //响应信息
        if (code != null) {
            boolean flag = service.active(code);
            //判断flag
            if (flag) {
                msg = "激活成功，请<a href='login.html'>登录</a>";
            } else {
                msg = "激活失败，请联系管理员";
            }
        }
        response.getWriter().write(msg);
    }

    /**
     * 从session中查找user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //session中获取user
        Object user = request.getSession().getAttribute("user");
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), user);
    }

    /**
     * 注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regis(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //判断验证码是否正确start
        String check = request.getParameter("check"); //获取表单的验证码内容
        HttpSession session = request.getSession();
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER"); //获取CheckCodeServlet中的验证码值
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            //情况一：验证码不正确
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //ResultInfo对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return; //不执行下面的封装user对象代码
        }

        //情况二：验证成功，封装对象，调用service层方法上传数据库
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        service.regist(user);
    }

    /**
     * 退出用户，销毁session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate(); //销毁session
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}
