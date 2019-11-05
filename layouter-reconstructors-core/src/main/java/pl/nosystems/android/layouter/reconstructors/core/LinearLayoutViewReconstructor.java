package pl.nosystems.android.layouter.reconstructors.core;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;


public class LinearLayoutViewReconstructor extends ViewReconstructor {
    private static final String TAG = LinearLayoutViewReconstructor.class.getSimpleName();

    public LinearLayoutViewReconstructor(float displayDensity) {
        super(displayDensity);
    }

    @Override
    public void reconstruct(@NonNull ViewHierarchyElement element, @NonNull View elementView) {
        if(! (elementView instanceof LinearLayout)) {
            return;
        }
        final LinearLayout linearLayout = (LinearLayout) elementView;

        super.reconstruct(element, elementView);
        final Iterable<ViewHierarchyElementAttribute> attributes = element.getAttributes();
        final ViewHierarchyElementAttribute orientationAttribute = findAttribute(attributes, "orientation", "android");
        if(orientationAttribute != null) {
            final String orientationValue = orientationAttribute.getValue();
            if(orientationValue.equals("vertical")) {
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            } else if(orientationValue.equals("horizontal")) {
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                Log.w(TAG, "Cannot apply orientation of value " + orientationValue + " to linear layout!");
            }
        }
    }
}
