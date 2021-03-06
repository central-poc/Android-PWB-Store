package cenergy.central.com.pwb_store.helpers

import android.content.Context
import androidx.appcompat.app.AlertDialog
import cenergy.central.com.pwb_store.R
import cenergy.central.com.pwb_store.model.APIError

class DialogHelper(var context: Context) {

    fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun getAlertDialog(message: String): AlertDialog {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        return builder.create()
    }

    fun showErrorDialog(error: APIError) {
        if (error.errorCode == null) {
            showAlertDialog(context.getString(R.string.not_connected_network))
        } else {
            when (error.errorCode) {
                "408", "404", "500" -> showAlertDialog(context.getString(R.string.server_not_found))
                else -> showAlertDialog(context.getString(R.string.some_thing_wrong))
            }
        }
    }

    fun errorDialog(error: APIError): AlertDialog {
       return if (error.errorCode == null) {
            getAlertDialog(context.getString(R.string.not_connected_network))
        } else {
            when (error.errorCode) {
                "408", "404", "500" -> getAlertDialog(context.getString(R.string.server_not_found))
                else -> getAlertDialog(context.getString(R.string.some_thing_wrong))
            }
        }
    }
}