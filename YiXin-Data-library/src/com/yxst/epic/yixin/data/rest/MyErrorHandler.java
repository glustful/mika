package com.yxst.epic.yixin.data.rest;

import org.androidannotations.annotations.EBean;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

@EBean
public class MyErrorHandler implements RestErrorHandler {
    @Override
    public void onRestClientExceptionThrown(RestClientException e) {
        // Do whatever you want here.
    }
}
