package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DSemanticDiagramSpec;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.ParameterBasedData;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.repository.impl.ParameterImpl;

public class SetVarParameterForParameterBasedData implements IExternalJavaAction {

	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();


	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		ParameterBasedData e1 = (ParameterBasedData) arg1.get("instance");

		//((Repository)((DSemanticDecorator)((DNodeList) arg1.get("vontainerView")).getParentDiagram()).getTarget()).getInterfaces__Repository();
		OperationSignature opSig = Services.getCorrectOperationSignature(((Repository)((DSemanticDecorator)((DNodeList) arg1.get("containerView")).getParentDiagram()).getTarget()).getInterfaces__Repository(), (OperationSignatureDataRefinement) arg1.get("container"));	
		if(opSig.equals(null)) {
			return;
		}		 

		Parameter newParameter = getParameter(opSig);
		
		e1.setParameter(newParameter);
	}
	
	private Parameter getParameter(OperationSignature opSig) {
		Collection<Object> filter = new ArrayList<Object>();

		filter.add(Repository.class);
		filter.add(Interface.class);
		filter.add(OperationSignature.class);
		filter.add(Parameter.class);

		Collection<EReference> additionalChildReferences = new ArrayList<EReference>();
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filter, additionalChildReferences,
				opSig.eResource().getResourceSet());
		dialog.setProvidedService(Parameter.class);
		
		filterDialogTree(dialog, opSig);
		
		dialog.open();

		return (Parameter) dialog.getResult();
	}
	
	private void filterDialogTree(PalladioSelectEObjectDialog dialog, OperationSignature opSig) {
		for(Object o : dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof Repository) {
					boolean found = false;
				for (Interface  interf: ((Repository) o).getInterfaces__Repository()) {
					if(((OperationInterface) interf).getSignatures__OperationInterface().contains(opSig)) {
						found = true;
						break;
					}
				}
				if(!found) {
					dialog.getTreeViewer().remove(o);
				}
			}
		}
		
		for(Object o : dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof Interface) {
				if(!((OperationInterface) o).getSignatures__OperationInterface().contains(opSig)) {
					dialog.getTreeViewer().remove(o);
				}
			}
		}
		
		for(Object o:dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof OperationSignature) {
				if(!o.equals(opSig)) {
					dialog.getTreeViewer().remove(o);
				}
			}
		}
			
	}
	
	
	
//	public OperationSignature getCorrectInterface(EList<Interface> interfaceList, OperationSignatureDataRefinement targetRefinement) {
//		for (Interface interfaceElement : interfaceList) {
//			if(interfaceElement instanceof OperationInterface) {
//				for (OperationSignature operationSignature : ((OperationInterface)interfaceElement).getSignatures__OperationInterface() ) {
//					if(StereotypeAPI.isStereotypeApplied(operationSignature, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
//						OperationSignatureDataRefinement returnedRef= StereotypeAPI.getTaggedValue(operationSignature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
//						if(returnedRef.equals(targetRefinement)) {
//							return operationSignature;
//							
//						}
//					}
//				}
//			}
//		}
//		return null;
//	}

}
