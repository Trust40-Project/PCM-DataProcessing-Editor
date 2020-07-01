package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.PerformDataTransmissionOperation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.util.DataMapping;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.util.UtilFactory;

public class AddOutputMapping implements IExternalJavaAction {

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		PerformDataTransmissionOperation op = (PerformDataTransmissionOperation) arg1.get("container");
		DataMapping dm = UtilFactory.eINSTANCE.createDataMapping();
		op.getOutputMappings().add(dm);
	}

}
