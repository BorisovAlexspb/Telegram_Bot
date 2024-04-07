package edu.java.configuration.retry;

import edu.java.configuration.retry.backoff.RetryBackoff;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public record RetryPolicy(List<HttpStatusCode> retryableStatuses, RetryBackoff retryBackoff){
}
