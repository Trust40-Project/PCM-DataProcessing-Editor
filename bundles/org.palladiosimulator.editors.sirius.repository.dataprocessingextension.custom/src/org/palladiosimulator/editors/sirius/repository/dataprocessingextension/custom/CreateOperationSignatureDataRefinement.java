package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.impl.RepositoryFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;

public class CreateOperationSignatureDataRefinement implements IExternalJavaAction {

	public CreateOperationSignatureDataRefinement() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		// TODO Auto-generated method stub
		for (EObject eObject : arg0) {
			if(eObject instanceof OperationSignature) {
				return !StereotypeAPI.isStereotypeApplied(eObject, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
			}
		}
		return false;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		DNodeContainer nodeSpec = (DNodeContainer) arg1.get("containerView");

		DSemanticDecorator decorator = (DSemanticDecorator) nodeSpec.getParentDiagram();
		Repository repo = (Repository) decorator.getTarget();
//		((Repository)((DSemanticDecorator)((DNodeList) arg1.get("containerView")).getParentDiagram()).getTarget()).getInterfaces__Repository();
		
		OperationSignature opSig = Services.getCorrectOperationSignature(repo.getInterfaces__Repository(), (OperationSignature) arg1.get("container"));
		
		OperationSignatureDataRefinement opSigDataRef = RepositoryFactoryImpl.init().createOperationSignatureDataRefinement();
		opSigDataRef.setEntityName(opSig.getInterface__OperationSignature().getEntityName()+"_"+opSig.getEntityName());
			
		DataSpecification dataSpec = Services.getCorrespondingDataspecification(repo.eAllContents(), repo);
		dataSpec.getOperationSignatureDataRefinement().add(opSigDataRef);
			
		StereotypeAPI.applyStereotype(opSig, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
		StereotypeAPI.setTaggedValue(opSig, opSigDataRef, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
	}
	
	
	
	

}
