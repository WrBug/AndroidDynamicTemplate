package com.wrbug.dynamictemplatedemo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
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


    /** json模板解析后的数组 */
    var WidgetInfos: Array<WidgetInfo>? = null

    /** 储存的控件值 */
    var values: Map<String, JsonElement>? = null

    /** js执行器 */
    val jsExecutor: JsExecutor by lazy {
        JsExecutor(this)
    }
    /**
     *  初始化脚本 */
    val initJs: StringBuilder = StringBuilder()

    companion object {
        val PREF = "pref"
        val KEY_USER = "KEY_USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val json = String(ResourceHelper.readAssestFile(this, "template.json"))

        //初始化js方法定义
        val js = String(ResourceHelper.readAssestFile(this, "run.js"))
        runJs(js)

        initValues()

        WidgetInfos = GsonBuilder().create().fromJson(json, object : TypeToken<Array<WidgetInfo>>() {}.type)
        addWidgets()

        //控件加载完成后执行初始化脚本
        container.post {
            runJs(initJs.toString())
        }
    }

    private fun initValues() {
        var str = getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_USER, "")
        if (!TextUtils.isEmpty(str)) {
            values = GsonBuilder().create().fromJson(str, object : TypeToken<Map<String, JsonElement>>() {}.type)
        }
    }

    private fun addWidgets() {
        var tranaction = supportFragmentManager.beginTransaction()
        for (widget in WidgetInfos!!) {
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

    /** 运行js脚本 */
    override fun runJs(js: String?) {
        if (!TextUtils.isEmpty(js)) {
            jsExecutor.excute(js!!)
        }
    }

    /** 接收fragment发过来的初始化脚本 */
    override fun postInitJs(js: String?) {
        if (!TextUtils.isEmpty(js)) {
            initJs.append(js)
        }
    }

    /** 获取已保存的原始数据 */
    override fun getOriginalValue(name: String?): JsonElement? {
        if (values == null) {
            return null
        }
        if (values!!.containsKey(name)) {
            return values!![name]
        }
        return null
    }


    /** js调用setVal方法 */
    override fun setVal(id: String, value: Any?) {
        var fragment = getFragmentById(id)
        if (fragment != null) {
            fragment.setVal(value)
        }
    }


    /** js调用getVal方法 */
    override fun getVal(id: String): Any? {
        var fragment = getFragmentById(id)
        if (fragment != null) {
            return fragment.getNewValue()
        }
        return null
    }

    /** js调用setProp方法 */
    override fun setProp(id: String, key: String, value: Any) {
        var fragment = getFragmentById(id)
        if (fragment != null) {
            fragment.setProp(key, value)
        }
    }

    /** 通过id获取fragment */
    fun getFragmentById(id: String): BaseWidgetFragment<*, *>? {
        val fragment = supportFragmentManager.findFragmentByTag(id) as BaseWidgetFragment<*, *>
        return fragment
    }

    /** js调用showToast方法 */
    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /** 获取提交的表单 */
    override fun getFormData(): Array<*> {
        val data = arrayOfNulls<Any>(3)
        try {
            val params = HashMap<String, Any>()

            //遍历控件。获取提交的数据
            WidgetInfos?.forEach {
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

    /** js调用submit方法 */
    override fun submit(any: Any) {
        var spref = getSharedPreferences(PREF, Context.MODE_PRIVATE)
        spref.edit().putString(KEY_USER, GsonBuilder().create().toJson(any)).apply()
        showToast("已保存")
    }
}