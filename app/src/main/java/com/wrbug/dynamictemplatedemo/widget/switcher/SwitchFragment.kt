package com.wrbug.dynamictemplatedemo.widget.switcher


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton

import com.wrbug.dynamictemplatedemo.R
import com.wrbug.dynamictemplatedemo.widget.common.BaseWidgetFragment
import kotlinx.android.synthetic.main.fragment_switch.*


/**
 * A simple [Fragment] subclass.
 * Use the [SwitchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SwitchFragment : BaseWidgetFragment<SwitchProp, Int>(), CompoundButton.OnCheckedChangeListener {

    companion object {
        fun newInstance(param1: String): SwitchFragment {
            val fragment = SwitchFragment()
            val args = Bundle()
            args.putString(KEY_WIDGET_INFO, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_switch, container, false)
    }

    override fun initView() {
        title.text = props.title
        switcher.isChecked = getOriginalValue() == 1
        switcher.setOnCheckedChangeListener(this)
    }

    /** 通过JSExecutor.setVal()方法调用 */
    override fun setVal(aVal: Any?) {
        if (aVal != null && aVal is Number) {
            switcher.isChecked = aVal.toInt() == 1
        }
    }

    override fun getNewValue(): Any {
        return if (switcher.isChecked) {
            1
        } else {
            0
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        //控件状态变化检测
        onChangeCheck()
    }

    override fun getData(): Map<String, Int> {
        return hashMapOf(getName() to getNewValue() as Int)
    }

}
