package pl.nosystems.android.layouter.glide;

import androidx.annotation.NonNull;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;


public interface ContentDecoder<Content> {

    @NonNull
    ViewHierarchyElement decode(@NonNull Content content);
}
