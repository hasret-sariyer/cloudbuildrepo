package smartface.com.nativetest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class AttributedTextTest extends AppCompatActivity {

    Context parentContext;

    int  TEXT_SIZE = 15;
    int  MAX_WIDTH = 650;

    Typeface FONT = Typeface.DEFAULT;

    String[] attributes = new String[]{
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>",
            "<p><span style=\"font-weight: bold\">1. Üniversite Öğrencilerine Dönemsel İndirim Kampanyası</span> <br>Tüm üniversite öğrencilerine, bahar ve güz dönemi başlangıç ve bitiş tarihlerinde birer ay (15 Ocak-15 Şubat, 1-30 Haziran ve 1-30 Eylül) süreyle, üniversite kimlik belgelerini ibraz etmek suretiyle, ticari gönderileri hariç (posta kargosu/kargo ve APS kuryelere) <span style=\"font-weight: bold\">%30 indirim</span> uygulaması bulunmaktadır.</p>"
    };

    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
    MovementMethod linkMovementMethod;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attributedtextest);

        float scale = getApplicationContext().getResources().getDisplayMetrics().density;

        parentContext = this;

        Intent intent = new Intent();

        Uri uri = new Uri.Builder().build();

        ArrayList arrayList = new ArrayList();

        RelativeLayout myRelativeLayout = (RelativeLayout)  findViewById(R.id.myRelativeLayout);

        TextView myTextView   = new TextView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300,150);
        layoutParams.topMargin = 50;
        myTextView.setLayoutParams(layoutParams);


//        myTextView.setWidth(MAX_WIDTH);
//        myTextView.setTypeface(FONT);
//        myTextView.setTextSize(TEXT_SIZE);

//        myTextView.setMaxLines(1000);

//        spannableStringBuilder.setSpan(new LineSpacing(), 0, spannableStringBuilder.length(),33);

        String[] attributedText = {"THIS IS LINKED TEXT " , "TEST TEST" , "TESTTEST","THIS IS LINKED TEXT " ,"THIS IS LINKED TEXT " ,"THIS IS LINKED TEXT " };

//        myTextView.setLinksClickable(true);

//        myTextView.setMaxLines(1);

        myTextView.setText(createAttributedText(attributedText),TextView.BufferType.SPANNABLE);
        myTextView.setMovementMethod(LinkMovementMethod.getInstance());
        myRelativeLayout.addView(myTextView);


        MovementMethod movementMethod = myTextView.getMovementMethod();


//        LinearLayout myLinearLayout = new LinearLayout(this);
//        myLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

//        Button actualSizeBtn = new Button(this);
//
//        actualSizeBtn.setText("Take Actual Size");
//        actualSizeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("Actual height " + myTextView.getHeight() + " width " + myTextView.getWidth());
//            }
//        });
//
//
//
//
//        Button  measuredSizeBtn = new Button(this);
//        measuredSizeBtn.setText("Take Measured Size");
//        measuredSizeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(parentContext, " screen width height " + (parentContext.getResources().getDisplayMetrics().heightPixels  - getSupportActionBar().getHeight()), Toast.LENGTH_LONG).show();
//                int measuredSize =getMeasuredSize(parentContext,spannableStringBuilder,TEXT_SIZE,MAX_WIDTH, FONT, 0 );
//
//                System.out.println("meuasure height " + measuredSize );
//            }
//        });


//        TextView equalTextView = new TextView(this);
//        equalTextView.setHeight(getMeasuredSize(parentContext,spannableStringBuilder,TEXT_SIZE,MAX_WIDTH, FONT, 0 ));
//        equalTextView.setWidth(50);
//        equalTextView.setBackgroundColor(Color.BLUE);

//        myLinearLayout.addView(actualSizeBtn);
//        myLinearLayout.addView(measuredSizeBtn);
//        myLinearLayout.addView(equalTextView);
//
//
//        myRelativeLayout.addView(myLinearLayout);


    }


    public SpannableStringBuilder createAttributedText(String[] text) {
        spannableStringBuilder.clear();

        for (String s : text) {
            createSpannyText(s);
        }

        return spannableStringBuilder;
    }

    public void createSpannyText(String text) {
        spannableStringBuilder.append(text);

        int start = spannableStringBuilder.length() - text.length();
        int end = spannableStringBuilder.length();

        if(text.contentEquals("TESTTEST"))
            spannableStringBuilder.setSpan(new ClickableSpanText(text) , start,end, 33);
        else {
            Typeface myTypeface = Typeface.create("monospace",Typeface.BOLD);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 33);
            spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.GRAY), start, end, 33);
            spannableStringBuilder.setSpan(new SFTypeFaceSpan("SF"), start, end, 33);
            spannableStringBuilder.setSpan(new AbsoluteSizeSpan(15, true), start, end, 33);
            spannableStringBuilder.setSpan(new UnderlineSpan(), start, end, 33);
        }
    }

    public class SFTypeFaceSpan extends TypefaceSpan{

        public SFTypeFaceSpan(@Nullable String family) {
            super(family);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, Typeface.create("monospace",Typeface.BOLD));
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint,Typeface.create("monospace",Typeface.BOLD));
        }
    }

    public class ClickableSpanText  extends ClickableSpan{
        String text ;
        ClickableSpanText(String text) {
            super();
            this.text = text;
        }

        @Override
        public void onClick(@NonNull View view) {
            Toast.makeText(parentContext, text  + "  ClickableSpanText onClick " + view.getId(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }
    }

    public class LineSpacing implements LineHeightSpan{
        @Override
        public void chooseHeight(CharSequence charSequence, int i, int i1, int i2, int i3, Paint.FontMetricsInt fontMetricsInt) {
            fontMetricsInt.ascent -= 0;
            fontMetricsInt.descent += 0;
        }
    }


    public void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        }
        else {
            oldStyle = old.getStyle();
        }
        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(tf);
    }


    public int getMeasuredSize(Context context, SpannableStringBuilder text, int textSize, int deviceWidth, Typeface typeface, int padding){

        TextView textView = new TextView(context);
        textView.setPadding(padding,0,padding,padding);
//        textView.setTypeface(typeface);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        textView.setMaxLines(1000);
        textView.setSingleLine(false);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

}



