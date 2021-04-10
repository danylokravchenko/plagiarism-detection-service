package kravchenko.danylo.plagiarism.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@ToString(exclude = "password")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "login")
    @NotNull(message = "Login cannot be empty")
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Login can contain only letters, numbers and underscores")
    private String login;

    @Column(name = "password")
    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, max = 100)
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_to_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<PermissionEntity> permissions;

}