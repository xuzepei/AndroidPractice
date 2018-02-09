package com.apps.radio;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;

/**
 * Created by xuzepei on 2018/2/7.
 */

public class CategoryFragment extends Fragment {

    private ListView listView;
    private LinkedList<Category> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment0, container, false);

        listView = (ListView) view.findViewById(R.id.category_list);
        if(listView != null) {
            if (items == null) {
                items = new LinkedList<Category>();
                items.add(new Category("Latest Contents", "", R.drawable.c_5, "5"));
                items.add(new Category("6 Minute English", "Our long-running series of topical discussion and new vocabulary, brought to you by your favourite BBC Learning English presenters.", R.drawable.c_2, "2"));
                items.add(new Category("English at Work", "Focuses on English communication in the office.", R.drawable.c_6, "6"));
                items.add(new Category("English at University", "A new animated series that brings you the English words and phrases you need to help you through your first year of study abroad.", R.drawable.c_7, "7"));
                items.add(new Category("The English We Speak", "The English We Speak is your chance to catch up on the very latest English words and phrases.", R.drawable.c_0, "0"));
                items.add(new Category("LingoHack", "Get up-to-date with the latest news and understand it too with Lingohack.", R.drawable.c_8, "8"));
                items.add(new Category("Drama", "Dramas from BBC Learning English", R.drawable.c_3, "3"));
                items.add(new Category("News Report", "Listen to our new series which features authentic news stories.", R.drawable.c_4, "4"));
                items.add(new Category("Words in the News", "Welcome to our weekly news section. In each programme one of our presenters tells you a story from the week's news.", R.drawable.c_1, "1"));
            }

            listView.setAdapter(new CategoryListAdapter(items,getContext()));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getContext(), ItemListActivity.class);

                    Category category = items.get(i);
                    if(category != null) {
                        intent.putExtra("title", category.getTitle());
                        intent.putExtra("cateID", category.getCateID());
                    }
                    startActivity(intent);
                }
            });
        }

        return view;
    }

}
