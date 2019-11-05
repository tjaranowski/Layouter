package pl.nosystems.android.layouter.reconstructors.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;


public class SwitchViewReconstructor extends CompoundButtonReconstructor {
    private static final String TAG = SwitchViewReconstructor.class.getSimpleName();

    @Override
    public void reconstruct(@NonNull ViewHierarchyElement element,
                            @NonNull View elementView,
                            @NonNull Context context) {

        if (!(elementView instanceof Switch)) {
            return;
        }

        super.reconstruct(element, elementView, context);
        final Switch switchView = (Switch) elementView;
        final Iterable<ViewHierarchyElementAttribute> attributes = element.getAttributes();

        reconstructThumb(switchView, attributes, context);
        reconstructTrack(switchView, attributes, context);
        //reconstructThumbTint(switchView, attributes, context); FIXME: requires API 23 for 'easy' version....
        //reconstructTrackTint(switchView, attributes, context); FIXME: not implemeneted
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void reconstructThumbTint(@NonNull Switch switchView,
                                      @NonNull Iterable<ViewHierarchyElementAttribute> attributes,
                                      @NonNull Context context) {
        final ViewHierarchyElementAttribute thumbTintAttribute = findAttribute(attributes, "thumbTint", "android");
        if (thumbTintAttribute != null) {
            final String attributeValue = thumbTintAttribute.getValue();
            if (attributeValue.length() > 0 && attributeValue.charAt(0) == '@') {
                // Color resource or error
                final String[] attributeParts = attributeValue.split("/");
                final String defType = attributeParts[0].substring(1);
                final String defValue = attributeParts[1];
                if(defType.equals("color")) {
                    final int resID = context.getResources().getIdentifier(defValue, defType, context.getPackageName());
                    ColorStateList colorStateList = ColorStateList.valueOf(context.getColor(resID));
                    switchView.setThumbTintList(colorStateList);
                    return;
                }
            } else if(attributeValue.length() > 0 && attributeValue.charAt(0) == '#') {
                // Literal color
                // FIXME
                Log.w(TAG, "thumbTint literal color not implememeted!");
                return;
            }

            Log.w(TAG, "Cannot apply thumb tint of value " + attributeValue + " to view " + switchView);
        }
    }

    private void reconstructThumb(@NonNull Switch switchView,
                                  @NonNull Iterable<ViewHierarchyElementAttribute> attributes,
                                  @NonNull Context context) {

        final ViewHierarchyElementAttribute thumbAttribute = findAttribute(attributes, "thumb", "android");
        if (thumbAttribute != null) {
            final String attributeValue = thumbAttribute.getValue();
            if (attributeValue.length() > 0 && attributeValue.charAt(0) == '@') {
                // Drawable resource?
                final String[] attributeParts = attributeValue.split("/");
                final String defType = attributeParts[0].substring(1);
                final String defValue = attributeParts[1];
                final int resID = context.getResources().getIdentifier(defValue, defType, context.getPackageName());
                switchView.setThumbResource(resID);
            }
        }
    }

    private void reconstructTrack(@NonNull Switch switchView,
                                  @NonNull Iterable<ViewHierarchyElementAttribute> attributes,
                                  @NonNull Context context) {

        final ViewHierarchyElementAttribute trackAttribute = findAttribute(attributes, "track", "android");
        if (trackAttribute != null) {
            final String attributeValue = trackAttribute.getValue();
            if (attributeValue.length() > 0 && attributeValue.charAt(0) == '@') {
                // Drawable resource?
                final String[] attributeParts = attributeValue.split("/");
                final String defType = attributeParts[0].substring(1);
                final String defValue = attributeParts[1];
                final int resID = context.getResources().getIdentifier(defValue, defType, context.getPackageName());
                switchView.setTrackResource(resID);
            }
        }
    }
}
