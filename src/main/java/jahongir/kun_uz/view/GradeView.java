package jahongir.kun_uz.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jahongir.kun_uz.entity.GradeEntity;
import jahongir.kun_uz.service.GradeService;

import java.util.Map;

@Route("grades")
@PageTitle("Grades")
public class GradeView extends VerticalLayout {

    private final GradeService gradeService;

    private final Grid<GradeEntity> crudGrid = new Grid<>(GradeEntity.class, false);

    public GradeView(GradeService gradeService) {
        this.gradeService = gradeService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Grade Management"));
        buildCrudSection();
        add(new H3("Student Performance Lookup"));
        buildAnalyticsSection();

        refreshCrud();
    }

    // ── CRUD section ──────────────────────────────────────────────────────────

    private void buildCrudSection() {
        crudGrid.addColumn(GradeEntity::getId).setHeader("ID").setWidth("260px").setFlexGrow(0);
        crudGrid.addColumn(g -> g.getStudent().getFirstName() + " " + g.getStudent().getLastName())
                .setHeader("Student").setAutoWidth(true);
        crudGrid.addColumn(g -> g.getCourse().getName())
                .setHeader("Course").setAutoWidth(true);
        crudGrid.addColumn(g -> String.format("%.2f", g.getScore()))
                .setHeader("Score").setAutoWidth(true);

        crudGrid.addComponentColumn(grade -> {
            Button edit = new Button("Edit Score", e -> openEditDialog(grade));
            edit.addThemeVariants(ButtonVariant.LUMO_SMALL);

            Button del = new Button("Delete", e -> confirmDelete(grade));
            del.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(edit, del);
        }).setHeader("Actions").setAutoWidth(true);

        add(crudGrid);
    }

    private void openEditDialog(GradeEntity grade) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit Score");

        NumberField score = new NumberField("Score (0–100)");
        score.setValue(grade.getScore());
        score.setMin(0);
        score.setMax(100);

        Button save = new Button("Save", e -> {
            try {
                gradeService.upsert(
                        grade.getStudent().getId(),
                        grade.getCourse().getId(),
                        score.getValue()
                );
                Notification.show("Score updated.");
                dialog.close();
                refreshCrud();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(new FormLayout(score));
        dialog.getFooter().add(new Button("Cancel", e -> dialog.close()), save);
        dialog.open();
    }

    private void confirmDelete(GradeEntity grade) {
        ConfirmDialog cd = new ConfirmDialog(
                "Delete Grade",
                "Delete this grade record?",
                "Delete", e -> {
                    gradeService.delete(grade.getId());
                    Notification.show("Deleted.");
                    refreshCrud();
                },
                "Cancel", e -> {}
        );
        cd.setConfirmButtonTheme("error primary");
        cd.open();
    }

    // ── Analytics section ─────────────────────────────────────────────────────

    private void buildAnalyticsSection() {
        TextField perfStudentId = new TextField("Student ID (UUID)");
        Button perfBtn = new Button("Show Performance Metrics", e -> showPerformance(perfStudentId.getValue().trim()));
        perfBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        add(new HorizontalLayout(perfStudentId, perfBtn));
    }

    private void showPerformance(String studentId) {
        if (studentId.isBlank()) {
            Notification.show("Please enter a Student ID.");
            return;
        }
        try {
            Map<String, Object> metrics = gradeService.getStudentPerformanceMetrics(studentId);
            Dialog d = new Dialog();
            d.setHeaderTitle("Performance — " + studentId);
            d.add(new Span(String.format(
                    "GPA: %.2f  |  Highest: %.2f  |  Lowest: %.2f  |  Graded Courses: %d",
                    (Double)  metrics.get("gpa"),
                    (Double)  metrics.get("highest"),
                    (Double)  metrics.get("lowest"),
                    ((Long)   metrics.get("gradeCount")).intValue()
            )));
            d.getFooter().add(new Button("Close", ev -> d.close()));
            d.open();
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void refreshCrud() {
        crudGrid.setItems(gradeService.findAll());
    }
}
