package com.example.moblieembedeed_mid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moblieembedeed_mid.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    String fstVal = "";
    String secVal = "";
    String oprVal = "";

    Boolean oprSta = false;
    Boolean eqrSta = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<Button> buttons_num = Arrays.asList(
                binding.calc0, binding.calc1, binding.calc2,
                binding.calc3, binding.calc4, binding.calc5,
                binding.calc6, binding.calc7, binding.calc8,
                binding.calc9
        );

        List<Button> buttons_opr = Arrays.asList(
                binding.calcD, binding.calcX, binding.calcM, binding.calcP
        );

        for(Button b: buttons_num){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String s = b.getTag().toString();
                    setNum(s);
                    printMainView();
                }
            });
        }
        for(Button b: buttons_opr){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String operator = b.getTag().toString();
                    setOpr(operator);
                    printMainView();
                    printCalcView();
                }
            });
        }
        binding.calcE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double result = 0;
                eqrSta = true;
                printMainView();
                printCalcView();
                result = calcVal();

                eqrSta = false;
            }
        });

    }
    void setNum(String s){
        String tmp;
        tmp = binding.valueView.getText().toString() + s;
        if(!oprSta){fstVal = tmp;}
        else{secVal = tmp;}
    }
    void setOpr(String s){
        if(fstVal.equals("")){ fstVal = "0";}
        oprSta = true;
        oprVal = s;
    }
    void printMainView(String s){
        if(!eqrSta){
            if(!oprSta){ binding.valueView.setText(fstVal);}
            else{ binding.valueView.setText(secVal);}
        } else {
            binding.valueView.setText(s);
        }

    }
    void printCalcView(){
        String tmp = fstVal + " " + oprVal + " " + secVal;
        if(!eqrSta){ binding.valueCalc.setText(tmp); }
        else{ binding.valueCalc.setText(tmp + " ="); }
    }

    double calcVal(){
        double result = 0;

        switch(oprVal){
            case "+": result = Double.parseDouble(fstVal) + Double.parseDouble(secVal); break;
            case "-": result = Double.parseDouble(fstVal) - Double.parseDouble(secVal); break;
            case "x": result = Double.parseDouble(fstVal) * Double.parseDouble(secVal); break;
            case "/": result = Double.parseDouble(fstVal) / Double.parseDouble(secVal); break;
        }

        return result;
    }
}