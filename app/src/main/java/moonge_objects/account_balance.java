package moonge_objects;

/**
 * Created by yazan on 07/01/16.
 */
public class account_balance {
    private int amount;
    private String name;
    private String objectId;

    public account_balance(int amount, String name, String objectId) {
        this.amount = amount;
        this.name = name;
        this.objectId = objectId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getObjectId() {
        return objectId;
    }
}
