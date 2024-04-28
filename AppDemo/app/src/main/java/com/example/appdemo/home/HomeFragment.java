package com.example.appdemo.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdemo.LoginActivity;
import com.example.appdemo.MainActivity;
import com.example.appdemo.R;
import com.example.appdemo.data.Case;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    float scrollX;
    float scrollY;

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    CaseAdapter mCaseAdapter ;
    List<Case> mCaseList = new ArrayList<>();

    boolean isRequesting = false;
    boolean isLoadingMore = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.case_list_recyclerview);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        setup(view);

        return view;
    }

    void setup(View view) {
        initSwipeRefreshLayout();

        Button addBtn = view.findViewById(R.id.add_case_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Case item = new Case();
                item.title = "新的";
                item.subtitle = "这是新加的案例";

                mCaseList.add(0, item);
                mCaseAdapter.notifyItemInserted(0);

                mRecyclerView.scrollToPosition(0);
            }
        });

        Button deleteBtn = view.findViewById(R.id.delete_case_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCaseList.size() > 0) {
                    mCaseList.remove(0);
                    mCaseAdapter.notifyItemRemoved(0);
                }
            }
        });
    }

    void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setSize(CircularProgressDrawable.DEFAULT);
        //mSwipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.light_blue_600, R.color.teal_200);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateContent();

                    }
                }, 5000);

            }
        });
    }

    void updateContent() {

        // 构造一些数据
        Case item1 = new Case();
        item1.title = "标题标题标题标题标题标题标题标题标题";
        item1.subtitle = "子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题子标题";
        mCaseList.add(item1);

        for (int i = 0; i < 30; i++) {
            Case item = new Case();
            item.title = "标题" + i;
            item.subtitle = "子标题" + i;
            mCaseList.add(item);
        }

        mCaseAdapter = new CaseAdapter();
        mRecyclerView.setAdapter(mCaseAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new
                DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(divider);


        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    scrollX = event.getX();
                    scrollY = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (view.getId() != 0 && Math.abs(scrollX - event.getX()) <= 5 && Math.abs(scrollY - event.getY()) <= 5) {
                        //处理空白区域的点击事件

                        Log.d("####", "onClick: space");
                    }
                }
                return false;
            }
        });

        mSwipeRefreshLayout.setRefreshing(false);
    }

    //列表
    class CaseViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTV;
        TextView mSubtitleTV;
        LinearLayout mContentLayout;

        public CaseViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTV = itemView.findViewById(R.id.case_title);
            mSubtitleTV = itemView.findViewById(R.id.case_subtitle);
            if(itemView != null) {
                Log.d("####", "setClick: " + itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("####", "onClick: " + getAbsoluteAdapterPosition());
                    }
                });

            }
        }
    }

    class CaseAdapter extends RecyclerView.Adapter<CaseViewHolder> {

        @NonNull
        @Override
        public CaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.simple_item, null);
            CaseViewHolder viewHolder = new CaseViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CaseViewHolder holder, int position) {
            Case item = mCaseList.get(position);
            holder.mTitleTV.setText(item.title);
            holder.mSubtitleTV.setText(item.subtitle);



//            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d("####", "onClick");
//                    //Toast.makeText(getContext(), "You have clicked case item", Toast.LENGTH_SHORT).show();
//                }
//            });

            // 设置最大高度
//            int maxHeight = 1000;
//            holder.itemView.post(new Runnable() {
//                @Override
//                public void run() {
//                    int height = holder.itemView.getHeight();
//                    if (height > maxHeight) {
//                        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//                        layoutParams.height = maxHeight;
//                        holder.itemView.setLayoutParams(layoutParams);
//                    }
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mCaseList.size();
        }
    }
}