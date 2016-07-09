package de.voicehired.wachak.wachakchanges;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import de.voicehired.wachak.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vetero on 01-02-2016.
 */
public class DiscoverAuthors extends Fragment {

    public static final String TAG = "DiscoverAuthors";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    String location = "http://52.77.238.226/api/v1/list";
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_author, null, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new getAuthors().execute();

        return view;
    }

    private class getAuthors extends AsyncTask {
        String responseData;

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                HttpURLConnection connection;
                InputStream is;
                URL url = new URL(location);
                connection = (HttpURLConnection) url.openConnection();
                is = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                responseData = response.toString();
                return responseData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            if (responseData != null) {
                mAdapter = new DiscoverAuthorsAdapter(getContext(), responseData);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Toast.makeText(getContext(), "Could not connect, please try later.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}