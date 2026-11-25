package com.its.crm.records.common.service;

import com.its.crm.records.common.util.AESUtil;
import org.springframework.stereotype.Service;

/**
 * Common Encrypt/Decrypt Service (mirrored from LMS common module).
 */
@Service
public class EncryptDecryptService {

    private final AESUtil aesUtil;

    public EncryptDecryptService(AESUtil aesUtil) {
        this.aesUtil = aesUtil;
    }

    public String encryptResponse(String data) {
        return aesUtil.encrypt(data);
    }

    public String decryptRequest(String encryptedData) {
        return aesUtil.decrypt(encryptedData);
    }
}
