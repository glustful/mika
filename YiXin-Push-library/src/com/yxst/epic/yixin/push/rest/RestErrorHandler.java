package com.yxst.epic.yixin.push.rest;

import org.springframework.web.client.RestClientException;

public interface RestErrorHandler {

	void onRestClientExceptionThrown(RestClientException e);
}
