package kravchenko.danylo.plagiarism.specification;

import kravchenko.danylo.plagiarism.domain.entities.HistoryEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;

public class HistorySpecificationFactory {

    public static Specification<HistoryEntity> plagiarismOnly(final boolean isPlagiarism) {
        return (root, query, cb) -> cb.equal(root.get("isPlagiarism"), isPlagiarism);
    }

    public static Specification<HistoryEntity> newestOnly(final LocalDateTime createdAfter) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("created_at"), createdAfter);
    }

    public static Specification<HistoryEntity> historyUser(final int userId) {
        return (root, query, cb) -> cb.equal(root.join("user").get("id"), userId);
    }

    public static Specification<HistoryEntity> fetchAll() {
        return (root, query, cb) -> {
            if (!query.getResultType().equals(Long.class)) {
                root.fetch("user");
            }
            return cb.conjunction();
        };
    }

}
