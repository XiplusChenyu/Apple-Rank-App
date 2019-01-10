package columbia.xichenyu.rank_app;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedDealer> songs;

    public FeedAdapter(Context context, int resource, List<FeedDealer> songs) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // No Need to reconstruct a new View each time!
        tvHolder tvHolder; // avoid use findViewbyid many times;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
             tvHolder = new tvHolder(convertView);
             convertView.setTag(tvHolder); // set Tag and retrieve in the future
        }
        else tvHolder = (tvHolder) convertView.getTag(); // pass the old one to current one;


//        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
//        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvArtist);
//        TextView tvRight = (TextView) convertView.findViewById(R.id.tvRight);
//        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
//        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);


        FeedDealer currentSong = songs.get(position);

        tvHolder.tvName.setText(currentSong.getTitle());
        tvHolder.tvArtist.setText(currentSong.getArtist());
        tvHolder.tvDate.setText("Release Date: " + currentSong.getDate());
        tvHolder.tvPrice.setText("Price: " + currentSong.getPrice());
        tvHolder.tvRight.setText(currentSong.getRight());
//        tvRight.setMovementMethod(ScrollingMovementMethod.getInstance());
//        tvRight.setHorizontallyScrolling(true);
//        tvRight.setFocusable(true);

        return convertView;
    }

    private class tvHolder{
        final TextView tvName;
        final TextView tvArtist;
        final TextView tvRight;
        final TextView tvPrice;
        final TextView tvDate;


        tvHolder(View v) {
            this.tvName = v.findViewById(R.id.tvName);
            this.tvArtist = v.findViewById(R.id.tvArtist);
            this.tvRight = v.findViewById(R.id.tvRight);
            this.tvPrice = v.findViewById(R.id.tvPrice);
            this.tvDate = v.findViewById(R.id.tvDate);

        }
    }
}
