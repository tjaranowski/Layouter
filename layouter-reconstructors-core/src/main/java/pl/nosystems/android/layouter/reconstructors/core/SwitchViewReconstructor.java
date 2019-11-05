package pl.nosystems.android.layouter.reconstructors.core;

import android.content.Context;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;


public class SwitchViewReconstructor extends CompoundButtonReconstructor {

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

        // reconstructThumb(switchView, attributes)
    }

    private void reconstructThumb(@NonNull Switch switchView,
                                  @NonNull Iterable<ViewHierarchyElementAttribute> attributes) {
        final ViewHierarchyElementAttribute thumbAttribute = findAttribute(attributes, "thumb", "android");
        if(thumbAttribute != null) {
            // TODO: this is a drawable to be used
        }
    }
}
