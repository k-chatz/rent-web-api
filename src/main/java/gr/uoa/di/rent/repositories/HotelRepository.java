package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findAll();

    Page<Hotel> findAll(Pageable pageable);

    Hotel findById(long id);

    @Transactional
    @Modifying
    @Query(value = "BEGIN TRANSACTION;\n" +
            "update wallets\n" +
            "set balance = balance - :amount\n" +
            "from users u\n" +
            "where u.id = :userID\n" +
            "  and u.wallet = wallets.id;\n" +
            "\n" +
            "update wallets\n" +
            "set balance = balance + :amount * 99 / 100\n" +
            "from hotels h,\n" +
            "     businesses b\n" +
            "where h.id = :hotelID\n" +
            "  and h.business = b.id\n" +
            "  and b.wallet = wallets.id;\n" +
            "\n" +
            "update wallets\n" +
            "set balance = balance + :amount * 1 / 100\n" +
            "from businesses b\n" +
            "where b.id = 1\n" +
            "  and b.wallet = wallets.id;\n" +
            "\n" +
            "COMMIT;", nativeQuery = true)
    void transferMoney(@Param("userID") Long userID, @Param("hotelID") Long hotelID, @Param("amount") Double amount);


    @Query(value = "SELECT h.*\n" +
            "FROM hotels as h,\n" +
            "       hotel_amenities as ha,\n" +
            "       amenities as a\n" +
            "       WHERE ha.amenity_id = a.id\n" +
            "       and ha.hotel_id = h.id\n" +
            "       AND a.name in :amenities\n" +
            "       GROUP BY h.id\n" +
            "       HAVING COUNT(DISTINCT a.id) = :amenities_count",
            countQuery = "SELECT COUNT(h.*)\n" +
                    "FROM hotels as h,\n" +
                    "       hotel_amenities as ha,\n" +
                    "       amenities as a\n" +
                    "       WHERE ha.amenity_id = a.id\n" +
                    "       AND ha.hotel_id = h.id\n" +
                    "       AND a.name in :amenities\n" +
                    "       GROUP BY h.id\n" +
                    "       HAVING COUNT(DISTINCT a.id) = :amenities_count",
            nativeQuery = true)
    Page<Hotel> findAllHotelsByAmenities(@Param("amenities") List<String> amenities,
                                         @Param("amenities_count") int amenities_count,
                                         Pageable pageable);

    @Query(value =
            "SELECT h.*\n" +
                    "FROM hotels h\n" +
                    "WHERE (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61",
            countQuery = "SELECT COUNT(h.*)\n" +
                    "FROM hotels h\n" +
                    "WHERE (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61",
            nativeQuery = true)
    Page<Hotel> findByLocationAndRadius(@Param("longitude") double longitude, @Param("latitude") double latitude, @Param("radius_km") double radius_km, Pageable pageable);

    @Query(value =
            "SELECT h.*\n" +
                    "FROM hotels h\n" +
                    "WHERE h.id in\n" +
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
                    "GROUP BY h.id"
            , nativeQuery = true)
    Page<Hotel> findWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            Pageable pageable
    );

    @Query(value =
            "SELECT h.*\n" +
                    "FROM hotels h\n" +
                    "WHERE h.id in\n" +
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
                    "GROUP BY h.id"
            , nativeQuery = true)
    List<Hotel> findWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors
    );

    @Query(value =
            "SELECT h.*\n" +
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
                    "                                            r.capacity >= :visitors\n" +
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
                    "HAVING COUNT(DISTINCT a.id) = :amenities_count"
            , nativeQuery = true)
    Page<Hotel> findWithAmenityFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            @Param("amenities") List<String> amenities,
            @Param("amenities_count") int amenities_count,
            Pageable pageable
    );


    @Query(value =
            "SELECT h.*\n" +
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
                    "                                            r.capacity >= :visitors\n" +
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
                    "HAVING COUNT(DISTINCT a.id) = :amenities_count"
            , nativeQuery = true)
    List<Hotel> findWithAmenityFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            @Param("amenities") List<String> amenities,
            @Param("amenities_count") int amenities_count
    );
}
