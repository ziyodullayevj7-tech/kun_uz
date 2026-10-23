package jahongir.kun_uz.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.LmsActivityEntity;
import jahongir.kun_uz.service.LmsActivityService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("lms")
@PageTitle("LMS Activities")
public class LmsActivityView extends VerticalLayout {

    private final LmsActivityService lmsActivityService;

    private final Grid<LmsActivityEntity>              crudGrid      = new Grid<>(LmsActivityEntity.class, false);
    private final Grid<AnalyticsDTO.LmsEngagementMetric> leaderGrid  = new Grid<>(AnalyticsDTO.LmsEngagementMetric.class, false);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public LmsActivityView(LmsActivityService lmsActivityService) {
        this.lmsActivityService = lmsActivityService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("LMS Activity Log"));
        buildCrudSection();
        add(new H3("Engagement Leaderboard (last 30 days)"));
        buildLeaderSection();

        refreshCrud();
        refreshLeaderboard();
    }

    // ── CRUD section ──────────────────────────────────────────────────────────

    private void buildCrudSection() {
        crudGrid.addColumn(LmsActivityEntity::getId).setHeader("ID").setWidth("260px").setFlexGrow(0);
        crudGrid.addColumn(a -> a.getStudent().getFirstName() + " " + a.getStudent().getLastName())
                .setHeader("Student").setAutoWidth(true);
        crudGrid.addColumn(LmsActivityEntity::getAction).setHeader("Action").setAutoWidth(true);
        crudGrid.addColumn(LmsActivityEntity::getDurationMinutes).setHeader("Minutes").setAutoWidth(true);
        crudGrid.addColumn(a -> a.getCreatedAt().format(FMT)).setHeader("Recorded At").setAutoWidth(true);

        crudGrid.addComponentColumn(activity -> {
            Button del = new Button("Delete", e -> confirmDelete(activity));
            del.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            return del;
        }).setHeader("Actions").setAutoWidth(true);

        add(crudGrid);
    }

    private void confirmDelete(LmsActivityEntity activity) {
        ConfirmDialog cd = new ConfirmDialog(
                "Delete Activity",
                "Delete this activity record?",
                "Delete", e -> {
                    lmsActivityService.delete(activity.getId());
                    Notification.show("Deleted.");
                    refreshAll();
                },
                "Cancel", e -> {}
        );
        cd.setConfirmButtonTheme("error primary");
        cd.open();
    }

    // ── Leaderboard section ───────────────────────────────────────────────────

    private void buildLeaderSection() {
        leaderGrid.addColumn(AnalyticsDTO.LmsEngagementMetric::studentName)
                .setHeader("Student").setAutoWidth(true);
        leaderGrid.addColumn(AnalyticsDTO.LmsEngagementMetric::activeMinutes)
                .setHeader("Active Minutes").setAutoWidth(true);
        leaderGrid.addColumn(AnalyticsDTO.LmsEngagementMetric::totalActions)
                .setHeader("Total Actions").setAutoWidth(true);

        // Per-student study hours lookup
        TextField studyStudentId = new TextField("Student ID");
        Button studyBtn = new Button("Show Study Hours", e -> showStudyHours(studyStudentId.getValue().trim()));
        studyBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        Button refresh = new Button("Refresh Leaderboard", ev -> refreshLeaderboard());
        add(new HorizontalLayout(refresh, studyStudentId, studyBtn), leaderGrid);
    }

    private void showStudyHours(String studentId) {
        if (studentId.isBlank()) {
            Notification.show("Please enter a Student ID.");
            return;
        }
        try {
            lmsActivityService.getStudentStudyHours(studentId).ifPresentOrElse(
                    m -> {
                        Dialog d = new Dialog();
                        d.setHeaderTitle("Study Hours — " + m.studentName());
                        d.add(new Span(
                                "Total Active Minutes: " + m.activeMinutes()
                                + "   |   Total Actions: " + m.totalActions()
                        ));
                        d.getFooter().add(new Button("Close", e -> d.close()));
                        d.open();
                    },
                    () -> Notification.show("No LMS activity found for this student.")
            );
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void refreshCrud() {
        crudGrid.setItems(lmsActivityService.findAll());
    }

    private void refreshLeaderboard() {
        List<AnalyticsDTO.LmsEngagementMetric> data = lmsActivityService.getEngagementLeaderboard(30);
        leaderGrid.setItems(data);
    }

    private void refreshAll() {
        refreshCrud();
        refreshLeaderboard();
    }
}
