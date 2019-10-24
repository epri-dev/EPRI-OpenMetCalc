package com.epri.metric_calculator.actions.export;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.ToolSetting;
import com.epri.metric_calculator.actions.AbstractAction;
import com.epri.metric_calculator.util.WindowsExplorerUtil;

/**
 * Generate Key Action
 * 
 * @author JoWookJae
 *
 */
public class GenerateKeyAction extends AbstractAction {

	/**
	 * Security provider
	 */
	private static final String SECURITY_PROVIDER = "BC"; //$NON-NLS-1$

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public GenerateKeyAction() {
		super("Generate Key", IconFactory.COMMON_GENERATE_KEY); //$NON-NLS-1$
	}

	@Override
	public void run() {
		MetCalcProject currentProject = MetCalcProject.getCurrent();

		// If current project is null, will return
		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}
		
		int keySize = ToolSetting.getInstance().getEncryptionKeySize();
		try {
			File dest = new File(currentProject.getProjectDir());
			generate(dest, keySize);
			// Displays the location of the settings file in the file explorer.
			notifySuccess();
			WindowsExplorerUtil.select(new File(dest, Const.PRIVATE_FILENAME).getAbsolutePath());
		} catch (Throwable e) {
			e.printStackTrace();
			notifyFail(e.getMessage());
		}
	}

	private void generate(File dir, int keySize) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Const.PROJECT_ENCRYTION_ALGORITHM, SECURITY_PROVIDER);
		keyGen.initialize(keySize, new SecureRandom());
		KeyPair keyPair = keyGen.generateKeyPair();

		Files.write((new File(dir, Const.PRIVATE_FILENAME)).toPath(), keyPair.getPrivate().getEncoded(),
				new OpenOption[0]);
		Files.write((new File(dir, Const.PUBLIC_FILENAME)).toPath(), keyPair.getPublic().getEncoded(),
				new OpenOption[0]);
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.export.GenerateKeyAction"; //$NON-NLS-1$
	}
}
