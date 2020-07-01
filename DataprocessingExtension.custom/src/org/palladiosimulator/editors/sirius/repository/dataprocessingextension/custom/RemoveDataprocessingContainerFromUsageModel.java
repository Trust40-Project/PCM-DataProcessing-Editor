package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.usagemodel.Delay;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

public class RemoveDataprocessingContainerFromUsageModel implements IExternalJavaAction {


	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		DataProcessingContainer dpContainer = (DataProcessingContainer)arg1.get("element");
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("containerView"), DSemanticDiagram.class);
		UsageModel usageModel = (UsageModel) semanticDiagram.getTarget();
		DataSpecification dataSpec = Services.getParentOfType(dpContainer, DataSpecification.class);
		
		for (UsageScenario uScenario : usageModel.getUsageScenario_UsageModel()) {
			for (EObject aAction: uScenario.getScenarioBehaviour_UsageScenario().getActions_ScenarioBehaviour()) {
				if(aAction instanceof EntryLevelSystemCall || aAction instanceof Delay) {
					if(StereotypeAPI.isStereotypeApplied(aAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
//						System.out.println("applied");
						if(StereotypeAPI.getTaggedValue(aAction, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING).equals(dpContainer)){
							StereotypeAPI.unapplyStereotype(aAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING);
						}
					}
				}
			}
		}
		dataSpec.getDataProcessingContainers().remove(dpContainer);
	}

}
