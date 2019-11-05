package pl.nosystems.android.layouter.reconstructors.core;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;


abstract class CompoundButtonReconstructor extends ViewReconstructor {

    @Override
    public void reconstruct(@NonNull ViewHierarchyElement element,
                            @NonNull View elementView,
                            @NonNull Context context) {

        if(! (elementView instanceof CompoundButton)) {
            return;
        }

        super.reconstruct(element, elementView, context);
        final CompoundButton compoundButton = (CompoundButton) elementView;
        final Iterable<ViewHierarchyElementAttribute> attributes = element.getAttributes();

        reconstructText(compoundButton, attributes);
    }

    private void reconstructText(@NonNull CompoundButton compoundButtonView,
                                 @NonNull Iterable<ViewHierarchyElementAttribute> attributes) {

        final ViewHierarchyElementAttribute textAttribute = findAttribute(attributes, "text", "android");
        if (textAttribute != null) {
            compoundButtonView.setText(textAttribute.getValue());
        }
    }
}
