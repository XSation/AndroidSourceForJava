/*
 * Copyright (C) 2010, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.sip;

import android.os.PowerManager;
import android.util.Log;

import java.util.HashSet;

class SipWakeLock {
    private static final boolean DEBUGV = SipService.DEBUGV;
    private static final String TAG = SipService.TAG;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager.WakeLock mTimerWakeLock;
    private HashSet<Object> mHolders = new HashSet<Object>();

    SipWakeLock(PowerManager powerManager) {
        mPowerManager = powerManager;
    }

    synchronized void reset() {
        mHolders.clear();
        release(null);
        if (DEBUGV) Log.v(TAG, "~~~ hard reset wakelock");
    }

    synchronized void acquire(long timeout) {
        if (mTimerWakeLock == null) {
            mTimerWakeLock = mPowerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, "SipWakeLock.timer");
            mTimerWakeLock.setReferenceCounted(true);
        }
        mTimerWakeLock.acquire(timeout);
    }

    synchronized void acquire(Object holder) {
        mHolders.add(holder);
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, "SipWakeLock");
        }
        if (!mWakeLock.isHeld()) mWakeLock.acquire();
        if (DEBUGV) Log.v(TAG, "acquire wakelock: holder count="
                + mHolders.size());
    }

    synchronized void release(Object holder) {
        mHolders.remove(holder);
        if ((mWakeLock != null) && mHolders.isEmpty()
                && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        if (DEBUGV) Log.v(TAG, "release wakelock: holder count="
                + mHolders.size());
    }
}
