package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;

public interface DataSelectionFilter {
	public void filterDialog(PalladioSelectEObjectDialog dialog, EObject resource);
	
}
