package smartface.com.nativetest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

/**
 * Created by smartface on 8.08.2018.
 */

public class ActionBarTest extends AppCompatActivity {

    Menu optionsMenu = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.keyboardpositiontest);

        final RelativeLayout flRoot = (RelativeLayout) findViewById(R.id.rootLayout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top);

        setSupportActionBar(myToolbar);

        ActionBar myActionBar =(ActionBar) this.getSupportActionBar();

        myActionBar.setTitle("");//Empty title

        FrameLayout myFrameLayout = new FrameLayout(this);
        myFrameLayout.setBackgroundColor(Color.RED);

        myToolbar.setContentInsetsRelative(300,0);

//        myToolbar.getContentInsetStart();

       myActionBar.setDisplayHomeAsUpEnabled(true);

        SearchView mySearchView = new SearchView(this);
        mySearchView.setIconifiedByDefault(false);

        myToolbar.addView(myFrameLayout, new Toolbar.LayoutParams(2000,50,1));


        View myView = new View(this);
        myView.setLayoutParams(new ViewGroup.LayoutParams(100,100));

        MenuItem myMenuItem = optionsMenu.add("1");
        myMenuItem.setActionView(myView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionsMenu = optionsMenu;

        return super.onCreateOptionsMenu(menu);
    }
}
