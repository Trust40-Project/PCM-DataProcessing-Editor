package my.storeModel.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;

import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;

/**
 * The services class used by VSM.
 */
public class Services {
	



	
	
	/**
	 * if the Component has the Stereotype StoreHaving, return the connected StoreContainer
	 */
	public EObject getStoreContainer(EObject component) {

		if (!StereotypeAPI.getStereotypeApplications(component, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)
				.isEmpty()) {
			EList<StereotypeApplication> pcmEntityStereotypeApplications = StereotypeAPI
					.getStereotypeApplications(component, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
			StereotypeApplication stereotypeApplication = pcmEntityStereotypeApplications.get(0);
			Stereotype stereotype = stereotypeApplication.getStereotype();
			EStructuralFeature taggedValue = stereotype.getTaggedValue(ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER);
			EObject container = (EObject) stereotypeApplication.eGet(taggedValue);
			
		
			return container;
		}
		return null;

	}

	
	/**
	 * return all the storeContainers of the BasicComponents in the current repository
	 */
	public List<EObject> getAllStoreContainers(EObject repo) {

		EList<EObject> componentList = repo.eContents();
		List<EObject> containers = new ArrayList<EObject>();

		for (int i = 0; i < componentList.size(); i++) {
			
			EObject component = componentList.get(i);
			
			containers.add(getStoreContainer(component));
		}

		return containers;

	}
	
	
	
	
	
}
