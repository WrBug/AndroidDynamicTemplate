package com.wrbug.dynamictemplatedemo.widget.input

import com.wrbug.dynamictemplatedemo.widget.common.BaseProp

/**
 * InputProp
 *
 * @author wrbug
 * @since 2017/7/5
 */
data class InputProp(var hint: String, var regexs: Array<RegexVo>?) : BaseProp() {

    /** 正则类 */
    data class RegexVo(var regex: String, var msg: String) {
        /**
         * regex: 正则代码
         * msg：检查不通过时的提示
         * */
    }
}