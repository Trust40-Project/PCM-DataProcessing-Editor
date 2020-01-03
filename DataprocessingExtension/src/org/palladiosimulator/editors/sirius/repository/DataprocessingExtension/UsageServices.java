package org.palladiosimulator.editors.sirius.repository.DataprocessingExtension;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.Data;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataOperation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

public class UsageServices {
	
	public String characteristicToString(Characteristic characteristic) {
		return characteristic.getCharacteristicType().getClass().getSimpleName();
	}
	
	
	public Collection<DataProcessingContainer> getDPCForUsageModel(EObject uModel){
		Collection<EObject> eAllcontent = new HashSet<>();
		Iterator<EObject> it;
		for (UsageScenario uScen : ((UsageModel)uModel).getUsageScenario_UsageModel()) {
			it = uScen.eAllContents();
			while (it.hasNext()) {
				eAllcontent.add(it.next());
			}
		}
		Collection<DataProcessingContainer> dataProcessingContainers = new HashSet<DataProcessingContainer>();
		Set<EObject> elemAction = new HashSet<>();

		for (EObject eObject : eAllcontent) {
			elemAction.add(eObject);
//			System.out.println(eObject.getClass());
//			System.out.println(StereotypeAPI.hasAppliedStereotype(elemAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING));
			if(StereotypeAPI.hasAppliedStereotype(elemAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
				dataProcessingContainers.add(StereotypeAPI.getTaggedValue(eObject, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING));
			}
			elemAction.clear();
		}
		return dataProcessingContainers;
	}
	
	public Collection<Data> getIncommingData(DataOperation dataOp){
		return dataOp.getIncomingData();
	}
	
	
}
