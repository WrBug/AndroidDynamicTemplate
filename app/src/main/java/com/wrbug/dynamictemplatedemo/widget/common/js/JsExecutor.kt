package com.wrbug.dynamictemplatedemo.widget.common.js

import org.mozilla.javascript.Context
import org.mozilla.javascript.Function
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.ScriptableObject

/**
 * JsExecutor
 *
 * js执行器
 * @author wrbug
 * @since 2017/7/5
 */
class JsExecutor(var jsCallback: JsCallback) {
    var cx: Context = Context.enter()
    var scope: Scriptable

    /** js方法头 */
    val jsName = "JSExecutor"

    init {
        cx.optimizationLevel = -1
        scope = cx.initStandardObjects() as Scriptable
        val wrappedOut = Context.javaToJS(this, scope)
        ScriptableObject.putProperty(scope, jsName, wrappedOut)
    }

    /** 执行js脚本 */
    fun excute(js: String) {
        try {
            cx.evaluateString(scope, js, "<cmd>", 1, null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
        }
    }

    /** 退出js执行器 */
    fun exit() {
        Context.exit()
    }


    /**********************************************************/
    /**                                                      **/
    /**  以下方法为js调用的java方法，调用方式：JSExecutor.方法名  **/
    /**                                                      **/
    /**********************************************************/


    /** 设置控件value值
     * JSExecutor.setVal()
     * */
    fun setVal(id: String, value: Any?) {
        jsCallback.setVal(id, value)
    }

    /** 获取控件value值
     * JSExecutor.getVal()
     * */
    fun getVal(id: String): Any? {
        return jsCallback.getVal(id)
    }


    /**
     * 设置控件prop值
     * JSExecutor.setProp()
     * */
    fun setProp(id: String, key: String, value: Any) {
        jsCallback.setProp(id, key, value)
    }

    /**
     *  获取表单数据
     *JSExecutor.getFormDataCallback(function)
     */
    fun getFormDataCallback(function: Function) {
        function.call(cx, scope, scope, jsCallback.getFormData())
    }

    /** 提交表单
     * JSExecutor.submit()
     */
    fun submit(any: Any) {
        jsCallback.submit(any)
    }

    /** 显示toast
     * JSExecutor.showToast()
     */
    fun showToast(msg: String) {
        jsCallback.showToast(msg)
    }
}