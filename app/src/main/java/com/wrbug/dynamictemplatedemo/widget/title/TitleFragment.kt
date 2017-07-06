package com.wrbug.dynamictemplatedemo.widget.title


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wrbug.dynamictemplatedemo.R
import com.wrbug.dynamictemplatedemo.widget.common.BaseWidgetFragment
import kotlinx.android.synthetic.main.fragment_title.*


/**
 * A simple [Fragment] subclass.
 * Use the [TitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TitleFragment : BaseWidgetFragment<TitleProp, Any>() {

    companion object {
        fun newInstance(param1: String): TitleFragment {
            val fragment = TitleFragment()
            val args = Bundle()
            args.putString(KEY_WIDGET_INFO, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_title, container, false)
    }

    override fun initView() {
        title.text = props.title
    }

}
