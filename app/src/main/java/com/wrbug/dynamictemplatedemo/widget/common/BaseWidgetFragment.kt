package com.wrbug.dynamictemplatedemo.widget.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


/**
 * BaseWidgetFragment
 *
 * @author wrbug
 * @since 2017/7/5
 */
abstract class BaseWidgetFragment<P : BaseProp, V> : Fragment() {

    protected lateinit var mWidgetInfoVo: WidgetInfo
    protected lateinit var props: P
    private lateinit var mWidgetAction: IWidgetAction
    protected var isHide = false
    private val propType: Type by lazy {
        (javaClass.genericSuperclass as java.lang.reflect.ParameterizedType).actualTypeArguments[0]
    }

    private val valueType: Type by lazy {
        (javaClass.genericSuperclass as java.lang.reflect.ParameterizedType).actualTypeArguments[1]
    }

    companion object {
        val KEY_WIDGET_INFO = "KEY_WIDGET_INFO"
    }

    abstract fun initView()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mWidgetAction = context as IWidgetAction
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        var json = arguments.getString(com.wrbug.dynamictemplatedemo.widget.common.BaseWidgetFragment.Companion.KEY_WIDGET_INFO)
        if (json.isNullOrEmpty()) {
            hideSelf()
        } else {
            mWidgetInfoVo = com.google.gson.GsonBuilder().create().fromJson(json, WidgetInfo::class.java)
            props = com.google.gson.GsonBuilder().create().fromJson(mWidgetInfoVo.config.prop, propType)
        }
    }

    override final fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        refresh()
        mWidgetAction.postInitJs(props.onChange)
    }

    private fun refresh() {
        if (props.isHide()) {
            hideSelf()
            return
        }
        showSelf()
        refreshView()
    }

    /**
     * 执行脚本后重新刷新view
     */
    open fun refreshView() {

    }

    /**
     * js脚本检查，运行js脚本
     */
    protected fun onChangeCheck() {
        runJs(props.onChange)
    }

    protected fun runJs(js: String) {
        if (TextUtils.isEmpty(js)) {
            return
        }
        mWidgetAction.runJs(js)
    }

    /**
     * 获取初始值

     * @return
     */
    protected fun getOriginalValue(): V? {
        var tNode = mWidgetAction.getOriginalValue(mWidgetInfoVo.config.name)
        if (tNode == null) {
            tNode = mWidgetInfoVo.config.value
        }
        val value: V = GsonBuilder().create().fromJson(tNode, valueType)
        return value
    }

    /**
     * 获取新值

     * @return
     */
    open fun getNewValue(): Any {
        return Any()
    }

    /**
     * 获取提交的数据

     * @return
     */
    @Throws(WidgetDataErrorException::class)
    open fun getData(): Map<String, *> {
        return HashMap<String, Any>()
    }

    /**
     * 检查数据是否修改

     * @return
     */
    fun isDataChanged(): Boolean {
        return false
    }

    @Throws(WidgetDataErrorException::class)
    protected fun throwDataError() {
        throw WidgetDataErrorException(mWidgetInfoVo.config.id, getName(), toString())
    }

    @Throws(WidgetDataErrorException::class)
    protected fun throwDataError(msg: String) {
        throw WidgetDataErrorException(mWidgetInfoVo.config.id, getName(), msg)
    }

    open fun setVal(aVal: Any?) {
    }

    /**
     * 设置属性，比较low的方案

     * @param key   prop字段
     * *
     * @param value prop新值
     */
    fun setProp(key: String, value: Any) {
        var json = GsonBuilder().create().toJson(props)
        //将prop转成map
        val map: HashMap<String, Any>? = Gson().fromJson(json, object : TypeToken<HashMap<String, Any>>() {}.type)
        //替换值
        map?.put(key, value)
        //将map转为json字符串
        json = Gson().toJson(map)
        //重新转为对象
        props = Gson().fromJson(json, props::class.java) as P
        refresh()
    }

    /**
     * 获取属性

     * @param key
     * *
     * @return
     */
    fun getProp(key: String): Any? {
        val pClass = props::class.java
        try {
            val field = pClass.getDeclaredField(key)
            if (field != null) {
                field.isAccessible = true
                return field.get(props)
            }
        } catch (e: NoSuchFieldException) {

        } catch (e: IllegalAccessException) {

        }

        return null
    }

    fun getName(): String {
        return mWidgetInfoVo.config.name
    }

    fun hideSelf() {
        fragmentManager.beginTransaction().hide(this).commitAllowingStateLoss()
        isHide = true
    }

    fun showSelf() {
        fragmentManager.beginTransaction().show(this).commitAllowingStateLoss()
        isHide = false
    }
}