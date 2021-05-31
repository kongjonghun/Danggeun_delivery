package org.techtown.dangguen.Adapter;
// adapter에서 item들을 관리, layout도 viewHoler에 넣어서 관리
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.dangguen.DetailActivity;
import org.techtown.dangguen.MessageActivity;
import org.techtown.dangguen.Model.Chat;
import org.techtown.dangguen.Model.User;
import org.techtown.dangguen.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//viewholder를 담고 있는 Adapter를 상속한다.
// recyclerview.adapter를 상속받음
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isChat;
    private boolean whichChat;

    String theLastMessage;

    public ChatAdapter(Context mContext, List<User> mUsers, boolean isChat, boolean whichChat){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.isChat = isChat;
        this.whichChat = whichChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }
//        holder.host.setVisibility(View.GONE);
//        holder.host_id.setVisibility(View.GONE);
        if(isChat){
            lastMessage(user.getId(), holder.last_msg);
        }
        else{
            holder.last_msg.setVisibility(View.GONE);
        }


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(whichChat){
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("user_img", user.getImageURL());
                    intent.putExtra("userid", user.getId());
                    intent.putExtra("x", user.getX());
                    intent.putExtra("Y", user.getY());
                    mContext.startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext, MessageActivity.class);
                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("user_img", user.getImageURL());
                    intent.putExtra("userid", user.getId());
                    intent.putExtra("x", user.getX());
                    intent.putExtra("Y", user.getY());
                    mContext.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    // 2개 Class 생성 필요 : Single Adapter Class + ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username, host_id;
        public CircleImageView profile_image;
        public TextView host;
        LinearLayout item;
        TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            host = itemView.findViewById(R.id.host);
            host_id = itemView.findViewById(R.id.host_id);
            username = itemView.findViewById(R.id.name);
            profile_image = itemView.findViewById(R.id.imageView);
            item = itemView.findViewById(R.id.item);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }
    // check fpr last message
    private void lastMessage(String userid, TextView last_msg){
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);

                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No Messaage");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
