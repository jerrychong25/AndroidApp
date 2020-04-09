package my.com.codeplay.loancalculator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.pow;

import static my.com.codeplay.loancalculator.Database.COL_MONTHLYPAYMENT;
import static my.com.codeplay.loancalculator.Database.COL_TOTALREPAYMENT;
import static my.com.codeplay.loancalculator.Database.COL_TOTALINTEREST;
import static my.com.codeplay.loancalculator.Database.COL_AVERAGEMONTHLYINTEREST;

public class CalculatorActivity extends AppCompatActivity {

    private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
    private TextView tvMonthlyPayment, tvTotalRepayment, tvTotalInterest, tvAverageMonthlyInterest;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sp;
    public Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        etLoanAmount = (EditText) findViewById(R.id.loan_amount);
        etDownPayment = (EditText) findViewById(R.id.down_payment);
        etTerm = (EditText) findViewById(R.id.term);
        etAnnualInterestRate = (EditText) findViewById(R.id.annual_interest_rate);

        // Set Previous Data If Previous Data is Detected
        String previous_loanAmount = sp.getString("LoanAmount", "");
        String previous_downPayment = sp.getString("DownPayment", "");
        String previous_interestRate = sp.getString("AnnualInterestRate", "");
        String previous_term = sp.getString("Term", "");
        etLoanAmount.setText(previous_loanAmount);
        etDownPayment.setText(previous_downPayment);
        etAnnualInterestRate.setText(previous_interestRate);
        etTerm.setText(previous_term);

        tvMonthlyPayment = (TextView) findViewById(R.id.monthly_repayment);
        tvTotalRepayment = (TextView) findViewById(R.id.total_repayment);
        tvTotalInterest = (TextView) findViewById(R.id.total_interest);
        tvAverageMonthlyInterest = (TextView) findViewById(R.id.average_monthly_interest);

        // Set Previous Data If Previous Data is Detected
        String previous_monthlyRepayment = sp.getString("MonthlyRepayment", "");
        String previous_totalRepayment = sp.getString("TotalRepayment", "");
        String previous_totalInterest = sp.getString("TotalInterest", "");
        String previous_monthlyInterest = sp.getString("MonthlyInterest", "");

        tvMonthlyPayment.setText(previous_monthlyRepayment);
        tvTotalRepayment.setText(previous_totalRepayment);
        tvTotalInterest.setText(previous_totalInterest);
        tvAverageMonthlyInterest.setText(previous_monthlyInterest);
    }

	@Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (db == null)
            db = new Database(this);
    }
	
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button_calculate:
                Log.d("Check", "Calculate Button Clicked!");
                calculate();
                break;
            case R.id.button_reset:
                Log.d("Check", "Reset Button Clicked!");
                reset();
                break;
//            case R.id.button_history:
//                Log.d("Check", "History Button Clicked!");
//                history();
//                break;
        }
    }

    private void calculate () {
        String amount = etLoanAmount.getText().toString();
        String downPayment = etDownPayment.getText().toString();
        String interestRate = etAnnualInterestRate.getText().toString();
        String term = etTerm.getText().toString();

        double loanAmount = Double.parseDouble(amount) - Double.parseDouble(downPayment);
        double interest = Double.parseDouble(interestRate)/12/100;
        double noOfMonth = (Integer.parseInt(term) * 12);

        double monthlyRepayment = loanAmount * (interest + (interest/(pow((1+interest), noOfMonth) - 1)));
        double totalRepayment = monthlyRepayment * noOfMonth;
        double totalInterest = totalRepayment - loanAmount;
        double monthlyInterest = totalInterest / noOfMonth;

        if(noOfMonth > 0) {
            tvMonthlyPayment.setText(String.format("%.2f", monthlyRepayment));
            tvTotalRepayment.setText(String.format("%.2f", totalRepayment));
            tvTotalInterest.setText(String.format("%.2f", totalInterest));
            tvAverageMonthlyInterest.setText(String.format("%.2f", monthlyInterest));
        }

		// Store Database
		ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MONTHLYPAYMENT, tvMonthlyPayment.getText().toString());
        contentValues.put(COL_TOTALREPAYMENT, tvTotalRepayment.getText().toString());
        contentValues.put(COL_TOTALINTEREST, tvTotalInterest.getText().toString());
        contentValues.put(COL_AVERAGEMONTHLYINTEREST, tvAverageMonthlyInterest.getText().toString());

        db.insert(contentValues);

        // Shared Preferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("LoanAmount", amount);
        editor.putString("DownPayment", downPayment);
        editor.putString("AnnualInterestRate", interestRate);
        editor.putString("Term", term);
        editor.putString("MonthlyRepayment", String.valueOf(monthlyRepayment));
        editor.putString("TotalRepayment", String.valueOf(totalRepayment));
        editor.putString("TotalInterest", String.valueOf(totalInterest));
        editor.putString("MonthlyInterest", String.valueOf(monthlyInterest));
        editor.apply();
        editor.commit();
    }

    private void reset(){
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");

        tvMonthlyPayment.setText(R.string.default_result);
        tvTotalRepayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);
    }

    private void history() {
        startActivity(new Intent(this, ListActivityView.class));
    }
}