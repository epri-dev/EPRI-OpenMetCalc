package com.epri.metric_calculator.actions.file;

import java.io.File;
import java.util.regex.Pattern;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.epri.metric_calculator.Activator;
import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.Messages;
import com.epri.metric_calculator.MetCalcProject;

/**
 * 
 * A wizard to select destination when performed save as action.
 * 
 * @author JoWookJae
 *
 */
public class SaveAsWizard extends Wizard {
	private static final String WINDOW_TITLE = Messages.SaveAsWizard_SAVE_AS_WINDOW_TITLE;
	private static final String WIZARD_TITLE = Messages.SaveAsWizard_SAVE_AS_WIZARD_TITLE;
	private static final String DESCRIPTION = Messages.SaveAsWizard_SAVE_AS_WIZARD_DESC;

	private static final String ILLEGAL_EXP = "[:\\\\/%*?:|\"<>]"; //$NON-NLS-1$

	private Text txtName;
	private Text txtDir;

	private File result;

	public SaveAsWizard() {
		setWindowTitle(WINDOW_TITLE);
	}

	/**
	 * Get selected destination after finish pressed.
	 * 
	 * @return
	 */
	public File getResult() {
		return result;
	}

	@Override
	public boolean performFinish() {
		File dir = new File(txtDir.getText());
		result = new File(dir, txtName.getText() + "." + Const.METCALC_PROJECT_EXTENSION); // $NON-NLS-1$

		return true;
	}

	@Override
	public void addPages() {
		WizardPage wizardPage = new WizardPage("") { //$NON-NLS-1$

			@Override
			public void createControl(Composite parent) {
				Composite container = new Composite(parent, SWT.NULL);

				setControl(container);
				container.setLayout(new GridLayout(3, false));

				Label lblPrjName = new Label(container, SWT.NONE);
				lblPrjName.setText(Messages.SaveAsWizard_LBL_PROJECT_NAME);

				txtName = new Text(container, SWT.BORDER);
				txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
				txtName.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						dialogChanged();
					}
				});

				Label lblDir = new Label(container, SWT.NONE);
				lblDir.setText(Messages.SaveAsWizard_LBL_DIRECTORY);

				txtDir = new Text(container, SWT.BORDER);
				txtDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				txtDir.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						dialogChanged();
					}
				});

				Button btnBrowse = new Button(container, SWT.NONE);
				btnBrowse.setText(Messages.SaveAsWizard_BTN_BROWSE);
				btnBrowse.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						DirectoryDialog directoryDlg = new DirectoryDialog(getShell());
						directoryDlg.setFilterPath(MetCalcProject.getCurrent().getProjectDir());
						String dir = directoryDlg.open();
						if (dir != null) {
							txtDir.setText(dir);
						}
					}
				});

				final String USER_HOME = System.getProperty("user.home"); //$NON-NLS-1$
				String defaultDir = USER_HOME + File.separatorChar + "Desktop" + File.separatorChar; //$NON-NLS-1$
				for (int i = 0;; i++) {
					String defaultName = "PRJ" + i;
					File fileDefault = new File(defaultDir + defaultName + "." + Const.METCALC_PROJECT_EXTENSION); //$NON-NLS-1$
					if (!fileDefault.exists()) {
						txtName.setText(defaultName);
						txtDir.setText(defaultDir);
						break;
					}
				}
				
				setDefaultText();
			}

			private void setDefaultText() {
				IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
				String projectDirPath = prefStore.getString(Const.PREFERENCE_STORE_PRESELECTED_PROJECT_DIRECTORY);

				if (projectDirPath == null || projectDirPath.isEmpty()) {
					projectDirPath = new File(System.getProperty("user.home"), "Desktop").getAbsolutePath(); //$NON-NLS-1$
				}

				txtDir.setText(projectDirPath);

				int i = 0;
				String name = null;
				File projectDir = null;
				do {
					name = "PRJ_" + i;
					projectDir = new File(projectDirPath, name);
					i++;
				} while (projectDir.exists() && projectDir.list() != null);
				txtName.setText(name);
			}

			private void dialogChanged() {
				if (txtName.getText() == null || txtName.getText().isEmpty()) {
					updateStatus(Messages.CreateProjectWizard_ERROR_MSG_2);
					return;
				}

				if (txtName.getText().contains(".")) { //$NON-NLS-1$
					updateStatus(Messages.CreateProjectWizard_ERROR_MSG_3);
					return;
				}

				if (!isValidFileName(txtName.getText())) {
					updateStatus(Messages.CreateProjectWizard_ERROR_MSG_7);
					return;
				}

				if (txtDir.getText() == null || txtDir.getText().isEmpty()) {
					updateStatus(Messages.CreateProjectWizard_ERROR_MSG_4);
					return;
				}

				if (!new File(txtDir.getText()).exists()) {
					updateStatus(Messages.CreateProjectWizard_ERROR_MSG_5);
					return;
				}

				String dir = txtDir.getText();
				String name = txtName.getText();

				result = new File(dir, name);
				if (result.exists() && result.list() != null) {
					updateStatus(NLS.bind(Messages.CreateProjectWizard_ERROR_MSG_8, result.getAbsolutePath()));
					return;
				}

				updateStatus(null);
			}

			private void updateStatus(String message) {
				setErrorMessage(message);
				setPageComplete(message == null);
			}
		};

		wizardPage.setTitle(WIZARD_TITLE);
		wizardPage.setDescription(DESCRIPTION);

		addPage(wizardPage);
	}

	private boolean isValidFileName(String fileName) {
		if (fileName == null || fileName.trim().length() == 0)
			return false;

		return !Pattern.compile(ILLEGAL_EXP).matcher(fileName).find();
	}
}