package com.wrbug.dynamictemplatedemo.widget.common

/**
 * BaseProp
 *
 * @author wrbug
 * @since 2017/7/5
 */
open class BaseProp {

    /** 是否隐藏控件 0 显示；1 隐藏 */
    var hide: Int = 0
    /** 控件状态改变脚本 */
    var onChange = ""

    fun isHide(): Boolean {
        return hide == 1
    }
}