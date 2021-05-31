package org.techtown.dangguen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.dangguen.Fragments.Frag_Chat;
import org.techtown.dangguen.Fragments.Frag_Eat;
import org.techtown.dangguen.Fragments.Frag_FoodMap;
import org.techtown.dangguen.Fragments.Frag_Home;
import org.techtown.dangguen.Fragments.Frag_My;

public class MainActivity extends AppCompatActivity
{
    private TextView tv_id, tv_password;
    private BottomNavigationView bottomNavigationView; // 하단바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag_Home home;
    private Frag_Eat eat;
    private Frag_FoodMap foodMap;
    private Frag_Chat chat;
    private Frag_My my;


    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그인
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");

        // 하단바 프래그먼트
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId()) // 아이템 아이디를 가지고 오는 것
                {
                    case R.id.action_home:
                        setFrag(0);
                        break;

                    case R.id.action_eat:
                        setFrag(1);
                        break;

                    case R.id.action_foodmap:
                        setFrag(2);
                        break;

                    case R.id.action_chat:
                        setFrag(3);
                        break;

                    case R.id.action_my:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });

        home = new Frag_Home();
        eat = new Frag_Eat();
        foodMap = new Frag_FoodMap();
        chat = new Frag_Chat();
        my = new Frag_My();
        setFrag(0); // 첫 화면은 home화면이기 때문에 0으로 지정

    }

    // 프래그먼트 교체가 일어나는 실행문 (하단바 교체)
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction(); // 실제적인 프래그먼트 교체가 이루어질 때, 트랜잭션
        switch (n)
        {
            case 0:
                ft.replace(R.id.main_frame, home);
                ft.addToBackStack(null);
                ft.commit(); // 저장

                break;

            case 1:
                ft.replace(R.id.main_frame, eat);
                ft.addToBackStack(null);
                ft.commit(); // 저장
                break;

            case 2:
                ft.replace(R.id.main_frame, foodMap);
                ft.addToBackStack(null);
                ft.commit(); // 저장
                break;

            case 3:
                ft.replace(R.id.main_frame, chat);
                ft.addToBackStack(null);
                ft.commit(); // 저장
                break;

            case 4:
                ft.replace(R.id.main_frame, my);
                ft.addToBackStack(null);
                ft.commit(); // 저장
                break;
        }
    }
}