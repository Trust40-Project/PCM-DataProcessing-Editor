package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;

public class Services {
	
	
 public static OperationSignature getCorrectInterface(EList<Interface> interfaceList, OperationSignatureDataRefinement targetRefinement) {
		for (Interface interfaceElement : interfaceList) {
			if(interfaceElement instanceof OperationInterface) {
				for (OperationSignature operationSignature : ((OperationInterface)interfaceElement).getSignatures__OperationInterface() ) {
					if(StereotypeAPI.isStereotypeApplied(operationSignature, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
						OperationSignatureDataRefinement returnedRef= StereotypeAPI.getTaggedValue(operationSignature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
						if(returnedRef.equals(targetRefinement)) {
							return operationSignature;
							
						}
					}
				}
			}
		}
		return null;
	}
}
