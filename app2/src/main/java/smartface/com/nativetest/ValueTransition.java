package smartface.com.nativetest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.FloatProperty;
import android.util.IntProperty;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by smartface on 31.07.2018.
 */

public class ValueTransition extends Transition {

    private static final String PROPERTYNAME_TEXTVIEWSIZE = "io.smartface:ValueTransition:textview";

    private static final Property<TextView, Float> TEXTVIEWSIZE_PROPERTY = new FloatProperty<TextView>("TEXTVIEWSIZE_PROPERTY") {
        @Override
        public void setValue(TextView txtV, float value) {
            txtV.setTextSize(value);
        }

        @Override
        public Float get(TextView txtV) {
            return  txtV.getTextSize();
        }
    };

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValue(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValue(transitionValues);
    }

    public void captureValue(TransitionValues transitionValues){
        if (transitionValues.view instanceof TextView) {
            TextView txtV = (TextView) transitionValues.view;
            transitionValues.values.put(PROPERTYNAME_TEXTVIEWSIZE,txtV.getTextSize());
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {

        if(sceneRoot == null || startValues == null || endValues == null)
            return null;
        if(startValues.values == null || endValues.values == null)
            return null;

            TextView txtV = (TextView) startValues.view;
            float startValue =(Float) startValues.values.get(PROPERTYNAME_TEXTVIEWSIZE);
            float endValue =(Float) endValues.values.get(PROPERTYNAME_TEXTVIEWSIZE);

                txtV.setTextSize((float) startValue);

                ValueAnimator valueAnimator = ObjectAnimator.ofFloat(txtV ,TEXTVIEWSIZE_PROPERTY, startValue, endValue);

                return valueAnimator;

    }

}
