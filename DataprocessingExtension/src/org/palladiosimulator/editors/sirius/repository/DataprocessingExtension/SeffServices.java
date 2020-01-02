package org.palladiosimulator.editors.sirius.repository.DataprocessingExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.Data;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataOperation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.PerformDataTransmissionOperation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.util.DataMapping;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.seff.AbstractAction;

public class SeffServices {
	public Collection<DataProcessingContainer> getDataProcessingContainer(EObject seff){
		TreeIterator<EObject> it = seff.eAllContents();
		Collection<AbstractAction> actions = new ArrayList<AbstractAction>();
		
		while (it.hasNext()) {
			EObject next = it.next();
			if(next instanceof AbstractAction) {
				actions.add((AbstractAction) next);
			}
		}
		Set<EObject> elemAction = new HashSet<>();
		Set<EObject> elemsWithData = new HashSet<>();
		
		
		for (AbstractAction abstractAction : actions) {
			elemAction.add((EObject) abstractAction);
			if(StereotypeAPI.hasAppliedStereotype(elemAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
				elemsWithData.add((EObject) abstractAction);
			};
			elemAction.clear();
		}
		
		Collection<Data> dataToReturn = new ArrayList<>();
		Collection<DataProcessingContainer> dpContainer = new ArrayList<>();
		for (EObject eObject : elemsWithData) {
			dpContainer.add(StereotypeAPI.getTaggedValue(eObject, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING));
		}		
		return dpContainer;
	}
//	
//	
//
	public Collection<DataOperation> getDataOperation(DataProcessingContainer dpContainer){
		return dpContainer.getOperations();
	}
//	
	public EObject connectAActionWithDataProcessingContainer(AbstractAction aaction) {
		Set<EObject> abstractActions = new HashSet<>();
		abstractActions.add((EObject) aaction);
		EObject dpc = null;
		if(StereotypeAPI.hasAppliedStereotype(abstractActions, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
			dpc = StereotypeAPI.getTaggedValue((EObject) aaction, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING);
		}
		return dpc;
	}
//	
//	public Collection<OperationRequiredRole> getExternalCallRequired(EObject seff) {
//		TreeIterator<EObject> it = seff.eAllContents();
//		Collection<ExternalCallAction> externalActions = new ArrayList<ExternalCallAction>();
//		
//		while (it.hasNext()) {
//			EObject next = it.next();
//			if(next instanceof ExternalCallAction) {
//				externalActions.add((ExternalCallAction) next);
//			}
//		}
//		
//		Collection<OperationRequiredRole> signatures = new ArrayList<>();
//		for (ExternalCallAction eaction : externalActions) {
//			signatures.add(eaction.getRole_ExternalService());
//		}
//		
//		return signatures;
//	}
//	
//	public DataProcessingContainer externalCallToDataProcessingContainer(ExternalCallAction eaction) {
//			return StereotypeAPI.getTaggedValue((EObject) eaction, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING);
//	}
//	
//	public OperationRequiredRole externalCallToOperationRequiredRole(ExternalCallAction eaction) {
//		return eaction.getRole_ExternalService();
//	}
//	
//	public Collection<Data> getTransmittedData(DataProcessingContainer dpc) {
//		List<DataOperation> transmissionList = dpc.getOperations().stream().filter(operation -> operation instanceof PerformDataTransmissionOperation).collect(Collectors.toList());
//		Collection<Data> dataCollection = new HashSet<>();
//		for (DataOperation dataOperation : transmissionList) {
//			System.out.println("In");
//			for (DataMapping data : ((PerformDataTransmissionOperation) dataOperation).getInputMappings()) {
//				System.out.println(data.getFrom());
//				System.out.println(data.getTo());
//				dataCollection.add(data.getFrom());
//				dataCollection.add(data.getTo());
//			}
//			System.out.println("out");
//			for (DataMapping data : ((PerformDataTransmissionOperation) dataOperation).getOutputMappings()) {
//				System.out.println(data.getFrom());
//				System.out.println(data.getTo());
//				dataCollection.add(data.getFrom());
//				dataCollection.add(data.getTo());
//			}
//		}
//		System.out.println();
//		return dataCollection;
//	}
//	
//
//
//
//
//
//
	public String getTransmissionFromString(DataMapping dm){
		return dm.getFrom().getEntityName();
	}
//
	public String getTransmissionToString(DataMapping dm){
		return dm.getTo().getEntityName();
	}
//
//	
	public Collection<DataMapping> getInputDataMapping(DataOperation dataOp){
		if(dataOp instanceof PerformDataTransmissionOperation) {
			Collection<DataMapping> dataMappings = new HashSet();
			for (DataMapping dataMapping : ((PerformDataTransmissionOperation) dataOp).getInputMappings()) {
				dataMappings.add(dataMapping);
			}
			return dataMappings;
		}
		return null;
	}
//	
	public Collection<DataMapping> getOutputDataMapping(DataOperation dataOp){
		if(dataOp instanceof PerformDataTransmissionOperation) {
			Collection<DataMapping> dataMappings = new HashSet();
			for (DataMapping dataMapping : ((PerformDataTransmissionOperation) dataOp).getOutputMappings()) {
				dataMappings.add(dataMapping);
			}
			return dataMappings;
		}
		return null;
	}
//	
//	
//	public EObject self (EObject self) {
//		return self;
//	}
//	
////	public PerformDataTransmissionOperation getPerformDataTransmissionOperation(DataOperation dataOperation){
////		if(dataOperation instanceof PerformDataTransmissionOperation) {
////			return (PerformDataTransmissionOperation) dataOperation;
////		}
////		return null;
////	}
////	
////	public Collection<Data> getTransmittedDataOutputTo(PerformDataTransmissionOperation dataOperation){
////		Collection<Data> dataCollection = new HashSet<>();
////			for (DataMapping data : dataOperation.getOutputMappings()) {
////				dataCollection.add(data.getTo());
////			}
////		return dataCollection;
////	}
////	
////	public Collection<Data> getTransmittedDataInputFrom(PerformDataTransmissionOperation dataOperation){
////		Collection<Data> dataCollection = new HashSet<>();
////			for (DataMapping data : dataOperation.getInputMappings()) {
////				dataCollection.add(data.getFrom());
////			}
////		return dataCollection;
////	}
////	
////		
	public Collection<PerformDataTransmissionOperation> getPerformDataTransmissionOperationFromContainer(DataProcessingContainer dpc){
		List<DataOperation> transmissionList = dpc.getOperations().stream().filter(operation -> operation instanceof PerformDataTransmissionOperation).collect(Collectors.toList());
		Collection<PerformDataTransmissionOperation> dataOperationCollection = new HashSet<>();
		for (DataOperation dataOperation : transmissionList) {
			dataOperationCollection.add((PerformDataTransmissionOperation)dataOperation);
		}
		return dataOperationCollection;
	}
////	
////	public Collection<Data> getTransmittedDataInputTo(PerformDataTransmissionOperation dataTransmision){
////		Collection<Data> dataCollection = new HashSet<>();
////			for (DataMapping data : dataTransmision.getInputMappings()) {
////				dataCollection.add(data.getTo());
////		}
////		return dataCollection;
////	}
////	
////	public Collection<Data> getTransmittedDataOutputFrom(PerformDataTransmissionOperation dataTransmision){
////		Collection<Data> dataCollection = new HashSet<>();
////			for (DataMapping data : dataTransmision.getOutputMappings()) {
////				dataCollection.add(data.getFrom());
////			}
////		return dataCollection;
////	}
////	
////	
//
}
