package jahongir.kun_uz.dto;

import java.time.LocalDateTime;

public final class AnalyticsDTO {

    private AnalyticsDTO() {}

    public record StudentSummaryAnalytics(
        String studentId,
        String fullName,
        String studentNumber,
        Double gpaScore,
        Long totalCampusVisits,
        Long totalLmsMinutes
    ) {}

    public record FaceIdFrequencyMetric(
        String studentId,
        String studentName,
        Long entryCount,
        LocalDateTime lastRecordedEntry
    ) {}

    public record LmsEngagementMetric(
        String studentId,
        String studentName,
        Long activeMinutes,
        Long totalActions
    ) {}

    public record CourseGradeAnalytics(
        String courseId,
        String courseName,
        Double averageScore,
        Double highestScore,
        Double lowestScore,
        Long gradedStudentsCount
    ) {}
}