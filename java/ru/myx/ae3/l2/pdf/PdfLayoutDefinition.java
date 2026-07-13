package ru.myx.ae3.l2.pdf;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.l2.ContextHandler;
import ru.myx.ae3.l2.LayoutDefinitionAbstract;

abstract class PdfLayoutDefinition implements LayoutDefinitionAbstract<PdfTargetContext>, ContextHandler<PdfTargetContext, BaseObject> {
	
	@Override
	public BaseObject onEnter(final PdfTargetContext target, final BaseObject layout) {
		
		target.dump(this.getClass().getName() + ", onEnter, layout=" + layout.baseGet("layout", BaseObject.UNDEFINED));
		return target.onEnter(target, layout);
	}

	@Override
	public abstract BaseObject onExecute(final PdfTargetContext context, final BaseObject layout);

	@Override
	public void onLeave(final PdfTargetContext target) {
		
		target.dump(this.getClass().getName() + ", onLeave");
		target.onLeave(target);
	}

	@Override
	public BaseObject onNest(final PdfTargetContext target, final BaseObject layout) {
		
		target.dump(this.getClass().getName() + ", onNest, layout=" + layout.baseGet("layout", BaseObject.UNDEFINED));
		return target.onNest(target, layout);
	}
}
