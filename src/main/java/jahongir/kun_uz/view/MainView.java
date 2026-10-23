package jahongir.kun_uz.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Home — Student Management")
public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("🎓 University Student Management");
        H3 subtitle = new H3("Select a section to get started");

        Button students  = navButton("👤 Students",     "/students");
        Button courses   = navButton("📚 Courses",      "/courses");
        Button faceId    = navButton("🔍 Face-ID Logs", "/faceid");
        Button lms       = navButton("💻 LMS Activity", "/lms");
        Button grades    = navButton("📊 Grades",       "/grades");

        FlexLayout nav = new FlexLayout(students, courses, faceId, lms, grades);
        nav.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        nav.setJustifyContentMode(JustifyContentMode.CENTER);
        nav.getStyle().set("gap", "16px");

        add(title, subtitle, nav);
    }

    private Button navButton(String label, String path) {
        Button btn = new Button(label, e -> UI.getCurrent().navigate(path));
        btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        btn.setWidth("220px");
        return btn;
    }
}
