package com.epri.metric_calculator.actions.viewaction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import com.epri.metric_calculator.views.ConsoleView;

public abstract class AbsConsoleViewAction extends Action {

	private String viewId;
	private String perspectiveId;

	private ConsoleView consoleView;
	private IPerspectiveDescriptor perspectiveDesc;

	public AbsConsoleViewAction(String text, Image image, String viewId, String perspectiveId) {
		setText(text);
		setImageDescriptor(ImageDescriptor.createFromImage(image));

		this.viewId = viewId;
		this.perspectiveId = perspectiveId;
	}

	@Override
	public final void run() {
		perspectiveDesc = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(perspectiveId);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(perspectiveDesc);

		consoleView = (ConsoleView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(viewId);

		run(consoleView);
	}

	protected abstract void run(ConsoleView consoleView);
}
