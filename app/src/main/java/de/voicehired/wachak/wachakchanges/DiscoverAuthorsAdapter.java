package de.voicehired.wachak.wachakchanges;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.voicehired.wachak.R;
import de.voicehired.wachak.activity.OnlineFeedViewActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vetero on 01-02-2016.
 */
public class DiscoverAuthorsAdapter extends RecyclerView.Adapter<DiscoverAuthorsAdapter
        .ViewHolder> {

    Context c;
    JSONArray jsonArray;

    public DiscoverAuthorsAdapter(Context context, String s) {
        try {
            this.c = context;
            JSONObject jsonObject = new JSONObject(s);
            jsonArray = jsonObject.getJSONArray("users");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.tvItemName.setText(jsonObject.getString("name"));
            holder.tvItemDes.setText(jsonObject.getString("title"));
            Picasso.with(c).load(jsonObject.getString("pic")).into(holder.imgThumbnail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgThumbnail;
        public TextView tvItemName;
        public TextView tvItemDes;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvItemName = (TextView) itemView.findViewById(R.id.tv_name);
            tvItemDes = (TextView) itemView.findViewById(R.id.tv_des_name);

            imgThumbnail.setOnClickListener(this);
            tvItemDes.setOnClickListener(this);
            tvItemName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(getAdapterPosition());
                Intent intent = new Intent(c, OnlineFeedViewActivity.class);
                intent.putExtra(OnlineFeedViewActivity.ARG_FEEDURL, jsonObject.getString("feed"));
                intent.putExtra(OnlineFeedViewActivity.ARG_TITLE, c.getString(R.string.add_feed_label));
                c.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
