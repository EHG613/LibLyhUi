/*
 *  PrivacyDialog.kt, 2022-12-12
 *  Copyright © 2015-2022  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget

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
import net.liangyihui.android.ui.R
import net.liangyihui.android.ui.UpdateDialog
import net.liangyihui.android.ui.databinding.PrivacyDialogSheetBinding

/**
 * @author lijian
 * @date 2022/12/12
 * @since
 * @description
 */
private typealias PositiveListener = () -> Unit

class PrivacyDialog : Sheet() {
    override val dialogTag = "PrivacyDialog"
    private var titleText: String? = null
    private var contentUrl: String? = null
    private lateinit var binding: PrivacyDialogSheetBinding
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

    fun contentUrl(url: String?) {
        this.contentUrl = url
    }

    override fun onCreateLayoutView(): View {
        return PrivacyDialogSheetBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTextView.text = this.titleText
        this.contentUrl?.let {
            binding.webView.loadUrl(it)
        }
        updateButtonsBackgroundStyleRounded()
    }

    /** Build and show [UpdateDialog] directly. */
    fun show(ctx: Context, width: Int? = null, func: PrivacyDialog.() -> Unit): PrivacyDialog {
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
                .setBackgroundResource(R.drawable.drawable_privacy_dialog_bottom_corners_white_8dp)
            (binding.root.parent.parent as ConstraintLayout).getViewById(R.id.buttons).updateLayoutParams<ConstraintLayout.LayoutParams> {
                height = (50 * resources.displayMetrics.density + 0.5f).toInt()
            }
        }

    }
}