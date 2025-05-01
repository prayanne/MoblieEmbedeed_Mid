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
import java.text.DecimalFormat;
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

    String memory = "0";
    Boolean memSta = false;

    private static final DecimalFormat DF = new DecimalFormat("0.###########");
    String deco_result = DF.format(result);


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
        binding.calcMMore.setOnClickListener(new View.OnClickListener() {
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
        List<Button> buttons_UnOpr = Arrays.asList(
                binding.calcL, binding.calc1px, binding.calcX2, binding.calcRoot
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
        for(Button b: buttons_UnOpr){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String operator = b.getTag().toString();
                    if(!eqrSta){
                        eqrSta = true;
                    }
                    secVal = "0";

                    setOpr(operator);
                    oprSta = false;
                    calcUnVal();
                    printMainView();
                    printUnCalcView();
                    result2fstVal();
                }
            });
        }

        binding.calcE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eqrSta){
                    eqrSta = true;
                }
                if(!(oprVal.equals("sqr") || oprVal.equals("1/") || oprVal.equals("√"))){
                    calcVal();
                    printMainView();
                    printCalcView();
                    result2fstVal();
                }
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
            //binding.valueView.setText(String.format("0.###", result));
            String deco_result = DF.format(result);
            binding.valueView.setText(deco_result);
        }
    }
    void printCalcView(){
        String tmp = fstVal + " " + oprVal;
        String temp = fstVal + " " + oprVal + " " + secVal;

        if(!eqrSta){ binding.valueCalc.setText(tmp); }
        else{ binding.valueCalc.setText(temp + " ="); }
    }
    void printUnCalcView(){
        switch (oprVal){
            case "sqr": binding.valueCalc.setText( "spr(" + fstVal + ")"); break;
            case "1/" : binding.valueCalc.setText("1/(" +  fstVal + ")" ); break;
            case "√"  : binding.valueCalc.setText("√" + fstVal); break;
            case "%"  : binding.valueCalc.setText(""); break;
        }
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

    void calcUnVal(){
        result = 0;
        if(fstVal.equals("")) fstVal = "0";
        if(secVal.equals("")) secVal = "0";
        if(!oprSta){
            switch (oprVal){
                case "sqr": result = Double.parseDouble(fstVal) * Double.parseDouble(fstVal); break;
                case "1/": result = 1 / Double.parseDouble(fstVal); break;
                case "√" : result = Math.sqrt(Double.parseDouble(fstVal)); break;
                case "%" : result = Double.parseDouble(fstVal) / 100; break;
            }
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
    void funcPM() {
        String tmp = "";
        Double temp = 0.0;
        if (!oprSta) {
            fstVal = funcPM_opt(fstVal);
        } else {
            secVal = funcPM_opt(secVal);
        }
    }
    String funcPM_opt(String s){
        String tmp = "";
        Double temp = 0.0;
        try {
            tmp = s.substring(0, 1);
            temp = Double.parseDouble(s);
        } catch (Exception e){
            tmp = "";
        }
        if(temp != 0.0){
            switch (tmp){
                case "" : return "0";
                case "-": return s.substring(1, s.length());
                default: return "-" + s;
            }
        }
        return "0";
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
        String tmp = deco_result = DF.format(Double.parseDouble(memory));
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