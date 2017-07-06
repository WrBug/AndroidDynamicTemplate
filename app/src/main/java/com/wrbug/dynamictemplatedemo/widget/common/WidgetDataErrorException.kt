package com.wrbug.dynamictemplatedemo.widget.common

import java.lang.Exception

/**
 * WidgetDataErrorException
 * 提交表单时异常
 * @author wrbug
 * @since 2017/7/5
 */
class WidgetDataErrorException(var id: String, var name: String, message: String) : Exception(message) {
    /**
     * id:控件id
     * name：控件name
     * message
     * */
}