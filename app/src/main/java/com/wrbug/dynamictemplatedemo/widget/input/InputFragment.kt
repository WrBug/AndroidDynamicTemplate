package com.wrbug.dynamictemplatedemo.widget.input


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wrbug.dynamictemplatedemo.R
import com.wrbug.dynamictemplatedemo.widget.common.BaseWidgetFragment
import kotlinx.android.synthetic.main.fragment_input.*


/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : BaseWidgetFragment<InputProp, String>() {


    companion object {
        fun newInstance(param1: String): InputFragment {
            val fragment = InputFragment()
            val args = Bundle()
            args.putString(KEY_WIDGET_INFO, param1)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_input, container, false)
    }

    override fun initView() {
        editText.setText(getOriginalValue())
        editText.hint = props.hint
    }

    override fun setVal(aVal: Any?) {
        if (aVal != null) {
            editText.setText(aVal.toString())
        } else {
            editText.text = null
        }
    }

    override fun getData(): Map<String, String> {
        val map = HashMap<String, String>()
        if (isHide) {
            return map
        }
        var str = editText.text.toString()
        props.regexs?.forEach {
            if (!str.matches(Regex(it.regex))) {
                throwDataError(it.msg)
            }
        }
        map.put(getName(), str)
        return map
    }

    override fun getNewValue(): Any {
        return editText.text.toString()
    }
}
