package pl.nosystems.android.layouter.reconstructors.core;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;
import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;


/**
 * Reconstructor that reconstructs LayoutParams for ViewGroup(s)
 */
abstract class ViewReconstructor implements ViewHierarchyElementReconstructor {
    private static final String TAG = ViewReconstructor.class.getSimpleName();

    @Override
    public void reconstruct(@NonNull ViewHierarchyElement element,
                            @NonNull View elementView,
                            @NonNull Context context) {

        final float displayDensity = context.getResources().getDisplayMetrics().density;
        final Iterable<ViewHierarchyElementAttribute> attributes = element.getAttributes();

        // Assert that elementView has LayoutParams.....
        {
            ViewGroup.LayoutParams layoutParams = elementView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(0, 0);
                elementView.setLayoutParams(layoutParams);
            }
        }

        ViewHierarchyElementAttribute layoutWidthAttribute = findAttribute(attributes, "layout_width", "android");
        if (layoutWidthAttribute != null) {
            ViewGroup.LayoutParams layoutParams = elementView.getLayoutParams();
            layoutParams.width = getLayoutDimensionValueFromAttribute(layoutWidthAttribute.getValue(), elementView, displayDensity);
            elementView.setLayoutParams(layoutParams);
        }

        ViewHierarchyElementAttribute layoutHeightAttribute = findAttribute(attributes, "layout_height", "android");
        if (layoutHeightAttribute != null) {
            ViewGroup.LayoutParams layoutParams = elementView.getLayoutParams();
            layoutParams.height = getLayoutDimensionValueFromAttribute(layoutHeightAttribute.getValue(), elementView, displayDensity);
            elementView.setLayoutParams(layoutParams);
        }
    }

    private int getLayoutDimensionValueFromAttribute(@NonNull String value,
                                                     @NonNull View elementView,
                                                     float displayDensity) {
        int dimension = 0;

        if (value.equals("wrap_content")) {
            dimension = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (value.equals("match_parent")) {
            dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {

            if (value.length() > 2) {
                final String unit = value.substring(value.length() -2);
                final String val = value.substring(0, value.length() -2);

                if(unit.equals("dp")) {
                    return (int) (Integer.parseInt(val) * displayDensity);
                }
            }

            Log.w(TAG, "Failed to set dimension attribute for value : " + value + " on view " + elementView);
        }
        return dimension;
    }



    @SuppressWarnings("SameParameterValue")
    @Nullable
    protected ViewHierarchyElementAttribute findAttribute(@NonNull Iterable<ViewHierarchyElementAttribute> attributes,
                                                          @NonNull String name,
                                                          @NonNull String namespacePrefix) {

        for (ViewHierarchyElementAttribute attribute : attributes) {
            if (attribute.getName().equals(name) && attribute.getNamespacePrefix().equals(namespacePrefix)) {
                return attribute;
            }
        }
        return null;
    }

}
