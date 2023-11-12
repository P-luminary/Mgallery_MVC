package com.example.mgallery.controller;

import com.example.mgallery.entity.Painting;
import com.example.mgallery.service.PaintingService;
import com.example.mgallery.utils.PageModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//实现增删改查
@WebServlet("/management")
public class ManagementController extends HttpServlet {
    private PaintingService paintingService = new PaintingService();

    public ManagementController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        //通过method参数区分不同的操作
        String method = req.getParameter("method");
        if(Objects.equals(method,"list")) {//分页查询列表
            this.list(req,resp);
            //正确使用 equals 方法避免产生空指针异常https://www.jianshu.com/p/306de20dd228
        } else if (Objects.equals(method,"delete")) {
            this.delete(req,resp);
        } else if (Objects.equals(method,"show_create")) {
            this.showCreatePage(req,resp); //带show的一定是跳转页面
        } else if (Objects.equals(method, "create")) {
            this.create(req, resp);
        } else if (Objects.equals(method,"show_update")) {
            this.showUpdatePage(req, resp);
        } else if (Objects.equals(method,"update")) {
            this.update(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    // 控制器代码的实现
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String p = req.getParameter("p");
        String r = req.getParameter("r");
        if (p == null) {
            p = "1";
        }
        if (r == null) {
            r = "6";
        }
        //2.调用Service方法,得到处理结果      增加了按类型筛选category
        PageModel pageModel = paintingService.pagination(Integer.parseInt(p), Integer.parseInt(r));
        req.setAttribute("pageModel",pageModel);//数据解耦最关键的一步
        //3.请求转发至对应JSP(view)进行数据展现
        req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req,resp); //跳转jsp
    }
    //显示新增页面
    private void showCreatePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(req, resp); //请求转发
    }
    //新增油画方法
    private void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*文件上传时的数据处理与标准表单完全不同
        String pname = req.getParameter("pname"); form表单enctype = "multipart/form-data"运用后 无法得到字符串格式的数据
        System.out.println(pname);
        req.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(req, resp); 请求转发*/
        //1.初始化FileUpload组件 包含表单项的数据对象
        FileItemFactory factory = new DiskFileItemFactory();
        /**
         * FileItemFactory 用于将前端表单的数据转换为一个个FileItem对象
         * ServletFileUpload 是为FileUpload组件提供Java web的Http请求解析
         */
        ServletFileUpload sf = new ServletFileUpload(factory);
        //2.遍历所有FileItem
        try {
            List<FileItem> formData = sf.parseRequest(req);//将表单数据转换为FileItem对象 和前台输入项一一对应
            Painting painting = new Painting();//进行封装
            //区分哪个是普通对象 哪个是文件对象
            for (FileItem fi : formData){
                if (fi.isFormField() == true){//普通输入框
                    System.out.println("普通输入项:" + fi.getFieldName() + ":" + fi.getString("UTF-8"));
                    switch (fi.getFieldName()) { //得到字段名
                        case "pname":
                            painting.setPname(fi.getString("UTF-8"));
                            break;
                        case "category":
                            painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "price":
                            painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "description":
                            painting.setDescription(fi.getString("UTF-8"));
                            break;
                        default:
                            break;
                    }
                }else { //文件上传框
                    System.out.println("文件上传项:" + fi.getFieldName()); //没有文本数值不用输出
                    //3.文件保存到服务器目录 已经确定了文件上传项
                    String path = req.getServletContext().getRealPath("/upload");
                    System.out.println("上传文件目录:" + path);
//                    String fileName = "test.jpg";
                    String fileName = UUID.randomUUID().toString();//随机生成文件名 根据计算机的特性 生成随机唯一字符串
                    //fi.getName()得到原始文件名, 截取最后一个"."后所有字符串，例如:wxml.jpg -> .jpg
                    String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
                    fi.write(new File(path, fileName + suffix)); //传入文件对象会自动的帮我们把客户端上传的文件传到服务器某个文件中
                    painting.setPreview("upload/" + fileName + suffix); //形成一个完整的可以访问的url地址
                }
            }
            paintingService.create(painting); //新增功能
            //若弹出另一个页面进行另外一个操作 新页面以后的后续操作 和前面的操作紧密联系的 用请求转发 当前请求給下一个功能继续操作
            resp.sendRedirect("/mgallery/management?method=list");//响应重定向跳转列表页继续相应的处理 跟前面的新增功能联系不那么紧密
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //显示更新页面
    private void showUpdatePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");//前台传来的id号
        Painting painting = paintingService.findById(Integer.parseInt(id));
        req.setAttribute("painting", painting); //将得到的放入其中
        req.getRequestDispatcher("/WEB-INF/jsp/update.jsp").forward(req,resp);
    }

    /**
     * k客户端采用Ajax方式提交Http请求
     * Controller方法处理后不再跳转任何jsp，而是通过响应输出JSON格式字符串
     * Tips：作为Ajax与服务器交互后，得到的不是整页HTML，而是服务器处理后的数据
     * @param request
     * @param response
     * @throws IOException
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            paintingService.delete(Integer.parseInt(id));
            //{"result":"ok"}
            out.println("{\"result\":\"ok\"}");
        }catch(Exception e) {
            e.printStackTrace();
            out.println("{\"result\":\"" + e.getMessage() + "\"}");
        }

    }
    private void update(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        int isPreviewModified = 0;
        //文件上传时的数据处理与标准表单完全不同
		/*
		String pname = request.getParameter("pname");
		System.out.println(pname);
		*/
        //1. 初始化FileUpload组件
        FileItemFactory factory = new DiskFileItemFactory();
        /**
         * FileItemFactory 用于将前端表单的数据转换为一个个FileItem对象
         * ServletFileUpload 则是为FileUpload组件提供Java web的Http请求解析
         */
        ServletFileUpload sf = new ServletFileUpload(factory);
        //2. 遍历所有FileItem
        try {
            List<FileItem> formData = sf.parseRequest(request);
            Painting painting = new Painting();
            for(FileItem fi:formData) {
                if(fi.isFormField()) {
                    System.out.println("普通输入项:" + fi.getFieldName() + ":" + fi.getString("UTF-8"));
                    switch (fi.getFieldName()) {
                        case "pname":
                            painting.setPname(fi.getString("UTF-8"));
                            break;
                        case "category":
                            painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "price":
                            painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "description":
                            painting.setDescription(fi.getString("UTF-8"));
                            break;
                        case "id":
                            painting.setId(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "isPreviewModified":
                            isPreviewModified = Integer.parseInt(fi.getString("UTF-8"));
                            break;
                        default:
                            break;
                    }
                }else {
                    if(isPreviewModified == 1) {
                        System.out.println("文件上传项:" + fi.getFieldName());
                        //3.文件保存到服务器目录
                        String path = request.getServletContext().getRealPath("/upload");
                        System.out.println("上传文件目录:" + path);
                        //String fileName = "test.jpg";
                        String fileName = UUID.randomUUID().toString();
                        //fi.getName()得到原始文件名,截取最后一个.后所有字符串,例如:wxml.jpg->.jpg
                        String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
                        //fi.write()写入目标文件
                        fi.write(new File(path,fileName + suffix));
                        painting.setPreview("/upload/" + fileName + suffix);
                    }
                }
            }
            //更新数据的核心方法
            paintingService.update(painting, isPreviewModified);
            response.sendRedirect("/mgallery/management?method=list");//返回列表页
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
