package org.techtown.dangguen.Adapter;

import android.view.View;

import org.techtown.dangguen.Adapter.RestaurantAdapter;

public interface OnRestaurantClickListener {
    //item을 누르면 이 함수가 호출된다
    public void onItemClick(RestaurantAdapter.ViewHolder holder, View view, int position);

}
