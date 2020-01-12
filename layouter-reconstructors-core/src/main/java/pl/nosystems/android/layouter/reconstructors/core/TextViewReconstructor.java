package pl.nosystems.android.layouter.reconstructors.core;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;


public class TextViewReconstructor extends ViewReconstructor {
    private static final String TAG = TextViewReconstructor.class.getSimpleName();

    @Override
    public void reconstruct(@NonNull ViewHierarchyElement element,
                            @NonNull View elementView,
                            @NonNull Context context) {

        // Since this reconstructor works only for TextView - if element is not TextView it has nothing to do
        if (!(elementView instanceof TextView)) {
            return;
        }

        super.reconstruct(element, elementView, context);
        final TextView textView = (TextView) elementView;
        final Iterable<ViewHierarchyElementAttribute> attributes = element.getAttributes();

        reconstructText(textView, attributes, context);
        reconstructTextSize(textView, attributes);
        reconstructGravity(textView, attributes);
    }

    private void reconstructGravity(@NonNull TextView textView,
                                    @NonNull Iterable<ViewHierarchyElementAttribute> attributes) {

        final ViewHierarchyElementAttribute gravityAttribute = findAttribute(attributes, "gravity", "android");
        if (gravityAttribute != null) {
            final String gravityStringValue = gravityAttribute.getValue();
            final String[] gravityStringParts = gravityStringValue.split("\\|");

            int gravity = 0;

            for (String part : gravityStringParts) {
                switch (part) {
                    case "center":
                        gravity |= Gravity.CENTER;
                        break;
                    case "top":
                        gravity |= Gravity.TOP;
                        break;
                    case "bottom":
                        gravity |= Gravity.BOTTOM;
                        break;
                    case "right":
                        gravity |= Gravity.RIGHT;
                        break;
                    case "left":
                        gravity |= Gravity.LEFT;
                        break;
                    case "end":
                        gravity |= Gravity.END;
                        break;
                    case "start":
                        gravity |= Gravity.START;
                        break;
                    case "center_vertical":
                        gravity |= Gravity.CENTER_VERTICAL;
                        break;
                    case "center_horizontal":
                        gravity |= Gravity.CENTER_HORIZONTAL;
                        break;
                    default:
                        Log.w(TAG, "Applying gravity of part : " + part + " is not implemented yet :/");
                }
            }

            textView.setGravity(gravity);
        }
    }

    private void reconstructTextSize(@NonNull TextView textView,
                                     @NonNull Iterable<ViewHierarchyElementAttribute> attributes) {

        final ViewHierarchyElementAttribute textSizeAttribute = findAttribute(attributes, "textSize", "android");
        if (textSizeAttribute != null) {

            String textSizeValue = textSizeAttribute.getValue();
            if (textSizeValue.length() > 2) {
                String possibleType = textSizeValue.substring(textSizeValue.length() - 2);
                String possibleValue = textSizeValue.substring(0, textSizeValue.length() - 2);
                if (possibleType.equals("dp")) {
                    textView.setTextSize(TypedValue.DENSITY_DEFAULT, Integer.parseInt(possibleValue));
                } else if (possibleType.equals("px")) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(possibleValue));
                } else if (possibleType.equals("sp")) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(possibleValue));
                } else {
                    Log.w(TAG, "Failed to apply property textSize to view: " + textView);
                }
            }
        }
    }

    private void reconstructText(@NonNull TextView textView,
                                 @NonNull Iterable<ViewHierarchyElementAttribute> attributes,
                                 @NonNull Context context) {

        final ViewHierarchyElementAttribute textAttribute = findAttribute(attributes, "text", "android");
        if (textAttribute != null) {
            final String value = textAttribute.getValue();
            if (value.contains("@string")) {
                final Resources resources = context.getResources();
                textView.setText(resources.getIdentifier(value,null, context.getPackageName()));
            } else {
                textView.setText(textAttribute.getValue());
            }
        }
    }

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
