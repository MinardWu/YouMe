package com.minardwu.youme.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GetWholeReceiver extends BroadcastReceiver{

    public static final String ACTION = "com.minardwu.youme.br.intent.action.GetWholeReceiver";

    public GetWholeReceiver(){
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
