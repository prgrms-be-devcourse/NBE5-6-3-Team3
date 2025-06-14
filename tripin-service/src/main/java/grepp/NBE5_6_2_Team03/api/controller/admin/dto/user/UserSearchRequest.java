package grepp.NBE5_6_2_Team03.api.controller.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
@AllArgsConstructor
public class UserSearchRequest {
    private Boolean locked;
    private int page;
    private int size;

    public Pageable getPageable() {
        return PageRequest.of(this.page, this.size);
    }

}