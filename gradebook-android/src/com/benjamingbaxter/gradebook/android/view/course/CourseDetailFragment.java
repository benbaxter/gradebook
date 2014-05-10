package com.benjamingbaxter.gradebook.android.view.course;


import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteAssignmentTypeDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.android.view.AssignmentWeightTypeLabelComparator;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.dao.AssignmentTypeDao;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.AssignmentType;
import com.benjamingbaxter.gradebook.model.AssignmentWeight;
import com.benjamingbaxter.gradebook.model.Course;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;

public class CourseDetailFragment extends DetailsFragment {
	
	protected final String TAG = this.getClass().getSimpleName();
	protected Course currentCourse;
	protected CourseDao courseDao;
	List<AssignmentType> assTypes;
	protected AssignmentTypeDao assignmentTypeDao;
	
	private final TableRow.LayoutParams params = 
			new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT, 1f);

	Comparator<AssignmentWeight> assignmentWeightComparator = new AssignmentWeightTypeLabelComparator();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO: FIXME! just like in the list frag, want to inject instead
		GradebookDbHelper dbHelper = new GradebookDbHelper(getActivity());
		courseDao = new SqliteCourseDao(dbHelper);
		assignmentTypeDao = new SqliteAssignmentTypeDao(dbHelper);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail_course, container, false);
		//TODO: maybe use this variable and have one listener instead of multiple?
		boolean addingNew = getArgumentsEnsureNotNull().getBoolean(EXTRA_DETAILS_ADD_MODE);
		if( addingNew ) {
			openAddDetails(rootView);
		} else {
			Object obj = getArgumentsEnsureNotNull().get(EXTRA_DETAILS_ID);
			if( obj instanceof ScreenModelObject ) {
				loadDetails((ScreenModelObject) obj, rootView);
			}
		}
		
		Query<AssignmentType> query = assignmentTypeDao.findAll();
		assTypes = query.all();
		query.close();
		
		return rootView;
	}

	private View createAssignmentTypeLabel(AssignmentType type) {
		TextView label = new TextView(getActivity());
		label.setText(type.getLabel());
		return label;
	}
	
	private View createWeightLabel(AssignmentWeight weight) {
		TextView label = new TextView(getActivity());
		label.setText(weight.getWeight().toString());
		return label;
	}

	private View createAssignmentTypeCheckBox(AssignmentType assType, boolean checked) {
		CheckBox checkbox = new CheckBox(getActivity());
		checkbox.setText(assType.getLabel());
		checkbox.setChecked(checked);
		checkbox.setTag("checkbox"+assType.getId());
		return checkbox;
	}
	
	private View createInvisibleCheckBox() {
		CheckBox checkbox = new CheckBox(getActivity());
		checkbox.setVisibility(View.INVISIBLE);
		return checkbox;
	}
	
	private View createAssignmentTypeWeight(AssignmentWeight weight, Long id) {
		EditText weightField = new EditText(getActivity());
		weightField.setLayoutParams(params);
		weightField.setHint(R.string.weight);
		if( weight != null ) {
			weightField.setText(String.valueOf(weight.getWeight()));
		}
		weightField.setTag("assWeight" + id);
		weightField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		return weightField;
	}
	
	private View createInvisibleEditText() {
		EditText weightField = new EditText(getActivity());
		weightField.setVisibility(View.INVISIBLE);
		weightField.setLayoutParams(params);
		return weightField;
	}

	private void clearAssTypesInputs() {
		LinearLayout layout = ((LinearLayout) getView()
				.findViewById(R.id.detail_edit_layout_container)
				.findViewById(R.id.assignment_types_layout));
		for (AssignmentType type : assTypes) {
			
			if( layout.findViewWithTag("checkbox" + type.getId()) != null ) {
				((CheckBox) layout.findViewWithTag("checkbox" + type.getId())).setChecked(false);
				((EditText) layout.findViewWithTag("assWeight" + type.getId())).setText("");
			}
		}
	}


	@Override
	public void loadDetails(ScreenModelObject detail) {
		loadDetails(detail, getView());
	}
	
	protected void loadDetails(ScreenModelObject detail, View view) {
		Log.d(TAG, "Loading details..."); 
		//load course into the view details section
		if( detail instanceof Course ) { 
			currentCourse = (Course) detail;

			view.findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
			view.findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
			
			Button selectCourseButton = ((Button) view.findViewById(R.id.button_select));
			selectCourseButton.setOnClickListener(new SelectCourseOnClickListener());
				
			bindCourseToDisplayableView(view);
			
			bindCurrentCourseToEditableView(view);
			
			((Button) view.findViewById(R.id.button_edit)).setOnClickListener(new EditOnClickListener());;
			
			Button submitButton = ((Button) view.findViewById(R.id.button_submit));
			submitButton.setText(R.string.action_update);
			submitButton.setOnClickListener(new UpdateOnClickListener());
		}
	}

	@Override
	public void openAddDetails() {
		openAddDetails(getView());
	}
	
	protected void openAddDetails(View view) {
		currentCourse = new Course();
		
		view.findViewById(R.id.detail_edit_layout_container).setVisibility(View.VISIBLE);
		view.findViewById(R.id.detail_view_layout_container).setVisibility(View.GONE);
		
		bindCurrentCourseToEditableView(view);
		
		final Button submitButton = ((Button) view.findViewById(R.id.button_submit));
		submitButton.setText(R.string.action_add);
		submitButton.setOnClickListener(new AddOnClickListener());
	}
	
	private void bindCourseToDisplayableView(View view) {
		((TextView) view.findViewById(R.id.text_title)).setText(currentCourse.getTitle());
		((TextView) view.findViewById(R.id.text_section)).setText(currentCourse.getSection());
		((TextView) view.findViewById(R.id.text_semester)).setText(currentCourse.getSemester());
		((TextView) view.findViewById(R.id.text_year)).setText(currentCourse.getYear());
		
		Set<AssignmentWeight> weights = new TreeSet<AssignmentWeight>(assignmentWeightComparator);
		weights.addAll(currentCourse.getAssignmentWeights());
		
		TableLayout table = new TableLayout(getActivity());
		Iterator<AssignmentWeight> iter = weights.iterator();
		while( iter.hasNext() ) {
			AssignmentWeight weight = iter.next();
			TableRow row = new TableRow(getActivity());
			
			row.addView(createAssignmentTypeLabel(weight.getAssignmentType()));
			row.addView(createWeightLabel(weight));
			
			if( iter.hasNext() ) {
				weight = iter.next();
				row.addView(createAssignmentTypeLabel(weight.getAssignmentType()));
				row.addView(createWeightLabel(weight));
			}
			table.addView(row);
		}
		((LinearLayout) getView()
				.findViewById(R.id.detail_view_layout_container)
				.findViewById(R.id.assignment_types_layout))
				.removeAllViews();
		((LinearLayout) getView()
				.findViewById(R.id.detail_view_layout_container)
				.findViewById(R.id.assignment_types_layout))
				.addView(table);

	}
	
	private void bindCurrentCourseToEditableView(View view) {
		((EditText) view.findViewById(R.id.edit_title)).setText(currentCourse.getTitle());
		((EditText) view.findViewById(R.id.edit_section)).setText(currentCourse.getSection());
		
		((Spinner) view.findViewById(R.id.edit_semester)).setSelection(getSelectedSemesterPosition());
		((EditText) view.findViewById(R.id.edit_year)).setText(currentCourse.getYear());

		clearAssTypesInputs();
		
		TableLayout table = new TableLayout(getActivity());
		Iterator<AssignmentType> iter = assTypes.iterator();
		while (iter.hasNext()) {
			AssignmentType type = iter.next();
			
			TableRow row = new TableRow(getActivity());
			
			AssignmentWeight weight = getWeightFromType(type, currentCourse.getAssignmentWeights());
			
			addEditableWeightToRow(type, row, weight);
			
			if( iter.hasNext() ) {
				type = iter.next();
				weight = getWeightFromType(type, currentCourse.getAssignmentWeights());
				addEditableWeightToRow(type, row, weight);
			} else {
				row.addView(createInvisibleCheckBox());
				row.addView(createInvisibleEditText());
			}
			table.addView(row);
		}
		((LinearLayout) getView()
				.findViewById(R.id.detail_edit_layout_container)
				.findViewById(R.id.assignment_types_layout))
				.addView(table);
	}

	private void addEditableWeightToRow(AssignmentType type, TableRow row,
			AssignmentWeight weight) {
		final CheckBox checkbox = (CheckBox) createAssignmentTypeCheckBox(type, weight != null);
		View weightField = createAssignmentTypeWeight(weight, type.getId());
		weightField.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				checkbox.setChecked(((EditText)v).getText().length() > 0);
				return false;
			}
		});
		row.addView(checkbox);
		row.addView(weightField);
	}

	private AssignmentWeight getWeightFromType(AssignmentType type, Set<AssignmentWeight> weights) {
		for (AssignmentWeight weight : weights) {
			if( type.getId() == weight.getAssignmentType().getId()) {
				return weight;
			}
		}
		return null;
	}
	
	private int getSelectedSemesterPosition() {
		String[] semesters = getResources().getStringArray(R.array.semesters);
		int position = 0;
		for(String semester : semesters) {
			if ( semester.equals(currentCourse.getSemester()) ) {
				return position;
			}
			position += 1;
		}
		//could not find it...
		return -1;
	}
	
	private void bindViewToCurrentCourse() {
		currentCourse.setTitle(((EditText) getView().findViewById(R.id.edit_title)).getText().toString());
		currentCourse.setSection(((EditText) getView().findViewById(R.id.edit_section)).getText().toString());
		currentCourse.setSemester(((Spinner) getView().findViewById(R.id.edit_semester)).getSelectedItem().toString());
		currentCourse.setYear(((EditText) getView().findViewById(R.id.edit_year)).getText().toString());
		
		Set<AssignmentWeight> weights = new HashSet<AssignmentWeight>();
		for (AssignmentType type : assTypes) {
			LinearLayout layout = ((LinearLayout) getView()
					.findViewById(R.id.detail_edit_layout_container)
					.findViewById(R.id.assignment_types_layout));
			
			boolean checked = ((CheckBox) layout.findViewWithTag("checkbox" + type.getId())).isChecked();
			
			if( checked ) {
				String weightString = ((EditText) layout.findViewWithTag("assWeight" + type.getId())).getText().toString();
				AssignmentWeight weight = new AssignmentWeight();
				weight.setWeight(Double.valueOf(weightString));
				weight.setAssignmentType(type);
				weights.add(weight);
			}
		}
		currentCourse.setAssignmentWeights(weights);
	}
	
	private class AddOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			bindViewToCurrentCourse();
			courseDao.create(currentCourse);
			bindCourseToDisplayableView(getView());

			getView().findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
			getView().findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
			//once added, then we need to change the button text under the covers
			((Button)getView().findViewById(R.id.button_submit)).setText(R.string.action_update);
			
			updateMaster();
		}
	}
	
	private class UpdateOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			bindViewToCurrentCourse();
			courseDao.update(currentCourse);
			bindCourseToDisplayableView(getView());
			
			getView().findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
			getView().findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
		}
	}
	
	private class EditOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			getView().findViewById(R.id.detail_edit_layout_container).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.detail_view_layout_container).setVisibility(View.GONE);
		}
	}
	
	private class SelectCourseOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			getGradebookApplication().setSelectedCourse(currentCourse);
		}
	}
	
	private Bundle getArgumentsEnsureNotNull() {
		Bundle bundle = getArguments();
		if( bundle == null ) {
			bundle = new Bundle();
		}
		return bundle;
	}
}
