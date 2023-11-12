package com.example.mgallery.dao;

import com.example.mgallery.entity.Painting;
import com.example.mgallery.utils.PageModel;
import com.example.mgallery.utils.XmlDataSource;

import java.util.ArrayList;
import java.util.List;

//获得最原始的 对其进行分页处理
public class PaintingDao {
    public PageModel pagination(int page, int rows) {
        //Painting油画对象集合
        List<Painting> list = XmlDataSource.getRawData();
        //PageModel分页处理得到分页数据及分页附加
        PageModel pageModel = new PageModel(list, page, rows);
        return pageModel;
    }

    public PageModel pagination(int catagory, int page, int rows) { //int catagory添加Dao层对数据进行筛选
        List<Painting> list = XmlDataSource.getRawData();
        List<Painting> categoryList = new ArrayList();
        for (Painting p : list) {
            //如果等于从外侧添加的筛选条件 则添加categoryList内
            if (p.getCategory() == catagory) {
                categoryList.add(p);
            }
        }
        PageModel pageModel = new PageModel(categoryList, page, rows);
        return pageModel;
    }
    /**
     * 数据新增
     */
    public void create(Painting painting){//只做xml传递调用
        XmlDataSource.append(painting);//依次调用
    }
    public Painting findById(Integer id){
        List<Painting> data = XmlDataSource.getRawData(); //得到原始数据
        Painting painting = null;
        for (Painting p : data){
            if (p.getId() == id){
                painting = p;
                break;
            }
        }
        return painting;
    }
    public void update(Painting painting) {
        XmlDataSource.update(painting);
    }

    public void delete(Integer id) {
        XmlDataSource.delete(id);
    }
}

