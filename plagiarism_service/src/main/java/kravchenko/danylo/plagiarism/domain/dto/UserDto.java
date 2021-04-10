package kravchenko.danylo.plagiarism.domain.dto;

import kravchenko.danylo.plagiarism.domain.entities.PermissionEntity;
import kravchenko.danylo.plagiarism.domain.entities.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class UserDto {

    @NotNull(message = "Login cannot be empty")
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Login can contain only letters, numbers and underscores")
    private String login;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, max = 40)
    private String password;

    /*
     * Convert UserDto instance to UserEntity object
     */
    public UserEntity toEntity(List<PermissionEntity> permissions) {
        return UserEntity.builder()
                .login(this.getLogin())
                .password(this.getPassword())
                .permissions(permissions)
                .build();
    }

}