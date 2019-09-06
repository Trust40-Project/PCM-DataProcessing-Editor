package org.palladiosimulator.editors.sirius.repository.DataprocessingExtension;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.ParameterBasedData;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.ResultBasedData;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.Store;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.StoreContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationSignature;


/**
 * The services class used by VSM.
 */
public class Services {
    
    //String für dataSignature
    public String getDataRefinement(OperationSignature signature) {
    	OperationSignatureDataRefinement refinements = StereotypeAPI.getTaggedValue(signature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
    	
    	//System.out.println(refinements.toString());
    	Collection<ParameterBasedData> param = refinements.getParameterRefinements();
    	Collection<ResultBasedData> result = refinements.getResultRefinements();
    	//String returnValue = refinements.getParameterRefinements().toString();
    	//returnValue += refinements.getResultRefinements();
    	String returnValue ="";
    	for (ParameterBasedData parameterBasedData : param) {
			returnValue += parameterBasedData.toString() + "\n";
		}
    	for (ResultBasedData resultBasedData : result) {
			returnValue += resultBasedData.toString() + "\n";
		}
    	
    	returnValue.trim();
//    	String signatureaName = signature.getEntityName();
    	
    	//System.out.println("testOutput2"+ refinements.getClass());
    	return returnValue;
    }
    
    public Collection<Store> getStores(BasicComponent basicComponent) {
    	StoreContainer obj2 = StereotypeAPI.getTaggedValue(basicComponent, ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
    	
    	return obj2.getStores();
    }
    
    public String getStoreName(Store store) {
    	return store.getEntityName();
    }
}
