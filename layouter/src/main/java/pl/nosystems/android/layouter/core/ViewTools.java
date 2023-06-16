package pl.nosystems.android.layouter.core;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewTools {

    public static List<View> findViewsOfType(ViewGroup container, ArrayList<Class<? extends View>> types) {
        ArrayList<View> output = new ArrayList<>();
        int childCount = container.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = container.getChildAt(i);
            if(types.contains(childAt.getClass())) {
                output.add(childAt);
                continue;
            }
            if(childAt instanceof ViewGroup) {
                List<View> viewsOfType = findViewsOfType((ViewGroup) childAt, types);
                output.addAll(viewsOfType);
            }
        }
        return output;
    }
}
