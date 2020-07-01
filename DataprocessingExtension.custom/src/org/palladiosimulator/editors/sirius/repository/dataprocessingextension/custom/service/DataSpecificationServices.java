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
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Delay;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.impl.DelayImpl;
import org.palladiosimulator.pcm.usagemodel.impl.ScenarioBehaviourImpl;
import org.palladiosimulator.pcm.usagemodel.impl.UsageScenarioImpl;

public class DataSpecificationServices {
	
	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

	
	@SuppressWarnings("unchecked")
	public static <T> T getCharacteristic(EObject resource, Collection<Object> filterList, Class<T> targetClass, DataSelectionFilter datafilter) {
		Collection<EReference> additionalChildReferences = new ArrayList<EReference>();
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filterList, additionalChildReferences,
				resource.eResource().getResourceSet());
		dialog.setProvidedService(targetClass);
		
		filterduplication(dialog);
		datafilter.filterDialog(dialog, resource);
		
		dialog.open();

		return (T) dialog.getResult();
	}
	
	private static void filterduplication(PalladioSelectEObjectDialog dialog) {
		Collection<Entity> types = new HashSet<Entity>();
		for (Object o: dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof UsageScenarioImpl) {
				if(isInCollection((Entity) o, types)) {
					dialog.getTreeViewer().remove(o);
				} else {
					types.add((Entity)o);
				}
			}
		}
	}
	
	private static boolean isInCollection(Entity lookUpObject, Collection<Entity> collection) {
		for (Entity eObject : collection) {
			if(eObject.getId().equals(lookUpObject.getId())) {
				return true;
			}
		}
		return false;
	}
	
	
}
