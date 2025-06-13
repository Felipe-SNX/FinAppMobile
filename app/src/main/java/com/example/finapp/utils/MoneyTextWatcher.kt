package com.example.finapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class MoneyTextWatcher(val editText: EditText): TextWatcher{
    companion object{
        private const val replaceRegex: String = "[R$,.\u00A0]";
        private const val replaceFinal: String = "R$\u00A0";
    }

    override fun beforeTextChanged(editable: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(editable: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        try{
            val stringEditable = editable.toString();

            if(stringEditable.isEmpty()) return;

            editText.removeTextChangedListener(this);
            val cleanString = stringEditable.replace(replaceRegex.toRegex(), "");

            val parsed = BigDecimal(cleanString)
                .setScale(2, RoundingMode.FLOOR)
                .divide(BigDecimal(100), RoundingMode.FLOOR);

            val decimalFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR")) as DecimalFormat;
            val formatted = decimalFormat.format(parsed);

            val stringFinal = formatted.replace(replaceFinal, "");
            editText.setText(stringFinal);
            editText.setSelection(stringFinal.length);
            editText.addTextChangedListener(this);

        }catch(e: Exception){
            Log.e("ERROR", e.toString());
        }
    }
}