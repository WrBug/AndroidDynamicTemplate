package com.wrbug.dynamictemplatedemo.widget.common.js

/**
 * JsCallback
 *
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