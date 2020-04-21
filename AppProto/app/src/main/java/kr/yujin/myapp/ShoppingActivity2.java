package kr.yujin.myapp;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ShoppingActivity2 extends Fragment {
    String[] category={"medicine","quasi-drug","cosmetic","household_goods","food"};
    String[][] kor_subcategory={{"진통제","항생제","항암제","소화제","천연의약품","","","","","","",""},{"연고","마스크","선크림","파스","연고","선크림","파스","마스크","","","",""},{"스킨케어","메이크업","소품","네일/향수","바디/헤어","","","","","","",""},{"건강/의료용품","반려동물용품","악기/취미","문구/사무용품","꽃/이벤트용품","","","","","","",""},{"쌀/과일/농수축산물","건강식품/다이어트","커피/음료","즉석/간식/가공식품","","","","","","",""}};
    String[][] subcategory={{"painkiller","antibiotic","anticancer_drug","peptic","natural_medicine","","","","","","",""},{"연고","마스크","선크림","파스","연고","선크림","파스","마스크","","","",""},{"스킨케어","메이크업","소품","네일/향수","바디/헤어","","","","","","",""},{"건강/의료용품","반려동물용품","악기/취미","문구/사무용품","꽃/이벤트용품","","","","","","",""},{"쌀/과일/농수축산물","건강식품/다이어트","커피/음료","즉석/간식/가공식품","","","","","","",""}};
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shopping2, container, false);

        final TabLayout tabLayout =(TabLayout) view.findViewById(R.id.tablayout);
        final GridView gridView = (GridView) view.findViewById(R.id.grid_view);

        final ImageButton closeshopping = (ImageButton) view.findViewById(R.id.closeshopping);
        //

        final int[] category_index = new int[3];


        tabLayout.addTab(tabLayout.newTab().setText("의약품"));
        tabLayout.addTab(tabLayout.newTab().setText("의약외품"));
        tabLayout.addTab(tabLayout.newTab().setText("코스메틱"));
        tabLayout.addTab(tabLayout.newTab().setText("생활용품"));
        tabLayout.addTab(tabLayout.newTab().setText("식품"));

        //초기값
        ArrayAdapter<String> arrayAdapter0 = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, kor_subcategory[0]);
        gridView.setAdapter(arrayAdapter0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                category_index[0]=tab.getPosition();
                category_index[1] =0;
                category_index[2] =0;
                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, kor_subcategory[tab.getPosition()]);
                gridView.setAdapter(arrayAdapter3);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        //ListView adapt
        final ArrayList<String> json_list = new ArrayList<>();
        final ListView listview = (ListView) view.findViewById(R.id.category_listview);
        final ArrayAdapter<String> listview_arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview_shopping_category,json_list);
        listview.setAdapter(listview_arrayAdapter);

        //subcategory 클릭시
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //JSON 연결
                json_list.clear();
                try {
                    category_index[1]=position;
                    JSONObject obj = new JSONObject(loadJSONFromAsset());
                    JSONObject sub_obj = obj.getJSONObject("category").getJSONObject(category[category_index[0]]);
                    JSONArray m_jArry = sub_obj.getJSONArray(subcategory[category_index[0]][position]);

                    for (int i = 0; i < m_jArry.length(); i++) {
                        json_list.add(m_jArry.getJSONObject(i).getString("name"));
                    }

                    listview_arrayAdapter.notifyDataSetChanged();

                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



        //뒤로가기
        closeshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).shop_to_ranking();
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
            InputStream is = getActivity().getAssets().open("shopping_category.json");

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