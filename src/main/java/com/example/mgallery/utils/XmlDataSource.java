package com.example.mgallery.utils;

import com.example.mgallery.entity.Painting;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

//用于将XML文件解析为Java对象
public class XmlDataSource {
    //通过static静态关键字保证数据全局唯一
    private static List data = new ArrayList(); //油画集合
    private static String dataFile;

    static { //程序运行以后去得到类路径目录下的/painting.xml的路径地址
        dataFile = XmlDataSource.class.getResource("/painting.xml").getPath();
        reload(); //在所有的写入操作以后都要写入
    }
    private static void reload(){
        //若得到特殊字符进行编码转换 空格 c:\new style\painting.xml
        try {
            URLDecoder.decode(dataFile, "UTF-8");
            System.out.println(dataFile);
            //利用Dom4j对XML进行解析 读取XML
            SAXReader reader = new SAXReader();
            //1.获取Document文档对象
            Document document = reader.read(dataFile);
            //2.Xpath得到XML节点集合 获取多个xml节点
            List<Node> nodes = document.selectNodes("/root/painting");
            data.clear(); //清空 在空的数据基础上重新添加
            for (Node node : nodes) {
                Element element = (Element) node;
                String id = element.attributeValue("id");//获得id
                String pname = element.elementText("pname");//获得子节点
                Painting painting = new Painting();
                painting.setId(Integer.parseInt(id));
                painting.setPname(pname);
                painting.setCategory(Integer.parseInt(element.elementText("category")));
                painting.setPrice(Integer.parseInt(element.elementText("price")));
                painting.setPreview(element.elementText("preview"));
                painting.setDescription(element.elementText("description"));
                data.add(painting);//将List data 保存油画的完整信息

                System.out.println(id + ";" + pname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取所有油画Painting对象
     *
     * @return Painting List
     */
    public static List<Painting> getRawData() {
        return data;
    }

    //Dom4j实现XML追加操作
    public static void append(Painting painting) { //末尾追加新的油画
        //1.读取XML文档，得到Document对象
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document = reader.read(dataFile);
            //2.创建新的painting节点
            Element root = document.getRootElement();//得到原始文档xml根节点 <root>
            Element p = root.addElement("painting");//创建一个新的子节点
            //3.创建painting节点的各个子节点
            p.addAttribute("id", String.valueOf(data.size() + 1)); //生成新的id对象
            p.addElement("pname").setText(painting.getPname()); //返回新的节点 设置其文本值
            p.addElement("category").setText(painting.getCategory().toString());
            p.addElement("price").setText(painting.getPrice().toString());
            p.addElement("preview").setText(painting.getPreview());
            p.addElement("description").setText(painting.getDescription());
            //4.写入XML，完成追加操作
            writer = new OutputStreamWriter(new FileOutputStream(dataFile), "UTF-8");
            document.write(writer); //向目标dataFile原始xml中进行新节点的更新
            System.out.println(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) { //write有开就有关 已经被实例化
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace(); //遇到异常 打印堆栈
                }
            }
            //清空 在空的数据基础上重新添加 无论成功与否都会重新加载数据 如果照片没找到 就清空数据
            reload();
        }

    }

    public static void main(String[] args) {
//        new XmlDataSource();
//        List<Painting> ps = XmlDataSource.getRawData();
//        System.out.println(ps);
        Painting p = new Painting();
        p.setPname("油画测试");
        p.setCategory(1);
        p.setPrice(4000);
        p.setPreview("upload/10.jpg");
        p.setDescription("测试油画描述");
        XmlDataSource.append(p);
    }

    /**
     * 更新对应id的XML油画数据
     * @param painting 要更新的油画数据
     * @throws IOException
     */
    public static void update(Painting painting){
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document = reader.read(dataFile);
            //节点路径[@属性名=属性值]
            // /root/paintin[@id=x]  根节点
            List<Node> nodes = document.selectNodes("/root/painting[@id=" + painting.getId() + "]");
            if (nodes.size() == 0){
                throw new RuntimeException("id=" + painting.getId() + "编号油画不存在");
            }
            Element p = (Element) nodes.get(0); //唯一的节点提取出来
            p.selectSingleNode("pname").setText(painting.getPname()); //得到指定标签名的唯一节点 指定油画更新id操作
            p.selectSingleNode("category").setText(painting.getCategory().toString());
            p.selectSingleNode("price").setText(painting.getPrice().toString());
            p.selectSingleNode("preview").setText(painting.getPreview());
            p.selectSingleNode("description").setText(painting.getDescription());
            writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
            document.write(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reload(); //对原始集合进行重载更新
        }
    }

    /**
     * 按id号删除XML油画数据
     * @param id 油画id
     * @throws IOException
     */

    public static void delete(Integer id) {
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document = reader.read(dataFile);
            List<Node> nodes = document.selectNodes("/root/painting[@id=" + id + "]");
            if(nodes.size() == 0) {
                throw new RuntimeException("id=" + id + "编号油画不存在");
            }
            Element p = (Element)nodes.get(0);
            p.getParent().remove(p);
            writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
            document.write(writer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(writer!=null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            reload();
        }
    }
}
