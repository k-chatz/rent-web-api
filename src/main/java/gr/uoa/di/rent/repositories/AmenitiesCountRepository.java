package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.AmenitiesCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AmenitiesCountRepository extends JpaRepository<AmenitiesCount, Long> {

    @Query(value = "SELECT b.name as amenity, COUNT(a.hotel_id)\n" +
            "FROM hotel_amenities a INNER JOIN amenities b ON a.amenity_id = b.id\n" +
            "WHERE a.hotel_id in(\n" +
            "SELECT h.id\n" +
            "FROM hotels h,\n" +
            "    hotel_amenities ha,\n" +
            "    amenities a\n" +
            "WHERE\n" +
            "        h.id in\n" +
            "        (\n" +
            "            SELECT hotelsfilter.id\n" +
            "            FROM hotels hotelsfilter\n" +
            "            WHERE\n" +
            "                    hotelsfilter.id in\n" +
            "                    -- 1) GEOLOCATION RADIUS SEARCH\n" +
            "                    (\n" +
            "                        SELECT h1.id\n" +
            "                        FROM hotels h1\n" +
            "                        WHERE (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61\n" +
            "                    )\n" +
            "              -- 2) VISITORS + CALENDAR AVAILABILITY\n" +
            "                    and\n" +
            "                    hotelsfilter.id in (\n" +
            "                            SELECT DISTINCT h2.id\n" +
            "                            FROM hotels h2 , rooms r1\n" +
            "\n" +
            "                            WHERE\n" +
            "                                    r1.hotel = h2.id\n" +
            "                              and\n" +
            "                                    r1.id in (\n" +
            "                                    SELECT r.id\n" +
            "                                    FROM rooms r\n" +
            "                                    WHERE\n" +
            "                                      r.capacity >= :visitors\n" +
            "                                      AND r.price >= :minPrice\n" +
            "                                      AND r.price <= :maxPrice\n" +
            "                                      AND\n" +
            "                                            r.id NOT IN (\n" +
            "                                            SELECT r.id\n" +
            "                                            FROM calendars c\n" +
            "                                            WHERE\n" +
            "                                                    c.room = r.id\n" +
            "                                              AND\n" +
            "                                                (((c.start_date <= :startDate AND :endDate <= c.end_date)\n" +
            "                                                    OR (c.start_date <= :endDate AND :endDate <= c.end_date)\n" +
            "                                                    OR (:startDate < end_date AND :endDate >= c.end_date)))\n" +
            "\n" +
            "                                            LIMIT 1  ---maybe not necessary\n" +
            "                                        )\n" +
            "                                )\n" +
            "                    )\n" +
            "\n" +
            "        )\n" +
            "\n" +
            "  -- 3) AMENITY SEARCH\n" +
            "  AND ha.amenity_id = a.id\n" +
            "  AND ha.hotel_id = h.id\n" +
            "  AND a.name in :amenities\n" +
            "\n" +
            "\n" +
            "GROUP BY h.id\n" +
            "HAVING COUNT(DISTINCT a.id) = :amenities_count" +
            ")\n" +
            "GROUP BY b.id"
            , nativeQuery = true)
    List<AmenitiesCount> findAmenitiesCountWithAmenityFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice,
            @Param("amenities") List<String> amenities,
            @Param("amenities_count") int amenities_count
    );

    @Query(value = "SELECT b.name as amenity, COUNT(a.hotel_id)\n" +
            "FROM hotel_amenities a INNER JOIN amenities b ON a.amenity_id = b.id\n" +
            "WHERE a.hotel_id in(\n" +
            "SELECT h.id\n" +
            "FROM hotels h\n" +
            "WHERE\n" +
            "        h.id in\n" +
            "        (\n" +
            "            SELECT hotelsfilter.id\n" +
            "            FROM hotels hotelsfilter\n" +
            "            WHERE\n" +
            "                    hotelsfilter.id in\n" +
            "                    -- 1) GEOLOCATION RADIUS SEARCH\n" +
            "                    (\n" +
            "                        SELECT h1.id\n" +
            "                        FROM hotels h1\n" +
            "                        WHERE (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61\n" +
            "                    )\n" +
            "              -- 2) VISITORS + CALENDAR AVAILABILITY\n" +
            "                    and\n" +
            "                    hotelsfilter.id in (\n" +
            "                            SELECT DISTINCT h2.id\n" +
            "                            FROM hotels h2 , rooms r1\n" +
            "\n" +
            "                            WHERE\n" +
            "                                    r1.hotel = h2.id\n" +
            "                              and\n" +
            "                                    r1.id in (\n" +
            "                                    SELECT r.id\n" +
            "                                    FROM rooms r\n" +
            "                                    WHERE\n" +
            "                                            r.capacity >= :visitors\n" +
            "                                      AND r.price >= :minPrice\n" +
            "                                      AND r.price <= :maxPrice\n" +
            "                                      AND\n" +
            "                                            r.id NOT IN (\n" +
            "                                            SELECT r.id\n" +
            "                                            FROM calendars c\n" +
            "                                            WHERE\n" +
            "                                                    c.room = r.id\n" +
            "                                              AND\n" +
            "                                                (((c.start_date <= :startDate AND :endDate <= c.end_date)\n" +
            "                                                    OR (c.start_date <= :endDate AND :endDate <= c.end_date)\n" +
            "                                                    OR (:startDate < end_date AND :endDate >= c.end_date)))\n" +
            "\n" +
            "                                            LIMIT 1  ---maybe not necessary\n" +
            "                                        )\n" +
            "                                )\n" +
            "                    )\n" +
            "\n" +
            "        )\n" +
            "GROUP BY h.id\n" +
            ")\n" +
            "GROUP BY b.id"
            , nativeQuery = true)
    List<AmenitiesCount> findAmenitiesCountWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice
    );
}
