package org.techtown.dangguen.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.dangguen.Adapter.RestaurantAdapter;
import org.techtown.dangguen.Model.Restaurant;
import org.techtown.dangguen.R;

import java.util.ArrayList;
import java.util.List;

public class Frag_res2 extends Fragment {

    RecyclerView recyclerView;
    RestaurantAdapter adapter;
    List<Restaurant> restaurants;
    private static String URL = "http://3.34.105.174/db/koreanfood.php";

    public Frag_res2(){ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        recyclerView = view.findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager((new LinearLayoutManager(view.getContext())));

        restaurants = new ArrayList<>();

        extractRestaurants();

        return view;
    }

    private void extractRestaurants() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);
                        String name = object.getString("name");
                        String image_path = object.getString("image_path");
                        String price = object.getString("price");
                        String min_del = object.getString("min_del");
                        String max_del = object.getString("max_del");

                        Restaurant restaurant = new Restaurant(name, image_path, price, min_del, max_del);
                        restaurants.add(restaurant);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new org.techtown.dangguen.Adapter.RestaurantAdapter(getActivity(), restaurants);

                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: ");
            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
