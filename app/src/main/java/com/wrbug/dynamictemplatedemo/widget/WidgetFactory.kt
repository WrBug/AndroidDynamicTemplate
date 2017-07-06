package com.wrbug.dynamictemplatedemo.widget

import com.wrbug.dynamictemplatedemo.widget.button.ButtonFragment
import com.wrbug.dynamictemplatedemo.widget.common.BaseWidgetFragment
import com.wrbug.dynamictemplatedemo.widget.common.WidgetInfo
import com.wrbug.dynamictemplatedemo.widget.input.InputFragment
import com.wrbug.dynamictemplatedemo.widget.switcher.SwitchFragment
import com.wrbug.dynamictemplatedemo.widget.title.TitleFragment

/**
 * WidgetFactory
 *
 * @author wrbug
 * @since 2017/7/5
 */
object WidgetFactory {
    private val WIDGET_BUTTON = "button"
    private val WIDGET_INPUT = "input"
    private val WIDGET_TITLE = "title"
    private val WIDGET_SWITCH="switch"

    fun createWidget(widgetInfo: WidgetInfo): BaseWidgetFragment<*, *>? {
        val str = widgetInfo.toString()
        return when (widgetInfo.component) {
            WIDGET_BUTTON -> {
                ButtonFragment.newInstance(str)
            }
            WIDGET_INPUT -> {
                InputFragment.newInstance(str)
            }
            WIDGET_TITLE -> {
                TitleFragment.newInstance(str)
            }
            WIDGET_SWITCH->{
                SwitchFragment.newInstance(str)
            }
            else -> {
                null
            }
        }
    }
}