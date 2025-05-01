package com.example.moblieembedeed_mid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moblieembedeed_mid.databinding.ActivityMainBinding;
import com.example.moblieembedeed_mid.databinding.CalculatorBinding;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CalculatorBinding binding;
    private ActivityMainBinding drawerBinding;

    String fstVal = "0";
    String secVal = "";
    String oprVal = "";
    double result = 0;

    Boolean fstDot = false;
    Boolean secDot = false;

    Boolean oprSta = false;
    Boolean eqrSta = false;
    Boolean zeroSta = false;

    String memory = "0";
    Boolean memSta = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        drawerBinding = ActivityMainBinding.inflate(getLayoutInflater());
        binding = drawerBinding.contentCalculator;

        setContentView(drawerBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // drawer View
        binding.btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerBinding.drawerView.openDrawer(GravityCompat.END);
            }
        });

        // list of buttons
        List<Button> buttons_num = Arrays.asList(
                binding.calc0, binding.calc1, binding.calc2,
                binding.calc3, binding.calc4, binding.calc5,
                binding.calc6, binding.calc7, binding.calc8,
                binding.calc9
        );

        List<Button> buttons_opr = Arrays.asList(
                binding.calcD, binding.calcX, binding.calcM, binding.calcP
        );

        // binding, clickLinstener
        for(Button b: buttons_num){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String s = b.getTag().toString();
                    if(eqrSta){ reset();}
//                    if(s.equals("0")){zeroSta = true;}
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
                    if(eqrSta){
                        eqrSta = false;
                        result2fstVal();
                    }
                    setOpr(operator);
                    printMainView();
                    printCalcView();
                }
            });
        }

        binding.calcE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eqrSta){
                    eqrSta = true;
                }
                calcVal();
                printMainView();
                printCalcView();
                result2fstVal();
            }
        });
        binding.calcC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        binding.calcCE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!oprSta){fstVal = "0";}
                else { secVal = "0";}
                printMainView();
            }
        });
        binding.calcB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backSpace();
            }
        });
        binding.calcPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcPM();
                printMainView();
            }
        });
        binding.calcDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDot();
                printMainView();
            }
        });


        //memory
        List<Button> buttons_mem = Arrays.asList(
                binding.calcMP, binding.calcMM
        );
        for(Button b: buttons_mem){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String s = b.getTag().toString();
                    if(binding.valueView.getText().toString().equals("")){
                        binding.valueView.setText("0");
                    }
                    calcMem(s);
                    printMainView();
                    memShow();
                }
            });
        }
        binding.calcMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memSave();
                printMainView();
                memShow();
            }
        });
        binding.calcMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memRead();
                printMainView();
                memShow();
            }
        });
        binding.calcMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memReset();
                memShow();
            }
        });
    }

    //calc func
    void setNum(String s){
        String tmp;
        tmp = binding.valueView.getText().toString();
        if(tmp.equals("0")){tmp = "";}
        tmp += s;
        if(!oprSta){fstVal = tmp;}
        else{secVal = tmp;}
    }
    void setOpr(String s){
        if(fstVal.equals("")){ fstVal = "0";}
        oprSta = true;
        oprVal = s;
    }

    void printMainView(){
        if(!eqrSta){
            if(!oprSta){ binding.valueView.setText(fstVal);}
            else{ binding.valueView.setText(secVal);}
        } else {
            binding.valueView.setText(String.format("%.3f", result));
        }
    }
    void printCalcView(){
        String tmp = fstVal + " " + oprVal + " " + secVal;
        if(!eqrSta){ binding.valueCalc.setText(tmp); }
        else{ binding.valueCalc.setText(tmp + " ="); }
    }

    void calcVal(){
        result = 0;
        if(fstVal.equals("")) fstVal = "0";
        if(secVal.equals("")) secVal = "0";
        switch(oprVal){
            case "+": result = Double.parseDouble(fstVal) + Double.parseDouble(secVal); break;
            case "-": result = Double.parseDouble(fstVal) - Double.parseDouble(secVal); break;
            case "x": result = Double.parseDouble(fstVal) * Double.parseDouble(secVal); break;
            case "/": result = Double.parseDouble(fstVal) / Double.parseDouble(secVal); break;
        }
    }

    void putDot(){
        if(!oprSta){
            if(fstVal.equals("")) fstVal = "0";
            if(!fstDot) {
                fstDot = true;
                fstVal += ".";
            }
        } else {
            if(secVal.equals("")) secVal = "0";
            if(!secDot) {
                fstDot = true;
                secVal += ".";
            }
        }
    }

    void backSpace(){
        String tmp = "";
        if(!oprSta){
            tmp  = fstVal.substring(0, fstVal.length()-1);
            if(tmp.equals("")){ tmp = "0";}
            fstVal = tmp;
        } else {
            tmp  = secVal.substring(0, secVal.length()-1);
            if(tmp.equals("")){ tmp = "0";}
            secVal = tmp;
        }
        printMainView();
    }

    // deafult func
    void reset(){
        eqrSta = false; oprSta = false;
        fstDot = false; secDot = false;
        fstVal = ""; secVal = ""; oprVal=""; result = 0;
        printCalcView();

        fstVal = "0";
        printMainView();
    }

    void result2fstVal(){
        fstVal = Double.toString(result);
    }
    void funcPM(){
        String tmp = "";
        if(!oprSta){
            try {
                tmp = fstVal.substring(0, 1);
            } catch (Exception e){
                tmp = "";
            }
            switch (tmp){
                case "" : fstVal = "0"; break;
                case "0": break;
                case "-": fstVal = fstVal.substring(1, fstVal.length()); break;
                default: fstVal = "-" + fstVal; break;
            }
        } else {
            try {
                tmp = secVal.substring(0, 1);
            } catch (Exception e) {
                tmp = "0";
            }
            switch (tmp){
                case "" : secVal = "0"; break;
                case "0": break;
                case "-": secVal = secVal.substring(1, secVal.length()); break;
                default: secVal = "-" + secVal; break;
            }
        }
    }

    // memory func
    void memSave(){
        memSta = true;
        String tmp = binding.valueView.getText().toString();
        memory = tmp;
    }
    void memRead(){
        if(memSta){
            if(!oprSta) fstVal = memory;
            else secVal = memory;
        }
    }
    void memReset(){
        memory = "0";
        memSta = false;
    }
    void calcMem(String op){
        memSta = true;
        String tmp =  binding.valueView.getText().toString();
        switch(op){
            case "+": memory = Double.toString(Double.parseDouble(memory) + Double.parseDouble(tmp)); break;
            case "-": memory = Double.toString(Double.parseDouble(memory) - Double.parseDouble(tmp)); break;
        }
    }
    void memShow(){
        String tmp = String.format("%.3f", Double.parseDouble(memory));
//        binding.memtext.setText(tmp);
        drawerBinding.MemoryVal.setText(tmp);
    }

    // drawer func
    @Override
    public void onBackPressed(){
        if (drawerBinding.drawerView.isDrawerOpen(GravityCompat.END)) {
            drawerBinding.drawerView.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}