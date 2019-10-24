package com.epri.metric_calculator.actions.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.IconFactory;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;
import com.epri.metric_calculator.NoProjectMessageDialog;
import com.epri.metric_calculator.actions.AbstractAction;
import com.github.davidmoten.security.PPK;
import com.google.common.io.Files;

public class ExportEncryptedCsvAction extends AbstractAction {
	public ExportEncryptedCsvAction() {
		super(Messages.ExportEncryptedCsvAction_0, IconFactory.COMMON_ENCRYPT_CSV);
	}

	@Override
	public void run() {
		MetCalcProject currentProject = MetCalcProject.getCurrent();

		// If current project is null, will return
		if (currentProject == null) {
			NoProjectMessageDialog.open();
			return;
		}

		File publicKeyFile = new File(currentProject.getProjectDir(), Const.PUBLIC_FILENAME);
		if (!publicKeyFile.exists()) {
			notifyFail(Messages.MetCalcProject_3);
			return;
		}

		// Select destination
		DirectoryDialog dirDlg = new DirectoryDialog(Display.getDefault().getActiveShell());
		dirDlg.setFilterPath(currentProject.getProjectDir());
		String dirPath = dirDlg.open();
		if (dirPath == null) {
			return;
		}

		File tempDir = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			// Make temporary directory.
			tempDir = Files.createTempDir();

			// Save the data point in temporary directory.
			currentProject.saveDataPoint(tempDir.getAbsolutePath());

			// Make target directory.
			String targetDir = dirPath + File.separator + currentProject.getProjectName()
					+ Const.SECURED_DATA_POINT_DIR_SUFFIX;

			if (!new File(targetDir).exists()) {
				new File(targetDir).mkdir();
			}

			// Copy private key file
			File privateKeyFile = new File(currentProject.getProjectDir(), Const.PRIVATE_FILENAME);
			if (privateKeyFile.exists()) {
				Files.copy(privateKeyFile, new File(targetDir, privateKeyFile.getName()));
			} else {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), "Warning",
						"Unable to find private key in project path. However, encryption continues.");
			}

			List<String> erroredFiles = new ArrayList<>();
			// Encrypt the data point files
			for (File file : tempDir.listFiles()) {
				String filename = Files.getNameWithoutExtension(file.getName());

				try {
					fis = new FileInputStream(file);

					File encryptedFile = new File(targetDir, filename + "." + Const.SECURED_DATA_POINT_EXTENSION);
					fos = new FileOutputStream(encryptedFile);

					PPK.publicKey(publicKeyFile).encrypt(fis, fos);
				} catch (IOException e) {
					e.printStackTrace();
					erroredFiles.add(file.getAbsolutePath());
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
				}
			}

			if (erroredFiles.isEmpty()) {
				notifySuccess();
			} else {
				notifyFail(String.format(
						"Could not write file for at least %d files. Please check if another process is using the file.",
						erroredFiles.size()));
			}

		} catch (Throwable e) {
			e.printStackTrace();
			notifyFail(e.getMessage());
			return;
		} finally {
			// Delete temporary directory.
			try {
				FileUtils.deleteDirectory(tempDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getId() {
		return "com.epri.metric_calculator.actions.export.EncryptDataPointAction"; //$NON-NLS-1$
	}
}
