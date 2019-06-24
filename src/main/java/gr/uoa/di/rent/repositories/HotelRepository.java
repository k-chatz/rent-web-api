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

    @Query(value = "SELECT DISTINCT h.*\n" +
            "FROM hotels h\n" +
            "         INNER JOIN (rooms r FULL OUTER JOIN calendars c ON r.id = c.room) ON h.id = r.hotel\n" +
            "WHERE (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61\n" +
            "  AND r.capacity >= :visitors\n" +
            "  AND r.price >= :minPrice\n" +
            "  AND r.price <= :maxPrice\n" +
            "  AND (\n" +
            "      c.start_date IS NULL OR c.end_date IS NULL OR (\n" +
            "          (:startDate < c.start_date AND :endDate < c.start_date) OR\n" +
            "     (:startDate > c.end_date AND :endDate > c.end_date)))\n" +
            "GROUP BY h.id, r.id"
            , nativeQuery = true)
    Page<Hotel> findWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice,
            Pageable pageable
    );

    @Query(value = "SELECT DISTINCT h.*\n" +
            "FROM hotels h\n" +
            "         INNER JOIN (rooms r FULL OUTER JOIN calendars c ON r.id = c.room) ON h.id = r.hotel\n" +
            "WHERE (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61\n" +
            "  AND r.capacity >= :visitors\n" +
            "  AND r.price >= :minPrice\n" +
            "  AND r.price <= :maxPrice\n" +
            "  AND (\n" +
            "      c.start_date IS NULL OR c.end_date IS NULL OR (\n" +
            "          (:startDate < c.start_date AND :endDate < c.start_date) OR\n" +
            "     (:startDate > c.end_date AND :endDate > c.end_date)))\n" +
            "GROUP BY h.id, r.id"
            , nativeQuery = true)
    List<Hotel> findWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice
    );

    @Query(value = "SELECT DISTINCT h.*\n" +
            "FROM hotels h\n" +
            "         INNER JOIN (rooms r FULL OUTER JOIN calendars c ON r.id = c.room) ON h.id = r.hotel,\n" +
            "     hotel_amenities ha,\n" +
            "     amenities a\n" +
            "WHERE (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61\n" +
            "  AND r.capacity >= :visitors\n" +
            "  AND r.price >= :minPrice\n" +
            "  AND r.price <= :maxPrice\n" +
            "  AND (\n" +
            "      c.start_date IS NULL OR c.end_date IS NULL OR (\n" +
            "          (:startDate < c.start_date AND :endDate < c.start_date) OR\n" +
            "     (:startDate > c.end_date AND :endDate > c.end_date)))\n" +
            "  AND ha.amenity_id = a.id\n" +
            "  AND ha.hotel_id = h.id\n" +
            "  AND a.name in :amenities\n" +
            "GROUP BY h.id, r.id\n" +
            "HAVING COUNT(DISTINCT a.id) = :amenities_count"
            , nativeQuery = true)
    Page<Hotel> findWithAmenityFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius_km") double radius_km,
            @Param("visitors") int visitors,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice,
            @Param("amenities") List<String> amenities,
            @Param("amenities_count") int amenities_count,
            Pageable pageable
    );


    @Query(value = "SELECT DISTINCT h.*\n" +
            "FROM hotels h\n" +
            "         INNER JOIN (rooms r FULL OUTER JOIN calendars c ON r.id = c.room) ON h.id = r.hotel,\n" +
            "     hotel_amenities ha,\n" +
            "     amenities a\n" +
            "WHERE (point(:longitude, :latitude) <@> point(lng, lat)) < :radius_km / 1.61\n" +
            "  AND r.capacity >= :visitors\n" +
            "  AND r.price >= :minPrice\n" +
            "  AND r.price <= :maxPrice\n" +
            "  AND (\n" +
            "      c.start_date IS NULL OR c.end_date IS NULL OR (\n" +
            "          (:startDate < c.start_date AND :endDate < c.start_date) OR\n" +
            "     (:startDate > c.end_date AND :endDate > c.end_date)))\n" +
            "  AND ha.amenity_id = a.id\n" +
            "  AND ha.hotel_id = h.id\n" +
            "  AND a.name in :amenities\n" +
            "GROUP BY h.id, r.id\n" +
            "HAVING COUNT(DISTINCT a.id) = :amenities_count"
            , nativeQuery = true)
    List<Hotel> findWithAmenityFilters(
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
}
