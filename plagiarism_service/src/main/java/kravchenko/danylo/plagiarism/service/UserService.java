package kravchenko.danylo.plagiarism.service;

import kravchenko.danylo.plagiarism.domain.dto.UserDto;
import kravchenko.danylo.plagiarism.domain.entities.PermissionEntity;
import kravchenko.danylo.plagiarism.domain.entities.UserEntity;
import kravchenko.danylo.plagiarism.domain.exceptions.NotFoundException;
import kravchenko.danylo.plagiarism.repository.PermissionRepository;
import kravchenko.danylo.plagiarism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    private final int DEFAULT_PERMISSION_ID = 1;

    @Transactional
    public UserEntity addNewUser(final UserDto user) {
        PermissionEntity permission = permissionRepository.findById(DEFAULT_PERMISSION_ID)
                .orElseThrow(() -> new NotFoundException("No permission found"));

        UserEntity newUser = user.toEntity(List.of(permission));

        userRepository.saveAndFlush(newUser);
        log.info("Added new user #" + newUser.getId().toString());

        return newUser;
    }

    public Optional<UserEntity> findByLogin(final String login) throws NotFoundException {
        return userRepository.findByLogin(login);
    }

    public Optional<UserEntity> findByUserId(final Integer id) throws NotFoundException {
        return userRepository.findById(id);
    }

    public static List<SimpleGrantedAuthority> mapAuthorities(final List<PermissionEntity> permissions) {
        return permissions.stream()
                .map(PermissionEntity::getPermission)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }
}