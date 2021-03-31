package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private final RouteService routeService = new RouteServiceImpl();
    private final FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收参数
        String currentPageStr = req.getParameter("currentPage");
        String pageSizeStr = req.getParameter("pageSize");
        String cidStr = req.getParameter("cid");

        //接收线路名称
        String rname = req.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");

        int cid = 0; //类别id
        if (cidStr != null && cidStr.length() > 0 && "null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0; //当前页码，默认为第一页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        int pageSize = 0; //每页显示条数，默认每页显示5条记录
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        //确定了当前页码和每页显示条数，就可以获取pageBean对象
        PageBean<Route> pageBean = routeService.pageQuery(cid, currentPage, pageSize, rname);
        writeJson(pageBean, req, resp);
    }

    /**
     * 根据id查询一个旅游路线的详细信息
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rid = req.getParameter("rid");
        Route route = routeService.findOne(rid);
        writeJson(route, req, resp);
    }

    public void isFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rid = req.getParameter("rid");
        User user = (User) req.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            uid = 0;
        } else {
            uid = user.getUid();
        }
        boolean flag = favoriteService.isFavorite(rid, uid);
        writeJson(flag, req, resp);
    }

    public void addFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rid = req.getParameter("rid");
        User user = (User) req.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            return;
        } else {
            uid = user.getUid();
        }
        favoriteService.add(rid, uid);
    }
}
