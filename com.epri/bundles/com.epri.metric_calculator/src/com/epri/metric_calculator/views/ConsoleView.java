package com.epri.metric_calculator.views;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import com.epri.metric_calculator.Const;

public class ConsoleView extends ViewPart {

	private static final Color BACKGROUND_ERROR = new Color(null, 255, 255, 255);
	private static final Color FOREGROUND_ERROR = new Color(null, 255, 0, 0);

	protected OutputStream outputStream;
	protected PrintStream printStream;
	protected StyledText text;
	protected StringBuilder totalText;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		text = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		text.setBackground(new Color(null, 255, 255, 255));
		totalText = new StringBuilder();
	}

	/**
	 * Clear console
	 */
	public void clear() {
		Display.getDefault().syncExec(() -> {
			text.setText("");
		});
	}

	public PrintStream getPrintStream() {
		if (printStream == null) {

			OutputStream outputStream = new OutputStream() {
				@Override
				public final void write(int b) throws IOException {
				}
			};

			printStream = new PrintStream(outputStream) {
				@Override
				public final void write(int b) {
					super.write(b);
				}

				@Override
				public final void println(String str) {
					// Create message
					StringBuilder message = new StringBuilder();
					message.append(str);
					message.append(System.getProperty("line.separator"));
					totalText.append(message);

					// Append message to text
					Display.getDefault().syncExec(() -> {
						int start = text.getCharCount() - 1;
						text.append(message.toString());
						text.setTopIndex(text.getLineCount() - 1);

						// If it is an error message, set color to red
						if (str.startsWith(Const.PREFIX_ERROR)) {
							try {
								StyleRange style = new StyleRange(start, message.length(), FOREGROUND_ERROR,
										BACKGROUND_ERROR);
								text.setStyleRange(style);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					});
				};
			};
		}

		return printStream;
	}

	public void println(String message) {
		getPrintStream().println(message);
	}

	public void info(String message) {
		println(Const.PREFIX_INFO + message);
	}

	public void warning(String message) {
		println(Const.PREFIX_WARNING + message);
	}

	public void error(String message) {
		println(Const.PREFIX_ERROR + message);
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}
}
