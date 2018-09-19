package com.mhsabuj1gmail.a4dchair;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;




public class MainActivity extends AppCompatActivity  {
    int count = 0;

    char[] command = {'l', 'c', 'r'};   //servo code


    EditText start,stop,start1,stop1;
    TextView text,second;//Time to send data

    //Bluetooth code
    private final String DEVICE_ADDRESS = "98:D3:31:F9:3C:BC"; //MAC Address of Bluetooth Module  //Has to change
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    //boolean found = false;

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    Button bluetooth_connect_btn;//Bluetooth code End


    //Timer Code

    Thread t;
    boolean check= true, stop_thread = false;
    int s,st,s1,st1,j_servo=0,j_servo1=0;
    void restart_thread()
    {
        t=new Thread(){


            @Override
            public void run(){

                while(!isInterrupted()){




                    try {
                        if(stop_thread)
                        {
                            break;
                        }
                        Thread.sleep(1000);  //1000ms = 1 sec
                        if(isInterrupted())
                            break;


                        runOnUiThread(new Runnable() {


                            @Override
                            public void run() {
                                count++;
                                second.setText(String.valueOf(count));


                                if(!start.getText().toString().equals("") && !stop.getText().toString().equals("") && start1.getText().toString().equals("") && stop1.getText().toString().equals(""))   //To activate first time motors
                                {
                                    s = Integer.parseInt(start.getText().toString());
                                    st = Integer.parseInt(stop.getText().toString());

                                    if(count>= s && count<= st)
                                    {

                                        try
                                        {
                                                outputStream.write("v".getBytes()); //transmits the value of command to the bluetooth module
                                                if(j_servo>=command.length)
                                                {
                                                    j_servo = 0;
                                                }

                                                if(j_servo != command.length)
                                                {
                                                    outputStream.write(command[j_servo]);
                                                }
                                                j_servo++;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    else if(count>st)
                                    {
                                        try
                                        {
                                                outputStream.write("e".getBytes()); //transmits the value of command to the bluetooth modul;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }   //End of first part


                                if(!start.getText().toString().equals("") && !stop.getText().toString().equals("") && !start1.getText().toString().equals("") && !stop1.getText().toString().equals(""))   //To activate first time motors
                                {
                                    s = Integer.parseInt(start.getText().toString());
                                    st = Integer.parseInt(stop.getText().toString());
                                    s1 = Integer.parseInt(start1.getText().toString());
                                    st1 = Integer.parseInt(stop1.getText().toString());

                                    if(count>= s && count<= st)
                                    {

                                        try
                                        {
                                                outputStream.write("v".getBytes()); //transmits the value of command to the bluetooth module
                                                if(j_servo>=command.length)
                                                {
                                                    j_servo = 0;
                                                }

                                                if(j_servo != command.length)
                                                {
                                                    outputStream.write(command[j_servo]);
                                                }
                                                j_servo++;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    else if(count>st)
                                    {
                                        try
                                        {

                                                outputStream.write("e".getBytes()); //transmits the value of command to the bluetooth modul;
                                                j_servo=0;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }  //End of first  part

                                    if(count>= s1 && count<= st1)
                                    {

                                        try
                                        {
                                                outputStream.write("v".getBytes()); //transmits the value of command to the bluetooth module
                                                if(j_servo1>=command.length)
                                                {
                                                    j_servo1 = 0;
                                                }

                                                if(j_servo1 != command.length)
                                                {
                                                    outputStream.write(command[j_servo1]);
                                                }
                                                j_servo1++;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    else if(count>st1)
                                    {
                                        try
                                        {
                                                outputStream.write("e".getBytes()); //transmits the value of command to the bluetooth modul;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }  //Second Part
                                }


                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };    //Timer Code

    }  //Timer Code






    //videoPlayer code
    private EditText videoPathEditor = null;

    private Button browseVideoFileButton = null;

    private Button playVideoButton = null;

    private Button stopVideoButton = null;

    private Button pauseVideoButton = null;

    private Button continueVideoButton = null;

    private Button replayVideoButton = null;

    private VideoView playVideoView = null;

    private ProgressBar videoProgressBar = null;

    // Request code for user select video file.
    private static final int REQUEST_CODE_SELECT_VIDEO_FILE = 1;

    // Request code for require android READ_EXTERNAL_PERMISSION.
    private static final int REQUEST_CODE_READ_EXTERNAL_PERMISSION = 2;

    // Used when update video progress thread send message to progress bar handler.
    private static final int UPDATE_VIDEO_PROGRESS_BAR = 3;

    // Save local video file uri.
    private Uri videoFileUri = null;

    // Wait update video progress thread sent message, then update video play progress.
    private Handler videoProgressHandler = null;

    // Save current video play position.
    private int currVideoPosition = 0;

    // Save whether the video is paused or not.
    private boolean isVideoPaused = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        start = findViewById(R.id.f_start_time);
        stop = findViewById(R.id.f_stop_time);
        start1 = findViewById(R.id.s_start_time);
        stop1 = findViewById(R.id.s_stop_time);
        second = findViewById(R.id.sec);   //display seconds


        //Bluetooth code
        bluetooth_connect_btn = (Button) findViewById(R.id.bluetooth_connect_btn);   //Connects to Bluetooth


        //Button that connects the device to the bluetooth module when pressed
        bluetooth_connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (BTinit()) {
                        BTconnect();
                    }

            }
        });   //Bluetooth Code



        //Timer Code

        t=new Thread(){


            @Override
            public void run(){

                while(!isInterrupted()){




                    try {
                        if(stop_thread)
                        {
                            break;
                        }
                        Thread.sleep(1000);  //1000ms = 1 sec
                        if(isInterrupted())
                            break;


                        runOnUiThread(new Runnable() {


                            @Override
                            public void run() {
                                count++;
                                second.setText(String.valueOf(count));


                                if(!start.getText().toString().equals("") && !stop.getText().toString().equals("") && start1.getText().toString().equals("") && stop1.getText().toString().equals(""))   //To activate first time motors
                                {
                                    s = Integer.parseInt(start.getText().toString());
                                    st = Integer.parseInt(stop.getText().toString());

                                    if(count>= s && count<= st)
                                    {

                                        try
                                        {
                                                outputStream.write("v".getBytes()); //transmits the value of command to the bluetooth module
                                                if(j_servo==command.length)
                                                {
                                                    j_servo = 0;
                                                }

                                                if(j_servo != command.length)
                                                {
                                                    outputStream.write(command[j_servo]);
                                                }
                                                j_servo++;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    else if(count>st)
                                    {
                                        try
                                        {
                                                outputStream.write("e".getBytes()); //transmits the value of command to the bluetooth modul;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }   //End of first part


                                if(!start.getText().toString().equals("") && !stop.getText().toString().equals("") && !start1.getText().toString().equals("") && !stop1.getText().toString().equals(""))   //To activate first time motors
                                {
                                    s = Integer.parseInt(start.getText().toString());
                                    st = Integer.parseInt(stop.getText().toString());
                                    s1 = Integer.parseInt(start1.getText().toString());
                                    st1 = Integer.parseInt(stop1.getText().toString());

                                    if(count>= s && count<= st)
                                    {

                                        try
                                        {
                                                outputStream.write("v".getBytes()); //transmits the value of command to the bluetooth module
                                                if(j_servo==command.length)
                                                {
                                                    j_servo = 0;
                                                }

                                                if(j_servo != command.length)
                                                {
                                                    outputStream.write(command[j_servo]);
                                                }
                                                j_servo++;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    else if(count>st)
                                    {
                                        try
                                        {
                                                outputStream.write("e".getBytes()); //transmits the value of command to the bluetooth modul;
                                                j_servo=0;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }  //End of first  part

                                    if(count>= s1 && count<= st1)
                                    {

                                        try
                                        {

                                                outputStream.write("v".getBytes()); //transmits the value of command to the bluetooth module
                                                if(j_servo1==command.length)
                                                {
                                                    j_servo1 = 0;
                                                }

                                                if(j_servo != command.length)
                                                {
                                                    outputStream.write(command[j_servo1]);
                                                }
                                                j_servo1++;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    else if(count>st1)
                                    {
                                        try
                                        {
                                                outputStream.write("e".getBytes()); //transmits the value of command to the bluetooth modul;

                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }  //Second Part
                                }


                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };    //Timer Code


        //Video Code
        setTitle("4D Chair");

        // Init this example used video components.
        initVideoControls();

        // When user input video file url in the video file path edittext input text box.
        videoPathEditor.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                int action = keyEvent.getAction();
                if(action == KeyEvent.ACTION_UP) {
                    String text = videoPathEditor.getText().toString();
                    if (text.length() > 0) {
                        // If user input video file url, enable play button.
                        playVideoButton.setEnabled(true);
                        pauseVideoButton.setEnabled(false);
                        replayVideoButton.setEnabled(false);
                    } else {
                        // If user input nothing, disable all buttons.
                        playVideoButton.setEnabled(false);
                        pauseVideoButton.setEnabled(false);
                        replayVideoButton.setEnabled(false);
                    }
                }
                return false;
            }
        });

        // If user click browse video file button.
        browseVideoFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check whether user has granted read external storage permission to this activity.
                int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                // If not grant then require read external storage permission.
                if(readExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
                {
                    String requirePermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(MainActivity.this, requirePermission, REQUEST_CODE_READ_EXTERNAL_PERMISSION);
                }else {
                    selectVideoFile();
                }
            }
        });

        // Click this button to play user browsed or input video file.
        playVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoFilePath = videoPathEditor.getText().toString();

                if(!TextUtils.isEmpty(videoFilePath))
                {
                    if(!videoFilePath.trim().toLowerCase().startsWith("http")) {
                        // Play local video file.
                        playVideoView.setVideoURI(videoFileUri);
                    }else
                    {
                        // Convert the web video url to a Uri object.
                        Uri webVideoFileUri = Uri.parse(videoFilePath.trim());

                        // Play web video file use the Uri object.
                        playVideoView.setVideoURI(webVideoFileUri);
                    }

                    playVideoView.setVisibility(View.VISIBLE);

                    videoProgressBar.setVisibility(ProgressBar.VISIBLE);

                    currVideoPosition = 0;

                    playVideoView.start();
                    j_servo=0;
                    t.start();
                    stop_thread=false;

                    playVideoButton.setEnabled(false);

                    stopVideoButton.setEnabled(true);

                    pauseVideoButton.setEnabled(true);

                    continueVideoButton.setEnabled(false);

                    replayVideoButton.setEnabled(true);
                }

            }
        });

        // Click this button to stop playing video file.
        stopVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Stop video playing.
                playVideoView.stopPlayback();
                restart_thread();
                count=0;
                j_servo=0;
                stop_thread = true;

                try
                {
                        outputStream.write("e".getBytes()); //transmits the value of command to the bluetooth modul;

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                // Seek to the beginning of the video file.
                playVideoView.seekTo(0);

                playVideoButton.setEnabled(true);

                stopVideoButton.setEnabled(false);

                pauseVideoButton.setEnabled(false);

                continueVideoButton.setEnabled(false);

                replayVideoButton.setEnabled(false);
            }
        });

        // Click this button to pause playing video file.
        pauseVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Pause video play.
                playVideoView.pause();
                t.interrupt();
                restart_thread();
                j_servo=0;
                stop_thread = true;

                try
                {
                        outputStream.write("e".getBytes()); //transmits the value of command to the bluetooth modul;

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                isVideoPaused = true;

                // Record current video play position.
                currVideoPosition = playVideoView.getCurrentPosition();

                playVideoButton.setEnabled(false);

                stopVideoButton.setEnabled(true);

                pauseVideoButton.setEnabled(false);

                continueVideoButton.setEnabled(true);

                replayVideoButton.setEnabled(true);
            }
        });

        // Click this button to continue play paused video.
        continueVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playVideoView.seekTo(currVideoPosition);
                j_servo=0;
                t.start();
                stop_thread=false;

                playVideoButton.setEnabled(false);


                pauseVideoButton.setEnabled(true);

                stopVideoButton.setEnabled(true);

                continueVideoButton.setEnabled(false);

                replayVideoButton.setEnabled(true);
            }
        });

        // Click this button to replay video file.
        replayVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Replay video.
                playVideoView.resume();

                // Set current video play position to 0.
                currVideoPosition = 0;

                playVideoButton.setEnabled(false);

                pauseVideoButton.setEnabled(true);

                stopVideoButton.setEnabled(true);

                continueVideoButton.setEnabled(false);

                replayVideoButton.setEnabled(true);
            }
        });
    }


    //Bluetooth code
    //Initializes bluetooth module
    public boolean BTinit()
    {

        Boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    public boolean BTconnect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
        }

        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return connected;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }                               //Bluetooth code exits






    /*
      Initialise play video example controls.
    * */
    private void initVideoControls()
    {
        if(videoPathEditor==null)
        {
            videoPathEditor = (EditText) findViewById(R.id.play_video_file_path_editor);
        }

        if(browseVideoFileButton==null)
        {
            browseVideoFileButton = (Button)findViewById(R.id.browse_video_file_button);
        }

        if(playVideoButton==null)
        {
            playVideoButton = (Button)findViewById(R.id.play_video_start_button);
        }

        if(stopVideoButton==null)
        {
            stopVideoButton = (Button)findViewById(R.id.play_video_stop_button);
        }

        if(pauseVideoButton==null)
        {
            pauseVideoButton = (Button)findViewById(R.id.play_video_pause_button);
        }

        if(continueVideoButton==null)
        {
            continueVideoButton = (Button)findViewById(R.id.play_video_continue_button);
        }

        if(replayVideoButton==null)
        {
            replayVideoButton = (Button)findViewById(R.id.play_video_replay_button);
        }

        if(playVideoView==null)
        {
            playVideoView = (VideoView)findViewById(R.id.play_video_view);
        }

        if(videoProgressBar==null)
        {
            videoProgressBar = (ProgressBar) findViewById(R.id.play_video_progressbar);
        }

        if(videoProgressHandler==null)
        {
            // This handler wait and receive update progress bar message from child thread.
            videoProgressHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    // When receive update progressbar message.
                    if(msg.what == UPDATE_VIDEO_PROGRESS_BAR)
                    {
                        // Get current video play position.
                        int currVideoPosition = playVideoView.getCurrentPosition();

                        // Get total video length.
                        int videoDuration = playVideoView.getDuration();

                        // Calculate the percentage.
                        int progressPercent = currVideoPosition * 100 / videoDuration;

                        // 10 times percentage value to make effect clear.
                        videoProgressBar.setProgress(progressPercent);
                    }
                }
            };

            // This thread send update video progress message to video progress Handler every 2 seconds.
            Thread updateProgressThread = new Thread()
            {
                @Override
                public void run() {

                    try {
                        while (true) {
                            // Create update video progressbar message.
                            Message msg = new Message();
                            msg.what = UPDATE_VIDEO_PROGRESS_BAR;

                            // Send the message to video progressbar update handler.
                            videoProgressHandler.sendMessage(msg);

                            // Sleep 2 seconds.
                            Thread.sleep(2000);
                        }
                    }catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            };
            // Start the thread.
            updateProgressThread.start();
        }

        setContinueVideoAfterSeekComplete();
    }

    /* This method start get content activity to let user select video file from local directory.*/
    private void selectVideoFile()
    {
        // Create an intent with action ACTION_GET_CONTENT.
        Intent selectVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);

        // Show video in the content browser.
        // Set selectVideoIntent.setType("*/*") to select all data
        // Intent for this action must set content type, otherwise you will encounter below exception : android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.GET_CONTENT }
        selectVideoIntent.setType("video/*");

        // Start android get content activity ( this is a android os built-in activity.) .
        startActivityForResult(selectVideoIntent, REQUEST_CODE_SELECT_VIDEO_FILE);
    }

    /* This method will be invoked when startActivityForResult method complete in selectVideoFile() method.
     *  It is used to process activity result that is started by startActivityForResult method.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Identify activity by request code.
        if(requestCode == REQUEST_CODE_SELECT_VIDEO_FILE)
        {
            // If the request is success.
            if(resultCode==RESULT_OK)
            {
                // To make example simple and clear, we only choose video file from local file,
                // this is easy to get video file real local path.
                // If you want to get video file real local path from a video content provider
                // Please read another article.
                videoFileUri = data.getData();

                String videoFileName = videoFileUri.getLastPathSegment();

                videoPathEditor.setText("You select video file is " + videoFileName);

                playVideoButton.setEnabled(true);

                pauseVideoButton.setEnabled(false);

                replayVideoButton.setEnabled(false);
            }
        }
    }

    /* Run this method after user choose grant read external storage permission or not. */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_READ_EXTERNAL_PERMISSION)
        {
            if(grantResults.length > 0)
            {
                int grantResult = grantResults[0];
                if(grantResult == PackageManager.PERMISSION_GRANTED)
                {
                    // If user grant the permission then open browser let user select audio file.
                    selectVideoFile();
                }else
                {
                    Toast.makeText(getApplicationContext(), "You denied read external storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /* This method is used to play video after seek complete, otherwise video playing will not be accurate.*/
    private void setContinueVideoAfterSeekComplete()
    {
        playVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {
                        if(isVideoPaused)
                        {
                            playVideoView.start();
                            isVideoPaused = false;
                        }
                    }
                });
            }
        });
    }
}
