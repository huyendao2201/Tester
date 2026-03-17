package com.hcmunre.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private int maxRetry;

    public RetryAnalyzer() {
        // Đọc giá trị retry.count từ ConfigReader
        try {
            this.maxRetry = ConfigReader.getInstance().getIntProperty("retry.count");
        } catch (Exception e) {
            this.maxRetry = 0; // Mặc định là 0 nếu không cấu hình
        }
    }

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            if (count < maxRetry) {
                count++;
                System.out.println("[Retry] Đang thử lại lần thứ: " + count + " cho test: " + result.getName());
                return true;
            }
        }
        return false;
    }
}
