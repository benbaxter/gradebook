package com.benjamingbaxter.gradebook.android.view.student;


import java.util.Collections;
import java.util.List;

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
import com.benjamingbaxter.gradebook.android.dao.SqliteStudentDao;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.dao.StudentDao;
import com.benjamingbaxter.gradebook.model.Course;
import com.benjamingbaxter.gradebook.model.Student;

public class StudentDetailFragment extends DetailsFragment<Student> {
	
	protected final String TAG = this.getClass().getSimpleName();
	protected Student mCurrentStudent;
	protected StudentDao studentDao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO: FIXME! just like in the list frag, want to inject instead
		studentDao = new SqliteStudentDao(
				new GradebookDbHelper(getActivity()));
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
			if( obj instanceof Student ) {
				loadDetails((Student) obj, rootView);
			}
		}
		return rootView;
	}

	@Override
	public void loadDetails(Student detail) {
		loadDetails(detail, getView());
	}
	
	protected void loadDetails(Student detail, View view) {
		Log.d(TAG, "Loading details...");
		mCurrentStudent = (Student) detail;
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
		mCurrentStudent = new Student();
		
		Course course = getGradebookApplication().getSelectedCourse();
		mCurrentStudent.setCourse(course);
		
		view.findViewById(R.id.detail_edit_layout_container).setVisibility(View.VISIBLE);
		view.findViewById(R.id.detail_view_layout_container).setVisibility(View.GONE);
		
		bindModelToEditableView(view);
		
		final Button submitButton = ((Button) view.findViewById(R.id.button_submit));
		submitButton.setText(R.string.action_add);
		submitButton.setOnClickListener(new AddOnClickListener());
	}
	
	@Override
	protected void bindModelToDisplayableView(View view) {
		((TextView) view.findViewById(R.id.text_name)).setText(mCurrentStudent.getFullName());
		((TextView) view.findViewById(R.id.text_email)).setText(mCurrentStudent.getEmail());
	}

	@Override
	protected void bindModelToEditableView(View view) {
		((EditText) view.findViewById(R.id.edit_first_name)).setText(mCurrentStudent.getFirstName());
		((EditText) view.findViewById(R.id.edit_last_name)).setText(mCurrentStudent.getLastName());
		((EditText) view.findViewById(R.id.edit_email)).setText(mCurrentStudent.getEmail());
	}

	@Override
	protected Student bindViewToModel() {
		mCurrentStudent.setFirstName(((EditText) getView().findViewById(R.id.edit_first_name)).getText().toString());
		mCurrentStudent.setLastName(((EditText) getView().findViewById(R.id.edit_last_name)).getText().toString());
		mCurrentStudent.setEmail(((EditText) getView().findViewById(R.id.edit_email)).getText().toString());
		return mCurrentStudent;
	}
	
	
	private class AddOnClickListener extends DetailMutableModelOnClickListener {
		@Override
		public void performAction() {
			studentDao.create(mCurrentStudent);

			view().findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
			view().findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
			//once added, then we need to change the button text under the covers
			((Button)view().findViewById(R.id.button_submit)).setText(R.string.action_update);
			
			updateMaster();
		}

		@Override
		protected List<String> validate() {
			return Collections.EMPTY_LIST;
		}

		@Override
		protected void clearErrors() {
		}

		@Override
		protected void addErrorsToView(List<String> errors) {
		}
	}
	
	private class UpdateOnClickListener extends DetailMutableModelOnClickListener {
		@Override
		public void performAction() {
			studentDao.update(mCurrentStudent);

			view().findViewById(R.id.detail_edit_layout_container).setVisibility(View.GONE);
			view().findViewById(R.id.detail_view_layout_container).setVisibility(View.VISIBLE);
		}

		@Override
		protected List<String> validate() {
			return Collections.EMPTY_LIST;
		}

		@Override
		protected void clearErrors() {
		}

		@Override
		protected void addErrorsToView(List<String> errors) {
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
