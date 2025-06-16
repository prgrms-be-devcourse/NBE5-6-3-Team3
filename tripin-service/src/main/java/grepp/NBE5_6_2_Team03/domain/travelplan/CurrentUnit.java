package grepp.NBE5_6_2_Team03.domain.travelplan;

import lombok.Getter;

@Getter
public enum CurrentUnit {
    KOREA("한국", "KRW"),
    JAPAN("일본", "JPY(100)"),
    THAILAND("태국", "THB");

    private final String country;
    private final String unit;

    CurrentUnit(String country, String unit) {
        this.country = country;
        this.unit = unit;
    }

    public boolean isKRW() {
        return this.unit.equals("KRW");
    }
}
