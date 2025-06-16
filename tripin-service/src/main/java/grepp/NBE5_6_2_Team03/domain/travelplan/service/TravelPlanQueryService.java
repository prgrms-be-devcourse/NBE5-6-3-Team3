package grepp.NBE5_6_2_Team03.domain.travelplan.service;

import grepp.NBE5_6_2_Team03.api.controller.travelplan.dto.response.TravelPlanAdjustResponse;
import grepp.NBE5_6_2_Team03.api.controller.travelplan.dto.response.TravelScheduleExpenseInfo;
import grepp.NBE5_6_2_Team03.domain.exchange.service.ExchangeService;
import grepp.NBE5_6_2_Team03.domain.travelplan.TravelPlan;
import grepp.NBE5_6_2_Team03.domain.travelplan.repository.TravelPlanQueryRepository;
import grepp.NBE5_6_2_Team03.domain.travelschedule.ScheduleStatus;
import grepp.NBE5_6_2_Team03.domain.travelschedule.TravelSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TravelPlanQueryService {

    private final TravelPlanQueryRepository planQueryRepository;
    private final ExchangeService exchangeService;

    public TravelPlanAdjustResponse getAdjustmentInfo(Long travelPlanId){
        TravelPlan travelPlan = planQueryRepository.getTravelPlanWithSchedules(travelPlanId);
        List<TravelSchedule> completedSchedules = travelPlan.getTravelSchedules().stream()
            .filter(s -> s.getScheduleStatus() == ScheduleStatus.COMPLETED)
            .collect(Collectors.toList());

        String curUnit = travelPlan.getCurrentUnit().getUnit();
        int sumExpenses = getSumExpenses(completedSchedules);
        int applicants = travelPlan.getApplicants();
        int remainMoney = getRemainMoney(travelPlan.getPublicMoney(), completedSchedules);

        int adjustmentAmount = Math.abs(remainMoney);
        boolean needToPay = remainMoney < 0;

        int lastestExchangeRate = exchangeService.getLatestExchangeRateInt(curUnit);
        int remainMoneyWon  = travelPlan.getCurrentUnit().isKRW() ? adjustmentAmount : exchangeService.exchangeToWon(curUnit, adjustmentAmount);
        int remainMoneyForeign = travelPlan.getCurrentUnit().isKRW() ? exchangeService.exchangeToForeign(curUnit, adjustmentAmount) : adjustmentAmount;

        int personalPrice = adjustmentAmount / applicants;
        int personalPriceWon = travelPlan.getCurrentUnit().isKRW() ? personalPrice : exchangeService.exchangeToWon(curUnit, personalPrice);
        int personalPriceForeign = travelPlan.getCurrentUnit().isKRW() ? exchangeService.exchangeToForeign(curUnit, personalPrice) : personalPrice;

        int rateCompareResult = exchangeService.compareLatestRateToAverageRate(curUnit);

        List<TravelScheduleExpenseInfo> expenses = TravelScheduleExpenseInfo.convertBy(completedSchedules);

        return TravelPlanAdjustResponse.of(expenses, travelPlan, sumExpenses, lastestExchangeRate, rateCompareResult,
                                           remainMoneyWon, remainMoneyForeign, needToPay, personalPriceWon, personalPriceForeign);
    }

    private int getRemainMoney(int publicMoney, List<TravelSchedule> schedules) {
        return publicMoney - getSumExpenses(schedules);
    }

    private int getSumExpenses(List<TravelSchedule> schedules) {
        return schedules.stream()
            .mapToInt(TravelSchedule::getExpense)
            .sum();
    }
}
