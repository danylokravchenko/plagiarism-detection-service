package kravchenko.danylo.plagiarism.repository;

import kravchenko.danylo.plagiarism.domain.entities.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer>, JpaSpecificationExecutor<HistoryEntity> {
}
