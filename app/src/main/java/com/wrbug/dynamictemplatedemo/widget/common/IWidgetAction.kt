package com.wrbug.dynamictemplatedemo.widget.common

import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * IWidgetAction
 * 控件外部通信接口
 * @author wrbug
 * @since 2017/7/5
 */
interface IWidgetAction {
    /** 调用脚本执行器执行脚本 */
    fun runJs(js: String?)

    /** 获取存储的原始值 */
    fun getOriginalValue(name: String?): JsonElement?

    /** 控件加载完成后发送初始化脚本信息 */
    fun postInitJs(js: String?)
}