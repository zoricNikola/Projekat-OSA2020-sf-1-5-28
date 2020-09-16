package osa.projekat.sf1528.emailClient.security.encryption;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class CertificateUtil {
	
	public static PublicKey getPublicKey() {
		try {
			KeyStore keyStore = KeyStoreReader.readKeyStore(CertificateGenerator.rootCAPath, CertificateGenerator.password);
			Certificate certificate = KeyStoreReader.getCertificateFromKeyStore(keyStore, CertificateGenerator.rootCAAlias);
			return certificate.getPublicKey();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PrivateKey getPrivateKey() {
		try {
			KeyStore keyStore = KeyStoreReader.readKeyStore(CertificateGenerator.rootCAPath, CertificateGenerator.password);
			return KeyStoreReader.getPrivateKeyFromKeyStore(keyStore, CertificateGenerator.rootCAAlias, CertificateGenerator.password);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
