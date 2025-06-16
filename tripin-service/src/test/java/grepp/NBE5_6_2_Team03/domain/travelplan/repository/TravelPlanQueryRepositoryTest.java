package grepp.NBE5_6_2_Team03.domain.travelplan.repository;

import grepp.NBE5_6_2_Team03.domain.expense.Expense;
import grepp.NBE5_6_2_Team03.domain.expense.repository.ExpenseRepository;
import grepp.NBE5_6_2_Team03.domain.travelplan.Country;

import grepp.NBE5_6_2_Team03.domain.travelplan.TravelPlan;
import grepp.NBE5_6_2_Team03.domain.travelschedule.TravelRoute;
import grepp.NBE5_6_2_Team03.domain.travelschedule.TravelSchedule;
import grepp.NBE5_6_2_Team03.domain.travelschedule.repository.TravelScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class TravelPlanQueryRepositoryTest {

    @Autowired
    private TravelPlanRepository travelPlanRepository;

    @Autowired
    private TravelPlanQueryRepository travelPlanQueryRepository;

    @Autowired
    private TravelScheduleRepository travelScheduleRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @DisplayName("여행 계획과 연관된 일정, 지출을 조회 한다.")
    @Test
    void getTravelPlanFetchScheduleAndExpense() {
        //given
        TravelPlan travelPlan = createTravelPlan(Country.JAPAN, "여행 계획", 10000, 2);
        travelPlanRepository.save(travelPlan);

        TravelRoute travelRoute = createTravelRoute();
        TravelSchedule travelSchedule = createTravelSchedule(travelPlan, travelRoute, "일정 내용1", "장소 이름1");
        TravelSchedule travelSchedule2 = createTravelSchedule(travelPlan, travelRoute, "일정 내용2", "장소 이름2");
        TravelSchedule travelSchedule3 = createTravelSchedule(travelPlan, travelRoute, "일정 내용3", "장소 이름3");
        travelScheduleRepository.saveAll(List.of(travelSchedule, travelSchedule2, travelSchedule3));

        Expense expense = createExpense(travelSchedule, 1000);
        Expense expense2 = createExpense(travelSchedule2, 2000);
        expenseRepository.saveAll(List.of(expense, expense2));

        //when
        TravelPlan findTravelPlan = travelPlanQueryRepository.getTravelPlanWithSchedules(travelPlan.getTravelPlanId());

        //then
        assertThat(findTravelPlan).isNotNull();

        List<TravelSchedule> travelSchedules = findTravelPlan.getTravelSchedules();
        assertThat(travelSchedules).hasSize(3);
        assertThat(travelSchedules)
                .extracting(TravelSchedule::getExpense)
                .filteredOn(Objects::nonNull)
                .hasSize(2);
    }

    private TravelRoute createTravelRoute() {
        return new TravelRoute("출발지", "목적지", "이동수단");
    }

    private TravelPlan createTravelPlan(Country country, String name, int publicMoney, int applicants){
        return TravelPlan.builder()
                .country(country)
                .name(name)
                .publicMoney(publicMoney)
                .applicants(applicants)
                .build();
    }

    private TravelSchedule createTravelSchedule(TravelPlan travelPlan, TravelRoute travelRoute, String content, String placeName){
       return TravelSchedule.builder()
                .travelPlan(travelPlan)
                .travelRoute(travelRoute)
                .content(content)
                .placeName(placeName)
                .build();
    }

    private Expense createExpense(TravelSchedule travelSchedule, int payedPrice){
        return Expense.builder()
                .travelSchedule(travelSchedule)
                .payedPrice(payedPrice)
                .build();
    }
}