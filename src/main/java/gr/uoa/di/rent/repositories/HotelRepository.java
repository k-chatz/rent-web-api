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
    @Query(value="update wallets\n" +
            "    set balance  = wallets.balance + :amount\n" +
            "    from wallets w , hotels h , businesses b\n" +
            "    where h.id = :hotelID and h.business = b.id and b.wallet = wallets.id\n", nativeQuery = true)
    int transferMoney(@Param("hotelID") Long hotelID,@Param("amount") Double amount );
}



//    //Update pending provider to true.
//    @Transactional
//    @Modifying
//    @Query(value="UPDATE users SET pending_provider = true WHERE id = :user_id", nativeQuery = true)
//    int updatePendingProvider(@Param("user_id") Long user_id);