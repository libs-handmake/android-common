package common.hoangdz.lib.extensions

import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

fun Fragment.showDialog(dialogFragment: DialogFragment) {
    dialogFragment.show(childFragmentManager, dialogFragment.javaClass.simpleName)
}

fun Fragment.toastMsg(msg: Int) {
    try {
        Toast.makeText(requireActivity(), this.getString(msg), Toast.LENGTH_SHORT).show()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun Fragment.toastMsg(msg: String) {
    try {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}