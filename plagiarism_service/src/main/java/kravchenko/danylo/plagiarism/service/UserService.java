package kravchenko.danylo.plagiarism.service;

import kravchenko.danylo.plagiarism.domain.dto.HistoryDto;
import kravchenko.danylo.plagiarism.domain.dto.PlagiarismRequestItem;
import kravchenko.danylo.plagiarism.domain.dto.PlagiarismReview;
import kravchenko.danylo.plagiarism.domain.dto.UserDto;
import kravchenko.danylo.plagiarism.domain.entities.HistoryEntity;
import kravchenko.danylo.plagiarism.domain.entities.PermissionEntity;
import kravchenko.danylo.plagiarism.domain.entities.UserEntity;
import kravchenko.danylo.plagiarism.domain.exceptions.NotFoundException;
import kravchenko.danylo.plagiarism.repository.HistoryRepository;
import kravchenko.danylo.plagiarism.repository.PermissionRepository;
import kravchenko.danylo.plagiarism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kravchenko.danylo.plagiarism.specification.HistorySpecificationFactory.fetchAll;
import static kravchenko.danylo.plagiarism.specification.HistorySpecificationFactory.historyUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final HistoryRepository historyRepository;

    private final DateProvider dateProvider;

    private final int DEFAULT_PERMISSION_ID = 1;
    private final int VISITOR_ID = 1;

    @Value("${plagiarismThreshold}")
    private Double plagiarismThreshold;

    public static List<SimpleGrantedAuthority> mapAuthorities(final List<PermissionEntity> permissions) {
        return permissions.stream()
                .map(PermissionEntity::getPermission)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    @CachePut(value = "users", key = "#user.login")
    public UserEntity addNewUser(final UserDto user) {
        PermissionEntity permission = permissionRepository.findById(DEFAULT_PERMISSION_ID)
                .orElseThrow(() -> new NotFoundException("No permission found"));

        UserEntity newUser = user.toEntity(List.of(permission));

        userRepository.saveAndFlush(newUser);
        log.info("Added new user #" + newUser.getId().toString());

        return newUser;
    }

    @Cacheable(value = "users", key = "#login")
    public Optional<UserEntity> findByLogin(final String login) throws NotFoundException {
        return userRepository.findByLogin(login);
    }

    @Cacheable(value = "users", key = "1")
    public Optional<UserEntity> getVisitor() throws NotFoundException {
        return userRepository.findById(VISITOR_ID);
    }

    public Optional<UserEntity> findByUserId(final Integer id) throws NotFoundException {
        return userRepository.findById(id);
    }

    public List<HistoryDto> getUserHistory(final Integer userId, final Integer page, final Integer limit) {

        final Page<HistoryEntity> historyPage = historyRepository.findAll(
                fetchAll()
                        .and(historyUser(userId)),
                PageRequest.of(page, limit)
        );

        return historyPage.stream()
                .map(HistoryEntity::toDto)
                .collect(Collectors.toList());

    }

    @SneakyThrows
    public HistoryEntity addUserHistory(final PlagiarismRequestItem item, final PlagiarismReview review, final Integer userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user found"));

        HistoryEntity history = HistoryEntity.builder()
                .textA(item.getTextA())
                .textB(item.getTextB())
                .plagiarismLevel(review.getPlagiarismLevel())
                .isPlagiarism(review.getPlagiarismLevel() >= plagiarismThreshold)
                .createdAt(dateProvider.getCurrentDateTime())
                .user(user)
                .build();

        historyRepository.saveAndFlush(history);
        log.info("Added new history #" + history.getId().toString() + " for user #" + user.getId());

        return history;

    }
}