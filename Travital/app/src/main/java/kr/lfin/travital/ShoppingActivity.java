package kr.lfin.travital;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ShoppingActivity extends Fragment {
    String[] category={"categories","by_age"};

    String[][] subcategory={{"entire","medicine","quasi-drug","cosmetic","food"},{"child","10","20","30","40"}};
   String[][] kor_subcategory={{"전체","의약품","의약외품","코스메틱","식품"},{"유아","10대","20대","30대","40대"},{"전체","의약품","의약외품","코스메틱","식품"}};
    private View view;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shopping, container, false);

        TabLayout category_tabLayout =(TabLayout) view.findViewById(R.id.category_tablayout);
        final TabLayout subcategory_tabLayout =(TabLayout) view.findViewById(R.id.subcategory_tablayout);
        final ImageButton openshopping = (ImageButton) view.findViewById(R.id.openshopping);


        final int[] category_index = new int[3];
        category_tabLayout.addTab(category_tabLayout.newTab().setText("# 카테고리별 랭킹"));
        category_tabLayout.addTab(category_tabLayout.newTab().setText("# 연령별 랭킹"));

        //ListView adapt
        final ArrayList<String> json_list = new ArrayList<>();
        final ListView listview = (ListView) view.findViewById(R.id.category_listview);
        final ArrayAdapter<String> listview_arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview,json_list);
        listview.setAdapter(listview_arrayAdapter);

        //초기화

        for(int i=0;i<subcategory[0].length;i++)
        {
            subcategory_tabLayout.addTab(subcategory_tabLayout.newTab().setText(kor_subcategory[0][i]));
        }
        json_list.clear();
        try {
            category_index[1]=0;
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONObject(category[category_index[0]]).getJSONArray(subcategory[category_index[0]][category_index[1]]);

            for (int i = 0; i < m_jArry.length(); i++) {
                json_list.add(m_jArry.getJSONObject(i).getString("name"));
            }

            listview_arrayAdapter.notifyDataSetChanged();
        }   catch (JSONException e) {
            e.printStackTrace();
        }
        // category 클릭시

        category_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                        category_index[0]=0;
                        break;
                    case 1:
                        category_index[0]=1;
                        break;
                    case 2:
                        category_index[0]=2;
                        break;
                }
                subcategory_tabLayout.removeAllTabs();
                for(int i=0;i<subcategory[tab.getPosition()].length;i++)
                {
                    subcategory_tabLayout.addTab(subcategory_tabLayout.newTab().setText(kor_subcategory[tab.getPosition()][i]));
                }
                //초기화

                json_list.clear();
                try {
                    category_index[1]=0;
                    JSONObject obj = new JSONObject(loadJSONFromAsset());
                    JSONArray m_jArry = obj.getJSONObject(category[category_index[0]]).getJSONArray(subcategory[category_index[0]][category_index[1]]);

                    for (int i = 0; i < m_jArry.length(); i++) {
                        json_list.add(m_jArry.getJSONObject(i).getString("name"));
                    }

                    listview_arrayAdapter.notifyDataSetChanged();
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //subcategory 클릭시
        subcategory_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                json_list.clear();
                try {
                    category_index[1]=tab.getPosition();
                    JSONObject obj = new JSONObject(loadJSONFromAsset());
                    JSONArray m_jArry = obj.getJSONObject(category[category_index[0]]).getJSONArray(subcategory[category_index[0]][category_index[1]]);

                    for (int i = 0; i < m_jArry.length(); i++) {
                        json_list.add(m_jArry.getJSONObject(i).getString("name"));
                    }

                    listview_arrayAdapter.notifyDataSetChanged();
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        openshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).ranking_to_shop();
            }
        });
        return view;
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("rankings_by_categories.json");

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