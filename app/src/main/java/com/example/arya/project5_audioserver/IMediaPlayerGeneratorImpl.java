package com.example.arya.project5_audioserver;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;

public class IMediaPlayerGeneratorImpl extends Service {
    MediaPlayer mPlayer;
    DatabaseAdapter dbHelper;
    String currentSong="empty";

    public IMediaPlayerGeneratorImpl() {
    }
    //method to create the reference of the database adapter and clear all the database on a new run
    public void onCreate() {
        dbHelper = new DatabaseAdapter(this);
        clearAll();
    }

    //Method to delete the SQL Lite Database every time the app is started.
    public void clearAll(){
        dbHelper.getWritableDatabase().delete(DatabaseAdapter.TABLE_NAME,null,null);
    }

    // Implement the Stub for this Object
    IMediaPlayerGenerator.Stub mBinder= new IMediaPlayerGenerator.Stub() {
        // Implement the play remote method
        @Override
        public void getPlay(String str) throws RemoteException {
            currentSong=str;
            String filename = "android.resource://" + getApplicationContext().getPackageName() + "/raw/"+"clip"+str;
            if(mPlayer!=null)
            {
                if(mPlayer.isPlaying())
                    mPlayer.stop();
            }

            //create new instance of the in-built media player in android
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(getApplicationContext(), Uri.parse(filename));
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mPlayer!=null) {
                //playing the music from the beginning when play is hit
                if (mPlayer.isPlaying()) {
                    mPlayer.seekTo(0);
                } else {
                    mPlayer.seekTo(0);
                    mPlayer.start();
                }

                //Sending the play-status to the database adapter
                if (!currentSong.equalsIgnoreCase("empty")){
                    dbHelper.insertData("Clip"+" "+currentSong +" "+ "is" +" "+ "Played");
                }
            }
        }

        // Implement the pause remote method
        @Override
        public void getPause() throws RemoteException {
            if (mPlayer!=null) {
                //pause the song when it is playing
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();

                    //Sending the pause-status to the database adapter
                    if (!currentSong.equalsIgnoreCase("empty")){
                        dbHelper.insertData("Clip"+" "+currentSong +" "+ "is" +" "+ "Paused");
                    }
                }
            }
        }

        // Implement the resume remote method
        @Override
        public void getResume() throws RemoteException {
            if (mPlayer!=null) {
                //resume the song when the song is playing
                if (!(mPlayer.isPlaying())) {
                    mPlayer.start();

                    //Sending the resume-status to the database adapter
                    if (!currentSong.equalsIgnoreCase("empty")){
                        dbHelper.insertData("Clip"+" "+currentSong +" "+ "is" +" "+ "Resumed");
                    }
                }
            }
        }

        // Implement the stop remote method
        @Override
        public void getStop() throws RemoteException {
            if (mPlayer!=null) {
                //pause the song when it is playing and take it to the beginning-> stop functionality
                if (mPlayer.isPlaying()) {
                    mPlayer.seekTo(0);
                    mPlayer.pause();

                    //Sending the stop-status to the database adapter
                    if (!currentSong.equalsIgnoreCase("empty")){
                        dbHelper.insertData("Clip"+" "+currentSong +" "+ "is" +" "+ "Stopped");
                    }
                }else {
                    if (mPlayer.getCurrentPosition()!=0){
                        mPlayer.seekTo(0);

                        //Sending the stop-status to the database adapter
                        if (!currentSong.equalsIgnoreCase("empty")){
                            dbHelper.insertData("Clip"+" "+currentSong +" "+ "is" +" "+ "Stopped");
                        }
                    }
                }
            }
        }

        //Method to retrieve all the records from the SQL Lite Database
        @Override
        public String[] getAllData() throws RemoteException {
            String arr[] = dbHelper.getAllData();
            return arr;
        }
    };

    //over-riding the in-built Binder method
         @Override
        public IBinder onBind(Intent intent) {
            return mBinder;
    }
}
