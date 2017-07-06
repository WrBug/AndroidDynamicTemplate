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
    val jsName = "JSExecutor"

    init {
        cx.optimizationLevel = -1
        scope = cx.initStandardObjects() as Scriptable
        val wrappedOut = Context.javaToJS(this, scope)
        ScriptableObject.putProperty(scope, jsName, wrappedOut)
    }

    fun excute(js: String) {
        try {
            cx.evaluateString(scope, js, "<cmd>", 1, null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
        }
    }

    fun exit() {
        Context.exit()
    }


    fun setVal(id: String, value: Any?) {
        jsCallback.setVal(id, value)
    }

    fun getVal(id: String): Any? {
        return jsCallback.getVal(id)
    }


    fun setProp(id: String, key: String, value: Any) {
        jsCallback.setProp(id, key, value)
    }

    fun getFormDataCallback(function: Function) {
        function.call(cx, scope, scope, jsCallback.getFormData())
    }

    fun submit(any: Any) {
        jsCallback.submit(any)
    }

    fun showToast(msg: String) {
        jsCallback.showToast(msg)
    }
}