package ma.siham.grpc.repositories;

import ma.siham.grpc.entities.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository pour accéder à la base de données
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, String> {
}
