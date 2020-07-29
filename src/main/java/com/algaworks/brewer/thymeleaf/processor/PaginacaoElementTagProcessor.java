package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class PaginacaoElementTagProcessor extends AbstractElementTagProcessor{

	private static final String NOME_TAG = "paginacao";
	private static final int PRECEDENCIA = 1000;

	public PaginacaoElementTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML,dialectPrefix, NOME_TAG,true,null,false, PRECEDENCIA );
	}
	@Override
	protected void doProcess(ITemplateContext context, 
			IProcessableElementTag tag, 
			IElementTagStructureHandler structureHandler) {
		IModelFactory modelfactory = context.getModelFactory();
		
		IAttribute page= tag.getAttribute("page");
		IModel model = modelfactory.createModel();
		
		model.add(modelfactory.createStandaloneElementTag("th:block",
				"th:replace",
				String.format("fragments/Paginacao :: paginar(%s)",
						page.getValue())));
		structureHandler.replaceWith(model, true);
	
	}

}