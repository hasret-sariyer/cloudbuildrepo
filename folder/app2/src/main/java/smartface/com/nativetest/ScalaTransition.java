package smartface.com.nativetest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ScalaTransition extends Transition {
    public static final String PROPERTYNAME_SCALE_X = "io.smartface:RotateTransition:scalex";
    public static final String PROPERTYNAME_SCALE_Y = "io.smartface:RotateTransition:scaley";


    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPERTYNAME_SCALE_X, transitionValues.view.getScaleX());
        transitionValues.values.put(PROPERTYNAME_SCALE_Y, transitionValues.view.getScaleY());
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPERTYNAME_SCALE_X, transitionValues.view.getScaleX());
        transitionValues.values.put(PROPERTYNAME_SCALE_Y, transitionValues.view.getScaleY());
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                       TransitionValues endValues) {
        if (startValues == null || endValues == null) {
                return null;
        }

        final View view = endValues.view;
        ArrayList<PropertyValuesHolder> propertyValuesHolders = new ArrayList<>();

        float startValue = (Float) startValues.values.get(PROPERTYNAME_SCALE_X);
        float endValue = (Float) endValues.values.get(PROPERTYNAME_SCALE_X);
        if (startValue != endValue) {
            propertyValuesHolders.add(PropertyValuesHolder.ofFloat(View.SCALE_X, startValue, endValue));
        }

        startValue = (Float) startValues.values.get(PROPERTYNAME_SCALE_Y);
        endValue = (Float) endValues.values.get(PROPERTYNAME_SCALE_Y);
        if (startValue != endValue) {
            propertyValuesHolders.add(PropertyValuesHolder.ofFloat(View.SCALE_Y, startValue, endValue));
        }

        if(propertyValuesHolders.size() > 0){
            // Return animation for all changed rotation properties
            return ObjectAnimator.ofPropertyValuesHolder(view, propertyValuesHolders.toArray(new PropertyValuesHolder[0]));
        }
        // Nothing changes. No animation needes.
        return null;
        }
}
