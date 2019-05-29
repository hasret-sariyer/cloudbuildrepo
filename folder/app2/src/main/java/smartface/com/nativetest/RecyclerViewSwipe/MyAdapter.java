package smartface.com.nativetest.RecyclerViewSwipe;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import smartface.com.nativetest.CustomTarget;
import smartface.com.nativetest.R;

public  class MyAdapter extends RecyclerView.Adapter  {

    private String[] mDataset;
    private Context parentContext;
    private HashMap hashMap = new HashMap();

    private final int TYPE_IMAGE = 0 ;
    private final int TYPE_CARD= 1 ;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public View myCardView;
        public MyViewHolder(View v) {
            super(v);
            myCardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        parentContext = parent.getContext();
        // create a new view

        View myView = null;
        switch (viewType){
            case TYPE_IMAGE:
                RelativeLayout relativeLayout = new RelativeLayout(parentContext);
                relativeLayout.setLayoutParams( new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800 ));
                relativeLayout.setBackgroundColor(Color.GRAY);
                myView = relativeLayout;
                break;
            default:
                 myView = (CardView)  LayoutInflater.from(parentContext).inflate(R.layout.cardview_test,parent,false);
                 break;
        }

        MyViewHolder vh = new MyViewHolder(myView);

        hashMap.put(vh.hashCode(),vh);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    CustomTarget customTarget;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder =(MyViewHolder) hashMap.get(holder.hashCode());

        View myCardView = (View) myViewHolder.myCardView ;

        if( myCardView.findViewById(R.id.myTextView) != null) {
            TextView myTextView = (TextView) myCardView.findViewById(R.id.myTextView);
            myTextView.setText("Swipe Left/Right Index " + position);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length -1 ;
    }


    @Override
    public int getItemViewType(int position) {
        return 2;
    }

    public  int convertPixelsToDp(float px){
        return Math.round(px / ((float) parentContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
