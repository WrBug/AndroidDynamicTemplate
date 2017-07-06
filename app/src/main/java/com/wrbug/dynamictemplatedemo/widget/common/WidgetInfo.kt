package com.wrbug.dynamictemplatedemo.widget.common

import com.google.gson.GsonBuilder

/**
 * WidgetInfo
 * 模板数据解析后的类
 * @author wrbug
 * @since 2017/7/5
 */
data class WidgetInfo(var component: String,
                      var config: WidgetConfig) {

    /**
     * component:控件名
     *
     * */
    override fun toString(): String {
        return GsonBuilder().create().toJson(this)
    }
}