package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.CharacteristicServices;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

public class RemoveCharacteristicFromResourceEnvironment implements IExternalJavaAction {

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		Characteristic characteristic = (Characteristic)arg1.get("element");
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("containerView"), DSemanticDiagram.class);
		ResourceEnvironment resInvironment = (ResourceEnvironment) semanticDiagram.getTarget();
		
		for (EObject eObject : resInvironment.getLinkingResources__ResourceEnvironment()) {
			CharacteristicServices.removeCharacteristic(eObject, characteristic);
		}
		for (EObject eObject : resInvironment.getResourceContainer_ResourceEnvironment()) {
			CharacteristicServices.removeCharacteristic(eObject, characteristic);
			for (EObject eObject2 : ((ResourceContainer)eObject).getNestedResourceContainers__ResourceContainer()) {
				CharacteristicServices.removeCharacteristic(eObject2, characteristic);

			}
		}
	}

}
