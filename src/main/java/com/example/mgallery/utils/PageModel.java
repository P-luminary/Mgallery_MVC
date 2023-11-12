package com.example.mgallery.utils;

import java.util.ArrayList;
import java.util.List;


public class PageModel {
    private int page;
    private int totalPages;
    private int rows; //每页几条数据
    private int totalRows; //原始数据多少条
    private int pageStartRow; //当前页是从第几行开始的
    private int pageEndRow; //到第几行结束的 结束行号
    private boolean hasNextPage; //是否下一页(尾页)
    private boolean hasPreviousPage; //是否上一页(首页)
    private List pageData; //当前页面数据

    public PageModel() {
    }

    /**
     * 初始化PageMode1对象，计算分页属性
     * @param page
     * @param rows
     * @param data
     */
    public PageModel(List data, int page, int rows) {
        this.page = page;
        this.rows = rows;
        totalRows = data.size();
        //总页数计算规则：总行数/每页记录数，能整除页数取整，不能整除向上取整
        //18/6=3 | 2/6≈3.33 向上取整=4  intValue()得到整数部分
        //Nath.ceil向上取整  Math.floor浮点数向下取整
        //小细节: 20/6≈3.33 但是totalRows 和 rows都是整数 20/6=3 向上取整还是3
        //仅需在一个后面×1f即可解决问题 (rows*1f)默认类型转换返回浮点数
        totalPages = new Double(Math.ceil(totalRows/(rows * 1f))).intValue();
        pageStartRow = (page - 1) * rows; //0
        pageEndRow = page * rows; //6
        //totalRows:20 | totalPage:4 | rows:6
        //pageEndRow=4*6=24>20 执行subList()抛出下标越界异常
        if (pageEndRow > totalRows){
            pageEndRow = totalRows; //让20作为结束行号 不会越界
        }
        pageData = data.subList(pageStartRow, pageEndRow); //得到分页数据
        if (page > 1) {
            hasPreviousPage = true;
        }else {
            hasPreviousPage = false;
        }
        if (page < totalPages) { //判断是否存在下一页
            hasNextPage = true;
        }else {
            hasNextPage = false;
        }
    }

    public static void main(String[] args) {
        List sample = new ArrayList();
        for (int i = 1; i < 100; i++) {
            sample.add(i);
        }
        PageModel pageModel = new PageModel(sample,6,8);
        System.out.println(pageModel.getPageData()); //当前页的list集合
        System.out.println(pageModel.getTotalPages());
        System.out.println(pageModel.getPageStartRow() + ":" + pageModel.getPageEndRow());
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public void setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public void setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public List getPageData() {
        return pageData;
    }

    public void setPageData(List pageData) {
        this.pageData = pageData;
    }
}
