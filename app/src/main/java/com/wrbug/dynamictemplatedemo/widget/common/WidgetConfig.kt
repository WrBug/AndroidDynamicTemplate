package com.wrbug.dynamictemplatedemo.widget.common

import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * WidgetConfig
 *
 * @author wrbug
 * @since 2017/7/5
 */
data class WidgetConfig(var id: String, var value: JsonElement, var name: String, var prop: JsonObject) {
    /**
     * id: 控件id值，唯一
     * value: 控件模板里面的value值，在控件初始化时才进行解析
     * name : 控件name，提交表单或者获取初始值时的key
     * prop：控件对应的prop  ，继承BaseProp，在控件初始化时才进行解析
     * */
}