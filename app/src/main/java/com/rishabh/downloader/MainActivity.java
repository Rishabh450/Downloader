package com.rishabh.downloader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ts= String.valueOf(System.currentTimeMillis());
                try {
                    final File sdCard = Environment.getExternalStorageDirectory();
                    File file = new File(sdCard.getAbsolutePath() + "/ADownloader"+"/Pictures/"+ts);
                    if(!file.exists()) {
                        //Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.chatback);
                        Log.d("pathssss", "onPictureTaken - wrote to ");


                        File dir = new File(sdCard.getAbsolutePath() + "/ADownloader" + "/Pictures");
                        dir.mkdirs();
                        long t = System.currentTimeMillis();
                        String time = String.valueOf(t);

                        String fileName = ts;
                        fileName.trim();
                        Log.d("pathsssss", "onPictureTaken - wrote to " + fileName);

                        File outFile = new File(dir, fileName);
                        Log.d("pathsssss", "onPictureTaken - wrote to " + fileName + dir);
                        URL url = null;

                        //File f = null;
                        //String url1 = ;
                        Toast.makeText(MainActivity.this, "Download Started", Toast.LENGTH_SHORT).show();

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse( editText.getText().toString()));
                        request.setDescription("Download ");
                        request.setTitle(ts);
// in order for this if to run, you must use the android 3.2 to compile your app
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        Log.d("bhaiwa",Environment.DIRECTORY_DOWNLOADS);
                        request.setDestinationInExternalPublicDir( "ADownloader/Pictures", ts);

// get download service and enqueue file
                        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                        // manager.enqueue(request);
                        final long downloadID = manager.enqueue(request);
                        Log.d("downid", String.valueOf(downloadID));
                        // final long downloadID = downloadManager.enqueue(request);
                        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                //Fetching the download id received with the broadcast
                                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                Log.d("downid1", String.valueOf(downloadID)+" "+id);

                                //Checking if the received broadcast is for our enqueued download by matching download id
                                if (downloadID == id) {
                                    Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                                    Intent intenti = new Intent(Intent.ACTION_VIEW);
                                    intenti.setDataAndType(Uri.parse(sdCard.getAbsolutePath() + "/ADownloader"+"/Pictures/"+ts), getMimeType(sdCard.getAbsolutePath() + "/ADownloader"+"/Pictures/"+ts));

                                   // startActivity(intenti);
                                }
                            }

                        };
                        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


                                /*try {
                                    FileUtils.copyURLToFile(url, outFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/

                          /*  outStream = new FileOutputStream(outFile);
                           // bit.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            outStream.flush();
                            outStream.close();
*/
                        Log.d("pathssss", "onPictureTaken - wrote to " + outFile.getAbsolutePath());
                    }
                    else {
                        Intent intenti = new Intent(Intent.ACTION_VIEW);
                        intenti.setDataAndType(Uri.parse(sdCard.getAbsolutePath() + "/ADownloader"+"/Pictures/"+ts), getMimeType(sdCard.getAbsolutePath() + "/ADownloader"+"/Pictures/"+ts));

                       //+ startActivity(intenti);
                    }



                } finally {
                }

            }
        });


    }
    private String getMimeType(String url)
    {
        String parts[]=url.split("\\.");
        String extension=parts[parts.length-1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
