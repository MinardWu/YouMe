package com.minardwu.youme.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MinardWu on 2016/3/20.
 */
public class GetSkipWholeReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.minardwu.youme.br.intent.action.GetSkipWholeReceiver";

    public GetSkipWholeReceiver(){
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
