package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;

public class RemoveOperationSignatureDataRefinement implements IExternalJavaAction {

	public RemoveOperationSignatureDataRefinement() {
		
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		
		OperationSignatureDataRefinement opSigDataRef = (OperationSignatureDataRefinement)arg1.get("element");
		
		DNodeContainer nodeSpec = (DNodeContainer) arg1.get("containerView");
		DSemanticDecorator decorator = (DSemanticDecorator) nodeSpec.getParentDiagram();
		Repository repo = (Repository) decorator.getTarget();
		OperationSignature opSig = Services.getCorrectOperationSignature(repo.getInterfaces__Repository(), opSigDataRef);
		if(opSig == null) {

		}else {
		
		DataSpecification dataSpec = Services.getParentOfType(opSigDataRef, DataSpecification.class);

		if(StereotypeAPI.isStereotypeApplied(opSig, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
			StereotypeAPI.unapplyStereotype(opSig, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
		}
		
		dataSpec.getOperationSignatureDataRefinement().remove(opSigDataRef);
		}
	}

}
