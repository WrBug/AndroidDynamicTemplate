package com.wrbug.dynamictemplatedemo.widget.button


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wrbug.dynamictemplatedemo.R
import com.wrbug.dynamictemplatedemo.widget.WidgetFactory
import com.wrbug.dynamictemplatedemo.widget.common.BaseWidgetFragment
import kotlinx.android.synthetic.main.fragment_button.*


/**
 * A simple [Fragment] subclass.
 * Use the [ButtonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ButtonFragment : BaseWidgetFragment<ButtonProp, Any>(), View.OnClickListener {


    companion object {
        fun newInstance(param1: String): ButtonFragment {
            val fragment = ButtonFragment()
            val args = Bundle()
            args.putString(KEY_WIDGET_INFO, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_button, container, false)
    }

    override fun initView() {
        button.text = props.title
        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //点击执行onClick的js脚本
        runJs(props.onClick)
    }
}
