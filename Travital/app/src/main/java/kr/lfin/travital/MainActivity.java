package kr.lfin.travital;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;

    final int[] login_flag = {0};

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private HomeActivity menu1Fragment = new HomeActivity();
    private ShoppingActivity menu2Fragment = new ShoppingActivity();
    private SearchActivity menu3Fragment = new SearchActivity();
    private MapsActivity menu4Fragment = new MapsActivity();
    private TransActivity menu5Fragment = new TransActivity();

    private ShoppingActivity2 newFragmentinmenu2 = new ShoppingActivity2();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);


         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout,  menu1Fragment).commitAllowingStateLoss();




        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_shopping: {
                        transaction.replace(R.id.frame_layout, menu2Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_search: {
                        transaction.replace(R.id.frame_layout, menu3Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_maps: {
                        transaction.replace(R.id.frame_layout, menu4Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_trans: {
                        transaction.replace(R.id.frame_layout, menu5Fragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });



        //GO TO HOME BUTTON (Travital)
        ImageButton gotoHome = (ImageButton) findViewById(R.id.gotoHome);
        gotoHome.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               bottomNavigationView.findViewById(R.id.navigation_home).performClick();
        }
        });



        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        final FrameLayout include_nav_not_login =(FrameLayout) findViewById(R.id.include_nav_not_login);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);ImageButton open_drawer_button = (ImageButton) findViewById(R.id.open_drawer_button);
        open_drawer_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(login_flag[0]==0)
                {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                }
                else
                {
                    LinearLayout total= (LinearLayout) findViewById(R.id.total);
                    total.setVisibility(View.INVISIBLE);
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
if(login_flag[0]==0){
        Button sign_up_button= (Button) findViewById(R.id.sign_up_button);
        Button sign_in_button= (Button) findViewById(R.id.sign_in_button);

        //밑줄긋기
        sign_up_button.setPaintFlags(sign_up_button.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        sign_up_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });


        sign_in_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                login_flag[0] =1;
                Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                startActivity(intent); //다음화면으로 넘어감
            }
        });}
    }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    public void ranking_to_shop() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, newFragmentinmenu2).commitAllowingStateLoss();
    }
    public void shop_to_ranking() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, menu2Fragment).commitAllowingStateLoss();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.open_drawer:

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.gotohome:
                break;
            case R.id.action_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (id == R.id.nav_personal) {
                // Handle the camera action
            } else if (id == R.id.nav_recent) {

            } else if (id == R.id.nav_chart) {

            } else if (id == R.id.nav_settings) {

            } else if (id == R.id.nav_logout) {
            }

            drawer.closeDrawer(GravityCompat.END);
            return true;

        }
}
