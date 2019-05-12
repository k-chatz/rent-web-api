package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findAll();

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
    int transferMoney(@Param("userID") Long userID, @Param("hotelID") Long hotelID, @Param("amount") Double amount);
}


//    //Update pending provider to true.
//    @Transactional
//    @Modifying
//    @Query(value="UPDATE users SET pending_provider = true WHERE id = :user_id", nativeQuery = true)
//    int updatePendingProvider(@Param("user_id") Long user_id);