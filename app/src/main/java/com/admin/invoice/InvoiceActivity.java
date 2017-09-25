package com.admin.invoice;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences.Editor;

public class InvoiceActivity extends AppCompatActivity implements OnEditorActionListener
{
    private EditText InputTextEdit;
    private TextView DiscountPercentAmtLabel;
    private TextView DiscountAmtLabel;
    private TextView TotalAmtLabel;
    private Float discount = 0.00f;
    private Float discountAmt = 0F;
    private Float total = 0F;
    private Float subTotal = 0F;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        InputTextEdit = (EditText) findViewById(R.id.InputTextEdit);
        DiscountPercentAmtLabel = (TextView) findViewById(R.id.DiscountPercentAmtLabel);
        DiscountAmtLabel = (TextView) findViewById(R.id.DiscountAmtLabel);
        TotalAmtLabel = (TextView) findViewById(R.id.TotalAmtLabel);

        InputTextEdit.setOnEditorActionListener(this);
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
    {
        if(actionId == EditorInfo.IME_ACTION_DONE)
        {
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onPause()
    {
        Editor editor = savedValues.edit();
        editor.putFloat("discount", discount);
        editor.putFloat("discountAmt", discountAmt);
        editor.putFloat("total", total);
        editor.putFloat("subTotal", subTotal);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        discount = savedValues.getFloat("discount", discount);
        discountAmt = savedValues.getFloat("discountAmt", discountAmt);
        total = savedValues.getFloat("total", total);
        subTotal = savedValues.getFloat("subTotal", subTotal);

        InputTextEdit.setText(subTotal.toString());

        calculateAndDisplay();
    }

    public void calculateAndDisplay()
    {
        subTotal = Float.parseFloat(InputTextEdit.getText().toString());

        if(subTotal > 199)
        {
            discount = .2f;
        }
        else if(subTotal < 200 && subTotal > 99)
        {
            discount = .1f;
        }
        else
        {
            discount = .0f;
        }

        discountAmt = Float.parseFloat(InputTextEdit.getText().toString()) * discount;
        total = subTotal - discountAmt;

        DiscountAmtLabel.setText(formatCurrency(discountAmt));
        DiscountPercentAmtLabel.setText(formatPercent(discount*100));
        TotalAmtLabel.setText(formatCurrency(total));
    }

    public String formatPercent(float f)
    {
        String formattedString = String.valueOf(f) + "%";

        return formattedString;
    }

    public String formatCurrency(float f)
    {
        String formattedString = "$" + String.valueOf(f);

        return formattedString;
    }

}
