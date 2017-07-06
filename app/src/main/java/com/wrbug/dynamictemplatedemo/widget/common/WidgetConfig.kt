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

}