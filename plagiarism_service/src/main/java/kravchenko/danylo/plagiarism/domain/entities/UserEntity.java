package kravchenko.danylo.plagiarism.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kravchenko.danylo.plagiarism.domain.dto.UserDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@ToString(exclude = {"password", "history"})
@EqualsAndHashCode(exclude = "history")
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
    @Size(min = 4, max = 20, message = "Login size should be more than {min} and less than {max}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Login can contain only letters, numbers and underscores")
    private String login;

    @Column(name = "password")
    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, max = 100, message = "Password size should be more than {min} and less than {max}")
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_to_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<PermissionEntity> permissions;

    @OneToMany(targetEntity = HistoryEntity.class, mappedBy="user", fetch = FetchType.LAZY)
    private List<HistoryEntity> history;

    /*
     * Convert UserEntity to UserDto
     */
    public UserDto toDto() {
        return UserDto.builder()
                .id(getId())
                .login(getLogin())
                .permissions(getPermissions().stream().map(PermissionEntity::getPermission).collect(Collectors.toList()))
                .build();
    }

    /*
     * check whether the user has needed permission
     */
    public boolean hasPermission(final String permissionCode) {

        for (PermissionEntity permissionEntity : getPermissions()) {
            if (permissionEntity.getPermission().name().equals(permissionCode)) {
                return true;
            }
        }

        return false;

    }

}