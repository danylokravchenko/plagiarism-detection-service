package kravchenko.danylo.plagiarism.domain.entities;

import kravchenko.danylo.plagiarism.domain.dto.HistoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "is_plagiarism")
    private boolean isPlagiarism;

    @Column(name = "plagiarism_level")
    private double plagiarismLevel;

    @Column(name = "text_a")
    private String textA;

    @Column(name = "text_b")
    private String textB;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public HistoryDto toDto() {
        return HistoryDto.builder()
                .id(getId())
                .isPlagiarism(isPlagiarism())
                .plagiarismLevel(getPlagiarismLevel())
                .textA(getTextA())
                .textB(getTextB())
                .createdAt(getCreatedAt())
                .user(getUser().toDto())
                .build();
    }

}
