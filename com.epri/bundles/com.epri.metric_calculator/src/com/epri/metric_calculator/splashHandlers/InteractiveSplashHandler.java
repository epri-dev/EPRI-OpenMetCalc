
package com.epri.metric_calculator.splashHandlers;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;

import com.epri.metric_calculator.Activator;
import com.epri.metric_calculator.IconFactory;

/**
 * A class that creates accept and reject buttons for splash screens and handles
 * events.
 */
public class InteractiveSplashHandler extends AbstractSplashHandler {

	private static final String INDICATOR_VERSION = "%VERSION%";

	private static final String BUTTON_TEXT_ACCEPT = "Accept";
	private static final String BUTTON_TEXT_REJECT = "Reject";

	/**
	 * When the accept button is clicked, the event loop terminates and the tool is
	 * executed.
	 */
	private boolean isAccept;

	@Override
	public void init(final Shell splash) {
		super.init(splash);
		splash.setLayout(new FillLayout());
		splash.setBackgroundMode(SWT.INHERIT_DEFAULT);
		createUI();
		splash.layout(true);
		doEventLoop();
	}

	/**
	 * Create UI
	 */
	private void createUI() {
		Shell splash = getSplash();

		Composite composite = new Composite(splash, SWT.BORDER);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 30;
		layout.marginHeight = 30;
		composite.setLayout(layout);

		Composite imageContainer = new Composite(composite, SWT.NONE);
		imageContainer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		imageContainer.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(Event event) {
				event.gc.drawImage(IconFactory.SPLASH_EPRI, 0, 0);
			}
		});

		Composite labelContainer = new Composite(composite, SWT.BORDER);
		GridLayout gdLabelContainer = new GridLayout();
		gdLabelContainer.marginWidth = 0;
		gdLabelContainer.marginHeight = 0;
		labelContainer.setLayout(gdLabelContainer);
		
		labelContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_VERTICAL));
		
		Text label = new Text(labelContainer, SWT.READ_ONLY | SWT.V_SCROLL | SWT.WRAP);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		label.setText(getSplashText());
		label.setBackground(new Color(null, 255, 255, 255));

		Composite buttonContainer = new Composite(composite, SWT.NONE);
		buttonContainer.setLayout(new GridLayout(2, false));
		buttonContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button btnAccept = new Button(buttonContainer, SWT.BOLD);
		btnAccept.setText(BUTTON_TEXT_ACCEPT);
		GridData gdBtnAccept = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true);
		gdBtnAccept.widthHint = 70;
		btnAccept.setLayoutData(gdBtnAccept);
		btnAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isAccept = true;
			}
		});
		btnAccept.setFocus();

		Button btnReject = new Button(buttonContainer, SWT.NONE);
		btnReject.setText(BUTTON_TEXT_REJECT);
		GridData gdBtnReject = new GridData(SWT.RIGHT, SWT.BOTTOM, false, true);
		gdBtnReject.widthHint = 70;
		btnReject.setLayoutData(gdBtnReject);
		btnReject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
	}

	private String getSplashText() {
		String splashText = "unknown";
		try {
			splashText = FileUtils.readFileToString(Activator.getFile("splash.txt"), "UTF-8");
			splashText = splashText.replaceAll(INDICATOR_VERSION,
					Activator.getDefault().getBundle().getVersion().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return splashText;
	}

	/**
	 * Loop until the accept button is pressed.
	 */
	private void doEventLoop() {
		Shell splash = getSplash();
		while (isAccept == false) {
			if (splash.getDisplay().readAndDispatch() == false) {
				splash.getDisplay().sleep();
			}
		}
	}
}
