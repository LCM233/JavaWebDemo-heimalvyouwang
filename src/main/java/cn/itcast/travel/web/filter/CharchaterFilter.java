package cn.itcast.travel.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    每次发起请求和响应都会进行的字符编码加强的过滤器
 */
@WebFilter("/*")
public class CharchaterFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        //解决post请求中文数据乱码问题
        String method = request.getMethod();
        if (method.equalsIgnoreCase("post")){
            request.setCharacterEncoding("utf-8");
        }
        //解决响应乱码问题
        response.setContentType("text/html;charset=utf-8");

        chain.doFilter(request,response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
