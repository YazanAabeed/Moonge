package moonge_objects;

import java.util.Date;

/**
 * Created by yazan on 20/01/16.
 */
public class invoices {
    public String objectId;
    public int amount;
    public Date pay_date;
    public String category;

    public invoices(int amount, String category, String objectId, Date pay_date) {
        this.amount = amount;
        this.category = category;
        this.objectId = objectId;
        this.pay_date = pay_date;
    }

    public Date getPay_date() {
        return pay_date;
    }

    public String getCategory() {
        return category;
    }

    public String getObjectId() {
        return objectId;
    }

    public int getAmount() {
        return amount;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPay_date(Date pay_date) {
        this.pay_date = pay_date;
    }
}
