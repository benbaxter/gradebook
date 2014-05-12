package com.benjamingbaxter.gradebook.android.view;

import java.util.List;

import android.view.View;

public abstract class MutateModelOnClickListener implements View.OnClickListener {
	@Override
	public void onClick(View v) {
		viewToModel();
		List<String> errors = validate();
		if( errors.isEmpty() ) {
			clearErrors();
			
			performAction();
			
			modelToView(view());
		} else {
			addErrorsToView(errors);
		}
	}
	
	protected abstract void performAction();
	
	protected abstract void viewToModel();
	
	protected abstract View view();
	
	protected abstract void modelToView(View view);
	
	protected abstract List<String> validate();
	
	protected abstract void clearErrors();
	
	protected abstract void addErrorsToView(List<String> errors);
}