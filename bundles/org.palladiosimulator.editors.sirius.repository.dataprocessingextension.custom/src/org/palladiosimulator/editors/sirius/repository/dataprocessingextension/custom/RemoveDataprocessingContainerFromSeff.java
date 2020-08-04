package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

public class RemoveDataprocessingContainerFromSeff implements IExternalJavaAction {


	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		DataProcessingContainer dpContainer = (DataProcessingContainer)arg1.get("element");
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("containerView"), DSemanticDiagram.class);
		ResourceDemandingSEFF seff = (ResourceDemandingSEFF) semanticDiagram.getTarget();
		DataSpecification dataSpec = Services.getParentOfType(dpContainer, DataSpecification.class);
		
		for (AbstractAction	abstractAction : seff.getSteps_Behaviour()) {
			if(!(abstractAction instanceof StartAction || abstractAction instanceof StopAction)) {
				if(StereotypeAPI.isStereotypeApplied(abstractAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
					if(StereotypeAPI.getTaggedValue(abstractAction, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING).equals(dpContainer)) {
						StereotypeAPI.unapplyStereotype(abstractAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING);
					}
				}
			}
		}
		dataSpec.getDataProcessingContainers().remove(dpContainer);
		
		
	}

}
