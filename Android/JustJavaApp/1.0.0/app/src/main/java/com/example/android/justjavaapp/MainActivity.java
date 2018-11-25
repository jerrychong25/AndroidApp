/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjavaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.justjavaapp.R;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;
    boolean isWhippedCream = false;
    boolean isChocolate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_whipped_cream:
                if (checked)
                {
                    isWhippedCream = true;
                }
                else
                {
                    isWhippedCream = false;
                }
                break;
            case R.id.checkbox_chocolate:
                if (checked)
                {
                    isChocolate = true;
                }
                else
                {
                    isChocolate = false;
                }
                break;
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        createOrderSummary(quantity);
    }

    private void createOrderSummary(int numberOrder) {
        int price = calculatePrice(numberOrder);
        String orderMessage = "";

        EditText etname = (EditText) findViewById(R.id.edit_text_name);
        String name = etname.getText().toString();

        orderMessage = getString(R.string.order_summary_name, name);

        if(isWhippedCream == true)
        {
            orderMessage = orderMessage + "\n" + getString(R.string.add) + getString(R.string.whipped_cream) + ": Yes";
        }
        else if(isWhippedCream == false)
        {
            orderMessage = orderMessage + "\n" + getString(R.string.add) + getString(R.string.whipped_cream) + ": No";
        }

        if(isChocolate == true)
        {
            orderMessage = orderMessage + "\n" + getString(R.string.add) + getString(R.string.chocolate) + ": Yes";
        }
        else if(isChocolate == false)
        {
            orderMessage = orderMessage + "\n" + getString(R.string.add) + getString(R.string.chocolate) + ": No";
        }

        orderMessage = orderMessage + "\n" + getString(R.string.quantity) + ": " + numberOrder;
        orderMessage = orderMessage + "\n" + getString(R.string.total) + ": $" + price;
        orderMessage = orderMessage + "\n" + getString(R.string.thank_you);

        composeEmail(getString(R.string.order_summary_email_subject, name), orderMessage);
    }

    private void composeEmail(String subject, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     */
    private int calculatePrice(int quantity) {
        int Price = 5;

        if(isWhippedCream == true)
        {
            Price += 1;
        }

        if(isChocolate == true)
        {
            Price += 2;
        }

        return (quantity * Price);
    }

    public void increment(View view) {
        if(quantity==100)
        {
            return;
        }

        quantity += 1;
        displayQuantity(quantity);
    }

    public void decrement(View view) {
        if(quantity==1)
        {
            return;
        }

        quantity -= 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}