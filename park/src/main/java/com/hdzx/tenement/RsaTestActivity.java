package com.hdzx.tenement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.Base64;
import com.hdzx.tenement.utils.RSAUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by anchendong on 15/6/30.
 */
public class RsaTestActivity extends Activity {

    //服务私钥
    private String privateKeyString = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMV9NuedUEsqD61LrVpFe8iqfxnatIvgoReuEESdGeDCfZ9PPNXxAjkUqZ8EPQlD4iqsbRujz52+sTgPYXsUNo4h8BPf+zzWFwLj44arWLogUQHVbreeJ4Qvo+DsgM8JMynm5fJsVYJG6c2OKXGcS9apwNKXkV3WYtVeLe1vBh+7AgMBAAECgYBRiuYEtFvW7UMT1s95Gn+F14AreLZbmyBo2qI08nkg6K1d/iWsYJr+Xp263tEk5jaYUCGs6/Jpu7cI4rDpVOtBAa94XBuOpAv9e1Xswu1qq4Rfonkh4vp29fNJk7XSfTzkkoiN1wtc0tB3JMRDGS2fnTY8ddeJ7D66uoIwewff4QJBAPxT5KND6hCCuD9f2I3zrXN2Fs/gbEg3v8Ob7ovCvPMzK/6NtRcW0yZhcvEu9VxTiAN4rmQOjGYp6UO9dKxOn/UCQQDIXQHUuj+9uJrzCrLppPMCdmFdMLVBAblK5e5zq0fBKhe4nY/yDM9PVK5r09mhQSUvl6GbujiFhWkLmL6392LvAkBYmYya12oEyiGv/xOnJH0vIbP44jCjWw/2u3YTlRmLu9gi1ddUeCtEOsuFbJuzA5GqxmFVuNYLuYOyyX+CUUlRAkEAr9dQQA2k2zAkbKA7HGozCzoxgMO6ju6gW2cnukPbmV8DqnY9WkR7vDepS+CE4sx1gislHbJ04FmRMyg+WN236QJAIYvOWtnfWlcuz4lh1mbb0padJIwoEN/sbGgbVMYAygJ/1nKYyKMsTyKh0TWffut72praZlFjBCUSDTn73M44ew==";
    //服务公钥
    private String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFfTbnnVBLKg+tS61aRXvIqn8Z2rSL4KEXrhBEnRngwn2fTzzV8QI5FKmfBD0JQ+IqrG0bo8+dvrE4D2F7FDaOIfAT3/s81hcC4+OGq1i6IFEB1W63nieEL6Pg7IDPCTMp5uXybFWCRunNjilxnEvWqcDSl5Fd1mLVXi3tbwYfuwIDAQAB";

    private TextView rsaStringTextView;

    private TextView rsaencodeStringTextView;

    private TextView rsadecodeStringTextView;

    private Button rsaCodeBtuuon;

    private TextView aesStringTextView;

    private TextView aesencodeStringTextView;

    private TextView aesdecodeStringTextView;

    private Button aesCodeBtuuon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rsa_test);
        rsaCodeBtuuon = (Button) findViewById(R.id.rsa_code_button);
        rsaStringTextView = (TextView) findViewById(R.id.rsa_string_txt);
        rsaencodeStringTextView = (TextView) findViewById(R.id.rsa_encode_txt);
        rsadecodeStringTextView = (TextView) findViewById(R.id.rsa_decode_txt);

        aesCodeBtuuon = (Button) findViewById(R.id.aes_code_button);
        aesStringTextView = (TextView) findViewById(R.id.aes_string_txt);
        aesencodeStringTextView = (TextView) findViewById(R.id.aes_encode_txt);
        aesdecodeStringTextView = (TextView) findViewById(R.id.aes_decode_txt);

        rsaCodeBtuuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PrivateKey privateKey = RSAUtils.loadPrivateKey(privateKeyString);
                    PublicKey publicKey = RSAUtils.loadPublicKey(publicKeyString);

//                    String codestr = rsaStringTextView.getText().toString();
                    String codestr = "123ABC安宸栋";
                    //使用服务私钥加密
                    byte[] encodedata = RSAUtils.encryptDataByPrivateKey(codestr.getBytes(), privateKey);
                    rsaencodeStringTextView.setText(Base64.encodeBytes(encodedata));
                    Log.v("test-","私钥加密后密文="+Base64.encodeBytes(encodedata));
                    String encodeString=Base64.encodeBytes(encodedata);
                    //使用服务公钥解密
                    String out = new String(RSAUtils.decryptDataByPublicKey(Base64.decode(encodeString), publicKey));
                    rsadecodeStringTextView.setText(out);
                    Log.v("test-","公钥解密后密文="+out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        aesCodeBtuuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encryptingCode = AESUtils.encrypt("resd", aesStringTextView.getText().toString());
                aesencodeStringTextView.setText(encryptingCode);
                String decryptingCode = AESUtils.decrypt("resd", encryptingCode);
                aesdecodeStringTextView.setText(decryptingCode);
            }
        });
    }
}
