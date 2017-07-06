package com.wrbug.dynamictemplatedemo.widget.common

import java.lang.Exception

/**
 * WidgetDataErrorException
 *
 * @author wrbug
 * @since 2017/7/5
 */
class WidgetDataErrorException(var id: String, var name: String, message: String) : Exception(message) {

}