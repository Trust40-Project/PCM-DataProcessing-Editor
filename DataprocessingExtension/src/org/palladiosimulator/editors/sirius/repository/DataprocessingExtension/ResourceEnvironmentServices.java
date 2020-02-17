package org.palladiosimulator.editors.sirius.repository.DataprocessingExtension;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;

public class ResourceEnvironmentServices {
    	
	public Collection<Characteristic> getCharacteristic(EObject eObject) {
    	Set<EObject> basicComponentSet = new HashSet<>();
    	basicComponentSet.add(eObject);
    	if(StereotypeAPI.hasAppliedStereotype(basicComponentSet, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE)) {
    		CharacteristicContainer characteristic = (StereotypeAPI.getTaggedValue(eObject, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE));
    		return characteristic.getOwnedCharacteristics();
    	}
    	return null;
    }
	
	public String getCharacteristicID(Characteristic characteristic) {
		return "Characteristic: " + characteristic.getId();
	}
	
	public CharacteristicType getCharacteristicType(Characteristic characteristic) {
		return characteristic.getCharacteristicType();
	}

	public String characteristicTypeToString(CharacteristicType type) {
		return type.getEntityName() +": "+ type.getId();
	}
	
}
