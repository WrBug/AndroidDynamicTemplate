package com.wrbug.dynamictemplatedemo.widget.common.js

/**
 * JsCallback
 *js脚本调用java方法回调
 * @author wrbug
 * @since 2017/7/5
 */
interface JsCallback {
    fun setVal(id: String, value: Any?)

    fun getVal(id: String): Any?


    fun setProp(id: String, key: String, value: Any)

    fun showToast(msg: String)

    fun getFormData(): Array<*>

    fun submit(any: Any)
}