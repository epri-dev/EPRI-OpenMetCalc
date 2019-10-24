package com.epri.metric_calculator.actions.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.actions.AbstractAction;
import com.github.davidmoten.security.PPK;

public class DecryptExportedCsvAction extends AbstractAction {
	public DecryptExportedCsvAction() {
		super(Messages.DecryptDataPointAction_0, IconFactory.COMMON_DECRYPT_CSV);
	}

	@Override
	public void run() {
		// Select csv files to import
		FileDialog dlg = new FileDialog(Display.getDefault().getActiveShell(), SWT.MULTI);
		dlg.setFilterExtensions(new String[] { "*." + Const.SECURED_DATA_POINT_EXTENSION }); //$NON-NLS-1$
		String selection = dlg.open();

		// If no selection then return
		if (selection == null) {
			return;
		}

		// Create a list of file paths
		File dir = new File(selection).getParentFile();
		String[] filenames = dlg.getFileNames();

		// Private key file
		File privateKeyFile = new File(dir, Const.PRIVATE_FILENAME);
		if (!privateKeyFile.exists()) {
			notifyFail(Messages.MetCalcProject_4);
			return;
		}

		List<String> erroredFiles = new ArrayList<>();
		FileOutputStream fos = null;
		FileInputStream fis = null;
		for (String filename : filenames) {
			File encryptedFile = new File(dir, filename);

			String onlyFileName = filename.substring(0,
					filename.length() - Const.SECURED_DATA_POINT_EXTENSION.length());
			File decryptedFile = new File(dir, onlyFileName + "csv");

			boolean success = false;
			try {
				fis = new FileInputStream(encryptedFile);
				fos = new FileOutputStream(decryptedFile);

				PPK.privateKey(privateKeyFile).decrypt(fis, fos);
				success = true;
			} catch (IOException e) {
				e.printStackTrace();
				erroredFiles.add(filename);
			} catch (Throwable e) {
				e.printStackTrace();
				notifyFail(e.getMessage());
				return;
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (success) {
					try {
						FileUtils.forceDelete(encryptedFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// Notify results
		if (erroredFiles.isEmpty()) {
			notifySuccess();
		} else {
			notifyFail(String.format(
					"Could not write file for at least %d files. Please check if another process is using the file.",
					erroredFiles.size()));
		}
	}

	@Override
	public String getId() {
		return Messages.DecryptDataPointAction_5;
	}
}
