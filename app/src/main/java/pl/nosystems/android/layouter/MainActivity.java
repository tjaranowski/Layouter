package pl.nosystems.android.layouter;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;
import pl.nosystems.android.layouter.dom4j.LayouterDom4J;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_LAYOUT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<androidx.constraintlayout.widget.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    tools:context=\".MainActivity\">\n" +
            "\n" +
            "    <TextView\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:text=\"Hello World!\"\n" +
            "        android:textSize=\"100px\"\n" +
            "        app:layout_constraintBottom_toBottomOf=\"parent\"\n" +
            "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
            "        app:layout_constraintRight_toRightOf=\"parent\"\n" +
            "        app:layout_constraintTop_toTopOf=\"parent\" />\n" +
            "\n" +
            "</androidx.constraintlayout.widget.ConstraintLayout>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content_container);

        reconstructors.add(new TextReconstructor());

        final ViewGroup viewGroup = findViewById(R.id.contentContainer);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new ByteArrayInputStream(TEST_LAYOUT.getBytes(StandardCharsets.UTF_8)));

            ViewHierarchyElement viewHierarchyElement = LayouterDom4J.createElementsFromDom4JDocument(document);

            // FIXME: is return value needed? <- currently will add to viewGroup anyways/........
            parseElementIntoView(viewHierarchyElement, viewGroup);

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    private final List<ViewHierarchyElementReconstructor> reconstructors = new ArrayList<>();

    @NonNull
    private void parseElementIntoView(@NonNull ViewHierarchyElement element,
                                      @NonNull ViewGroup container) {

        final View elementView = createElementViewForElement(element);

        for (ViewHierarchyElementReconstructor reconstructor : reconstructors) {
            reconstructor.reconstruct(element, elementView);
        }

        if(elementView instanceof ViewGroup) {
            for (ViewHierarchyElement child : element.getChildren()) {
                parseElementIntoView(child, (ViewGroup) elementView);
            }
        }

        container.addView(elementView);
    }

    @NonNull
    private View createElementViewForElement(@NonNull ViewHierarchyElement viewHierarchyElement) {
        String rootName = viewHierarchyElement.getFullyQualifiedName();
        return createInstanceForViewName(rootName, null);
    }

    @NonNull
    private View createInstanceForViewName(@NonNull String name,
                                           @Nullable AttributeSet attributeSet) {
        try {
            Class rootClass = Class.forName(name);
            Constructor[] constructors = rootClass.getConstructors();

            for (Constructor c : constructors) {
                if (c.getParameterCount() == 2
                        && c.getParameterTypes()[0] == Context.class
                        && c.getParameterTypes()[1] == AttributeSet.class) {
                    return (View) c.newInstance(this, attributeSet);
                }
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException();
    }
}
