package ma.siham.grpc.services;

import ma.siham.grpc.entities.BankAccountEntity;
import ma.siham.grpc.repositories.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    // Récupérer tous les comptes
    public List<BankAccountEntity> findAllAccounts() {
        return repository.findAll();
    }

    // Récupérer un compte par ID
    public BankAccountEntity findAccountById(String id) {
        return repository.findById(id).orElse(null);
    }

    // Sauvegarder un compte (création ou mise à jour)
    public BankAccountEntity saveAccount(BankAccountEntity account) {
        return repository.save(account);
    }
}
