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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.dangguen.Adapter.ChatAdapter;
import org.techtown.dangguen.Model.Chatlist;
import org.techtown.dangguen.Model.User;
import org.techtown.dangguen.R;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Frag_Chat extends Fragment
{

    private RecyclerView recyclerView;
    private ChatAdapter roomAdapter;
    private List<User> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;
    private List<Chatlist> userList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("@@@@", "1됨");
        View view = inflater.inflate(R.layout.chat, container, false); // home 화면 연결

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Chatlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Log.e("@@@@22",snapshot1.getValue() + " / " +  snapshot1.getKey());

                    //내가 보낸 메시지
                    if (snapshot1.getKey().equals(fuser.getUid())) {
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            Chatlist chatlist = new Chatlist(snapshot2.getKey());// snapshot2.getValue(Chatlist.class);
                            userList.add(chatlist);
                        }
                    }
                    else{
                        //내가 받은 메시지
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            if (snapshot2.getKey().equals(fuser.getUid())) {
                                Chatlist chatlist = new Chatlist(snapshot1.getKey());// snapshot2.getValue(Chatlist.class);
                                userList.add(chatlist);
                            }
                        }
                    }
                }
                //중복제거 코드
                Set<Chatlist> set = new LinkedHashSet<>(userList);
                userList = new ArrayList<>(set);
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        reference = FirebaseDatabase.getInstance().getReference("Chatlist").orderByChild(fuser.getUid()).getRef(); //.child(fuser.getUid());// .orderByChild(fuser.getUid()).getRef();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.e("@@@@", snapshot.toString());
//                userList.clear();
//                for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                    Log.e("@@@@22",snapshot1.toString());
//                    if (snapshot1.getKey().equals(fuser.getUid())) {
//                        continue;
//                    }
//                    Chatlist chatlist = new Chatlist(snapshot1.getKey());// snapshot2.getValue(Chatlist.class);
//                    userList.add(chatlist);
//
//                }
//                chatList();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return view;
    }

    private void chatList() {
        Log.e("@@@@", "3됨");
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("@@@@", "4됨");
                mUsers.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);


                    for(Chatlist chatlist : userList){
                        if(user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                Log.e("@@@@", "5됨");
                roomAdapter = new ChatAdapter(getContext(), mUsers, true, false);
                Log.e("@@@@", "6됨");
                recyclerView.setAdapter(roomAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
