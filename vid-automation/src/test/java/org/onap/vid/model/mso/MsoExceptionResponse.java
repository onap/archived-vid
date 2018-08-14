package org.onap.vid.model.mso;

public class MsoExceptionResponse {

    public static class ServiceException {

        public ServiceException(String messageId, String text) {
            this.messageId = messageId;
            this.text = text;
        }

        public ServiceException() {
        }

        public String messageId;
        public String text;
    }

    public ServiceException serviceException;

    public MsoExceptionResponse() {
    }

    public MsoExceptionResponse(String messageId, String text) {
        this.serviceException = new ServiceException(messageId, text);
    }

    public MsoExceptionResponse(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception);
        this.serviceException = new ServiceException(exceptionResponse.getException(), exceptionResponse.getMessage());
    }
}
