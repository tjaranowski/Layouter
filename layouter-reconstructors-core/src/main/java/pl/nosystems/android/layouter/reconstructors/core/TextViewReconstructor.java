package pl.nosystems.android.layouter.reconstructors.core;

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

    public TextViewReconstructor(float displayDensity) {
        super(displayDensity);
    }

    @Override
    public void reconstruct(@NonNull ViewHierarchyElement element,
                            @NonNull View elementView) {
        // Since this reconstructor works only for TextView - if element is not TextView it has nothing to do
        if(! (elementView instanceof TextView)) {
            return;
        }

        super.reconstruct(element, elementView);
        final TextView textView = (TextView) elementView;
        final Iterable<ViewHierarchyElementAttribute> attributes = element.getAttributes();

        ViewHierarchyElementAttribute textAttribute = findAttribute(attributes, "text", "android");
        if(textAttribute != null) {
            textView.setText(textAttribute.getValue());
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
    protected ViewHierarchyElementAttribute findAttribute(@NonNull Iterable<ViewHierarchyElementAttribute> attributes,
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
