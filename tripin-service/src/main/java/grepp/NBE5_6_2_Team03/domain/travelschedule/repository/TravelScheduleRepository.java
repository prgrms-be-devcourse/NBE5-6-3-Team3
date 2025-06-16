package grepp.NBE5_6_2_Team03.domain.travelschedule.repository;

import grepp.NBE5_6_2_Team03.domain.travelplan.TravelPlan;
import grepp.NBE5_6_2_Team03.domain.travelschedule.TravelSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TravelScheduleRepository extends JpaRepository<TravelSchedule, Long> {

    @Query("SELECT s FROM TravelSchedule s " +
        "WHERE s.travelPlan = :plan " +
        "ORDER BY s.travelScheduleDate ASC, s.scheduleStatus DESC")
    List<TravelSchedule> findSortedSchedules(@Param("plan") TravelPlan plan);

    @Query("select coalesce(sum(s.expense), 0) from TravelSchedule s where s.travelPlan.travelPlanId = :travelPlanId")
    int sumPriceByPlanId(@Param("travelPlanId") Long travelPlanId);

    @Query("SELECT s FROM TravelSchedule s JOIN FETCH s.travelPlan WHERE s.travelScheduleId = :id")
    Optional<TravelSchedule> findByIdWithTravelPlan(@Param("id") Long id);
}
