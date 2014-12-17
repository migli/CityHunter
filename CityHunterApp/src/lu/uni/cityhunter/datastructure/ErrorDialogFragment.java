package lu.uni.cityhunter.datastructure;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class ErrorDialogFragment extends DialogFragment{
	
	private Dialog errorDialog;
	
	public ErrorDialogFragment(){
		super();
		errorDialog = null;
	}
	
	public void setDialog(Dialog errorDialog){
		this.errorDialog = errorDialog;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return errorDialog;
    }
	
}
