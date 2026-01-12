package ma.siham.grpc.controllers;
import io.grpc.stub.StreamObserver;
import ma.siham.grpc.services.BankAccountService;
import ma.siham.grpc.stubs.*;
import net.devh.boot.grpc.server.service.GrpcService;
import ma.siham.grpc.stubs.BankAccountServiceGrpc;
import ma.siham.grpc.stubs.BankAccount;
import ma.siham.grpc.stubs.GetAllAccountsRequest;
import ma.siham.grpc.stubs.GetAllAccountsResponse;
import ma.siham.grpc.stubs.SaveAccountRequest;
import ma.siham.grpc.stubs.SaveAccountResponse;
import ma.siham.grpc.stubs.GetAccountByIdRequest;
import ma.siham.grpc.stubs.GetAccountByIdResponse;
import ma.siham.grpc.stubs.GetTotalBalanceRequest;
import ma.siham.grpc.stubs.GetTotalBalanceResponse;
import ma.siham.grpc.stubs.BalanceStats;
import ma.siham.grpc.stubs.AccountType;

import java.util.stream.Collectors;

@GrpcService
public class BankAccountGrpcService extends BankAccountServiceGrpc.BankAccountServiceImplBase {

    private final BankAccountService accountService;

    public BankAccountGrpcService(BankAccountService accountService) {
        this.accountService = accountService;
    }

    // Méthode pour récupérer tous les comptes
    @Override
    public void getAllAccounts(GetAllAccountsRequest request, StreamObserver<GetAllAccountsResponse> responseObserver) {
        var accounts = accountService.findAllAccounts().stream()
                .map(account -> BankAccount.newBuilder()
                        .setId(account.getId())
                        .setBalance(account.getBalance())
                        .setCreationDate(account.getCreationDate())
                        .setType(AccountType.valueOf(account.getType()))
                        .build())
                .collect(Collectors.toList());

        responseObserver.onNext(GetAllAccountsResponse.newBuilder()
                .addAllAccounts(accounts)
                .build());
        responseObserver.onCompleted();
    }

    // Méthode pour créer un compte
    @Override
    public void saveAccount(SaveAccountRequest request, StreamObserver<SaveAccountResponse> responseObserver) {
        var req = request.getAccount();

        var accountEntity = new ma.siham.grpc.entities.BankAccountEntity();
        accountEntity.setBalance(req.getBalance());
        accountEntity.setCreationDate(req.getCreationDate());
        accountEntity.setType(req.getType().name());

        var saved = accountService.saveAccount(accountEntity);

        var grpcAccount = BankAccount.newBuilder()
                .setId(saved.getId())
                .setBalance(saved.getBalance())
                .setCreationDate(saved.getCreationDate())
                .setType(AccountType.valueOf(saved.getType()))
                .build();

        responseObserver.onNext(SaveAccountResponse.newBuilder()
                .setAccount(grpcAccount)
                .build());
        responseObserver.onCompleted();
    }

    // Méthode pour récupérer un compte par ID
    @Override
    public void getAccountById(GetAccountByIdRequest request, StreamObserver<GetAccountByIdResponse> responseObserver) {
        var account = accountService.findAccountById(request.getId());

        if (account != null) {
            var grpcAccount = BankAccount.newBuilder()
                    .setId(account.getId())
                    .setBalance(account.getBalance())
                    .setCreationDate(account.getCreationDate())
                    .setType(AccountType.valueOf(account.getType()))
                    .build();

            responseObserver.onNext(GetAccountByIdResponse.newBuilder()
                    .setAccount(grpcAccount)
                    .build());
        } else {
            responseObserver.onError(new Throwable("Account not found"));
        }

        responseObserver.onCompleted();
    }

    // Méthode pour calculer les statistiques
    @Override
    public void getTotalBalance(GetTotalBalanceRequest request, StreamObserver<GetTotalBalanceResponse> responseObserver) {
        var accounts = accountService.findAllAccounts();
        int count = accounts.size();
        float sum = (float) accounts.stream().mapToDouble(ma.siham.grpc.entities.BankAccountEntity::getBalance).sum();
        float average = count > 0 ? sum / count : 0;

        var stats = BalanceStats.newBuilder()
                .setCount(count)
                .setSum(sum)
                .setAverage(average)
                .build();

        responseObserver.onNext(GetTotalBalanceResponse.newBuilder()
                .setStats(stats)
                .build());
        responseObserver.onCompleted();
    }
}
