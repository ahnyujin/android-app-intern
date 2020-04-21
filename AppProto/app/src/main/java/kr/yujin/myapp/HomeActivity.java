package kr.yujin.myapp;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class HomeActivity extends Fragment {
    ViewPager viewPager;
    CustomSwipeAdapter adapter;

    Timer event_timer;
    final long DELAY_MS = 0;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.

    public HomeActivity(){
        //Require empty public constructor
    }
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter=new CustomSwipeAdapter(this.getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                viewPager.setCurrentItem((viewPager.getCurrentItem()+1) %adapter.getCount(), true);
            }
        };

        event_timer = new Timer(); // This will create a new Thread
        event_timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        ImageButton left_button = (ImageButton) view.findViewById(R.id.viewpager_left_button);
        ImageButton right_button = (ImageButton) view.findViewById(R.id.viewpager_right_button);

        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(adapter.getCount()-1, true);
                }
                else viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
            }
        });
        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewPager.getCurrentItem() == adapter.getCount()-1) {
                    viewPager.setCurrentItem(0, true);
                }
                else viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
            }
        });
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);


        final LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.ranking_bar);
        final LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.category_ranking);

        final ImageButton button=(ImageButton) view.findViewById(R.id.show_ranking_button);
        final Button button1=(Button) view.findViewById(R.id.showlist_button1);
        final Button button2=(Button) view.findViewById(R.id.showlist_button2);

        final TextView ranking_Textview=(TextView) view.findViewById(R.id.ranking_textview);


        final ArrayList<String> list = new ArrayList<>(); //ranking list에 올릴 list
        final ArrayList<String> json_list = new ArrayList<>();//3초마다 json에서 받는 list

        //JSON 연결
        json_list.clear();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("shopping-ranking");

            for (int i = 0; i < m_jArry.length(); i++) {
                json_list.add(m_jArry.getJSONObject(i).getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ListView adapt
        ListView listview = (ListView) view.findViewById(R.id.ranking_listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview,list);
        listview.setAdapter(arrayAdapter);



        TimerTask rankin_tt = new TimerTask() {
            int counter=0;
            @Override
            public void run() {

                if(counter>19)counter=0;

                ranking_Textview.setText((counter+1)+"  "+json_list.get(counter));
                counter++;
            }
        };
        Timer ranking_timer = new Timer();
        ranking_timer.schedule(rankin_tt,0,3000);
        layout2.setVisibility(View.GONE);



        button.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(layout2.getVisibility() == View.GONE) {
                    button.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
                    layout2.setVisibility(View.VISIBLE);
                    button1.performClick(); //초기상태
                }
                else {
                    button.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                    layout2.setVisibility(View.GONE);
                }

            }
        });

        // 1~10 & 11~20 button clicked
        final TextView ranking_updated_in = (TextView) view.findViewById(R.id.ranking_updated_in);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //"Ranking updated in"
                ranking_updated_in.setText("");
                SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy", Locale.KOREA);
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm", Locale.KOREA);
                Date currenTime = new Date();
                String dTime =formatter1.format( currenTime);
                String dTime2 =formatter2.format( currenTime);
                ranking_updated_in.append("Updated in ");
                ranking_updated_in.append(dTime);
                ranking_updated_in.append(" at ");
                ranking_updated_in.append(dTime2);
                button1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_corner_btn));
                button2.setBackgroundColor(Color.TRANSPARENT);

                // show 1 ~ 10 in listview

                list.clear();
                    for (int i = 0; i < 10; i++) {
                        if(i<9) list.add("     "+(i+1)+"    "+json_list.get(i));
                        else list.add("    "+(i+1)+"    "+json_list.get(i));
                    }

                arrayAdapter.notifyDataSetChanged();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"Ranking updated in"
                ranking_updated_in.setText("");
                SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy", Locale.KOREA);
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm", Locale.KOREA);
                Date currenTime = new Date();
                String dTime =formatter1.format( currenTime);
                String dTime2 =formatter2.format( currenTime);
                ranking_updated_in.append("Updated in ");
                ranking_updated_in.append(dTime);
                ranking_updated_in.append(" at ");
                ranking_updated_in.append(dTime2);
                button2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_corner_btn));
                button1.setBackgroundColor(Color.TRANSPARENT);


                // show 11 ~ 20 in listview

                list.clear();

                    for (int i = 10; i < 20; i++) {
                        list.add("    "+(i+1)+"    "+json_list.get(i));
                    }

                arrayAdapter.notifyDataSetChanged();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductActivity newfragment = new ProductActivity();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,newfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                }
            });
        return view;
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("ranking.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}