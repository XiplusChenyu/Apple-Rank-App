package columbia.xichenyu.rank_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listSongs;
    private MenuItem checkBox;
    private String feedURL="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        checkBox = menu.findItem(R.id.mnuLimit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean itemLimited = false;
        int limitNum = 50;

        switch (id) {
            case R.id.mnuApps:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                checkBox.setEnabled(true);
                checkBox.setChecked(false);
                break;

            case R.id.mnuMovies:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml";
                checkBox.setEnabled(false);
                checkBox.setChecked(false);
                break;

            case R.id.mnuSong:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                checkBox.setEnabled(true);
                checkBox.setChecked(false);
                break;

            case R.id.mnuLimit:
                if (!item.isChecked()){
                    item.setChecked(true);
                    itemLimited = true;
                }
                else {
                    item.setChecked(false);
                    itemLimited = false;
                }


                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        if (itemLimited) limitNum = 5;

        downloadURL(String.format(feedURL, limitNum));
        return true;
    }

    private void downloadURL(String feedURL){
        DownloadData downloadData = new DownloadData(); // when call execute method; Async is called
        downloadData.execute(feedURL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listSongs = (ListView) findViewById(R.id.xmlview);
        String URLDefault = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=50/xml";

        downloadURL(URLDefault);

//        DownloadData downloadData = new DownloadData(); // when call execute method; Async is called
//        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml");
//        Log.d(TAG, "onCreate finished Async");
    }

    // create the async task instead of black the onCreate method

    private class DownloadData extends AsyncTask<String, Void, String>{

        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseXML parsexml = new parseXML();
            parsexml.parse(s);
//            Log.d(TAG, "XML file: " + s); // add this line to show what the rss feed look like;

//            ArrayAdapter<FeedDealer> arrayAdapter = new ArrayAdapter<FeedDealer>(
//                    MainActivity.this, R.layout.list_item, parsexml.getSongs());
//            listSongs.setAdapter(arrayAdapter);
            FeedAdapter feedAdapter =  new FeedAdapter(MainActivity.this, R.layout.list_record, parsexml.getSongs());
            listSongs.setAdapter(feedAdapter);


        }

        @Override
        protected String doInBackground(String... strings) {
            String feed = downloadXML(strings[0]);
            if (feed == null)  Log.e(TAG, "doInBackground: Download failed" );
            // return to onPostExecute
            return feed;

        }

        // build the XML from BufferedReader
        private StringBuilder createXML(BufferedReader reader){
            StringBuilder xmlfile = new StringBuilder();
            int charNumber;
            char[] inputBuffer = new char[500];
            try{
                while(true){
                    charNumber = reader.read(inputBuffer);
                    if (charNumber < 0) break;
                    if (charNumber > 0){
                        xmlfile.append(String.copyValueOf(inputBuffer, 0, charNumber));
                    }
                }
                reader.close();
            }
            catch (IOException e){
                Log.e(TAG, "XML I/O Exception reading data" + e.getMessage());
            }
            return xmlfile;
        }

        // Write this method to obtain XML from Server
        private String downloadXML(String path){
            try{
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                int response = conn.getResponseCode();

                // read from the Http Connection
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder xmlfile = new StringBuilder();
                xmlfile = createXML(reader);
                return xmlfile.toString();
            }
            catch (MalformedURLException e){
                Log.e(TAG, "XML invalid XML" + e.getMessage());
            }
            catch (IOException e){
                Log.e(TAG, "XML I/O Exception reading data" + e.getMessage());
            }

            // need to catch security exception here
            catch (SecurityException e){
                Log.e(TAG, "XML SecurityException: " + e.getMessage());
            }
            return null;
        }
    }

}
