package com.wrbug.dynamictemplatedemo.widget.input

import com.wrbug.dynamictemplatedemo.widget.common.BaseProp

/**
 * InputProp
 *
 * @author wrbug
 * @since 2017/7/5
 */
data class InputProp(var hint: String, var regexs: Array<RegexVo>?) : BaseProp() {

    data class RegexVo(var regex: String, var msg: String) {

    }
}