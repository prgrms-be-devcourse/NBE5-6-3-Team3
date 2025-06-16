package grepp.NBE5_6_2_Team03.domain.travelplan.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.statistic.CountriesStatisticResponse;
import grepp.NBE5_6_2_Team03.api.controller.admin.dto.statistic.MonthlyStatisticResponse;
import grepp.NBE5_6_2_Team03.domain.travelplan.TravelPlan;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static grepp.NBE5_6_2_Team03.domain.travelplan.QTravelPlan.travelPlan;
import static grepp.NBE5_6_2_Team03.domain.travelschedule.QTravelSchedule.travelSchedule;

@Repository
@RequiredArgsConstructor
public class TravelPlanQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TravelPlan getTravelPlanWithSchedules(Long travelPlanId){
        return queryFactory.selectDistinct(travelPlan)
            .from(travelPlan)
            .leftJoin(travelPlan.travelSchedules, travelSchedule).fetchJoin()
            .where(travelPlan.travelPlanId.eq(travelPlanId))
            .fetchOne();
    }

    public List<CountriesStatisticResponse> getCountriesStatistics() {
        return queryFactory
            .select(Projections.constructor(CountriesStatisticResponse.class,
                travelPlan.country,
                travelPlan.travelPlanId.count()
            ))
            .from(travelPlan)
            .groupBy(travelPlan.country)
            .fetch();
    }

    public List<MonthlyStatisticResponse> getMonthStatistics() {
        // TODO Projection 공부하기
        return queryFactory
            .select(Projections.constructor(MonthlyStatisticResponse.class,
                Expressions.dateTimeTemplate(Integer.class, "month({0})", travelPlan.travelStartDate),
                travelPlan.travelPlanId.count()
            ))
            .from(travelPlan)
            .groupBy(Expressions.dateTimeTemplate(Integer.class, "month({0})", travelPlan.travelStartDate))
            .orderBy(Expressions.dateTimeTemplate(Integer.class, "month({0})", travelPlan.travelStartDate).asc())
            .fetch();
    }

}