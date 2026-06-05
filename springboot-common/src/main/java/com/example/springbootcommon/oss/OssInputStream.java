package com.example.springbootcommon.oss;

import com.aliyun.oss.OSS;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

class OssInputStream extends FilterInputStream {

    private final OSS client;

    OssInputStream(InputStream inputStream, OSS client) {
        super(inputStream);
        this.client = client;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            client.shutdown();
        }
    }
}
