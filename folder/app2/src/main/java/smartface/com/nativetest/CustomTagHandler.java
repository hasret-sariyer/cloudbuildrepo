package smartface.com.nativetest;

import android.text.Editable;
import android.text.Html;
import android.util.Log;

import org.xml.sax.XMLReader;

public class CustomTagHandler implements Html.TagHandler {
    @Override
    public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {
       CharSequence myChar =  editable.subSequence(0, editable.length());
       String outputText = myChar.toString();

       Log.i("CustomTagHandler", " " + outputText + " tags " + s);
    }
}
