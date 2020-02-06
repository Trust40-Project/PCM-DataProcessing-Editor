package org.palladiosimulator.editors.sirius.repository.DataprocessingExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.ParameterBasedData;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.ResultBasedData;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.Store;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.StoreContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;


/**
 * The services class used by VSM.
 * @param <E>
 */
public class RepositoryServices<E> {
    public Collection<OperationInterface> getSetCorrespondingInterfaces(Repository repo){
    	Collection<Interface> interfaceSet = repo.getInterfaces__Repository();
    	Collection<OperationInterface> operationInterfaceSet = new ArrayList<>();
    	
    	for (Interface interface1 : interfaceSet) {
    		if((interface1 instanceof OperationInterface)) {
    			operationInterfaceSet.add((OperationInterface)interface1);
    		}
    			
    	}
    	return  operationInterfaceSet;
    }
	
	
	
    public String getDataRefinement(OperationSignature signature) {
    	OperationSignatureDataRefinement refinements = StereotypeAPI.getTaggedValue(signature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
    	Collection<ParameterBasedData> param = refinements.getParameterRefinements();
    	Collection<ResultBasedData> result = refinements.getResultRefinements();
    	String returnValue ="";
    	for (ParameterBasedData parameterBasedData : param) {
			returnValue += parameterBasedData.toString() + "\n";
		}
    	for (ResultBasedData resultBasedData : result) {
			returnValue += resultBasedData.toString() + "\n";
		}
    	returnValue.trim();
    	return returnValue;
    }
    
    public Collection<ParameterBasedData> getParameterBasedData(OperationSignature signature) {
    	Set<OperationSignature> signatureSet = new HashSet<>();
    	signatureSet.add(signature);
    	if(StereotypeAPI.hasAppliedStereotype(signatureSet, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
    		OperationSignatureDataRefinement refinements = StereotypeAPI.getTaggedValue(signature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
    		return refinements.getParameterRefinements();
    	}
    	
    	//TODO warum ist hier ein elemeent ohne applieter stereotype??
    	return null;
    }
    

    
    public Collection<ResultBasedData> getReturnBasedData(OperationSignature signature) {
    	Set<OperationSignature> signatureSet = new HashSet<>();
    	signatureSet.add(signature);
    	if(StereotypeAPI.hasAppliedStereotype(signatureSet, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
    	    	OperationSignatureDataRefinement refinements = StereotypeAPI.getTaggedValue(signature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
    	    
    	    	return refinements.getResultRefinements();
    	}
    	return null;
    }
    
    public String nameOfParameterBasedData(ParameterBasedData param) {
    	return param.getParameter().getParameterName() + "  " + param.getEntityName();
    }
    
    
    public Collection<ResultBasedData> getResultBasedData(OperationSignature signature) {
    	OperationSignatureDataRefinement refinements = StereotypeAPI.getTaggedValue(signature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
    	return refinements.getResultRefinements();
    }
    
    public String nameOfResultBasedData(ResultBasedData param) {
    	return "return: " + param.getEntityName();
    }
    
    
    public Collection<Store> getStores(BasicComponent basicComponent) {
    	Set<BasicComponent> basicComponentSet = new HashSet<>();
    	basicComponentSet.add(basicComponent);
    	if(StereotypeAPI.hasAppliedStereotype(basicComponentSet, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)) {
    		return ((StoreContainer) StereotypeAPI.getTaggedValue(basicComponent, ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)).getStores();
    	}
    	return null;
    }
    
    public String getStoreName(Store store) {
    	return store.getEntityName();
    }    
    
    
    public OperationSignatureDataRefinement getRefinement(OperationSignature signature ) {
		if(StereotypeAPI.isStereotypeApplied(signature, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
			return StereotypeAPI.getTaggedValue(signature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
		}
    	return null;
    }
    
    public Collection<ParameterBasedData> getParameterBasedDataOfRef(OperationSignatureDataRefinement signature) {
    	return signature.getParameterRefinements();
    }

    
    public Collection<ResultBasedData> getReturnBasedDataOfRef(OperationSignatureDataRefinement signature) {
    	return signature.getResultRefinements();
    }
    
}
