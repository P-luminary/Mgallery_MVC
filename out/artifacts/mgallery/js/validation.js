/**
 * 隐藏、显示错误信息
 * @param onOff true 验证成功, 隐藏错误     false 校验失败，显示错误
 * @param input 表单域选择器
 * @param errSelector 错误提示选择器
 * @param message 错误信息
 * @returns
 */
function switchValid(onOff, input, errSelector, message) {
    if (onOff == false) {
        $(errSelector).text(message);
        $(input).addClass("error_input"); //错误标红
        $(errSelector).addClass("error_message");
    } else {
        $(errSelector).text("");
        $(input).removeClass("error_input"); //错误标红
        $(errSelector).removeClass("error_message");
    }
}

/**
 * 检查是否为空
 * @param input 表单域选择器
 * @param errSelector 错误提示选择器
 * @returns true-校验成功 false-校验失败
 */
function checkEmpty(input, errSelector) {
    var val = $(input).val(); //获取当前选择的数值
    if ($.trim(val) == "") { //将字符串前后空格删掉
        switchValid(false, input, errSelector, "请输入内容"); //非空校验失败时显示错误
        return false;
    } else {
        switchValid(true, input, errSelector); //正确情况
        return true;
    }
}

function checkCategory(input, errSelector) {
    var val = $(input).val(); //获取当前选择的数值
    if (val == -1) {
        switchValid(false, input, errSelector, "请选择油画类型"); //非空校验失败时显示错误
        return false;
    } else {
        switchValid(true, input, errSelector); //正确情况
        return true;
    }
}

/**
 * 价格必须是整数
 * @param input 表单域选择器
 * @param errSelector 错误提示选择器
 * @returns true-校验成功 false-校验失败
 */
function checkPrice(input, errSelector) {
    var val = $(input).val(); //获取当前选择的数值
    var regex = /^[1-9][0-9]*$/   //利用正则表达式进行校验信息
    if (!regex.test(val)) {
        switchValid(false, input, errSelector, "无效的价格"); //非空校验失败时显示错误
        return false;
    } else {
        switchValid(true, input, errSelector); //正确情况
        return true;
    }
}

/**
 * 上传文件必须是图片
 * @param input 表单域选择器
 * @param errSelector 错误提示选择器
 * @returns true-校验成功 false-校验失败
 */
function checkFile(input, errSelector) {
    if (checkEmpty(input, errSelector) == false) {
        return false;
    }
    var val = $(input).val().toLowerCase()//小写转换 PNG png
    if (val.length < 4) { //x.xxxx
        switchValid(false, input, errSelector, "请选择有效的图片"); //非空校验失败时显示错误
        return false;
    }
    suffix = val.substring(val.length - 3); //拿到最后的扩展名
    if (suffix == "jpg" || suffix == "png" || suffix == "gif") {
        switchValid(true.input, errSelector);
        return true;
    }else {
        switchValid(false, input, errSelector, "请选择有效的图片");
        return false;
    }
}















