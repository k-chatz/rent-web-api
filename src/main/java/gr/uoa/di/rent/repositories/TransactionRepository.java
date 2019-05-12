package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Business;
import gr.uoa.di.rent.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
