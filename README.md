LegacyMenu
==========

This is an Xposed module for Android devices. It requires the [Xposed Framework](http://forum.xda-developers.com/xposed/xposed-installer-versions-changelog-t2714053) to be installed.

Enforces the legacy menu ('3 dot menu') to be always visible in the onscreen navigation bar.

The intention of this module is to provide a workaround so that my Samsung S4's stock apps remain usable **without a working hardware menu key**. Very handy if the capacitive back/menu buttons have been damaged (e.g. due to water damage). Of course, alternatively you could just flash a custom ROM like CM which properly supports the onscreen navigation bar in all its built-in apps.

I recommend using this in combination with the [Hideable Nav Bar Xposed module](http://repo.xposed.info/module/ztc1997.hideablenavbar), which will ensure that stock apps like the **Samsung dialer** will be able to properly fit onscreen by hiding the navigation bar when needed.

Requires the navigation bar to be enabled. This can be enabled by the following line in `/system/build.prop`:

    qemu.hw.mainkeys=0

Note: This either works or doesn't, depending on manufacturer ROM customisations - see the internet for more information.

## Screenshot

The three dot menu is visible - it's at the very right of the navigation bar. yay :)

![Screenshot](https://dl2.pushbulletusercontent.com/yj6HryeS3C9xoLifoR8OFTdOPJLnsvxO/Screenshot_2015-11-17-21-12-52.png)

## Tested on
Samsung Galaxy S4 i9505, Lollipop 5.0.1

## Attributions
[rovo89's App Settings module](https://github.com/rovo89/XposedAppSettings)
