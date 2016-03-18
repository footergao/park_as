package com.hdzx.tenement.http.protocol.impl;

import android.content.Context;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;

public class NewHttpDepartureGraber implements IContentReportor {
    private static final String GET_AREA_CODE = "GET_AREA_CODE";

    private static final String GET_BUS_STATION_CODE = "GET_BUS_STATION_CODE";

    private HttpAsyncTask httpAsyncTask;

    private Context context;

    private IContentReportor reportor;

    private boolean isRunning = false;

    private static NewHttpDepartureGraber instance = new NewHttpDepartureGraber();

    public static NewHttpDepartureGraber getInstance() {
        return instance;
    }

    private NewHttpDepartureGraber() {

    }


    @Override
    public void reportBackContent(ResponseContentTamplate responseContent) {

    }
}

