package org.techtown.dangguen.Adapter;

import android.view.View;

public interface OnRoomClickListener {
    //item을 누르면 이 함수가 호출된다
    public void onItemClick(ChatAdapter.ViewHolder holder, View view, int position);

}
