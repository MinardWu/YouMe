package com.minardwu.youme.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MinardWu on 2016/3/14.
 */
public class GetUserNPReceiver extends BroadcastReceiver{

    public static final String ACTION = "com.minardwu.youme.br.intent.action.GetUserNPReceiver";

    public GetUserNPReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
