package columbia.xichenyu.rank_app;

import android.provider.CalendarContract;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class parseXML {
    private static final String TAG = "parseXML";
    private ArrayList<FeedDealer> songs;

    public parseXML() {
        this.songs = new ArrayList<>();
    }

    public ArrayList<FeedDealer> getSongs() { // get songs;
        return songs;
    }

    public boolean parse(String fileXML){
        boolean status = true;
        FeedDealer currentSong = null;
        String dateFormat = "";
        boolean inEntry = false;
        boolean trueName = false; // there are many field calls name and use this to get the right one.
        String textValue = "";
        try {

            // Parse the XML file here
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(fileXML));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if ("entry".equalsIgnoreCase(tagName)){
//                            Log.d(TAG, "Start TagName " + tagName);
                            inEntry = true;
                            trueName = true;
                            currentSong = new FeedDealer();
                        } else if ("releasedate".equalsIgnoreCase(tagName)){
                             dateFormat = xpp.getAttributeValue(null,"label");

                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "End TagName " + tagName);

                        if (inEntry){
                            // avoid null when there is no tag

                            if ("entry".equalsIgnoreCase(tagName)){
                                songs.add(currentSong);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName) & trueName){
                                currentSong.setTitle(textValue);
                                trueName =false;
                            } else if ("artist".equalsIgnoreCase(tagName)){
                                currentSong.setArtist(textValue);
                            } else if ("releasedate".equalsIgnoreCase(tagName)) {
                                currentSong.setDate(dateFormat);

                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentSong.setImageURL(textValue);
                            } else if ("rights".equalsIgnoreCase(tagName)) {
                                currentSong.setRight(textValue);
                            } else if ("price".equalsIgnoreCase(tagName)) {
                                currentSong.setPrice(textValue);
                            }
                        }

                        break;

                    default:


                }

                eventType = xpp.next();
            }

            for (FeedDealer song: songs){
                Log.d(TAG, "\n******\n"+ song.toString());
            }


        }
        catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
