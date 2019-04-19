package smartface.com.nativetest.RecyclerViewSwipe;

import android.animation.Animator;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import smartface.com.nativetest.RecyclerViewSwipe.ItemTouchHelperExtention.ItemTouchHelper;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import smartface.com.nativetest.R;


public class PicassoTest  extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String[] myDataset = new String[1000];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.picassotest);

        RelativeLayout myRelativLayout = (RelativeLayout) findViewById(R.id.myRelativeLayout);


        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        SwipeRefreshLayout  swipeRefreshLayout = new SwipeRefreshLayout(this);
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(this ,R.style.ScrollBarRecyclerView);
        RecyclerView mRecyclerView = new RecyclerView(themeWrapper);
        mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setItemViewCacheSize(0);
        mRecyclerView.setClipToPadding(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        for(int i=0; i < 800 ; i++){
            myDataset[i] = "https://picsum.photos/300/100/?image=" + i + 10;
        }

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        SwipeHelper swipeHelper =  new SwipeHelper(this ,mRecyclerView.getAdapter(), mRecyclerView );
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);



        swipeRefreshLayout.addView(mRecyclerView);

        myRelativLayout.addView(swipeRefreshLayout);
    }

}
