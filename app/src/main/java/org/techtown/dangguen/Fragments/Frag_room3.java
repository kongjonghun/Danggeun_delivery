package org.techtown.dangguen.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.dangguen.Adapter.RoomAdapter;
import org.techtown.dangguen.Model.Room;
import org.techtown.dangguen.R;
import org.techtown.dangguen.RecyclerViewEmptySupport;

import java.util.ArrayList;
import java.util.List;

public class Frag_room3 extends Fragment {
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter2;
    private List<Room> rooms;
    private TextView list_empty;
    private static ArrayList list_id;

    private static String manager;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = user.getUid();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_room3, container, false);

        recyclerView = view.findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        list_empty = view.findViewById(R.id.list_empty);
//        recyclerView.setEmptyView(list_empty);

        rooms = new ArrayList<>();

        list_id = new ArrayList();
        jsonParse();
        return view;
    }

    private void jsonParse() {

        String URL = "http://3.34.105.174/db/cal_5min.php?id="+ user_id;
        Log.d("URLl", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String id = object.getString("host_id");
                        Log.d("id2", id);

                        list_id.add(id);
                    }
                    Log.d("list3", list_id.toString());
                    readroom();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: ");
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void readroom(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Roomlist");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Room room1 = new Room(
                        snapshot.child("manager_id").getValue(String.class),
                        snapshot.child("res_img").getValue(String.class),
                        snapshot.child("res_name").getValue(String.class),
                        snapshot.child("min_del").getValue(String.class),
                        snapshot.child("max_del").getValue(String.class),
                        snapshot.child("price").getValue(String.class));

                for (int i=0; i<list_id.size();i++){
                    if (room1.getManager_id().equals(list_id.get(i).toString())){
                        rooms.add(room1);
                    }
                }
                roomAdapter2 = new RoomAdapter(getContext(), rooms);
                recyclerView.setAdapter(roomAdapter2);

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}