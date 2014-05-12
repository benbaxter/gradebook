package com.benjamingbaxter.gradebook.android.view.assignment;


import static com.benjamingbaxter.gradebook.android.view.MasterDetailFragment.BUNDLE_ASSIGNMENT_TYPE;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.dao.AssignmentDao;
import com.benjamingbaxter.gradebook.dao.GradedAssignmentDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.Assignment;
import com.benjamingbaxter.gradebook.model.AssignmentType;
import com.benjamingbaxter.gradebook.model.Course;
import com.benjamingbaxter.gradebook.model.GradedAssignment;
import com.benjamingbaxter.gradebook.model.Student;

public class AssignmentDetailFragment extends DetailsFragment<Assignment> {
	
	protected final String TAG = this.getClass().getSimpleName();
	protected Assignment currentAssignment;
	protected AssignmentDao assignmentDao;
	protected GradedAssignmentDao gradedAssignmentDao;
	private AssignmentType assignmentType;

	private final TableRow.LayoutParams params = 
			new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT, 1f);

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		setAssignmentTypeFromArgs();
	}

	private void setAssignmentTypeFromArgs() {
		Bundle args = getArguments();
		if( args != null && args.containsKey(BUNDLE_ASSIGNMENT_TYPE)) {
			assignmentType = (AssignmentType) args.get(BUNDLE_ASSIGNMENT_TYPE);
		}
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setAssignmentTypeFromArgs();
		
		assignmentDao = getGradebookApplication().getAssignmentDao();
		gradedAssignmentDao = getGradebookApplication().getGradedAssignmentDao();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.d(TAG, "Creating view...");
		
		setAssignmentTypeFromArgs();

		View rootView = inflater.inflate(R.layout.fragment_detail_assignment, container, false);
		//TODO: maybe use this variable and have one listener instead of multiple?
		boolean addingNew = getArgumentsEnsureNotNull().getBoolean(EXTRA_DETAILS_ADD_MODE);
		if( addingNew ) {
			openAddDetails(rootView);
		} else {
			Object obj = getArgumentsEnsureNotNull().get(EXTRA_DETAILS_ID);
			if( obj instanceof Assignment ) {
				loadDetails((Assignment) obj, rootView);
			}
		}
		return rootView;
	}

	@Override
	public void loadDetails(Assignment detail) {
		loadDetails(detail, getView());
	}
	
	protected void loadDetails(Assignment detail, View view) {
		Log.d(TAG, "Loading details...");
		//load candidate into the view details section
		currentAssignment = detail;
		view.findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
		view.findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
			
		bindModelToDisplayableView(view);
		
		bindModelToEditableView(view);
		
		((Button) view.findViewById(R.id.button_edit)).setOnClickListener(new EditOnClickListener());;
		
		Button submitButton = ((Button) view.findViewById(R.id.button_submit));
		submitButton.setText(R.string.action_update);
		submitButton.setOnClickListener(new UpdateOnClickListener());
	}

	@Override
	public void openAddDetails() {
		openAddDetails(getView());
	}
	
	protected void openAddDetails(View view) {
		currentAssignment = new Assignment();
		currentAssignment.setType(assignmentType);
		
		Course course = getGradebookApplication().getSelectedCourse();
		currentAssignment.setCourse(course);
		course.addAssignment(currentAssignment);
		for(Student student : course.getStudents() ) {
			student.addAssignment(currentAssignment);
		}
		
		view.findViewById(R.id.detail_edit_layout_container).setVisibility(View.VISIBLE);
		view.findViewById(R.id.detail_view_layout_container).setVisibility(View.GONE);
		
		bindModelToEditableView(view);
		
		final Button submitButton = ((Button) view.findViewById(R.id.button_submit));
		submitButton.setText(R.string.action_add);
		submitButton.setOnClickListener(new AddOnClickListener());
	}
	
	@Override
	protected void bindModelToDisplayableView(View view) {
		((TextView) view.findViewById(R.id.text_title))
			.setText(currentAssignment.getTitle());
		((TextView) view.findViewById(R.id.text_possible_points))
			.setText(String.valueOf(currentAssignment.getPossiblePoints()));
		
		//TODO: Look at making it a list view...
		TableLayout table = new TableLayout(getActivity());
		
		for( Student student : getGradebookApplication().getSelectedCourse().getStudents() ) {
			TableRow row = new TableRow(getActivity());
			
			TextView name = new TextView(getActivity());
			name.setLayoutParams(params);
			name.setTextSize(20);
			name.setText(student.display());
			
			row.addView(name);
			
			GradedAssignment grade = null;
			Query<GradedAssignment> query = gradedAssignmentDao
					.findByCourseIdAndAssignmentIdAndStudentId
					(currentAssignment.getCourse().getId(), 
							currentAssignment.getId(), 
							student.getId()); 
			if( query.next() ) {
				grade = query.current();
			}
			query.close();
			
			LinearLayout layout = new LinearLayout(getActivity());
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setLayoutParams(params);
			
			TextView earnedPoints = new TextView(getActivity());
			earnedPoints.setTextSize(20);
			if(grade != null) {
				earnedPoints.setText(String.valueOf(grade.getEarnedPoints()));
			}
			layout.addView(earnedPoints);
			
			final Button editButton = new Button(getActivity());
			editButton.setText(R.string.action_edit);
			editButton.setOnClickListener(new View.OnClickListener() {
				boolean edit = true;
				@Override
				public void onClick(View v) {
					if( edit ) {
						editButton.setText(R.string.action_update);
					} else {
						editButton.setText(R.string.action_edit);
					}
					edit = ! edit;
				}
			});
			layout.addView(editButton);
			
			row.addView(layout);
			
			table.addView(row);
		}
		
		((LinearLayout) getView()
				.findViewById(R.id.detail_view_layout_container)
				.findViewById(R.id.assignment_student_view_layout))
				.removeAllViews();;
		((LinearLayout) getView()
				.findViewById(R.id.detail_view_layout_container)
				.findViewById(R.id.assignment_student_view_layout))
				.addView(table);
	}
	
	@Override
	protected void bindModelToEditableView(View view) {
		((EditText) view.findViewById(R.id.edit_title))
			.setText(currentAssignment.getTitle());
		((EditText) view.findViewById(R.id.edit_possible_points))
			.setText(String.valueOf(currentAssignment.getPossiblePoints()));
	}
	
	@Override
	protected Assignment bindViewToModel() {
		currentAssignment.setTitle(
				((EditText) getView().findViewById(R.id.edit_title))
				.getText().toString());
		currentAssignment.setPossiblePoints(Double.valueOf(
				(((EditText) getView().findViewById(R.id.edit_possible_points))
						.getText().toString())));
		return currentAssignment;
	}
	
	protected List<String> isValidAssignment() {
		List<String> errors = new ArrayList<String>();

		if(currentAssignment.getTitle().isEmpty() ) {
			errors.add("Title cannot be blank");
		}
		
		if(currentAssignment.getPossiblePoints() <= -1) {
			errors.add("Cannot have negative points");
		}

		return errors;
	}
	
	protected void addErrorsInDispaly(List<String> errors) {
		clearErrorsInDisplay();
		LinearLayout layout = ((LinearLayout) getView()
				.findViewById(R.id.detail_edit_layout_container)
				.findViewById(R.id.assignment_errors_layout));
		for (String error : errors) {
			TextView text = new TextView(getActivity());
			text.setTextColor(getResources().getColor(R.color.danger));
			text.setTextSize(20);
			text.setText(error);
			
			
			layout.addView(text);
		}
		layout.setVisibility(View.VISIBLE);
	}
	
	protected void clearErrorsInDisplay() {
		LinearLayout layout = ((LinearLayout) getView()
				.findViewById(R.id.detail_edit_layout_container)
				.findViewById(R.id.assignment_errors_layout));
		
		layout.removeAllViews();
		layout.setVisibility(View.GONE);
	}
	
	private class AddOnClickListener extends DetailMutableModelOnClickListener {
		@Override
		public void performAction() {
			assignmentDao.create(currentAssignment);

			getView().findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
			getView().findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
			//once added, then we need to change the button text under the covers
			((Button)getView().findViewById(R.id.button_submit)).setText(R.string.action_update);
			
			updateMaster();
		}

		@Override
		protected List<String> validate() {
			return isValidAssignment();
		}

		@Override
		protected void clearErrors() {
			clearErrorsInDisplay();
		}

		@Override
		protected void addErrorsToView(List<String> errors) {
			addErrorsInDispaly(errors);
		}
	}
	
	private class UpdateOnClickListener extends DetailMutableModelOnClickListener {
		@Override
		public void performAction() {
			assignmentDao.update(currentAssignment);

			getView().findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
			getView().findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
		}
		
		@Override
		protected List<String> validate() {
			return isValidAssignment();
		}

		@Override
		protected void clearErrors() {
			clearErrorsInDisplay();
		}

		@Override
		protected void addErrorsToView(List<String> errors) {
			addErrorsInDispaly(errors);
		}
	}

	private class EditOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			getView().findViewById(R.id.detail_edit_layout_container).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.detail_view_layout_container).setVisibility(View.GONE);
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
