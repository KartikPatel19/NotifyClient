package com.deucate.kartik.notifyclient;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BackService extends Service {


    FirebaseDatabase mDatabase;
    DatabaseReference mReference, mLatRed;

    private static int CURRENT_VERSION = 1;
    private int latestversion = 1;

    String mTitle, mLink, mUpdateLink,mLastMsg;

    NotificationManager mManager;

    NotificationCompat.Builder mBuilder;
    Intent dataIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getData();

        return START_STICKY;
    }

    private void getData(){

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Latest");
        mLatRed = mDatabase.getReference();

        mLatRed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latestversion = dataSnapshot.child("LVClient").getValue(int.class);
                if (latestversion != CURRENT_VERSION) {
                    mUpdateLink = dataSnapshot.child("ClientLink").getValue(String.class);
                    updateApp();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTitle = dataSnapshot.child("Title").getValue(String.class);
                mLink = dataSnapshot.child("Link").getValue(String.class);

                dataIntent = new Intent(getApplicationContext(), TempActivity.class);
                dataIntent.putExtra("Link", mLink);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, dataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setLights(Color.BLUE, 500, 500);
                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                mBuilder.setVibrate(pattern);
                mBuilder.setStyle(new NotificationCompat.InboxStyle());
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setContentTitle("Notify");
                mBuilder.setContentText(mTitle);
                mBuilder.setSound(Uri.parse("android.resource://com.deucate.kartik.notifyclient/raw/notification"));
                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(mTitle));

//                Notification notification = mBuilder.build();

                if(mLastMsg.equals(mTitle)){
                    return;
                }else {
                    mManager.notify(1, mBuilder.build());
                    mLastMsg = mTitle;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateApp() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
        alertDialog.setTitle("Please update your app");
        alertDialog.setMessage("Update your app and get new deal!!");
        alertDialog.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Uri uri = Uri.parse(mUpdateLink);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent1);

            }
        });

        alertDialog.create().show();

    }

}
