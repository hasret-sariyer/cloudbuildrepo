package smartface.com.nativetest;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.net.URI;
import java.util.Locale;

import smartface.com.nativetest.SnapAlignment.GridviewHolderAdapter;

import static smartface.com.nativetest.FragmentOnDestroyTest.mutableContextWrappers;
import static smartface.com.nativetest.FragmentOnDestroyTest.parentContext;

/**
 * Created by smartface on 15.08.2018.
 */

public class FirstFragemnet  extends Fragment{
        SecondFragement secondFragement2;
        SecondFragement secondFragement1;

        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        public static StaggeredGridLayoutManager mLayoutManager;
        static boolean  scrollEnable = false;
        String[] myDataset = new String[50];

        // The onCreateView method is called when Fragment should create its View object hierarchy,
        // either dynamically or via XML layout inflation.
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
                setHasOptionsMenu(true);

                 if(FragmentOnDestroyTest.rootViewFragments == null) {

                 FragmentOnDestroyTest.rootViewFragments = (ViewGroup) inflater.inflate(R.layout.textviewsize_animate, null);

                 secondFragement2 = new SecondFragement();
                 secondFragement1 = new SecondFragement();

                 Context context = this.getContext();

                 ViewGroup view = FragmentOnDestroyTest.rootViewFragments.findViewById(R.id.page_layout);

                 RelativeLayout myRelativLayout = new RelativeLayout(context);
                 myRelativLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                 SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(context);

                 ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context, R.style.ScrollBarRecyclerView);

                 final RecyclerView constRecyclerview = mRecyclerView;
                 RecyclerView mRecyclerView = new RecyclerView(themeWrapper){
                     @Override
                     protected void onAttachedToWindow() {
                         super.onAttachedToWindow();
                         mLayoutManager.onRestoreInstanceState(FragmentOnDestroyTest.restoreInstance);
                     }
                 };

                 mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                 mRecyclerView.setHasFixedSize(true);
                 mRecyclerView.setDrawingCacheEnabled(true);
                 mRecyclerView.setClipToPadding(false);

                 mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

                 Parcelable parcelable = mLayoutManager.onSaveInstanceState();
                 mLayoutManager.onRestoreInstanceState(parcelable);

                 mLayoutManager.setGapStrategy(0);

                 mRecyclerView.setLayoutManager(mLayoutManager);

                 for (int i = 0; i < myDataset.length; i++) {
                         myDataset[i] = "https://picsum.photos/400/100?image=" + i;
                 }

                 mAdapter = new GridviewHolderAdapter(myDataset, FragmentOnDestroyTest.activity);
                 mRecyclerView.setAdapter(mAdapter);

                 swipeRefreshLayout.addView(mRecyclerView);

                 myRelativLayout.addView(swipeRefreshLayout);

                 view.addView(myRelativLayout);
                }
                return FragmentOnDestroyTest.rootViewFragments;
        }

        @Override
        public void onAttach(Context context) {
                super.onAttach(context);
        }

        // This event is triggered soon after onCreateView().
        // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
                super.onCreateOptionsMenu(menu,inflater);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
        }

        public static void setTimeout(Runnable runnable, int delay){
                new Thread(() -> {
                        try {
                                Thread.sleep(delay);
                                runnable.run();
                        }
                        catch (Exception e){
                                System.err.println(e);
                        }
                }).start();
        }


        public class WebClient extends WebViewClient {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        return super.shouldOverrideUrlLoading(view, request);
                }
        }
}
