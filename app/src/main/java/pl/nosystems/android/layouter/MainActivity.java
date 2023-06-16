package pl.nosystems.android.layouter;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;
import pl.nosystems.android.layouter.core.ViewTools;
import pl.nosystems.android.layouter.dom4j.LayouterDom4J;
import pl.nosystems.android.layouter.dom4j.LayouterDom4JBuilder;
import pl.nosystems.android.layouter.glide.GlideLayouter;
import pl.nosystems.android.layouter.glide.GlideLayouterBuilder;
import pl.nosystems.android.layouter.glide.GlideLayouterRequest;
import pl.nosystems.android.layouter.glide.GlideLayouterRequestBuilder;
import pl.nosystems.android.layouter.reconstructors.core.LinearLayoutViewReconstructor;
import pl.nosystems.android.layouter.reconstructors.core.SwitchViewReconstructor;
import pl.nosystems.android.layouter.reconstructors.core.TextViewReconstructor;
import pl.nosystems.android.layouter.resources.FromXML;

public class MainActivity extends AppCompatActivity {

    private final List<ViewHierarchyElementReconstructor> reconstructors = new ArrayList<>();

    private ViewGroup contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content_container);

        reconstructors.add(new TextViewReconstructor());
        reconstructors.add(new LinearLayoutViewReconstructor());
        reconstructors.add(new SwitchViewReconstructor());


        contentContainer = findViewById(R.id.contentContainer);
        Button runLayouterButton = findViewById(R.id.runLayouterButton);

        runLayouterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                final GlideLayouterRequestBuilder<ViewHierarchyElement> requestBuilder = glideLayouter
                        .startBuildingRequestFrom(() ->
                                new FromXML()
                                .parse(getResources()
                                        .getLayout(R.layout.activity_main))
                        )
                        .withRawDataDecoder(s -> s)
                        .withCallback(view -> {
                            if(view instanceof ViewGroup) {
                                ArrayList<Class<? extends View>> types = new ArrayList<>();
                                types.add(Switch.class);
                                ViewGroup vg = (ViewGroup) view;
                                List<View> viewsOfType = ViewTools.findViewsOfType(vg, types);
                                for (View switchView :viewsOfType){
                                    ((Switch)switchView).setOnCheckedChangeListener((compoundButton, b) -> {
                                        Toast.makeText(compoundButton.getContext(), "Switch check state: " + b, Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }
                        })
                        .into(contentContainer);

                glideLayouter.queueRequest(requestBuilder.build());


                final GlideLayouterRequestBuilder<ViewHierarchyElement> requestBuilder2 = glideLayouter
                        .startBuildingRequestFrom(() ->
                                new FromXML()
                                        .parse(getResources()
                                                .getLayout(R.layout.test))
                        )
                        .withRawDataDecoder(s -> s)
                        .into(contentContainer);

                glideLayouter.queueRequest(requestBuilder2.build());

                final GlideLayouterRequest request3 = glideLayouter.startBuildingRequestFrom(() -> parse)
                        .withRawDataDecoder(s -> {
                            Thread.sleep(1000);
                            return s;
                        })
                        .into(contentContainer)
                        .build();

                //glideLayouter.queueRequest(request3);
            }
        });
    }
}
