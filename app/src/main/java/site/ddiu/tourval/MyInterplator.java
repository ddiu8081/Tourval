package site.ddiu.tourval;

import android.animation.TimeInterpolator;

public class MyInterplator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        float fraction;
        if(input <=0.3)
            fraction = 0.0f;
        else if (input<=0.7)
            fraction = 0.8f;
        else
            fraction =1.0f;
        return 0;
    }
}
