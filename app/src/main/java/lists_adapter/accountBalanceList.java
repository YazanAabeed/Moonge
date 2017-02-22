package lists_adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import v1.moonge.moonge.R;

/**
 * Created by yazan on 07/01/16.
 */
public class accountBalanceList extends ArrayAdapter<moonge_objects.account_balance> {

    ArrayList<moonge_objects.account_balance> data;
    private final Activity context;

    public accountBalanceList(Activity context, ArrayList<moonge_objects.account_balance> data) {
        super(context, -1, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_account_balance_item, null, true);

        TextView name = (TextView) rowView.findViewById(R.id.name_account);
        name.setText(data.get(position).getName());

        TextView amount = (TextView) rowView.findViewById(R.id.account_amount);
        amount.setText(Integer.toString(data.get(position).getAmount()));

        return rowView;
    }

}
