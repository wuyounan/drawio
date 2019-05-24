package com.huigou.data.exception;

import com.huigou.exception.ApplicationException;

public class ExportExcelException extends ApplicationException {

    private static final long serialVersionUID = -8915515383672613717L;

    public ExportExcelException() {
    }

    public ExportExcelException(String message) {
        super(message);
    }

    public ExportExcelException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ExportExcelException(Throwable throwable) {
        super(throwable);
    }
}
