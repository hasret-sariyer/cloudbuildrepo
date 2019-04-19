package smartface.com.nativetest;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.services.common.CommonUtils;
import io.intercom.com.bumptech.glide.BitmapTypeRequest;


/**
 * Created by smartface on 27.07.2018.
 */

/*
1 - What causes in web view ?
2- How about changing all fragments context ?
3-
 */

public class MaterialTextField extends AppCompatActivity {
    Context parent;
    Context myContext;
    AppCompatActivity  instance;

    static int indexForText = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myContext = this;

        setContentView(R.layout.material_test);
        parent = this;

        instance = this;

        LinearLayout rtl = (LinearLayout) findViewById(R.id.rootLayout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//
//        TextInputLayout test = new TextInputLayout(this);
//
//        TextInputEditText editText = new TextInputEditText(test.getContext());
//        test.addView(editText);


//        Method[] methods = Class.class.getMethods();
//
//        for (Method method : methods) {
//            Log.d("method", " method " +  method.getName());
//        }

//        Class<?> editTextClass = editText.getClass();
//        Class<?>[] types = new Class<?>[0];
//        try {
//            Method[] testkkjp = editTextClass.getDeclaredMethods();
//        }catch (SecurityException e ){
//
//        }



//        EditText editText2 = new EditText(this);
//        test.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));


        TextView textView  = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(200,500));
        textView.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        textView.setEllipsize(TruncateAt.START);
        textView.getEllipsize();


        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.create(null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * From aTable", null);





//        textView.setSingleLine(false);
        textView.setMaxLines(0);

        rtl.addView(textView);

        Log.d("textViewTest", " textView getEllipsize  " + Integer.MAX_VALUE);
    }

}