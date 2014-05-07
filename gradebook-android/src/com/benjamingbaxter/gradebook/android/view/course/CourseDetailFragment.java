package com.benjamingbaxter.gradebook.android.view.course;


import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteAssignmentTypeDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.dao.AssignmentTypeDao;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.AssignmentType;
import com.benjamingbaxter.gradebook.model.Course;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;

public class CourseDetailFragment extends DetailsFragment {
	
	protected final String TAG = this.getClass().getSimpleName();
	protected Course currentCourse;
	protected CourseDao courseDao;
	
	protected AssignmentTypeDao assignmentTypeDao;

	private final TableRow.LayoutParams params = 
			new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT, 1f);

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO: FIXME! just like in the list frag, want to inject instead
		courseDao = new SqliteCourseDao(new GradebookDbHelper(getActivity()));
		assignmentTypeDao = new SqliteAssignmentTypeDao(new GradebookDbHelper(getActivity()));
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
		TableLayout table = new TableLayout(getActivity());
		while( query.next() ) {
			TableRow row = new TableRow(getActivity());
			
			row.addView(createAssignmentTypeCheckBox(query.current()));
			
			if( query.next() ) {
				row.addView(createAssignmentTypeCheckBox(query.current()));
			}
			table.addView(row);
		}
		((LinearLayout) rootView
				.findViewById(R.id.detail_edit_layout_container)
				.findViewById(R.id.assignment_types_layout))
				.addView(table);
		
		if( query.moveToFirst() ) {
			table = new TableLayout(getActivity());
			while( query.next() ) {
				TableRow row = new TableRow(getActivity());
				
				row.addView(createCheckedIconAndAssignmentTypeLayout(query.current()));
				
				if( query.next() ) {
					row.addView(createCheckedIconAndAssignmentTypeLayout(query.current()));
				}
				table.addView(row);
			}
			((LinearLayout) rootView
					.findViewById(R.id.detail_view_layout_container)
					.findViewById(R.id.assignment_types_layout))
					.addView(table);
		}
		
		return rootView;
	}

	private LinearLayout createCheckedIconAndAssignmentTypeLayout(AssignmentType assType) {
		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		ImageView checkMark = new ImageView(getActivity());
		checkMark.setImageResource(android.R.drawable.checkbox_on_background);
		layout.addView(checkMark);
		
		TextView textView = new TextView(getActivity());
		textView.setText(assType.getLabel());
		layout.addView(textView);
		
		return layout;
	}

	private CheckBox createAssignmentTypeCheckBox(AssignmentType assType) {
		CheckBox checkbox = new CheckBox(getActivity());
		checkbox.setText(assType.getLabel());
		checkbox.setLayoutParams(params);
		return checkbox;
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
		
		//TODO: add assignment types that are added to a course
	}
	
	private void bindCurrentCourseToEditableView(View view) {
		((EditText) view.findViewById(R.id.edit_title)).setText(currentCourse.getTitle());
		((EditText) view.findViewById(R.id.edit_section)).setText(currentCourse.getSection());
		
		((Spinner) view.findViewById(R.id.edit_semester)).setSelection(getSelectedSemesterPosition());
		((EditText) view.findViewById(R.id.edit_year)).setText(currentCourse.getYear());

		//Note that in 'add' mode there are no assignment types yet,
		//so none of the check boxes will be selected

		//TODO: check those that are added to a course
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
	
	private void bindViewToCurrentCandidate() {
		currentCourse.setTitle(((EditText) getView().findViewById(R.id.edit_title)).getText().toString());
		currentCourse.setSection(((EditText) getView().findViewById(R.id.edit_section)).getText().toString());
		currentCourse.setSemester(((Spinner) getView().findViewById(R.id.edit_semester)).getSelectedItem().toString());
		currentCourse.setYear(((EditText) getView().findViewById(R.id.edit_year)).getText().toString());
	}
	
	private class AddOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			bindViewToCurrentCandidate();
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
			bindViewToCurrentCandidate();
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
