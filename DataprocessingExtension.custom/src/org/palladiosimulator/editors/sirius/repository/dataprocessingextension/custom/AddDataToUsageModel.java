package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.AddDataService;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataOperation;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

public class AddDataToUsageModel implements IExternalJavaAction {


	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	
	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		for (EObject eObject : arg0) {
			if(eObject instanceof DataOperation) {
				for (EStructuralFeature feature : eObject.eClass().getEAllStructuralFeatures()) {
					if(feature instanceof EReference) {
						if(((EReference)feature).isContainment()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		DataOperation dataOp = (DataOperation) arg1.get("element");
		EReference eref = AddDataService.selectEReference(dataOp); 

		
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("elementView"), DSemanticDiagram.class);
		UsageModel usageModel = (UsageModel) semanticDiagram.getTarget();
		EClass selectedDataType = AddDataService.selectClassifier(usageModel, dataOp);
		
		
		AddDataService.addDataElement(eref, dataOp, selectedDataType);		
	}
}
