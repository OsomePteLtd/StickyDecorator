package com.osome.stickydecorator;

import android.content.res.Resources;

abstract class Util {

    static int dp2px(int px) {
        return (int) dpToPxF(px);
    }

    static float dpToPxF(int px) {
        return Resources.getSystem().getDisplayMetrics().density * px;
    }
}
