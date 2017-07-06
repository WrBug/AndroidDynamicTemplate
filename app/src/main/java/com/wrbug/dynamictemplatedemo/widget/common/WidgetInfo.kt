package com.wrbug.dynamictemplatedemo.widget.common

import com.google.gson.GsonBuilder

/**
 * WidgetInfo
 *
 * @author wrbug
 * @since 2017/7/5
 */
data class WidgetInfo(var component: String,
                      var config: WidgetConfig) {
    override fun toString(): String {
        return GsonBuilder().create().toJson(this)
    }
}