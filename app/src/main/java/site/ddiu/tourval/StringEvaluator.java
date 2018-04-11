package site.ddiu.tourval;

import android.animation.TypeEvaluator;

public class StringEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        String startStr = (String) startValue;
        String endStr = (String) endValue;
        String currStr = null;
        if (fraction <=0.1) {
            currStr = startStr;
        }
        if (fraction <=0.9) {
            currStr = startStr.substring(0,3);
        }
        else
            currStr = endStr;
        return null;
    }
}
