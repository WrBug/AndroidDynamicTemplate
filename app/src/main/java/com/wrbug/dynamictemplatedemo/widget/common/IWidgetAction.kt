package com.wrbug.dynamictemplatedemo.widget.common

import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * IWidgetAction
 *
 * @author wrbug
 * @since 2017/7/5
 */
interface IWidgetAction {
    fun runJs(js: String?)
    fun getOriginalValue(name: String?): JsonElement?
    fun postInitJs(js: String?)
}