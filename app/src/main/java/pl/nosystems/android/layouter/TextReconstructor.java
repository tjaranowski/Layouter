package pl.nosystems.android.layouter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.annimon.stream.Stream;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;
import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;


public class TextReconstructor implements ViewHierarchyElementReconstructor {
    private static final String TAG = TextReconstructor.class.getSimpleName();

    @Override
    public void reconstruct(@NonNull ViewHierarchyElement element,
                            @NonNull View elementView) {

        ViewHierarchyElementAttribute textAttribute = Stream.of(element.getAttributes())
                .filter(attribute -> attribute.getName().equals("text") && attribute.getNamespacePrefix().equals("android"))
                .findFirst()
                .orElse(null);
        if(textAttribute != null) {
            if(elementView instanceof TextView) {
                ((TextView) elementView).setText(textAttribute.getValue());
            } else {
                Log.w(TAG, "Cannot apply android:text attribute to view : " + elementView);
            }
        }
    }
}
