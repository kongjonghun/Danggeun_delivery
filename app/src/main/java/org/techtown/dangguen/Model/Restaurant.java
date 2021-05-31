package org.techtown.dangguen.Model;
// 데이터 담고있는 restaurant객체
public class Restaurant {

    String name;
    String img;
    String price;
    String min_del;
    String max_del;

    public Restaurant(String name, String img, String price, String min_del, String max_del) {
        this.name = name;
        this.img = img;
        this.price = price;
        this.min_del = min_del;
        this.max_del = max_del;
    }
    // Data가져오기
    public String getName() {
        return name;
    }
    // Data설정하기
    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
