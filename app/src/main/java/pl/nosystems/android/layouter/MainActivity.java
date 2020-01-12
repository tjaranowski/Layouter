package pl.nosystems.android.layouter;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;
import pl.nosystems.android.layouter.dom4j.LayouterDom4J;
import pl.nosystems.android.layouter.dom4j.LayouterDom4JBuilder;
import pl.nosystems.android.layouter.glide.GlideLayouter;
import pl.nosystems.android.layouter.glide.GlideLayouterBuilder;
import pl.nosystems.android.layouter.glide.GlideLayouterRequest;
import pl.nosystems.android.layouter.glide.GlideLayouterRequestBuilder;
import pl.nosystems.android.layouter.reconstructors.core.LinearLayoutViewReconstructor;
import pl.nosystems.android.layouter.reconstructors.core.SwitchViewReconstructor;
import pl.nosystems.android.layouter.reconstructors.core.TextViewReconstructor;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_LAYOUT = "" +
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
            "    <Switch\n" +
            "        android:id=\"@+id/switch1\"\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:gravity=\"left\"\n" +
            "        android:layout_gravity=\"left\"\n" +
            "        android:thumbTint=\"@color/colorPrimary\"" +
            "        android:thumb=\"@mipmap/ic_launcher\"/>\n" +
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

    private static final String TEST = "" +
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    android:id=\"@+id/foo\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:orientation=\"vertical\"\n" +
            "    android:text=\"@string/app_name\">\n" +
            "\n" +
            "</TextView>";

    private final List<ViewHierarchyElementReconstructor> reconstructors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content_container);

        reconstructors.add(new TextViewReconstructor());
        reconstructors.add(new LinearLayoutViewReconstructor());
        reconstructors.add(new SwitchViewReconstructor());


        final ViewGroup viewGroup = findViewById(R.id.contentContainer);
        final SAXReader saxReader = new SAXReader();
        final GlideLayouter glideLayouter = GlideLayouterBuilder
                .instance()
                .withReconstructors(reconstructors)
                .build();

        final LayouterDom4J layouterDom4J = LayouterDom4JBuilder
                .instance()
                .build();

        final XmlResourceParser layout = getResources().getLayout(R.layout.xml_pull_parser_test);


        final ViewHierarchyElement parse = new FromXML().parse(layout);

        final GlideLayouterRequestBuilder<Document> requestBuilder = glideLayouter
                .startBuildingRequestFrom(() -> TEST_LAYOUT)
                .withRawDataTransformator(s -> saxReader.read(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8))))
                .withRawDataDecoder(layouterDom4J::createElementsFromDom4JDocument)
                .into(viewGroup);

        glideLayouter.queueRequest(requestBuilder.build());


        final GlideLayouterRequestBuilder<Document> requestBuilder2 = glideLayouter
                .startBuildingRequestFrom(() -> TEST)
                .withRawDataTransformator(s -> saxReader.read(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8))))
                .withRawDataDecoder(layouterDom4J::createElementsFromDom4JDocument)
                .into(viewGroup);

        glideLayouter.queueRequest(requestBuilder2.build());

        final GlideLayouterRequest request3 = glideLayouter.startBuildingRequestFrom(() -> parse)
                .withRawDataDecoder(s -> s)
                .into(viewGroup)
                .build();

        glideLayouter.queueRequest(request3);



        /*
        GlideLayouter
                .<Document>under(this)
                .from(Uri.parse("https://nosystems.pl/layouter/sample.xml"))
                .withDecoder(o -> LayouterDom4JBuilder.instance().build().createElementsFromDom4JDocument(o))
                .withPlaceholderLayout(R.layout.support_simple_spinner_dropdown_item)
                .into(viewGroup);


        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new ByteArrayInputStream(TEST_LAYOUT.getBytes(StandardCharsets.UTF_8)));

            LayouterDom4J layouterDom4J = LayouterDom4JBuilder
                    .instance()
                    .build();

            ViewHierarchyElement viewHierarchyElement = layouterDom4J.createElementsFromDom4JDocument(document);

            // FIXME: is return value needed? <- currently will add to viewGroup anyways/........
            LayouterBuilder
                    .instance()
                    .build()
                    .parseElementIntoView(viewHierarchyElement, reconstructors, viewGroup, this);

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }*/
    }
}
