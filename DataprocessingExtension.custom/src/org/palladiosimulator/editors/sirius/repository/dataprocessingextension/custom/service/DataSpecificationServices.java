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
	public static <T> T getCharacteristic(EObject resource, Collection<Object> filterList, Class<T> targetClass) {
		Collection<EReference> additionalChildReferences = new ArrayList<EReference>();
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filterList, additionalChildReferences,
				resource.eResource().getResourceSet());
		dialog.setProvidedService(targetClass);
		
		filterduplication(dialog);
		if(targetClass.equals(AbstractUserAction.class)) {
			filterUnapplyableAbstractUserActions(dialog);
			filterOtherUsageModels(dialog, resource);
		}
		
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
	
	private static void filterUnapplyableAbstractUserActions(PalladioSelectEObjectDialog dialog) {
		for(Object o : dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof ScenarioBehaviour) {
					System.out.println("remove");
					for (EObject object : ((ScenarioBehaviour) o).getActions_ScenarioBehaviour()) {
						boolean show = false;
						if(object instanceof EntryLevelSystemCall || object instanceof Delay) {
							if(!StereotypeAPI.isStereotypeApplicable((object), ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
								dialog.getTreeViewer().remove(object);
							}else {
								show = true;
							}
						}
					}
				
			}
		}
	}
	
	private static void filterOtherUsageModels(PalladioSelectEObjectDialog dialog, EObject uModel) {
		for(Object o : dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof UsageModel) {
				if(!o.equals(uModel)) {
					dialog.getTreeViewer().remove(o);
				}
			}
		}
	}
	
	
}
