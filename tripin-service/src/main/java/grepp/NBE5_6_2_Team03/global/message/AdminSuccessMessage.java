package grepp.NBE5_6_2_Team03.global.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminSuccessMessage {
    USER_INFO_UPDATED("유저 정보가 수정되었습니다."),
    USER_LOCKED("유저를 잠금처리하였습니다."),
    USER_UNLOCKED("유저를 잠금해제하였습니다."),
    USER_DELETED("유저를 탈퇴처리하였습니다."),
    PLACE_UPDATED("여행지 정보가 수정되었습니다.");

    private final String message;
}
