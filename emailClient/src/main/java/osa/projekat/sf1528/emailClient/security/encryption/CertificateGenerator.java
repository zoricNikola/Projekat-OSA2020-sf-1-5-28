package osa.projekat.sf1528.emailClient.security.encryption;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class CertificateGenerator {
	
	public static char[] password = "password".toCharArray();
	public static String rootCAPath = "./data/root.jks";
	public static String rootCAAlias = "Root CA Certificate";
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static KeyPair generateKeyPair() {
		
		try {
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGenerator.initialize(2048, random);
			KeyPair pair = keyGenerator.generateKeyPair();
			return pair;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static X509Certificate generateCertificate(IssuerData issuerData, SubjectData subjectData) {
		try {
			JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
			signerBuilder.setProvider("BC");
			ContentSigner contentSigner = signerBuilder.build(issuerData.getPrivateKey());
			
			X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
					issuerData.getX500name(), new BigInteger(subjectData.getSerialNumber()), 
					subjectData.getStartDate(), subjectData.getEndDate(),
					subjectData.getX500name(), subjectData.getPublicKey());
			
			X509CertificateHolder holder = builder.build(contentSigner);
			
			JcaX509CertificateConverter converter = new JcaX509CertificateConverter();
			converter = converter.setProvider("BC");
			
			return converter.getCertificate(holder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void generateMyRootCA() {
		//Generating certificate
		SimpleDateFormat iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		Date endDate = new Date();
		try {
			startDate = iso8601Formatter.parse("2020-08-01");
			endDate = iso8601Formatter.parse("2025-08-01");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		X500NameBuilder nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
		nameBuilder.addRDN(BCStyle.CN, "Nikola Zoric");
		nameBuilder.addRDN(BCStyle.SURNAME, "Zoric");
		nameBuilder.addRDN(BCStyle.GIVENNAME, "Nikola");
		nameBuilder.addRDN(BCStyle.O, "CSS");
		nameBuilder.addRDN(BCStyle.OU, "NS");
		nameBuilder.addRDN(BCStyle.C, "RS");
		nameBuilder.addRDN(BCStyle.E, "nikola.se.zoric@gmail.com");
		nameBuilder.addRDN(BCStyle.UID, "1");
		
		String serialNumber = "1";
		
		X500Name x500Name = nameBuilder.build();
		
		KeyPair keyPair = generateKeyPair();
		
		IssuerData issuerData = new IssuerData(keyPair.getPrivate(), x500Name);
		
		SubjectData subjectData = new SubjectData(keyPair.getPublic(), x500Name, serialNumber, startDate, endDate);
		
		X509Certificate certificate = generateCertificate(issuerData, subjectData);
		System.out.println("Root CA created");
		
//		Generating KeyStore
		KeyStore keyStore = null;
		
		try {
			keyStore = KeyStore.getInstance("JKS");
			keyStore.load(null, "password".toCharArray());
			
			KeyStoreWriter.addToKeyStore(keyStore, rootCAAlias, keyPair.getPrivate(), password, certificate);
			System.out.println("Root CA added to KeyStore");
			KeyStoreWriter.saveKeyStore(keyStore, rootCAPath, password);
			System.out.println("Root CA KeyStore saved");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception creating new keyStore");
			return;
		}
		
	}

}
