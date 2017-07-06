package com.wrbug.dynamictemplatedemo.widget.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*


/**
 * BaseWidgetFragment
 *
 *
 * P    控件属性
 * V    控件value
 *
 * @author wrbug
 * @since 2017/7/5
 */
abstract class BaseWidgetFragment<P : BaseProp, out V> : Fragment() {

    protected lateinit var mWidgetInfoVo: WidgetInfo
    protected lateinit var props: P
    private lateinit var mWidgetAction: IWidgetAction
    protected val gson: Gson by lazy {
        GsonBuilder().create()
    }
    /**是否隐藏状态*/
    protected var isHide = false

    /**控件属性Type，用于gson解析，下同*/
    private val propType: Type by lazy {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    }


    /**控件value值Type*/
    private val valueType: Type by lazy {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
    }

    companion object {
        /** 控件json的key值 */
        val KEY_WIDGET_INFO = "KEY_WIDGET_INFO"
    }


    /**********************************************************/
    /**                                                      **/
    /**              供外部调用的，或者需要重新的方法             **/
    /**                                                      **/
    /**********************************************************/


    /** view初始化抽象方法 */
    abstract fun initView()

    /**
     * 执行脚本后重新刷新view,钩子方法，子类需要时重写
     */
    open fun refreshView() {

    }

    /**
     * 获取新值
     * 空方法，子类需要时重写
     * @return
     */
    open fun getNewValue(): Any {
        return Any()
    }

    /**
     * 获取提交的数据
     * 空方法，子类需要时重写
     * @return
     */
    @Throws(WidgetDataErrorException::class)
    open fun getData(): Map<String, *> {
        return HashMap<String, Any>()
    }

    /**
     * js设置控件value值
     * 空方法，子类需要时重写
     * @return
     */
    open fun setVal(aVal: Any?) {
    }

    /**********************************************************/


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mWidgetAction = context as IWidgetAction
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        var json = arguments.getString(KEY_WIDGET_INFO)
        if (json.isNullOrEmpty()) {
            //json为空隐藏该控件
            hideSelf()
        } else {
            //获取控件参数
            mWidgetInfoVo = gson.fromJson(json, WidgetInfo::class.java)
            props = gson.fromJson(mWidgetInfoVo.config.prop, propType)
        }
    }

    /** 申明为final，防止重写 */
    override final fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        refresh()
        //加载完成发送初始化js脚本
        mWidgetAction.postInitJs(props.onChange)
    }

    /** 重新设置prop后刷新界面 */
    private fun refresh() {
        if (props.isHide()) {
            hideSelf()
            return
        }
        showSelf()
        refreshView()
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


    @Throws(WidgetDataErrorException::class)
    protected fun throwDataError() {
        throw WidgetDataErrorException(mWidgetInfoVo.config.id, getName(), toString())
    }

    @Throws(WidgetDataErrorException::class)
    protected fun throwDataError(msg: String) {
        throw WidgetDataErrorException(mWidgetInfoVo.config.id, getName(), msg)
    }


    /**********************************************************/
    /**                                                      **/
    /**                   通过js脚本获取/设置属性的方法          **/
    /**                                                      **/
    /**********************************************************/


    /**
     * 设置属性
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

    /**********************************************************/

    protected fun getName(): String {
        return mWidgetInfoVo.config.name
    }

    /** 隐藏 */
    private fun hideSelf() {
        if (isHide) {
            return
        }
        fragmentManager.beginTransaction().hide(this).commitAllowingStateLoss()
        isHide = true
    }

    /** 显示 */
    private fun showSelf() {
        if (!isHide) {
            return
        }
        fragmentManager.beginTransaction().show(this).commitAllowingStateLoss()
        isHide = false
    }
}