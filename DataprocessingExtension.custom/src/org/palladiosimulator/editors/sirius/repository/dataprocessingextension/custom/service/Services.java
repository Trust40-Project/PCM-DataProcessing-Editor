package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;

public class Services {
	
	
	 public static OperationSignature getCorrectOperationSignature(EList<Interface> interfaceList, OperationSignatureDataRefinement targetRefinement) {
			for (Interface interfaceElement : interfaceList) {
				if(interfaceElement instanceof OperationInterface) {
					for (OperationSignature operationSignature : ((OperationInterface)interfaceElement).getSignatures__OperationInterface() ) {
						if(StereotypeAPI.isStereotypeApplied(operationSignature, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
							OperationSignatureDataRefinement returnedRef= StereotypeAPI.getTaggedValue(operationSignature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
							if(returnedRef.equals(targetRefinement)) {
								return operationSignature;
							}
						}
						System.out.println();
					}
				}
				System.out.println();
			}
			return null;
		}
	 
		
		
	public static OperationSignature getCorrectOperationSignature(EList<Interface> interfaceList, OperationSignature targetSignature) {
			for (Interface interfaceElement : interfaceList) {
				if(interfaceElement instanceof OperationInterface) {
					for (OperationSignature operationSignature : ((OperationInterface)interfaceElement).getSignatures__OperationInterface() ) {
						if(operationSignature.equals(targetSignature)) {
							return operationSignature;
						}
					}
				}
			}
			return null;
		}
	
	
//	TODO effizienter
	public static DataSpecification getCorrespondingDataspecification(Repository repo) {
		Iterator treeIt = repo.eAllContents();
		Collection<EObject> available = new HashSet<>();
		
		
		while(treeIt.hasNext()) {
			EObject object = (EObject) treeIt.next();
			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE)) {
				available.add(StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE));
			}

			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
				available.add(StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING));
			}

			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
				available.add(StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT));
			}

			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_STORE_CHARACTERIZATION)) {
				available.add(StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_STORE_CHARACTERIZATION, ProfileConstants.STEREOTYPE_NAME_STORE_CHARACTERIZATION));
			}

			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)) {
				available.add(StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING));
			}			
		}
		
		for (EObject availabEObject : available) {
			try{
				DataSpecification dataSpec = (DataSpecification) availabEObject.eContainer();
				return dataSpec;
			} catch(Exception e) {
				
			}
		}
		return null;
	}


}


