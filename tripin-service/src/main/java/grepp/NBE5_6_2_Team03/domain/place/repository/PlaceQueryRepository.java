package grepp.NBE5_6_2_Team03.domain.place.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import grepp.NBE5_6_2_Team03.domain.place.entity.Place;
import grepp.NBE5_6_2_Team03.domain.place.entity.QPlace;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlaceQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QPlace place = QPlace.place;

    public Page<Place> findPlacesPage(String country, String city, String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (country != null && !country.isBlank()) {
            builder.and(place.country.eq(country));
        }
        if (city != null && !city.isBlank()) {
            builder.and(place.city.eq(city));
        }
        if (keyword != null && !keyword.isBlank()) {
            builder.and(place.placeName.contains(keyword));
        }

        List<Place> content = jpaQueryFactory
            .selectFrom(place)
            .where(builder)
            .orderBy(place.country.asc(), place.city.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = jpaQueryFactory
            .selectFrom(place)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

}
