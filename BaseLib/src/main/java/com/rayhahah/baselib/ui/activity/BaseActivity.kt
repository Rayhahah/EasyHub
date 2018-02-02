package com.rayhahah.baselib.ui.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentManager
import android.transition.Fade
import android.transition.Transition
import android.view.View
import android.widget.FrameLayout
import com.rayhahah.baselib.R
import com.rayhahah.baselib.common.AppManager
import com.rayhahah.baselib.ui.fragment.BaseFragment
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.jetbrains.anko.find

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 *
 * @author Rayhahah
 * @blog http://rayhahah.com
 * @time 2018/1/30
 * @tips 这个类是Object的子类
 * @fuction Activity基类，业务无关
 */
abstract class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppManager.instance.addActivity(this)
        super.onCreate(savedInstanceState)
        initTheme()
        initWindowTransition(getWindowTransition())
        val layoutID = getLayoutID()
        if (layoutID != 0) {
            setContentView(layoutID)
        }
        initThemeAttrs()
        setStatusColor()
        initEventAndData(savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.finishActivity(this)
    }

    abstract fun initEventAndData(savedInstanceState: Bundle?)

    /**
     * 设置布局界面id
     *
     * @return
     */
    protected abstract fun getLayoutID(): Int

    /**
     * 设置Fragment容器id
     *
     * @return
     */
    protected abstract fun setFragmentContainerResId(): Int


    protected fun setStatusColor() {

    }

    /**
     * 初始化主题颜色
     */
    protected fun initTheme() {
        //
    }

    /**
     * 初始化获取当前主题中的颜色
     *
     * @return
     */
    protected fun initThemeAttrs() {}


    /**
     * 设置过渡动画
     * 默认是淡入淡出，可重写
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected fun getWindowTransition(): Transition {
        return Fade()
    }


    /**
     * 初始化过渡动画
     *
     * @param transition
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initWindowTransition(transition: Transition) {
        window.returnTransition = transition
        window.exitTransition = transition
        window.enterTransition = transition
        window.reenterTransition = transition
    }

    lateinit var fm: FragmentManager

    lateinit var currentFragment: BaseFragment

    /**
     * 显示Fragment
     *
     * @param fragment
     */
    protected fun showFragment(fragment: BaseFragment, position: Int) {
        if (fm == null) {
            fm = supportFragmentManager
        }
        val transaction = fm.beginTransaction()
        //Fragment添加
        if (!fragment.isAdded()) {
            transaction.add(setFragmentContainerResId(), fragment, position.toString() + "")
        }
        if (currentFragment == null) {
            currentFragment = fragment
        }
        //通过tag进行过渡动画滑动判断
        if (Integer.parseInt(currentFragment.getTag()) >= Integer.parseInt(fragment.getTag())) {
            transaction.setCustomAnimations(R.anim.fragment_push_left_in, R.anim.fragment_push_right_out)
        } else {
            transaction.setCustomAnimations(R.anim.fragment_push_right_in, R.anim.fragment_push_left_out)
        }

        transaction.hide(currentFragment).show(fragment)
        transaction.commit()
        currentFragment = fragment
    }


    //获取Window中视图content
    val contentView: View
        get() {
            val content = find<FrameLayout>(android.R.id.content)
            return content.getChildAt(0)
        }
}