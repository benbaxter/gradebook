package com.benjamingbaxter.gradebook.android.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.benjamingbaxter.gradebook.model.AssignmentWeight;

public final class AssignmentWeightTypeLabelComparator implements
		Comparator<AssignmentWeight> {
	@Override
	public int compare(AssignmentWeight lhs, AssignmentWeight rhs) {
		return lhs.getAssignmentType().getLabel().compareTo(rhs.getAssignmentType().getLabel());
	}
	
	public List<AssignmentWeight> sortAssignmentWeights(Set<AssignmentWeight> unsortedSet) {
		List<AssignmentWeight> list = new ArrayList<AssignmentWeight>(unsortedSet);
		Collections.sort(list, new AssignmentWeightTypeLabelComparator());
		return list;
	}
}