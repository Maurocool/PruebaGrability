package com.grability.PruebaGrability;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.grability.Tool.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());
        
        
        //Se captura el resumen del error
        
        StringBuilder errorReportResum = new StringBuilder();
        
        int pos = stackTrace.toString().lastIndexOf("Caused by:");        
        if(pos == -1)
        	pos = 0;        
        errorReportResum.append(stackTrace.toString().substring(pos));

        //Se captura la informacion del dispositivo
        StringBuilder errorReportDevice = new StringBuilder();
        
        errorReportDevice.append("\n************ DEVICE INFORMATION ***********\n");
        errorReportDevice.append("Brand: ");
        errorReportDevice.append(Build.BRAND);
        errorReportDevice.append(LINE_SEPARATOR);
        errorReportDevice.append("Device: ");
        errorReportDevice.append(Build.DEVICE);
        errorReportDevice.append(LINE_SEPARATOR);
        errorReportDevice.append("Model: ");
        errorReportDevice.append(Build.MODEL);
        errorReportDevice.append(LINE_SEPARATOR);
        errorReportDevice.append("Id: ");
        errorReportDevice.append(Build.ID);
        errorReportDevice.append(LINE_SEPARATOR);
        errorReportDevice.append("Product: ");
        errorReportDevice.append(Build.PRODUCT);
        errorReportDevice.append(LINE_SEPARATOR);
        errorReportDevice.append("\n************ FIRMWARE ************\n");
        errorReportDevice.append("SDK: ");
        errorReportDevice.append(Build.VERSION.SDK);
        errorReportDevice.append(LINE_SEPARATOR);
        errorReportDevice.append("Release: ");
        errorReportDevice.append(Build.VERSION.RELEASE);
        errorReportDevice.append(LINE_SEPARATOR);
        errorReportDevice.append("Incremental: ");
        errorReportDevice.append(Build.VERSION.INCREMENTAL);
        errorReportDevice.append(LINE_SEPARATOR);
        errorReportDevice.append("Imei: ");
        errorReportDevice.append(Util.obtenerImei(myContext));
        
        Log.e("BuscarProductos", errorReport.toString());

        Intent intent = new Intent(myContext, MainActivity.class);
        intent.putExtra("error", errorReport.toString());
        intent.putExtra("errorResumen", errorReportResum.toString());
        intent.putExtra("dispositivo", errorReportDevice.toString());
        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}