package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Prices;
import gr.uoa.di.rent.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelPricesRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT MIN(price) AS min, MAX(price) as max\n" +
            "FROM    rooms r\n" +
            "WHERE   r.id IN (SELECT r.id\n" +
            "                    FROM rooms r\n" +
            "                             FULL OUTER JOIN calendars c ON r.id = c.room,\n" +
            "                         hotels h,\n" +
            "                         hotel_amenities ha,\n" +
            "                         amenities a\n" +
            "                    WHERE h.id = r.hotel\n" +
            "                      AND (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61\n" +
            "                      AND r.capacity >= :visitors\n" +
            "                      AND (\n" +
            "                            c.start_date IS NULL OR c.end_date IS NULL OR (\n" +
            "                                (:startDate < c.start_date AND :endDate < c.start_date) OR\n" +
            "                                (:startDate > c.end_date AND :endDate > c.end_date)\n" +
            "                            )\n" +
            "                        )\n" +
            "                      AND ha.amenity_id = a.id\n" +
            "                      AND ha.hotel_id = h.id\n" +
            "                      AND a.name in :amenities\n" +
            "                    GROUP BY h.id, r.id\n" +
            "                    HAVING COUNT(DISTINCT a.id) = :amenities_count)\n"
            , nativeQuery = true)
    Prices findHotelPricesWithAmenityFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            @Param("amenities") List<String> amenities,
            @Param("amenities_count") int amenities_count
    );

    @Query(value = "SELECT MIN(price) AS min, MAX(price) as max\n" +
            "FROM    rooms r\n" +
            "WHERE   r.id IN (SELECT r.id\n" +
            "                    FROM rooms r\n" +
            "                             FULL OUTER JOIN calendars c ON r.id = c.room,\n" +
            "                         hotels h" +
            "                    WHERE h.id = r.hotel\n" +
            "                      AND (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61\n" +
            "                      AND r.capacity >= :visitors\n" +
            "                      AND (\n" +
            "                            c.start_date IS NULL OR c.end_date IS NULL OR (\n" +
            "                                (:startDate < c.start_date AND :endDate < c.start_date) OR\n" +
            "                                (:startDate > c.end_date AND :endDate > c.end_date)\n" +
            "                            )\n" +
            "                        )\n" +
            "                    GROUP BY h.id, r.id)\n"
            , nativeQuery = true)
    Prices findHotelPricesWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors
    );
}
