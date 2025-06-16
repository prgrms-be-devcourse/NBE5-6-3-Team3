package grepp.NBE5_6_2_Team03.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import grepp.NBE5_6_2_Team03.api.controller.user.dto.response.QTestQueryDsl;
import grepp.NBE5_6_2_Team03.api.controller.user.dto.response.TestQueryDsl;
import grepp.NBE5_6_2_Team03.domain.user.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static grepp.NBE5_6_2_Team03.domain.user.QUser.*;

@Repository
public class UserQueryRepository {

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public UserQueryRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public TestQueryDsl queryDslTest(Long userId){
        return queryFactory.select(
                new QTestQueryDsl(user.email, user.name, user.phoneNumber))
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();
    }

    public Page<User> findUsersPage(Boolean isLocked, Pageable pageable) {

        BooleanExpression lockStatus = createLockStatusCondition(isLocked);

        List<User> users = queryFactory
            .selectFrom(user)
            .where(lockStatus)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .selectFrom(user)
            .where(lockStatus)
            .fetchCount();

        return new PageImpl<>(users, pageable, total);
    }

    private BooleanExpression createLockStatusCondition(Boolean isLocked) {
        return Optional.ofNullable(isLocked)
            .map(user.isLocked::eq)
            .orElse(null);

    }
}
