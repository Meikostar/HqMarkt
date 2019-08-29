package com.hqmy.market.http;

import android.annotation.SuppressLint;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * 设置请求信任所有证书
 * Created by Administrator on 2017/2/21.
 */

class SSLSocketUtils {
    /**
     * 信任所有主机-对于任何证书都不做检查
     */
    private static class InsecureTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            // return null;
            return new X509Certificate[]{};
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
            // System.out.println("cert: " + chain[0].toString() + ", authType: "
            // + authType);
        }
    }

    /**
     * 服务器需验证客户端身份时使用该方法
     * 信任所有服务器
     *
     * @param certificates
     * @param builder
     */
    public static void addClientCertificates(List<byte[]> certificates, OkHttpClient.Builder builder)
            throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        keyStore.load(null);

        int i = 1;
        for (byte[] bytes : certificates) {
            InputStream certificate = new ByteArrayInputStream(bytes);

            String certificateAlias = Integer.toString(i++);

            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

            certificate.close();
        }

        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        builder.sslSocketFactory(sslContext.getSocketFactory(), new InsecureTrustManager());
    }

    /**
     * 服务器不验证客户端身份时使用该方法
     * 信任所有服务器
     *
     * @param builder
     */
    public static void ignoreClientCertificate(OkHttpClient.Builder builder) throws KeyManagementException, NoSuchAlgorithmException {
        final TrustManager[] trustAllCerts = new TrustManager[]{new InsecureTrustManager()};
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        builder.sslSocketFactory(socketFactory, new InsecureTrustManager());
    }
}
