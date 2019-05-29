package smartface.com.nativetest;

import android.content.Context;
import android.support.design.widget.TextInputLayout;

public class MaterialTextBoxEditted extends TextInputLayout {

    public MaterialTextField test ;
    public MaterialTextBoxEditted(Context context) {
        super(context);

        test = new MaterialTextField();

    }

    @Override
    public void setHintTextAppearance(int resId) {
        super.setHintTextAppearance(resId);
    }
}
