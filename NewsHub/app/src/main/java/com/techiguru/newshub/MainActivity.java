package com.techiguru.newshub;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv_data;
    ArrayList<String> newsList;
    ArrayAdapter<String> ad;
    String apiUrl="https://newsdata.io/api/1/latest?apikey=pub_19b1db7a97bc40f1ac7f4415bc18d6ba&country=in&language=en";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        lv_data=findViewById(R.id.lv_data);
        newsList=new ArrayList<>();
        ad=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,newsList);
        lv_data.setAdapter(ad);
        fetchdata();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchdata() {


            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    apiUrl,
                    null,

                    response -> {

                        try {

                            JSONArray results = response.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {

                                JSONObject obj = results.getJSONObject(i);

                                String title = obj.optString("title");
                                String description = obj.optString("description");
                                String sourceName = obj.optString("source_name");
                                String pubDate = obj.optString("pubDate");
                                String country = obj.optString("country");
                                String language = obj.optString("language");
                                String category = obj.optString("category");
                                String datatype = obj.optString("datatype");
                                String creator = obj.optString("creator");
                                //String country=obj.getJSONObject("result").getString("country");

                                newsList.add(
                                        "Title: " + title +
                                                "\n\nDescription: " + description +
                                                "\n\nSource: " + sourceName+
                                                "\n\nPublish Date: " + pubDate+
                                                "\n\nType of News: " + datatype+
                                                "\n\ncreator: " + creator+
                                                "\n\nCategory: " + category+
                                                "\n\nLanguage: " + language+
                                                "\n\nCountry: " + country


                                );
                            }

                            ad.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    },

                    error -> Toast.makeText(
                            MainActivity.this,
                            error.toString(),
                            Toast.LENGTH_LONG
                    ).show()
            );

            queue.add(request);
        }

}
