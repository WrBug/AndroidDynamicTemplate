package com.wrbug.dynamictemplatedemo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.reflect.TypeToken
import com.wrbug.dynamictemplatedemo.util.ResourceHelper
import com.wrbug.dynamictemplatedemo.widget.WidgetFactory
import com.wrbug.dynamictemplatedemo.widget.common.BaseWidgetFragment
import com.wrbug.dynamictemplatedemo.widget.common.IWidgetAction
import com.wrbug.dynamictemplatedemo.widget.common.WidgetDataErrorException
import com.wrbug.dynamictemplatedemo.widget.common.WidgetInfo
import com.wrbug.dynamictemplatedemo.widget.common.js.JsCallback
import com.wrbug.dynamictemplatedemo.widget.common.js.JsExecutor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IWidgetAction, JsCallback {


    var array: Array<WidgetInfo>? = null
    var widgetValues: Map<String, JsonElement>? = null
    val jsExecutor: JsExecutor by lazy {
        JsExecutor(this)
    }
    val initJs: StringBuilder = StringBuilder()

    companion object {
        val PREF = "pref"
        val KEY_USER = "KEY_USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val json = String(ResourceHelper.readAssestFile(this, "template.json"))
        val js = String(ResourceHelper.readAssestFile(this, "run.js"))
        runJs(js)
        array = GsonBuilder().create().fromJson(json, object : TypeToken<Array<WidgetInfo>>() {}.type)
        initData()
        addWidgets()
        container.post {
            runJs(initJs.toString())
        }
    }

    private fun initData() {
        var str = getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_USER, "")
        if (!TextUtils.isEmpty(str)) {
            widgetValues = GsonBuilder().create().fromJson(str, object : TypeToken<Map<String, JsonElement>>() {}.type)
        }
    }

    private fun addWidgets() {
        var tranaction = supportFragmentManager.beginTransaction()
        for (widget in array!!) {
            val fragment = WidgetFactory.createWidget(widget)
            if (fragment != null) {
                tranaction.add(R.id.container, fragment, widget.config.id)
            }
        }
        tranaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        jsExecutor.exit()
    }

    override fun runJs(js: String?) {
        if (!TextUtils.isEmpty(js)) {
            jsExecutor.excute(js!!)
        }
    }

    override fun postInitJs(js: String?) {
        if (!TextUtils.isEmpty(js)) {
            initJs.append(js)
        }
    }

    override fun getOriginalValue(name: String?): JsonElement? {
        if (widgetValues == null) {
            return null
        }
        if (widgetValues!!.containsKey(name)) {
            return widgetValues!![name]
        }
        return null
    }


    override fun setVal(id: String, value: Any?) {
        var fragment = getFragmentById(id)
        if (fragment != null) {
            fragment.setVal(value)
        }
    }

    override fun getVal(id: String): Any? {
        var fragment = getFragmentById(id)
        if (fragment != null) {
            return fragment.getNewValue()
        }
        return null
    }

    override fun setProp(id: String, key: String, value: Any) {
        var fragment = getFragmentById(id)
        if (fragment != null) {
            fragment.setProp(key, value)
        }
    }

    fun getFragmentById(id: String): BaseWidgetFragment<*, *>? {
        var fragment = supportFragmentManager.findFragmentByTag(id) as BaseWidgetFragment<*, *>
        return fragment
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun getFormData(): Array<*> {
        val data = arrayOfNulls<Any>(3)
        try {
            val params = HashMap<String, Any>()
            array?.forEach {
                var fragment = getFragmentById(it.config.id)
                var map = fragment?.getData()
                params.putAll(map as HashMap<String, *>)
            }
            data[0] = 0
            data[1] = params
        } catch (e: WidgetDataErrorException) {
            data[0] = 1
            data[2] = e.message
        }
        return data
    }

    override fun submit(any: Any) {
        var spref = getSharedPreferences(PREF, Context.MODE_PRIVATE)
        spref.edit().putString(KEY_USER, GsonBuilder().create().toJson(any)).apply()
        showToast("已保存")
    }
}