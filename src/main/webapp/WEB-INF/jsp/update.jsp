<%@page contentType="text/html;charset=utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- 修改油画页面 -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>作品更新</title>
    <link rel="stylesheet" type="text/css" href="css\create.css">
    <script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="js/validation.js"></script>
    <script type="text/javascript">
        <!-- 提交前表单校验 -->
        function checkSubmit() {
            var result = true;
            var r1 = checkEmpty("#pname", "#errPname");
            var r2 = checkCategory('#category', '#errCategory');
            var r3 = checkPrice('#price', '#errPrice');
            // var r4 = checkFile('#painting', '#errPainting');
            var r4 = null;
            if ($("#isPreviewModified").val() == "1"){
                r4 = checkFile('#painting','#errPainting');
            }else {
                r4 = true;
            }
            var r5 = checkEmpty('#description', '#errDescription');
            if (r1 && r2 && r3 && r4 && r5) {
                return true;
            } else {
                return false;
            }
        }
        //整个html被解析完后执行代码
        $(function(){//前面的被html解释完才执行 后面的EL表达式jsp渲染在服务器端产生 油画类型默认产生
            $("#category").val(${painting.category});
        })
        //检查预览图片
        function selectPreview(){
            checkFile("#painting","#errPainting");  //对文件选择框进行条件检查
            $("#preview").hide();                  //选择新图片把旧的隐藏
            $("#isPreviewModified").val(1); //是否需要修改值变化
        }
    </script>
</head>
<body>
<div class="container">
    <fieldset>
        <legend>作品名称</legend>
        <form action="[这里写更新URL]" method="post"
              autocomplete="off" enctype="multipart/form-data"
              onsubmit="return checkSubmit()">
            <ul class="ulform">
                <li>
                    <span>油画名称</span>
                    <span id="errPname"></span>
                    <input id="pname" name="pname" onblur="checkEmpty('#pname','#errPname')" value="${painting.pname}"/>
                </li>
                <li>
                    <span>油画类型</span>
                    <span id="errCategory"></span>
                    <select id="category" name="category" onchange="checkCategory('#category','#errCategory')"
                            value="${painting.category}">
                        <option value="-1">请选择油画类型</option>
                        <option value="1">现实主义</option>
                        <option value="2">抽象主义</option>
                    </select>
                </li>
                <li>
                    <span>油画价格</span>
                    <span id="errPrice"></span>
                    <input id="price" name="price" onblur="checkPrice('#price','#errPrice')" value="${painting.price}"/>
					</li>
					<li>
						<span>作品预览</span>
                        <input type="hidden" id="isPreviewModified" name="isPreviewModified" value="0">
                        <span id=" errPainting"></span><br/>
                    <img id="preview" src="${painting.preview}" style="width:361px;height:240px"/><br/>
<%--            添加上方hidden隐藏域  如果文件选择框没有产生任何重新选择的话在服务端就不对preview进行更新 这样就不用每次都上传文件--%>
                    <input id="painting" name="painting" type="file" style="padding-left:0px;" accept="image/*"
                    onchange="selectPreview()"/>
                </li>
                <li>
                    <span>详细描述</span>
                    <span id="errDescription"></span>
                    <textarea
                            id="description" name="description"
                            onblur="checkEmpty('#description','#errDescription')"
                    >
                        ${painting.description}
                    </textarea>
                </li>
                <li style="text-align: center;">
                    <input type="hidden" id="id" name="id" value="${painting.id}"> <!--增加隐藏域-->
                    <button type="submit" class="btn-button">提交表单</button>
                </li>
            </ul>
        </form>
    </fieldset>
</div>

</body>
</html>
