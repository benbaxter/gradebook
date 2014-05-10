package com.benjamingbaxter.gradebook.android.view.assignment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteStudentDao;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.dao.StudentDao;
import com.benjamingbaxter.gradebook.model.Course;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;
import com.benjamingbaxter.gradebook.model.Student;

public class AssignmentDetailFragment extends DetailsFragment {
	
	protected final String TAG = this.getClass().getSimpleName();
	protected Student mCurrentStudent;
	protected StudentDao studentDao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO: FIXME! just like in the list frag, want to inject instead
		studentDao = new SqliteStudentDao(
				new GradebookDbHelper(getActivity()),
				new SqliteCourseDao(new GradebookDbHelper(getActivity())));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "Creating view...");
		View rootView = inflater.inflate(R.layout.fragment_detail_student, container, false);
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
		return rootView;
	}

	@Override
	public void loadDetails(ScreenModelObject detail) {
		loadDetails(detail, getView());
	}
	
	protected void loadDetails(ScreenModelObject detail, View view) {
		Log.d(TAG, "Loading details...");
		//load candidate into the view details section
		if( detail instanceof Student ) { 
			mCurrentStudent = (Student) detail;
			view.findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
			view.findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
				
			bindStudentToDisplayableView(view);
			
			bindCurrentCandidateToEditableView(view);
			
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
		mCurrentStudent = new Student();
		
		Course course = getGradebookApplication().getSelectedCourse();
		mCurrentStudent.setCourse(course);
		
		view.findViewById(R.id.detail_edit_layout_container).setVisibility(View.VISIBLE);
		view.findViewById(R.id.detail_view_layout_container).setVisibility(View.GONE);
		
		bindCurrentCandidateToEditableView(view);
		
		final Button submitButton = ((Button) view.findViewById(R.id.button_submit));
		submitButton.setText(R.string.action_add);
		submitButton.setOnClickListener(new AddOnClickListener());
	}
	
	private void bindStudentToDisplayableView(View view) {
		((TextView) view.findViewById(R.id.text_name)).setText(mCurrentStudent.getFullName());
		((TextView) view.findViewById(R.id.text_email)).setText(mCurrentStudent.getEmail());
	}
	
	private void bindCurrentCandidateToEditableView(View view) {
		((EditText) view.findViewById(R.id.edit_first_name)).setText(mCurrentStudent.getFirstName());
		((EditText) view.findViewById(R.id.edit_last_name)).setText(mCurrentStudent.getLastName());
		((EditText) view.findViewById(R.id.edit_email)).setText(mCurrentStudent.getEmail());
	}
	
	private void bindViewToCurrentStudent() {
		mCurrentStudent.setFirstName(((EditText) getView().findViewById(R.id.edit_first_name)).getText().toString());
		mCurrentStudent.setLastName(((EditText) getView().findViewById(R.id.edit_last_name)).getText().toString());
		mCurrentStudent.setEmail(((EditText) getView().findViewById(R.id.edit_email)).getText().toString());
	}
	
	private class AddOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			bindViewToCurrentStudent();
			studentDao.create(mCurrentStudent);
			bindStudentToDisplayableView(getView());

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
			bindViewToCurrentStudent();
			studentDao.update(mCurrentStudent);
			bindStudentToDisplayableView(getView());

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
	
	private Bundle getArgumentsEnsureNotNull() {
		Bundle bundle = getArguments();
		if( bundle == null ) {
			bundle = new Bundle();
		}
		return bundle;
	}
}
