package com.android.clockwork.flags;

import android.content.ContentResolver;
import android.provider.Settings;

public final class ClockworkFlags {
    private ClockworkFlags() {}

    /**
     * Enable or disable the User Absent, Touch Off Feature.
     */
    public static BooleanFlag userAbsentTouchOff(ContentResolver contentResolver) {
        return new BooleanFlag(
                contentResolver,
                Settings.Global.USER_ABSENT_TOUCH_OFF_FOR_SMALL_BATTERY_ENABLED,
                true);
    }

    /**
     * Enable or disable the User Absent, Radios Off Feature.
     */
    public static BooleanFlag userAbsentRadiosOff(ContentResolver contentResolver) {
        return new BooleanFlag(
                contentResolver,
                Settings.Global.USER_ABSENT_RADIOS_OFF_FOR_SMALL_BATTERY_ENABLED,
                true);
    }
}
