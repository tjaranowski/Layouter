package pl.nosystems.android.layouter.glide;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;

public class GlideLayouterBuilder {
    @NonNull
    private final List<ViewHierarchyElementReconstructor> reconstructorList = new ArrayList<>();

    @NonNull
    public static GlideLayouterBuilder instance() {
        return new GlideLayouterBuilder();
    }

    @NonNull
    public GlideLayouterBuilder withReconstructors(@NonNull List<ViewHierarchyElementReconstructor> reconstructorList) {
        this.reconstructorList.addAll(reconstructorList);
        return this;
    }

    @NonNull
    public GlideLayouter build() {
        return new GlideLayouter(reconstructorList);
    }
}
