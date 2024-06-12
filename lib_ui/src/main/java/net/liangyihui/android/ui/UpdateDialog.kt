/*
 *  UpdateDialog.kt, 2022-09-28
 *  Copyright © 2015-2022  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.maxkeppeler.sheets.core.Sheet
import net.liangyihui.android.ui.databinding.UpdateDialogSheetBinding

/**
 * @author lijian
 * @date 2022/2/18
 * @description 自定义更新弹窗对话框,固定宽高比(280+(16边距x2))：352；设计稿基础宽度375
 * @since 8.8
 */
private typealias PositiveListener = () -> Unit

class UpdateDialog : Sheet() {
    override val dialogTag = "UpdateDialog"

    private lateinit var binding: UpdateDialogSheetBinding
    private var titleText: String? = null
    private var versionText: String? = null
    private var contentText: String? = null


    fun onPositive(positiveListener: PositiveListener) {
        this.positiveListener = positiveListener
    }

    fun onPositive(@StringRes positiveRes: Int, positiveListener: PositiveListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveListener = positiveListener
    }

    fun onPositive(positiveText: String, positiveListener: PositiveListener? = null) {
        this.positiveText = positiveText
        this.positiveListener = positiveListener
    }

    fun titleText(title: String?) {
        this.titleText = title
    }

    fun versionText(versionName: String?) {
        this.versionText = versionName
    }

    fun contentText(content: String?) {
        this.contentText = content
    }

    override fun onCreateLayoutView(): View {
        return UpdateDialogSheetBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTextView.text = this.titleText
        binding.versionTextView.text = this.versionText
        binding.contentTextView.text = this.contentText
        updateButtonsBackgroundStyleRounded()
    }

    /** Build [UpdateDialog] and show it later. */
    fun build(ctx: Context, width: Int? = null, func: UpdateDialog.() -> Unit): UpdateDialog {
        this.style(com.maxkeppeler.sheets.core.SheetStyle.DIALOG)
        this.windowContext = ctx
        this.width = width ?: (ctx.resources.displayMetrics.widthPixels * 312 / 375f).toInt()
        this.func()
        return this
    }

    /** Build and show [UpdateDialog] directly. */
    fun show(ctx: Context, width: Int? = null, func: UpdateDialog.() -> Unit): UpdateDialog {
        this.style(com.maxkeppeler.sheets.core.SheetStyle.DIALOG)
        this.windowContext = ctx
        this.width = width ?: (ctx.resources.displayMetrics.widthPixels * 312 / 375f).toInt()
        this.func()
        this.show()
        return this
    }

    /**
     * fix:java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
    at androidx.fragment.app.FragmentManager.n(FragmentManager.java:2)
    at androidx.fragment.app.FragmentManager.V(FragmentManager.java:5)
    at androidx.fragment.app.a.f(BackStackRecord.java:12)
    at androidx.fragment.app.a.commit
    at androidx.fragment.app.DialogFragment.show(DialogFragment.java:5)
    at com.maxkeppeler.sheets.core.SheetFragment.show(SheetFragment.kt:2)
    at net.liangyihui.android.ui.UpdateDialog.show(UpdateDialog.kt:5)
    at net.liangyihui.android.ui.UpdateDialog.show$default
    at com.dop.h_doctor.view.ex.FunKt.b(Fun.kt:2)
    at com.dop.h_doctor.view.ex.FunKt.a
    at com.dop.h_doctor.view.ex.d.accept
    at com.blankj.utilcode.util.e1$f.onSuccess(Utils.java:2)
    at com.blankj.utilcode.util.ThreadUtils$f$c.run(ThreadUtils.java:1)
    at android.os.Handler.handleCallback(Handler.java:900)
    at android.os.Handler.dispatchMessage(Handler.java:103)
    at android.os.Looper.loop(Looper.java:219)
    at android.app.ActivityThread.main(ActivityThread.java:8668)
    at java.lang.reflect.Method.invoke(Method.java)
    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:513)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1109)
     * @see https://www.jianshu.com/p/bcc5af0085d3
     */
    override fun show(manager: FragmentManager, tag: String?) {
//        super.show(manager, tag)
        try {
            val dismissed = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed[this] = false
        } catch (ignore: NoSuchFieldException) {
        } catch (ignore: IllegalAccessException) {
        }
        try {
            val shown = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shown.isAccessible = true
            shown[this] = true
        } catch (ignore: NoSuchFieldException) {
        } catch (ignore: IllegalAccessException) {
        }
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    private fun updateButtonsBackgroundStyleRounded() {
        if (this::binding.isInitialized && binding.root.parent.parent is ConstraintLayout) {
            (binding.root.parent.parent as ConstraintLayout).getViewById(R.id.buttons)
                .setBackgroundResource(R.drawable.drawable_update_dialog_bottom_white_8dp)
            (binding.root.parent.parent as ConstraintLayout).getViewById(R.id.buttons).updateLayoutParams<ConstraintLayout.LayoutParams> {
                height = (44 * resources.displayMetrics.density + 0.5f).toInt()
            }
        }

    }
}
