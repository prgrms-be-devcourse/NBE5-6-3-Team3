package grepp.NBE5_6_2_Team03.api.controller.admin;

import grepp.NBE5_6_2_Team03.api.controller.admin.dto.statistic.StatisticResponse;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.user.UserInfoResponse;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.user.UserInfoUpdateRequest;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.user.UserSearchRequest;

import grepp.NBE5_6_2_Team03.domain.admin.AdminService;
import grepp.NBE5_6_2_Team03.global.message.AdminSuccessMessage;
import grepp.NBE5_6_2_Team03.global.response.ApiResponse;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/user-info")
    public ApiResponse<Page<UserInfoResponse>> getUserInfos(
        @ModelAttribute UserSearchRequest request
    ) {
        return ApiResponse.success(adminService.findUsersPage(request));
    }

    @PatchMapping("/user-info/{id}")
    public ApiResponse<String> updateUserInfo(
        @PathVariable("id") Long id,
        @RequestBody UserInfoUpdateRequest request
    ) {
        adminService.updateUserInfo(id, request);
        return ApiResponse.success(AdminSuccessMessage.USER_INFO_UPDATED.getMessage());
    }

    @DeleteMapping("/user-info/{id}")
    public ApiResponse<String> deleteUserInfo(
        @PathVariable("id") Long id
    ) {
        adminService.deleteUserById(id);
        return ApiResponse.success(AdminSuccessMessage.USER_DELETED.getMessage());
    }

    @PatchMapping("/user-info/{id}/lock")
    public ApiResponse<String> lockUser(
        @PathVariable("id") Long userId
    ) {
        adminService.lockUser(userId);
        return ApiResponse.success(AdminSuccessMessage.USER_LOCKED.getMessage());
    }

    @PatchMapping("/user-info/{id}/unlock")
    public ApiResponse<String> unlockUser(
        @PathVariable("id") Long userId
    ) {
        adminService.unlockUser(userId);
        return ApiResponse.success(AdminSuccessMessage.USER_UNLOCKED.getMessage());
    }

    @GetMapping("/statistic")
    public ApiResponse<StatisticResponse> getStatistics() {
        StatisticResponse statisticResponse = StatisticResponse
            .builder()
            .countries(adminService.getCountriesStatistics())
            .monthly(adminService.getMonthStatistics())
            .build();
        return ApiResponse.success(statisticResponse);
    }

    @GetMapping("/valid-email")
    public ApiResponse<Map<String, Boolean>> validateEmail(@RequestParam("email") String email) {
        boolean isDuplicatedEmail = adminService.isDuplicatedEmail(email);
        return ApiResponse.success(isDuplicatedEmail ?
            Collections.singletonMap("duplicated", true) : Collections.singletonMap("duplicated", false));
    }

    @GetMapping("/valid-name")
    public ApiResponse<Map<String, Boolean>> validateName(@RequestParam("name") String name) {
        boolean isDuplicatedName = adminService.isDuplicatedUsername(name);
        return ApiResponse.success(isDuplicatedName ?
            Collections.singletonMap("duplicated", true) : Collections.singletonMap("duplicated", false));
    }

}