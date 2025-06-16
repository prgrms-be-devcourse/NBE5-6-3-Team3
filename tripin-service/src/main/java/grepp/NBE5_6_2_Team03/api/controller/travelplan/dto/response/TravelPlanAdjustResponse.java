package grepp.NBE5_6_2_Team03.api.controller.travelplan.dto.response;

import grepp.NBE5_6_2_Team03.domain.travelplan.TravelPlan;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TravelPlanAdjustResponse {

    private final List<TravelScheduleExpenseInfo> expenses;
    private final String country;
    private final String curUnit;
    private final int publicMoney;
    private final int applicants;
    private final int sumExpenses;
    private final int lastestExchangeRate;
    private final int rateCompareResult;
    private final int remainMoneyWon;
    private final int remainMoneyForeign;
    private final boolean needToPay;
    private final int personalPriceWon;
    private final int personalPriceForeign;

    @Builder
    private TravelPlanAdjustResponse(List<TravelScheduleExpenseInfo> expenses, String country, String curUnit, int publicMoney,
                                     int applicants, int sumExpenses, int lastestExchangeRate, int rateCompareResult, int remainMoneyWon,
                                     int remainMoneyForeign, boolean needToPay, int personalPriceWon, int personalPriceForeign) {
        this.expenses = expenses;
        this.country = country;
        this.curUnit = curUnit;
        this.publicMoney = publicMoney;
        this.applicants = applicants;
        this.sumExpenses = sumExpenses;
        this.lastestExchangeRate = lastestExchangeRate;
        this.rateCompareResult = rateCompareResult;
        this.remainMoneyWon = remainMoneyWon;
        this.remainMoneyForeign = remainMoneyForeign;
        this.needToPay = needToPay;
        this.personalPriceWon = personalPriceWon;
        this.personalPriceForeign = personalPriceForeign;
    }

    public static TravelPlanAdjustResponse of(List<TravelScheduleExpenseInfo> expenseInfos, TravelPlan travelPlan, int sumExpenses, int lastestExchangeRate,
                                              int rateCompareResult, int remainMoneyWon, int remainMoneyForeign, boolean needToPay, int personalPriceWon, int personalPriceForeign) {
        return TravelPlanAdjustResponse.builder()
            .expenses(expenseInfos)
            .country(travelPlan.getCountry().getCountryName())
            .curUnit(travelPlan.getCurrentUnit().getUnit())
            .publicMoney(travelPlan.getPublicMoney())
            .applicants(travelPlan.getApplicants())
            .sumExpenses(sumExpenses)
            .lastestExchangeRate(lastestExchangeRate)
            .rateCompareResult(rateCompareResult)
            .remainMoneyWon(remainMoneyWon)
            .remainMoneyForeign(remainMoneyForeign)
            .needToPay(needToPay)
            .personalPriceWon(personalPriceWon)
            .personalPriceForeign(personalPriceForeign)
            .build();
    }
}
