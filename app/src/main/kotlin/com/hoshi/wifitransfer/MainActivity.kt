package com.hoshi.wifitransfer

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hoshi.lib.annotation.StatusTextLight
import com.hoshi.lib.base.BaseActivity
import com.hoshi.lib.extentions.showToast
import com.hoshi.lib.utils.popup.XPopupCommonUtils
import com.hoshi.lib.utils.wifi.WiFiUtils
import com.hoshi.wifitransfer.databinding.ActivityMainBinding
import com.hoshi.wifitransfer.databinding.DialogWifiOpenBinding

@StatusTextLight
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initView() {
        setSupportActionBar(binding.toolbar) // 将一个 toolbar 设置为 ActionBar

        binding.fab.setOnClickListener {
            val context = this
            XXPermissions.with(context)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            animHide(it)
                        } else {
                            showToast("请给权限")
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        if (never) {
                            showToast("永不提示了，请给权限")
                        } else {
                            showToast("请给权限")
                        }
                    }
                })
        }
    }

    /**
     * 按钮展示动画
     * @param view View 目标按钮
     */
    private fun animShow(view: View) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", view.height * 2F, 0F).setDuration(200L)
        objectAnimator.interpolator = AccelerateInterpolator()
        objectAnimator.start()
    }

    /**
     * 按钮隐藏动画
     * @param view View 目标按钮
     */
    private fun animHide(view: View) {
        val context = view.context
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", 0F, view.height * 2F).setDuration(200L)
        objectAnimator.interpolator = AccelerateInterpolator()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animator: Animator?) {}
            override fun onAnimationCancel(animator: Animator?) {}
            override fun onAnimationRepeat(animator: Animator?) {}
            override fun onAnimationStart(animator: Animator?) {
                WebService.start(context)
                if (WiFiUtils.openWifi()) {
                    val binding = DialogWifiOpenBinding.inflate(LayoutInflater.from(context))
                    binding.tvAddress.text = "http://" + WiFiUtils.getWiFiIp(context) + ":" + Const.HTTP_PORT

                    XPopupCommonUtils.showCustom(
                        context,
                        "WLAN 服务已经开启",
                        binding.root,
                        getString(R.string.cancel),
                        posListener = {
                            WebService.stop(context)
                            animShow(view)
                        },
                        isDismissOnTouchOutside = false,
                        isHideNeg = true
                    )
                }
            }
        })
        objectAnimator.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu) // 加载 menu 布局
        return true
    }

}