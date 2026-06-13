package com.techiguru.edith;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
ImageView iv_speak;
int processID=100;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        iv_speak=findViewById(R.id.iv_speak);
        //to click on tap to speak
        iv_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voice=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //to get all languages
                voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                //to show the message for speak
                voice.putExtra(RecognizerIntent.EXTRA_PROMPT,"speak now");
                //pass the intent to OS
                startActivityForResult(voice,processID);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //to get the OS output voice to text format

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==processID && data!=null)
        {
            //to get the text from intent
            ArrayList<String> list=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this, list.get(0).toString(), Toast.LENGTH_SHORT).show();
            //to pass the number of operations based on statement
            switch (list.get(0).toString().toLowerCase())
            {
                case "open camera":
                    Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(camera);
                    break;

                case "turn on bluetooth":
                    Intent bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(
                                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                                    100);

                            return;
                        }
                    }

                    startActivity(bluetooth);
                    break;

                case "play song":
                    mp = MediaPlayer.create(this,
                            Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3?"));
                    mp.start();
                    break;

                case "stop song":
                    mp.stop();
                    break;

                default:
                    Intent share=new Intent(Intent.ACTION_SEND);
                    //to attach the message with intent
                    share.putExtra(Intent.EXTRA_TEXT,list.get(0).toString());
                    //to define the data type
                    share.setType("text/plain");
                    startActivity(Intent.createChooser(share,"share via"));
            }
        }
    }
}