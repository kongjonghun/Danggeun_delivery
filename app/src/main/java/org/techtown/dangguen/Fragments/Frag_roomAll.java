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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.dangguen.Adapter.RoomAdapter;
import org.techtown.dangguen.Model.Room;
import org.techtown.dangguen.R;

import java.util.ArrayList;
import java.util.List;


public class Frag_roomAll extends Fragment {

    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter2;
    private List<Room> rooms;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String user_id = user.getUid();
        Log.d(")))", user_id);
        View view = inflater.inflate(R.layout.fragment_roomall, container, false);

        recyclerView = view.findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rooms = new ArrayList<>();
        readroom();
        return view;
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


                rooms.add(room1);
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