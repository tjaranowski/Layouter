package pl.nosystems.android.layouter.reconstructors.core;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;
import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;


public class TextViewReconstructor implements ViewHierarchyElementReconstructor {
    private static final String TAG = TextViewReconstructor.class.getSimpleName();

    @Override
    public void reconstruct(@NonNull ViewHierarchyElement element,
                            @NonNull View elementView) {

        // Since this reconstructor works only for TextView - if element is not TextView it has nothing to do
        if(! (elementView instanceof TextView)) {
            return;
        }
        final TextView textView = (TextView) elementView;
        final Iterable<ViewHierarchyElementAttribute> attributes = element.getAttributes();

        ViewHierarchyElementAttribute textAttribute = findAttribute(attributes, "text", "android");
        if(textAttribute != null) {
            textView.setText(textAttribute.getValue());
        }

        {
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(0, 0);
                textView.setLayoutParams(layoutParams);
            }
        }
        ViewHierarchyElementAttribute layoutWidthAttribute = findAttribute(attributes, "layout_width", "android");
        if(layoutWidthAttribute != null) {
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            final String width = layoutWidthAttribute.getValue();
            if(width.equals("wrap_content")) {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else if (width.equals("match_parent")) {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                Log.w(TAG, "Failed to set layout_width for value : " + width + " on view " + textView);
            }
            textView.setLayoutParams(layoutParams);
        }

        ViewHierarchyElementAttribute layoutHeightAttribute = findAttribute(attributes, "layout_height", "android");
        if(layoutHeightAttribute != null) {
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            final String height = layoutHeightAttribute.getValue();
            if(height.equals("wrap_content")) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else if (height.equals("match_parent")) {
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                Log.w(TAG, "Failed to set layout_height for value : " + height + " on view " + textView);
            }
            textView.setLayoutParams(layoutParams);
        }

        ViewHierarchyElementAttribute textSizeAttribute = findAttribute(attributes, "textSize", "android");
        if(textSizeAttribute != null) {

            String textSizeValue = textSizeAttribute.getValue();
            if(textSizeValue.length() > 2) {
                String possibleType = textSizeValue.substring(textSizeValue.length()-2);
                String possibleValue = textSizeValue.substring(0, textSizeValue.length()-2);
                if(possibleType.equals("dp")) {
                    textView.setTextSize(TypedValue.DENSITY_DEFAULT, Integer.parseInt(possibleValue));
                } else if(possibleType.equals("px")) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(possibleValue));
                } else if(possibleType.equals("sp")) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(possibleValue));
                } else {
                    Log.w(TAG, "Failed to apply property textSize to view: " + textView);
                }
            }
        }

        ViewHierarchyElementAttribute gravityAttribute = findAttribute(attributes, "gravity", "android");
        if(gravityAttribute != null) {
            final String gravityStringValue = gravityAttribute.getValue();
            final String gravityStringParts[] = gravityStringValue.split("\\|");

            int gravity = 0;

            for(String part : gravityStringParts) {
                switch (part) {
                    case "center":
                        gravity |= Gravity.CENTER;
                    default:
                        Log.w(TAG, "Applying gravity of part : " + part + " is not implemented yet :/");
                }
            }


            textView.setGravity(gravity);
        }
    }



    @Nullable
    private ViewHierarchyElementAttribute findAttribute(@NonNull Iterable<ViewHierarchyElementAttribute> attributes,
                                                        @NonNull String name,
                                                        @NonNull String namespacePrefix) {

        for(ViewHierarchyElementAttribute attribute: attributes) {
            if(attribute.getName().equals(name) && attribute.getNamespacePrefix().equals(namespacePrefix)) {
                return attribute;
            }
        }
        return null;
    }
}
