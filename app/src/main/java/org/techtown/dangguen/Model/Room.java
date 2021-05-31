package org.techtown.dangguen.Model;

import android.util.Log;

// 데이터 담고있는 restaurant객체
public class Room {

    String manager_id;
    String res_img;
    String res_name;
    String min_del;
    String max_del;
    String prcie;

    public Room(String manager_id, String res_img, String res_name, String min_del, String max_del, String prcie) {
        this.manager_id = manager_id;
        this.res_img = res_img;
        this.res_name = res_name;
        this.min_del = min_del;
        this.max_del= max_del;
        this.prcie = prcie;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getRes_img() {
        return res_img;
    }

    public void setRes_img(String res_img) {
        this.res_img = res_img;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public String getMin_del() {
        return min_del;
    }

    public void setMin_del(String min_del) {
        this.min_del = min_del;
    }

    public String getMax_del() {
        return max_del;
    }

    public void setMax_del(String max_del) {
        this.max_del = max_del;
    }

    public String getPrcie() {
        return prcie;
    }

    public void setPrcie(String prcie) {
        this.prcie = prcie;
    }
}
