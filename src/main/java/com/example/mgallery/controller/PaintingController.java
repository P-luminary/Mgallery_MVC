package com.example.mgallery.controller;

import com.example.mgallery.service.PaintingService;
import com.example.mgallery.utils.PageModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/page")
public class PaintingController extends HttpServlet {
    private PaintingService paintingService = new PaintingService();
    public PaintingController(){
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.接收Http数据
        String page = req.getParameter("p"); //页号
        String rows = req.getParameter("r"); //每页记录数
        String category = req.getParameter("c");
        if (page == null){
            page = "1"; //没传入则默认查询第一个
        }if (rows == null){
            rows = "6"; //每页默认显示六条数据
        }
        //2.调用Service方法,得到处理结果      增加了按类型筛选category
        PageModel pageModel = paintingService.pagination(Integer.parseInt(page), Integer.parseInt(rows), category);
        req.setAttribute("pageModel",pageModel);//数据解耦最关键的一步
        //3.请求转发至对应JSP(view)进行数据展现
        req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req,resp); //跳转jsp
    }
}
