package ma.siham.grpc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private float balance;
    private String creationDate;
    private String type;

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
