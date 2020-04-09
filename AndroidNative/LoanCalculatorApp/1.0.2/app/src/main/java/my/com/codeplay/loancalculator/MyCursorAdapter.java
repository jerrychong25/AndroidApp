package my.com.codeplay.loancalculator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter{

    public MyCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_listview, parent, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tv_monthlyRepayment = (TextView) view.findViewById(R.id.monthly_repayment);
        viewHolder.tv_totalRepayment = (TextView) view.findViewById(R.id.total_repayment);
        viewHolder.tv_totalInterest = (TextView) view.findViewById(R.id.total_interest);
        viewHolder.tv_monthlyInterest = (TextView) view.findViewById(R.id.average_monthly_interest);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.tv_monthlyRepayment.setText(cursor.getString(1));
        viewHolder.tv_totalRepayment.setText(cursor.getString(2));
        viewHolder.tv_totalInterest.setText(cursor.getString(3));
        viewHolder.tv_monthlyInterest.setText(cursor.getString(4));
    }
    public class ViewHolder {
        public TextView tv_monthlyRepayment;
        public TextView tv_totalRepayment;
        public TextView tv_totalInterest;
        public TextView tv_monthlyInterest;
    }
}
