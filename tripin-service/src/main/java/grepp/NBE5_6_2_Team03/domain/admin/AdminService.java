package grepp.NBE5_6_2_Team03.domain.admin;

import grepp.NBE5_6_2_Team03.api.controller.admin.dto.statistic.CountriesStatisticResponse;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.statistic.MonthlyStatisticResponse;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.user.UserInfoResponse;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.user.UserInfoUpdateRequest;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.user.UserSearchRequest;
import grepp.NBE5_6_2_Team03.domain.travelplan.repository.TravelPlanQueryRepository;
import grepp.NBE5_6_2_Team03.domain.travelplan.repository.TravelPlanRepository;
import grepp.NBE5_6_2_Team03.domain.user.User;
import grepp.NBE5_6_2_Team03.domain.user.repository.UserQueryRepository;
import grepp.NBE5_6_2_Team03.domain.user.repository.UserRepository;
import grepp.NBE5_6_2_Team03.global.exception.CannotModifyAdminException;
import grepp.NBE5_6_2_Team03.global.message.ExceptionMessage;
import grepp.NBE5_6_2_Team03.global.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final TravelPlanQueryRepository travelPlanQueryRepository;

    public Page<UserInfoResponse> findUsersPage(UserSearchRequest userSearchRequest) {
        Boolean isLocked = userSearchRequest.getLocked();
        Pageable pageable = userSearchRequest.getPageable();
        Page<User> lockedUserInfos = userQueryRepository.findUsersPage(isLocked, pageable);
        return lockedUserInfos.map(UserInfoResponse::of);
    }

    @Transactional
    public void updateUserInfo(Long id, UserInfoUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND)
        );

        user.updateProfile(
            request.getEmail(),
            request.getName(),
            request.getPhoneNumber(),
            null
        );
    }

    @Transactional
    public void lockUser(Long userId) {
        User user = getModifiableUser(userId);
        user.lock();
    }

    @Transactional
    public void unlockUser(Long userId) {
        User user = getModifiableUser(userId);
        user.unlock();
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = getModifiableUser(userId);
        travelPlanRepository.deleteByUserId(userId);
        userRepository.delete(user);
    }

    private User getModifiableUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
        if(user.isAdmin()) {
            throw new CannotModifyAdminException(ExceptionMessage.ADMIN_NOT_MODIFIED);
        }
        return user;
    }

    public List<MonthlyStatisticResponse> getMonthStatistics() {
        List<MonthlyStatisticResponse> projections = travelPlanQueryRepository.getMonthStatistics();
        return projections.stream()
            .map(p -> new MonthlyStatisticResponse(p.getMonth(), p.getCount()))
            .collect(Collectors.toList());
    }

    public List<CountriesStatisticResponse> getCountriesStatistics() {
        return travelPlanQueryRepository.getCountriesStatistics();
    }

    public boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isDuplicatedUsername(String username) {
        return userRepository.findByName(username).isPresent();
    }

}