package com.wrbug.dynamictemplatedemo.widget.common

/**
 * BaseProp
 *
 * @author wrbug
 * @since 2017/7/5
 */
open class BaseProp {

    var hide: Int = 0
    var onChange = ""

    fun isHide(): Boolean {
        return hide == 1
    }
}