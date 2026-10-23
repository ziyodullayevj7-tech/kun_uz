package jahongir.kun_uz.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.CourseEntity;
import jahongir.kun_uz.service.CourseService;

@Route("courses")
@PageTitle("Courses")
public class CourseView extends VerticalLayout {

    private final CourseService courseService;

    private final Grid<CourseEntity> crudGrid = new Grid<>(CourseEntity.class, false);
    private final Grid<AnalyticsDTO.CourseGradeAnalytics> analyticsGrid =
            new Grid<>(AnalyticsDTO.CourseGradeAnalytics.class, false);

    public CourseView(CourseService courseService) {
        this.courseService = courseService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Course Management"));
        buildCrudSection();
        add(new H3("Course Grade Analytics"));
        buildAnalyticsSection();

        refreshCrud();
        refreshAnalytics();
    }

    // ── CRUD section ──────────────────────────────────────────────────────────

    private void buildCrudSection() {
        crudGrid.addColumn(CourseEntity::getId).setHeader("ID").setWidth("260px").setFlexGrow(0);
        crudGrid.addColumn(CourseEntity::getCode).setHeader("Code").setAutoWidth(true);
        crudGrid.addColumn(CourseEntity::getName).setHeader("Name").setAutoWidth(true);
        crudGrid.addColumn(CourseEntity::getCredits).setHeader("Credits").setAutoWidth(true);

        crudGrid.addComponentColumn(course -> {
            Button edit = new Button("Edit", e -> openEditDialog(course));
            edit.addThemeVariants(ButtonVariant.LUMO_SMALL);

            Button del = new Button("Delete", e -> confirmDelete(course));
            del.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(edit, del);
        }).setHeader("Actions").setAutoWidth(true);

        add(crudGrid);
    }

    private void openEditDialog(CourseEntity course) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit Course");

        TextField name       = new TextField("Name",    course.getName(),    "");
        IntegerField credits = new IntegerField("Credits");
        credits.setValue(course.getCredits());

        FormLayout form = new FormLayout(name, credits);

        Button save = new Button("Save", e -> {
            CourseEntity patch = new CourseEntity();
            patch.setName(name.getValue().trim());
            patch.setCredits(credits.getValue() == null ? 0 : credits.getValue());
            try {
                courseService.update(course.getId(), patch);
                Notification.show("Course updated.");
                dialog.close();
                refreshAll();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(form);
        dialog.getFooter().add(new Button("Cancel", e -> dialog.close()), save);
        dialog.open();
    }

    private void confirmDelete(CourseEntity course) {
        ConfirmDialog cd = new ConfirmDialog(
                "Delete Course",
                "Delete course \"" + course.getName() + "\"?",
                "Delete", e -> {
                    courseService.delete(course.getId());
                    Notification.show("Deleted.");
                    refreshAll();
                },
                "Cancel", e -> {}
        );
        cd.setConfirmButtonTheme("error primary");
        cd.open();
    }

    // ── Analytics section ─────────────────────────────────────────────────────

    private void buildAnalyticsSection() {
        analyticsGrid.addColumn(AnalyticsDTO.CourseGradeAnalytics::courseName)
                .setHeader("Course").setAutoWidth(true);
        analyticsGrid.addColumn(r -> String.format("%.2f", r.averageScore()))
                .setHeader("Avg Score").setAutoWidth(true);
        analyticsGrid.addColumn(r -> String.format("%.2f", r.highestScore()))
                .setHeader("Highest").setAutoWidth(true);
        analyticsGrid.addColumn(r -> String.format("%.2f", r.lowestScore()))
                .setHeader("Lowest").setAutoWidth(true);
        analyticsGrid.addColumn(AnalyticsDTO.CourseGradeAnalytics::gradedStudentsCount)
                .setHeader("Graded Students").setAutoWidth(true);

        Button refresh = new Button("Refresh Analytics", e -> refreshAnalytics());
        add(refresh, analyticsGrid);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void refreshCrud() {
        crudGrid.setItems(courseService.findAll());
    }

    private void refreshAnalytics() {
        analyticsGrid.setItems(courseService.getAllCoursePerformance());
    }

    private void refreshAll() {
        refreshCrud();
        refreshAnalytics();
    }
}
