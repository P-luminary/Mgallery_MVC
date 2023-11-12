package com.example.mgallery.service;

import com.example.mgallery.dao.PaintingDao;
import com.example.mgallery.entity.Painting;
import com.example.mgallery.utils.PageModel;

import java.util.List;
//完成业务逻辑 service与dao进行传递调用
public class PaintingService {
    private PaintingDao paintingDao = new PaintingDao();
    public PageModel pagination(int page, int rows, String...category){  //最后一个是添加 可选参数(可能/不一定出现一个或多个)
        if (rows == 0){
            throw new RuntimeException("无效的rows参数");
        }
        if (category.length==0 || category[0] == null){
        return paintingDao.pagination(page, rows); //调用并返回
        }else { //程序进行可选调用 两个不同路径 尽量不要去修改类结构
            return paintingDao.pagination(Integer.parseInt(category[0]), page, rows);
        }
    }

    public void create(Painting painting){
        paintingDao.create(painting);
    }
    // 按编号查询油画 id油画编号 return油画对象
    public Painting findById(Integer id){
        Painting p = paintingDao.findById(id);
        if (p==null){
            throw new RuntimeException("[id=]" + id + "]油画不存在");
        }
        return p;
    }

    /**
     * 更新业务逻辑
     * @param newPainting 新的油画数据
     * @param isPreviewModified 是否修改Preview属性
     */
    public void update(Painting newPainting,Integer isPreviewModified) {
        //createtime:
        //在原始数据基础上覆盖更新
        Painting oldPainting = this.findById(newPainting.getId());
        oldPainting.setPname(newPainting.getPname());
        oldPainting.setCategory(newPainting.getCategory());
        oldPainting.setPrice(newPainting.getPrice());
        oldPainting.setDescription(newPainting.getDescription());
        if(isPreviewModified == 1) {
            oldPainting.setPreview(newPainting.getPreview());
        }
        paintingDao.update(oldPainting);
    }


    /**
     * 按id号删除数据
     * @param id
     */
    public void delete(Integer id) {
        paintingDao.delete(id);
    }

    public static void main(String[] args) {
        PaintingService paintingService = new PaintingService();
        PageModel pageModel = paintingService.pagination(2, 6);//每页显示六条
        List<Painting> paintingList = pageModel.getPageData();
        for (Painting painting : paintingList){
            System.out.println(painting.getPname());
        }
        System.out.println(pageModel.getPageStartRow() + ":" + pageModel.getPageEndRow());

    }
}
