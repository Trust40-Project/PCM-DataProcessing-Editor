package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;

public interface DataopSelectionFilter {
	void filterOtherModels(PalladioSelectEObjectDialog dialog, EObject resource, DataProcessingContainer dpContainer);
	void removeUnapplyableElements(PalladioSelectEObjectDialog dialog, DataProcessingContainer dpContainer);
}
