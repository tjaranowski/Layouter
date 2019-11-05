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

import pl.nosystems.android.layouter.core.Layouter;
import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;
import pl.nosystems.android.layouter.dom4j.LayouterDom4J;
import pl.nosystems.android.layouter.reconstructors.core.LinearLayoutViewReconstructor;
import pl.nosystems.android.layouter.reconstructors.core.TextViewReconstructor;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_LAYOUT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    android:orientation=\"vertical\"\n" +
            "    tools:context=\".MainActivity\">\n" +
            "\n" +
            "    <EditText\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"64dp\" />\n" +
            "\n" +
            "    <TextView\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"match_parent\"\n" +
            "        android:layout_gravity=\"center\"\n" +
            "        android:gravity=\"center\"\n" +
            "        android:text=\"Hello World!\"\n" +
            "        android:textSize=\"48sp\" />\n" +
            "\n" +
            "</LinearLayout>";


    private final List<ViewHierarchyElementReconstructor> reconstructors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content_container);

        final float displayDensity = getResources().getDisplayMetrics().density;

        reconstructors.add(new TextViewReconstructor(displayDensity));
        reconstructors.add(new LinearLayoutViewReconstructor(displayDensity));

        final ViewGroup viewGroup = findViewById(R.id.contentContainer);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new ByteArrayInputStream(TEST_LAYOUT.getBytes(StandardCharsets.UTF_8)));

            ViewHierarchyElement viewHierarchyElement = LayouterDom4J.createElementsFromDom4JDocument(document);

            // FIXME: is return value needed? <- currently will add to viewGroup anyways/........
            Layouter.parseElementIntoView(viewHierarchyElement, reconstructors, viewGroup, this);

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
