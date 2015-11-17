package org.eu.au.navhaxs.legacymenu;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.getAdditionalInstanceField;
import static de.robv.android.xposed.XposedHelpers.getStaticIntField;
import static de.robv.android.xposed.XposedHelpers.setAdditionalInstanceField;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class LegacyMenu implements  IXposedHookLoadPackage, IXposedHookZygoteInit {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam packageParam)
            throws Throwable {
        hookAppProcess(packageParam);
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        hookActivitySettings();
    }

    private void hookAppProcess(XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod("android.view.ViewConfiguration",
                packageParam.classLoader, "hasPermanentMenuKey",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        param.setResult(Boolean.valueOf(true));
                    }
                });
    }

    private static int FLAG_NEEDS_MENU_KEY = getStaticIntField(WindowManager.LayoutParams.class, "FLAG_NEEDS_MENU_KEY");
    private static final String PROP_LEGACY_MENU = "AppSettings-LegacyMenu";

    public static void hookActivitySettings() {
        try {
            findAndHookMethod("com.android.internal.policy.impl.PhoneWindow", null, "generateLayout",
                    "com.android.internal.policy.impl.PhoneWindow.DecorView", new XC_MethodHook() {

                        @SuppressLint("InlinedApi")
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Window window = (Window) param.thisObject;
                            View decorView = (View) param.args[0];
                            Context context = window.getContext();
                            String packageName = context.getPackageName();

                            window.setFlags(FLAG_NEEDS_MENU_KEY, FLAG_NEEDS_MENU_KEY);
                            setAdditionalInstanceField(window, PROP_LEGACY_MENU, Boolean.TRUE);

                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }

        try {
            findAndHookMethod(Window.class, "setFlags", int.class, int.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                            int flags = (Integer) param.args[0];
                            int mask = (Integer) param.args[1];
                            if ((mask & FLAG_NEEDS_MENU_KEY) != 0) {
                                Boolean menu = (Boolean) getAdditionalInstanceField(param.thisObject, PROP_LEGACY_MENU);
                                if (menu != null) {
                                    if (menu.booleanValue()) {
                                        flags |= FLAG_NEEDS_MENU_KEY;
                                    }
                                    param.args[0] = flags;
                                }
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }

    }
}