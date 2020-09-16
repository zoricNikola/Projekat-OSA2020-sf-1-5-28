package osa.projekat.sf1528.emailClient;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import osa.projekat.sf1528.emailClient.security.encryption.CertificateGenerator;
import osa.projekat.sf1528.emailClient.security.encryption.CertificateUtil;

@SpringBootApplication
public class EmailClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailClientApplication.class, args);
		
		// check for root CA KeyStore
		File f = new File("./data/root.jks");
		if (!f.exists()) {
			CertificateGenerator.generateMyRootCA();
		}
		else {
			PublicKey publicKey = CertificateUtil.getPublicKey();
			byte[] encKey = publicKey.getEncoded();
			
			System.out.println(publicKey);
			System.out.println(publicKey);
			System.out.println(publicKey);
			
			PrivateKey privateKey = CertificateUtil.getPrivateKey();
			System.out.println(privateKey);
			System.out.println(privateKey);
			System.out.println(privateKey);
		}
	}

}
