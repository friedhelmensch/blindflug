package de.kapuzenholunder.germanwings;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.Enumeration;

import javax.swing.UIManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;

import de.kapuzenholunder.germanwings.view.MainWindow;

public class Starter
{
	
	public static void main(String[] args)
	{
		try
		{
			PlasticLookAndFeel plastics = new PlasticLookAndFeel();
			UIManager.setLookAndFeel(plastics);
			
		} catch (Exception e)
		{
			e.printStackTrace();

		}
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					new MainWindow();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void readKeyStore()
	{
		FileInputStream is = null;
		try
		{

			File file = new File("D:\\KaddasCall\\GermanWings\\webstart\\certificateContainer.pfx");
			is = new FileInputStream(file);
			//KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			
			KeyStore keystore = KeyStore.getInstance("PKCS12");
			
			String password = "4everLove";
			
			keystore.load(is, password.toCharArray());

			Enumeration<String> enumeration = keystore.aliases();
			while (enumeration.hasMoreElements())
			{
				String alias = (String) enumeration.nextElement();
				System.out.println("alias name: " + alias);
				Certificate certificate = keystore.getCertificate(alias);
				System.out.println(certificate.toString());

			}

		} catch (java.security.cert.CertificateException e)
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (KeyStoreException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (null != is)
				try
				{
					is.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}

}
