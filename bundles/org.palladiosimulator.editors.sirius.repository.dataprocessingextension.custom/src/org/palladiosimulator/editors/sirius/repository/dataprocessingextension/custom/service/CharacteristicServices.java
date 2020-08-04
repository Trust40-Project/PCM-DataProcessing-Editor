package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.impl.CharacteristicsFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;

public class CharacteristicServices {

	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

	
	public static void addCharacteristicToElement(DataSpecification dataSpec, EObject object){
		if(dataSpec != null) {
			CharacteristicContainer characContainer;
			if(StereotypeAPI.isStereotypeApplicable(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) || 
					(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) && 
					StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE).equals(null))) {
				characContainer = CharacteristicsFactoryImpl.init().createCharacteristicContainer();
				dataSpec.getCharacteristicContainer().add(characContainer);
				StereotypeAPI.applyStereotype(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
				StereotypeAPI.setTaggedValue(object, characContainer, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER);
				characContainer.setEntityName(((Entity)object).getEntityName() + "Container");
			}else {
				characContainer = StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
			}
			addCharacteristic(dataSpec, characContainer);
		}
	}
	
	private static void addCharacteristic(DataSpecification dataSpec, CharacteristicContainer characContainer) {
		Characteristic newCharac = CharacteristicsFactoryImpl.init().createEnumCharacteristic();
		newCharac.setCharacteristicType(getCharacteristic(dataSpec));
		characContainer.getOwnedCharacteristics().add(newCharac);
	}
	
	public static CharacteristicType getCharacteristic(DataSpecification dataSpec) {
		Collection<Object> filter = new ArrayList<Object>();

//		filter.add(DataSpecification.class);
//		filter.add(CharacteristicContainer.class);
//		filter.add(Characteristic.class);
		filter.add(CharacteristicType.class);
		filter.add(CharacteristicTypeContainer.class);

		Collection<EReference> additionalChildReferences = new ArrayList<EReference>();
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filter, additionalChildReferences,
				dataSpec.eResource().getResourceSet());
		dialog.setProvidedService(CharacteristicType.class);
		
		filterCharacteristic(dialog);
		
		dialog.open();

		return (CharacteristicType) dialog.getResult();
	}
	
	private static void filterCharacteristic(PalladioSelectEObjectDialog dialog) {
		Collection<CharacteristicTypeContainer> types = new HashSet<CharacteristicTypeContainer>();
		for (Object o: dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof CharacteristicTypeContainer) {
				if(isInCollection((CharacteristicTypeContainer) o, types)) {
					dialog.getTreeViewer().remove(o);
				} else {
					types.add((CharacteristicTypeContainer)o);
				}
			}
		}
	}
	
	private static boolean isInCollection(CharacteristicTypeContainer lookUpObject, Collection<CharacteristicTypeContainer> collection) {
		for (CharacteristicTypeContainer eObject : collection) {
			if(eObject.getId().equals(lookUpObject.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public static void removeCharacteristic(EObject appliedObject, Characteristic characteristic) {
		if(StereotypeAPI.isStereotypeApplied(appliedObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE)) {
			CharacteristicContainer characContainer = StereotypeAPI.getTaggedValue(appliedObject, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
			if(characContainer == null) {
				return;
			}
			if(characContainer.getOwnedCharacteristics().contains(characteristic)) {
				if(characContainer.getOwnedCharacteristics().size() > 1) {
					characContainer.getOwnedCharacteristics().remove(characteristic);
				}else if(characContainer.eContainer() != null){
					((DataSpecification) characContainer.eContainer()).getCharacteristicContainer().remove(characContainer);
					StereotypeAPI.unapplyStereotype(appliedObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
				}else {
					StereotypeAPI.unapplyStereotype(appliedObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
				}
				return;
			}
		}
	}
}
