package com.yuk.fuckcloudservice

import android.os.Bundle
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            "com.miui.cloudservice" -> {
                XposedHelpers.findAndHookMethod(
                    "com.miui.cloudservice.ui.DeviceActivationInfoActivity",
                    lpparam.classLoader,
                    "onCreate",
                    Bundle::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            XposedBridge.log("云服务Hook成功")
                            XposedHelpers.setStaticObjectField(
                                param.thisObject.javaClass,
                                "b",
                                listOf<String>()
                            )
                        }
                    })
            }
            "com.android.settings" -> {
                XposedHelpers.findAndHookMethod(
                    "com.android.settings.utils.ActivationInfoUtil",
                    lpparam.classLoader,
                    "isCurrentDeviceInBlockList",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            XposedBridge.log("设置Hook成功")
                            param.result = false
                        }
                    })
            }
            else -> {
                return
            }
        }
    }
}