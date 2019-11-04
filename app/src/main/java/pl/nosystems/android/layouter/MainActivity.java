package pl.nosystems.android.layouter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();

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
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        printAttributeSetForView(name, attrs);

        View view;
        if(isFullyQualifiedName(name)) {
            view = createInstanceForViewName(name, attrs);
        } else {
            view = createInstanceForAndroidViewName(name, attrs);
        }

        if(view.getId() == android.R.id.content) {
            SAXReader saxReader = new SAXReader();
            try {
                Document document = saxReader.read(new ByteArrayInputStream(TEST_LAYOUT.getBytes(StandardCharsets.UTF_8)));
                Element rootElement = document.getRootElement();

                return parseElementIntoView(rootElement, (ViewGroup) view);
               
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        }

        return view;
    }
    
    private ViewGroup parseElementIntoView(Element rootElement,
                                           ViewGroup container) {

        printAttributesForElement(rootElement);

        String rootName = rootElement.getName();
        View root;
        if(isFullyQualifiedName(rootName)) {
            root = createInstanceForViewName(rootName, null);
        } else {
            root = createInstanceForAndroidViewName(rootName, null);
        }

        if("TextView".equalsIgnoreCase(rootName)) {
            TextView textView = (TextView) root;
            Attribute text = rootElement.attribute("text");

            if(text != null) {
                textView.setText(text.getValue());


                ((TextView)root).setText(text.getValue());
            }

            Attribute layoutWidth = rootElement.attribute("layout_width");
            if(layoutWidth != null) {
                ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
                if(layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(layoutParams);
                }
                layoutParams.width = "wrap_content".equalsIgnoreCase(layoutWidth.getValue()) ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
                textView.setLayoutParams(layoutParams);
            }

            Attribute layoutHeight = rootElement.attribute("layout_height");
            if(layoutHeight != null) {
                ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
                layoutParams.height = "wrap_content".equalsIgnoreCase(layoutHeight.getValue()) ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
                textView.setLayoutParams(layoutParams);
            }
        }
        
        container.addView(root);

        for(Element element : rootElement.elements()) {
            parseElementIntoView(element, (ViewGroup) root);
        }

        return container;
    }

    private void printAttributesForElement(Element rootElement) {
        Log.d(TAG, "Element: " + rootElement.getName());

        for(Attribute attribute : rootElement.attributes()) {
            Log.d(TAG, "  Attribute: " + attribute.getName() + " : " + attribute.getValue());
        }
    }

    private void printAttributeSetForView(@NonNull String name,@NonNull AttributeSet attrs) {
        Log.d(TAG, "Attributes for view " + name);

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.d(TAG, "  attribute " + (i+1) + ":" + attrs.getAttributeName(i));
            Log.d(TAG, "  attribute value: " + attrs.getAttributeValue(i));
        }
    }

    @NonNull
    private View createInstanceForViewName(@NonNull String name, @Nullable AttributeSet attributeSet) {
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
        } catch (InstantiationException|InvocationTargetException|IllegalAccessException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException();
    }

    @NonNull
    private View createInstanceForAndroidViewName(@NonNull String name, @Nullable AttributeSet attributeSet) {
        Class rootClass = null;

        try {
            rootClass = Class.forName("android.widget." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if(rootClass == null) {
            try {
                rootClass = Class.forName("android.view." + name);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        try {
            Constructor[] constructors = rootClass.getConstructors();

            for (Constructor c : constructors) {
                if (c.getParameterCount() == 2
                        && c.getParameterTypes()[0] == Context.class
                        && c.getParameterTypes()[1] == AttributeSet.class) {
                    return (View) c.newInstance(this, attributeSet);
                }
            }
        } catch (InstantiationException|InvocationTargetException|IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException();
    }

    private boolean isFullyQualifiedName(@NonNull String name) {
        return name.contains(".");
    }
}
