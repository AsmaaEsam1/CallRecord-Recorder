package com.example.record;
import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Recording extends AccessibilityService {
   // private MediaRecorder mediaRecorder;
    private MP3Recorder mRecorder;
    public boolean recordStart;
    private File file;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Save audio file in external storage in alarm file
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        Date date = new Date();
        CharSequence c = DateFormat.format("MM-hh-mm-ss",date.getTime());
         mRecorder = new MP3Recorder(file.getAbsolutePath()+"/"+c+"rec.mp3");

      /*  mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setOutputFile(file.getAbsolutePath()+"/"+c+"rec.amr");
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
            public void onError(MediaRecorder arg0, int arg1, int arg2) {
                Toast.makeText(getApplicationContext(), "Crashed", Toast.LENGTH_SHORT).show();
            }};
        mediaRecorder.setOnErrorListener(errorListener);*/
        //call
        TelephonyManager telephonyManager =(TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        // 5

        telephonyManager.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                //stop Record
                if (TelephonyManager.CALL_STATE_IDLE == state && mRecorder == null){
                    mRecorder.stop();
                    recordStart = false;
                    stopSelf();
                }
                //start Record
                else if (TelephonyManager.CALL_STATE_OFFHOOK == state){
                    try {
                        mRecorder.start();
                        Toast.makeText(Recording.this, "start Recording", Toast.LENGTH_SHORT).show();
                        recordStart = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
        return START_STICKY;
    }

        }


